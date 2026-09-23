package de.sharpsharp.vendingmachine;

import io.cucumber.java.de.Angenommen;
import io.cucumber.java.de.Dann;
import io.cucumber.java.de.Und;
import io.cucumber.java.de.Wenn;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.empty;
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

    @Wenn("{int} Euro im Automat sind")
    public void moneyInTheAutomaton(int moneyInEuro) {
        machine.insertCoin(moneyInEuro * 100);
    }

    @Wenn("kein Geld im Automat ist")
    public void noMoneyInTheAutomaton() {
        moneyInTheAutomaton(0);
    }

    @Dann("liegt eine Dose {drink} im Ausgabefach")
    public void oneCanLiesInTheOutputTray(Drink drink) {
        assertThat(machine.outputTray(), contains(drink));
    }

    @Dann("ist das Ausgabefach leer")
    public void outputTrayEmpty() {
        assertThat(machine.outputTray(), is(empty()));
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

    @Und("ich den Vorgang abbreche")
    public void iCancelTheProcess() {
        machine.cancel();
    }

    @Und("sehe ich Münzen im Wert von {string} in der Münzrückgabe")
    public void iSeeCoinsWithValue(String coin_value) {
        Coin coin = Enum.valueOf(Coin.class, coin_value);
        int expected_value = coin.value();
        
        int actual_value = machine.showChange();
        assertThat(actual_value, is(expected_value));
    }

    @Wenn("ich die Münzrückgabe entleere")
    public void iEmptyTheCoinReturn() {
        machine.emptyChange();
    }

    //     am Display wird "Prost!" angezeigt
    @Dann("am Display wird {string} angezeigt")
    public void displayShowsMessage(String message) {
        assertThat(machine.message(), is(message));
    }

    @Dann("ein Restgeld von {int} Euro wird angezeigt")
    public void displayShowsChange(int changeInEuro) {
        assertThat(machine.getCredit(), is(changeInEuro));
    }
}
