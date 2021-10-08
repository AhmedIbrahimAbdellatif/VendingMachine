package com.vendingmachine.kata;

import com.vendingmachine.kata.CoinInsertion.CoinInsertionModule;
import com.vendingmachine.kata.Machine.VendingMachine;
import com.vendingmachine.kata.MoneyChange.MoneyChangeModule;
import org.javatuples.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static java.util.List.of;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@ExtendWith(MockitoExtension.class)
public class VendingMachineTest {
    @InjectMocks
    VendingMachine machineUnderTest;
    @Mock
    CoinInsertionModule coinInsertionModule;
    @Mock
    MoneyChangeModule moneyChangeModule;

    @BeforeEach
    void setUp() {
        machineUnderTest = new VendingMachine(coinInsertionModule, moneyChangeModule);
    }

    @Test
    void start_machine_default() {
        when(moneyChangeModule.isMoneyAvailable()).thenReturn(true);

        String actualMessage = machineUnderTest.start();

        String expectedMessage = "INSERT COIN";
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void start_machine_no_money_for_change() {
        when(moneyChangeModule.isMoneyAvailable()).thenReturn(false);

        String actualMessage = machineUnderTest.start();

        String expectedMessage = "EXACT CHANGE ONLY";
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void insert_coins_test_accepting_valid_coins() {
        List<String> coins = List.of("nickel", "dime", "quarter");
        when(coinInsertionModule.acceptCoins(anyList())).thenReturn(true);
        when(coinInsertionModule.getAcceptedAmount()).thenReturn(0.4);

        Pair<String, List<String>> actualOutput = machineUnderTest.insertCoins(coins);
        String actualMessage = actualOutput.getValue(VendingMachine.ParameterOrder.MESSAGE.ordinal()).toString();
        List<String> actualCoinReturn = (List<String>)actualOutput.getValue(VendingMachine.ParameterOrder.COIN_RETURN.ordinal());

        String expectedMessage = "$0.4";
        List<String> expectedCoinReturn = new ArrayList<>();
        assertEquals(expectedMessage, actualMessage);
        assertThat(actualCoinReturn, containsInAnyOrder(expectedCoinReturn.toArray()));
    }

    @Test
    void insert_coins_test_rejecting_invalid_coins() {
        List<String> coins = List.of("dollar", "cent");
        when(coinInsertionModule.acceptCoins(anyList())).thenReturn(false);
        when(coinInsertionModule.getAcceptedAmount()).thenReturn(0.0);
        when(coinInsertionModule.takeRejectedCoinsBack()).thenReturn(List.of("dollar", "cent"));

        Pair<String, List<String>> actualOutput = machineUnderTest.insertCoins(coins);
        String actualMessage = actualOutput.getValue(VendingMachine.ParameterOrder.MESSAGE.ordinal()).toString();
        List<String> actualCoinReturn = (List<String>)actualOutput.getValue(VendingMachine.ParameterOrder.COIN_RETURN.ordinal());

        String expectedMessage = "INSERT COIN";
        List<String> expectedCoinReturn = coins;
        assertEquals(expectedMessage, actualMessage);
        assertThat(actualCoinReturn, containsInAnyOrder(expectedCoinReturn.toArray()));
    }

    @Test
    void insert_coins_test_accepting_valid_coins_and_returning_invalid_ones() {
        List<String> coins = List.of("nickel", "dime", "quarter", "dollar", "cent");
        when(coinInsertionModule.acceptCoins(anyList())).thenReturn(false);
        when(coinInsertionModule.getAcceptedAmount()).thenReturn(0.4);
        when(coinInsertionModule.takeRejectedCoinsBack()).thenReturn(List.of("dollar", "cent"));

        Pair<String, List<String>> actualOutput = machineUnderTest.insertCoins(coins);
        String actualMessage = actualOutput.getValue(VendingMachine.ParameterOrder.MESSAGE.ordinal()).toString();
        List<String> actualCoinReturn = (List<String>)actualOutput.getValue(VendingMachine.ParameterOrder.COIN_RETURN.ordinal());

        String expectedMessage = "$0.4";
        List<String> expectedCoinReturn = List.of("dollar", "cent");
        assertEquals(expectedMessage, actualMessage);
        assertThat(actualCoinReturn, containsInAnyOrder(expectedCoinReturn.toArray()));
    }
}
