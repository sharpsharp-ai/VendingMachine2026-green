package de.sharpsharp.vendingmachine;

import java.util.List;
import org.mockito.Mockito;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;

public class VendingMachineTest{
    private VendingMachine vendingMachine;
    
    @Test
    public void drinksAreFree() {
        Clock clock = Mockito.mock(Clock.class);
        vendingMachine = new VendingMachine(clock);
        vendingMachine.selectDrink(Drink.COLA);
        List<Drink> outputTray = vendingMachine.outputTray();
        assertThat(outputTray, is(empty()));
        assertThat(vendingMachine.message(), is("zu wenig Geld"));
    }

    @Test
    public void drinksHavePrices() {
        //arrange
        Clock clock = Mockito.mock(Clock.class);
        vendingMachine = new VendingMachine(clock);
        //act
        int priceCola = vendingMachine.price(Drink.COLA);
        //assert
        assertThat(priceCola, is(100));
    }

    @Test
    public void beerCosts2Euros() {
        //arrange
        Clock clock = Mockito.mock(Clock.class);
        vendingMachine = new VendingMachine(clock);
        //act
        int priceBeer = vendingMachine.price(Drink.BEER);
        //assert
        assertThat(priceBeer, is(200));
    }

    @Test
    public void creditsAreShown() {
      //arrange
      Clock clock = Mockito.mock(Clock.class);
      vendingMachine = new VendingMachine(clock);
      vendingMachine.insertCoin(100);
      //act
      int credit = vendingMachine.getCredit();
      //assert
      assertThat(credit, is(100));
    }

    @Test
    public void cancelReturnsCreditToCoinReturn() {
        Clock clock = Mockito.mock(Clock.class);
        vendingMachine = new VendingMachine(clock);
        vendingMachine.insertCoin(200);
        //act
        vendingMachine.cancel();
        //assert
        assertThat(vendingMachine.getCredit(), is(0));
        assertThat(vendingMachine.coinReturn(), contains(200));
    }

    @Test
    public void showChangeReturnsTotalCoinReturn() {
        Clock clock = Mockito.mock(Clock.class);
        vendingMachine = new VendingMachine(clock);
        vendingMachine.insertCoin(200);
        vendingMachine.cancel();
        //act
        int change = vendingMachine.showChange();
        //assert
        assertThat(change, is(200));
    }

    @Test
    public void emptyChangeClearsCoinReturn() {
        Clock clock = Mockito.mock(Clock.class);
        vendingMachine = new VendingMachine(clock);
        vendingMachine.insertCoin(200);
        vendingMachine.cancel();
        //act
        vendingMachine.emptyChange();
        //assert
        assertThat(vendingMachine.showChange(), is(0));
        assertThat(vendingMachine.coinReturn().size(), is(0));
    }

    @Test
    public void cancelWithNoCreditDoesNothing() {
        Clock clock = Mockito.mock(Clock.class);
        vendingMachine = new VendingMachine(clock);
        //act
        vendingMachine.cancel();
        //assert
        assertThat(vendingMachine.getCredit(), is(0));
        assertThat(vendingMachine.coinReturn().size(), is(0));
    }

    @Test
    public void sufficientCreditAllowsPurchase() {
        Clock clock = Mockito.mock(Clock.class);
        vendingMachine = new VendingMachine(clock);
        vendingMachine.insertCoin(200);
        vendingMachine.selectDrink(Drink.COLA);
        List<Drink> outputTray = vendingMachine.outputTray();
        assertThat(outputTray, contains(Drink.COLA));
        assertThat(vendingMachine.message(), is("Prost!"));
        assertThat(vendingMachine.getCredit(), is(100));
    }

    @Test
    public void insufficientCreditBlocksPurchase() {
        Clock clock = Mockito.mock(Clock.class);
        vendingMachine = new VendingMachine(clock);
        vendingMachine.insertCoin(50);
        vendingMachine.selectDrink(Drink.BEER);
        List<Drink> outputTray = vendingMachine.outputTray();
        assertThat(outputTray, is(empty()));
        assertThat(vendingMachine.message(), is("zu wenig Geld"));
        assertThat(vendingMachine.refused(), is(true));
    }

    @Test
    public void changeAfterPurchase() {
        Clock clock = Mockito.mock(Clock.class);
        vendingMachine = new VendingMachine(clock);
        vendingMachine.insertCoin(200);
        vendingMachine.selectDrink(Drink.COLA);
        assertThat(vendingMachine.getCredit(), is(100));
    }

    @Test
    public void beerRequiresTwoEuros() {
        Clock clock = Mockito.mock(Clock.class);
        vendingMachine = new VendingMachine(clock);
        vendingMachine.insertCoin(100);
        vendingMachine.selectDrink(Drink.BEER);
        List<Drink> outputTray = vendingMachine.outputTray();
        assertThat(outputTray, is(empty()));
        assertThat(vendingMachine.refused(), is(true));
    }

    @Test
    public void noCreditShowsInsufficientMessage() {
        Clock clock = Mockito.mock(Clock.class);
        vendingMachine = new VendingMachine(clock);
        vendingMachine.selectDrink(Drink.COLA);
        assertThat(vendingMachine.message(), is("zu wenig Geld"));
        assertThat(vendingMachine.refused(), is(true));
    }
}