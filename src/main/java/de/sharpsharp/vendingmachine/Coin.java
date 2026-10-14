package de.sharpsharp.vendingmachine;

public enum Coin {
  COIN_50(50),
  COIN_100(100),
  COIN_200(200);

  private final Integer value;

  Coin(Integer value) {
    this.value = value;
  }

  public Integer getValue() {
    return this.value;
  }

  public static Coin valueOf(int value) {
    for (Coin coin : Coin.values()) {
      if (coin.getValue().equals(value)) {
        return coin;
      }
    }

    return null;
  }
}
