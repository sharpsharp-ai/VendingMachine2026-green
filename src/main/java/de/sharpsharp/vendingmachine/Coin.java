package de.sharpsharp.vendingmachine;

public enum Coin {
  ONE_EURO("OneEuro", 100),
  TWO_EURO("TwoEuro", 200),
  CENTS_50("50Cents", 50),
  NONE("NONE", 0);

  private final String displayName;
  private final int value;

  Coin(String displayName, int value) {
    this.displayName = displayName;
    this.value = value;
  }

  public String displayName() {
    return displayName;
  }

  public int value() {
    return value;
  }
}
