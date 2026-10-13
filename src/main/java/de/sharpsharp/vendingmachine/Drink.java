package de.sharpsharp.vendingmachine;

/** The four slots of the machine, one drink per slot, in their order on the front. */
public enum Drink {
    COLA("Cola", 200),
    ORANGE("Orange", 50),
    LEMON("Zitrone", 150),
    BEER("Bier", 1000);

    private final String displayName;
    private final Integer price;

    Drink(String displayName, Integer price) {
        this.displayName = displayName;
        this.price = price;
    }

    /** The German name shown on the slot, e.g. "Zitrone". */
    public String displayName() {
        return displayName;
    }

    public Integer getPrice() {
        return price;
    }
}
