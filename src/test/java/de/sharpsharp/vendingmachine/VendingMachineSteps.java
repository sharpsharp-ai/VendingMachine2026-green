package de.sharpsharp.vendingmachine;

import io.cucumber.java.de.Angenommen;
import io.cucumber.java.de.Dann;
import io.cucumber.java.de.Wenn;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;

/**
 * Step definitions for the feature files. The scenarios talk to the machine directly,
 * never through the web page or HTTP. Cucumber creates a new instance of this class
 * for every scenario, so every scenario starts with a freshly built machine.
 */
public class VendingMachineSteps {

    private final FakeClock clock = new FakeClock();
    private final VendingMachine machine = new VendingMachine(clock);

    @Angenommen("der Automat ist frisch gestartet")
    public void theMachineIsFreshlyStarted() {
        // Nothing to do: Cucumber builds this class, and with it the machine, fresh for every scenario.
    }

    @Wenn("ich {drink} wähle")
    public void iSelect(Drink drink) {
        machine.selectDrink(drink);
    }

    @Dann("liegt eine Dose {drink} im Ausgabefach")
    public void oneCanLiesInTheOutputTray(Drink drink) {
        assertThat(machine.outputTray(), contains(drink));
    }

    @Dann("kostet eine Dose {drink} {int} Euro")
    public void oneCanCostsMoney(Drink drink, int money) {
        assertThat(machine.price(drink), is(money*100));
    }

    @Wenn("ich Münzen im Wert von {string} einwerfe")
    public void ichImWertVonEinwerfe(String coin_value) {
        Coin coin = Enum.valueOf(Coin.class, coin_value);
        machine.insertCoin(coin.value());
    }
    
    @Dann("habe ich ein Guthaben von {string} Euro")
    public void iHaveCredits(String expected_credit) {
        Coin coin = Enum.valueOf(Coin.class, expected_credit);
        int expected_credit_int = coin.value();
        assertThat(machine.getCredit(), is(expected_credit_int));
    }
}
