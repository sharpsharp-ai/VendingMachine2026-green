package de.sharpsharp.vendingmachine;

import java.time.LocalTime;

import org.mockito.Mockito;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;

public class VendingMachineTest{
    private VendingMachine vendingMachine;
    
    @Test
    public void drinksAreFree() {
        //arrange
        Clock clock = Mockito.mock(Clock.class);
        vendingMachine = new VendingMachine(clock);
        //act
        vendingMachine.selectDrink(Drink.COLA);
        //assert
        var outputTray = vendingMachine.outputTray();
        assertThat(outputTray, contains(Drink.COLA));
        
    }
}