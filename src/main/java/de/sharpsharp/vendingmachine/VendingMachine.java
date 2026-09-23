package de.sharpsharp.vendingmachine;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
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

    private final Map<Drink, Integer> stock = new EnumMap<>(Drink.class);
    /** The time of day, for rules that depend on it. Never read the system time directly: ask the clock. */
    private final Clock clock;
    private List<Drink> selectedDrinks = new ArrayList<>();
    private int credit = 0;
    private String displayMessage = "Bitte Münzen einwerfen";
    private boolean refused = false;
    private List<Integer> coinReturn = new ArrayList<>();

    public VendingMachine(Clock clock) {
        this.clock = clock;
        for (Drink drink : Drink.values()) {
            stock.put(drink, CANS_PER_SLOT);
        }
    }

    // ---- What a customer can do --------------------------------------------

    public synchronized void insertCoin(int cents) {
      credit += cents;
    }

    public int getCredit() {
      return credit;
    }

    public synchronized void selectDrink(Drink drink) {
        int price = drink.price();
        if (credit < price) {
            displayMessage = "zu wenig Geld";
            refused = true;
            return;
        }
        refused = false;
        credit -= price;
        selectedDrinks.add(drink);
        displayMessage = "Prost!";
    }

    public synchronized void cancel() {
        if (credit > 0) {
            coinReturn.add(credit);
            credit = 0;
        }
        selectedDrinks.clear();
    }

    /** Empties the output tray and returns the cans that were in it. */
    public synchronized List<Drink> takeDrinks() {
        List<Drink> drinks = selectedDrinks;
        selectedDrinks = new ArrayList<>();
        return drinks;
    }

    /** Empties the coin return and returns the coins that were in it, in cents. */
    public synchronized List<Integer> takeCoins() {
        List<Integer> coins = new ArrayList<>(coinReturn);
        coinReturn.clear();
        return coins;
    }

    // ---- What the machine shows ---------------------------------------------

    /** In cents. */
    public synchronized int credit() {
        return credit;
    }

    public synchronized String message() {
        return displayMessage;
    }

    /** True while the display shows a refusal such as "Ausverkauft"; the page then flashes it red. */
    public synchronized boolean refused() {
        return refused;
    }

    public synchronized int stock(Drink drink) {
        return stock.get(drink);
    }

    /** The price shown behind the name of the drink, in cents. Null: the machine knows no price yet. */
    public synchronized Integer price(Drink drink) {
        return drink.price();
    }

    /** The cans that dropped out and have not been taken yet. */
    public synchronized List<Drink> outputTray() {
        return selectedDrinks;
    }

    /** The coins that came back and have not been taken yet, in cents. */
    public synchronized List<Integer> coinReturn() {
        return new ArrayList<>(coinReturn);
    }

    /* Returns the value of coins in the change slot */
    public int showChange() {
        int total = 0;
        for (int coin : coinReturn) {
            total += coin;
        }
        return total;
    }

    /* Empties the coins in the change slot */
    public void emptyChange() {
        coinReturn.clear();
    }
}
