package de.sharpsharp.vendingmachine;

import java.time.LocalTime;

import io.cucumber.java.de.Angenommen;
import io.cucumber.java.de.Dann;
import io.cucumber.java.de.Und;
import io.cucumber.java.de.Wenn;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

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

    @Dann("hat Cola den korrekten Preis")
    public void siehtManBeiColaAuchDenPreisBeimNamen() { assertThat(machine.price(Drink.COLA),is(Drink.COLA.getPrice()));
    }

    @Und("es ist {int}:{int} Uhr")
    public void dieUhrzeitIst(int hour, int minute) {
        clock.set(LocalTime.of(hour, minute));
    }

    @Dann("liegt nichts im Ausgabefach")
    public void liegtNichtsImAusgabefach() {
        assertThat(machine.outputTray(), empty());
    }
}
