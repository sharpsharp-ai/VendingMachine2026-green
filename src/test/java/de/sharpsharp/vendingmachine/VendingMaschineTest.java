package de.sharpsharp.vendingmachine;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;

public class VendingMaschineTest {

  @Test
  public void newVendingMaschine_givesDrinksForFree() {
    VendingMachine vendingMachine = new VendingMachine(new FakeClock());
    Drink drink = Drink.BEER;
    vendingMachine.selectDrink(drink);
    assertThat(vendingMachine.outputTray(), contains(drink));
  }

  @Test
  public void newVendingMachine_hasProductPrices() {
    VendingMachine vendingMachine = new VendingMachine(new FakeClock());

    assertThat(vendingMachine.price(Drink.COLA), is(1));
  }
}
