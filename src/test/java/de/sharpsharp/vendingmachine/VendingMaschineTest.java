package de.sharpsharp.vendingmachine;

import org.junit.Test;

import java.time.LocalTime;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;

public class VendingMaschineTest {

  @Test
  public void newVendingMachine_hasProductPrices() {
    VendingMachine vendingMachine = new VendingMachine(new FakeClock());
    assertThat(vendingMachine.price(Drink.COLA), is(Drink.COLA.getPrice()));
  }

  @Test
  public void bierVorVierzUhrGibtKeineDose() {
    FakeClock clock = new FakeClock();
    VendingMachine vendingMachine = new VendingMachine(clock);
    clock.set(LocalTime.of(15, 59));
    vendingMachine.selectDrink(Drink.BEER);
    assertThat(vendingMachine.outputTray(), empty());
  }

  @Test
  public void bierAbMitternachtGibtKeineDose() {
    FakeClock clock = new FakeClock();
    VendingMachine vendingMachine = new VendingMachine(clock);
    clock.set(LocalTime.of(0, 0));
    vendingMachine.selectDrink(Drink.BEER);
    assertThat(vendingMachine.outputTray(), empty());
  }

  @Test
  public void bierNachVierzUhrGibtEineDose() {
    FakeClock clock = new FakeClock();
    VendingMachine vendingMachine = new VendingMachine(clock);
    clock.set(LocalTime.of(16, 0));
    vendingMachine.selectDrink(Drink.BEER);
    assertThat(vendingMachine.outputTray(), org.hamcrest.Matchers.contains(Drink.BEER));
  }

  @Test
  public void bierVorMitternachtGibtEineDose() {
    FakeClock clock = new FakeClock();
    VendingMachine vendingMachine = new VendingMachine(clock);
    clock.set(LocalTime.of(23, 59));
    vendingMachine.selectDrink(Drink.BEER);
    assertThat(vendingMachine.outputTray(), org.hamcrest.Matchers.contains(Drink.BEER));
  }
}
