package de.sharpsharp.vendingmachine;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * The machine. So far it does nothing: every action is ignored, and what it reports is the
 * state right after switching it on. The rules in the README are still to be built.
 * <p>
 * Amounts are in cents, messages are plain text. The methods are synchronized because the
 * web page may send several requests at once, while there is one machine and one customer at a time.
 */
public class VendingMachine {

    public static final int CANS_PER_SLOT = 5;
    public static final int BEER_TIME_START_HOUR = 16;
    public static final int BEER_TIME_END_HOUR = 24;

    private final Map<Drink, Integer> stock = new EnumMap<>(Drink.class);
    /** The time of day, for rules that depend on it. Never read the system time directly: ask the clock. */
    private final Clock clock;

    private List<Drink> tray = new ArrayList<>();

    private List<Integer> coinTray = new ArrayList<>();

    private final Map<Drink, Integer> prices = new EnumMap<>(Drink.class);

    public VendingMachine(Clock clock) {
        this.clock = clock;
        for (Drink drink : Drink.values()) {
            stock.put(drink, CANS_PER_SLOT);
        }
    }

    // ---- What a customer can do --------------------------------------------

    public synchronized void insertCoin(int cents) {
        Coin coin = Coin.valueOf(cents);
        if (coin != null) {
            coinTray.add(coin.getValue());
        }
    }

    public synchronized void selectDrink(Drink drink) {
        if (drink == Drink.BEER && !isBeerTime()) {
            return;
        }
        tray.add(drink);
    }

    private boolean isBeerTime() {
        java.time.LocalTime now = clock.now();
        return now.getHour() >= BEER_TIME_START_HOUR && now.getHour() < BEER_TIME_END_HOUR;
    }

    public synchronized void cancel() {
    }

    /** Empties the output tray and returns the cans that were in it. */
    public synchronized List<Drink> takeDrinks() {
        return List.of();
    }

    /** Empties the coin return and returns the coins that were in it, in cents. */
    public synchronized List<Integer> takeCoins() {
        return List.of();
    }

    // ---- What the machine shows ---------------------------------------------

    /** In cents. */
    public synchronized int credit() {
        return coinTray.stream().mapToInt(Integer::intValue).sum();
    }

    public synchronized String message() {
        return "Bitte Münzen einwerfen";
    }

    /** True while the display shows a refusal such as "Ausverkauft"; the page then flashes it red. */
    public synchronized boolean refused() {
        return false;
    }

    public synchronized int stock(Drink drink) {
        return stock.get(drink);
    }

    /** The price shown behind the name of the drink, in cents. Null: the machine knows no price yet. */
    public synchronized Integer price(Drink drink) {
        return drink.getPrice();
    }

    /** The cans that dropped out and have not been taken yet. */
    public synchronized List<Drink> outputTray() {
        return tray;
    }

    /** The coins that came back and have not been taken yet, in cents. */
    public synchronized List<Integer> coinReturn() {
        return coinTray;
    }
}
