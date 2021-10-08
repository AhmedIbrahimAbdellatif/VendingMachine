package com.vendingmachine.kata;

import com.vendingmachine.kata.CoinInsertion.CoinInsertionModule;
import com.vendingmachine.kata.Machine.VendingMachine;
import com.vendingmachine.kata.MoneyChange.MoneyChangeModule;
import com.vendingmachine.kata.ProductSelection.ProductSelectionModule;
import org.javatuples.Pair;
import org.javatuples.Triplet;
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
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
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
    @Mock
    ProductSelectionModule productSelectionModule;

    @BeforeEach
    void setUp() {
        machineUnderTest = new VendingMachine(coinInsertionModule, moneyChangeModule, productSelectionModule);
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
        List<String> actualCoinReturn = (List<String>)actualOutput.getValue(VendingMachine.ParameterOrder.MONEY_RETURN.ordinal());

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
        List<String> actualCoinReturn = (List<String>)actualOutput.getValue(VendingMachine.ParameterOrder.MONEY_RETURN.ordinal());

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
        List<String> actualCoinReturn = (List<String>)actualOutput.getValue(VendingMachine.ParameterOrder.MONEY_RETURN.ordinal());

        String expectedMessage = "$0.4";
        List<String> expectedCoinReturn = List.of("dollar", "cent");
        assertEquals(expectedMessage, actualMessage);
        assertThat(actualCoinReturn, containsInAnyOrder(expectedCoinReturn.toArray()));
    }

    @Test
    void buy_product_test_valid_product_money_equal_price() {
        String product = "cola";
        when(productSelectionModule.selectProduct(anyString())).thenReturn(true);
        when(productSelectionModule.getSelectedProductPrice()).thenReturn(1.00);
        when(coinInsertionModule.getAcceptedAmount()).thenReturn(1.00);

        Triplet<String, Double, String> actualOutput = machineUnderTest.buyProduct(product);
        String actualMessage = actualOutput.getValue(VendingMachine.ParameterOrder.MESSAGE.ordinal()).toString();
        Double actualChangeReturn = (Double)actualOutput.getValue(VendingMachine.ParameterOrder.MONEY_RETURN.ordinal());
        String actualProductOut = actualOutput.getValue(VendingMachine.ParameterOrder.PRODUCT.ordinal()).toString();

        String expectedMessage = "THANK YOU";
        Double expectedChangeReturn = 0.0;
        String expectedProductOut = "cola";
        assertEquals(expectedMessage, actualMessage);
        assertEquals(expectedChangeReturn, actualChangeReturn);
        assertEquals(expectedProductOut, actualProductOut);
    }

    @Test
    void buy_product_test_valid_product_money_more_than_price_change_available() {
        String product = "cola";
        when(productSelectionModule.selectProduct(anyString())).thenReturn(true);
        when(productSelectionModule.getSelectedProductPrice()).thenReturn(1.00);
        when(coinInsertionModule.getAcceptedAmount()).thenReturn(1.50);
        when(moneyChangeModule.getAvailableMoney()).thenReturn(1.00);
        doNothing().when(moneyChangeModule).decreaseMoneyBy(anyDouble());

        Triplet<String, Double, String> actualOutput = machineUnderTest.buyProduct(product);
        String actualMessage = actualOutput.getValue(VendingMachine.ParameterOrder.MESSAGE.ordinal()).toString();
        Double actualChangeReturn = (Double)actualOutput.getValue(VendingMachine.ParameterOrder.MONEY_RETURN.ordinal());
        String actualProductOut = actualOutput.getValue(VendingMachine.ParameterOrder.PRODUCT.ordinal()).toString();

        String expectedMessage = "THANK YOU";
        Double expectedChangeReturn = 0.50;
        String expectedProductOut = "cola";
        assertEquals(expectedMessage, actualMessage);
        assertEquals(expectedChangeReturn, actualChangeReturn);
        assertEquals(expectedProductOut, actualProductOut);
    }

    @Test
    void buy_product_test_valid_product_money_more_than_price_no_change_available() {
        String product = "cola";
        when(productSelectionModule.selectProduct(anyString())).thenReturn(true);
        when(productSelectionModule.getSelectedProductPrice()).thenReturn(1.00);
        when(coinInsertionModule.getAcceptedAmount()).thenReturn(1.50);
        when(moneyChangeModule.getAvailableMoney()).thenReturn(0.40);
        when(coinInsertionModule.returnAllAcceptedAmount()).thenReturn(1.50);

        Triplet<String, Double, String> actualOutput = machineUnderTest.buyProduct(product);
        String actualMessage = actualOutput.getValue(VendingMachine.ParameterOrder.MESSAGE.ordinal()).toString();
        Double actualChangeReturn = (Double)actualOutput.getValue(VendingMachine.ParameterOrder.MONEY_RETURN.ordinal());
        String actualProductOut = actualOutput.getValue(VendingMachine.ParameterOrder.PRODUCT.ordinal()).toString();

        String expectedMessage = "EXACT CHANGE ONLY";
        Double expectedChangeReturn = 1.5;
        String expectedProductOut = "NONE";
        assertEquals(expectedMessage, actualMessage);
        assertEquals(expectedChangeReturn, actualChangeReturn);
        assertEquals(expectedProductOut, actualProductOut);
    }

    @Test
    void buy_product_test_valid_product_money_less_than_price() {
        String product = "cola";
        when(productSelectionModule.selectProduct(anyString())).thenReturn(true);
        when(productSelectionModule.getSelectedProductPrice()).thenReturn(1.00);
        when(coinInsertionModule.getAcceptedAmount()).thenReturn(0.50);

        Triplet<String, Double, String> actualOutput = machineUnderTest.buyProduct(product);
        String actualMessage = actualOutput.getValue(VendingMachine.ParameterOrder.MESSAGE.ordinal()).toString();
        Double actualChangeReturn = (Double)actualOutput.getValue(VendingMachine.ParameterOrder.MONEY_RETURN.ordinal());
        String actualProductOut = actualOutput.getValue(VendingMachine.ParameterOrder.PRODUCT.ordinal()).toString();

        String expectedMessage = "INSERT COIN";
        Double expectedChangeReturn = 0.0;
        String expectedProductOut = "NONE";
        assertEquals(expectedMessage, actualMessage);
        assertEquals(expectedChangeReturn, actualChangeReturn);
        assertEquals(expectedProductOut, actualProductOut);
    }

    @Test
    void buy_product_test_invalid_product() {
        String product = "ice cream";
        when(productSelectionModule.selectProduct(anyString())).thenReturn(false);
        when(coinInsertionModule.returnAllAcceptedAmount()).thenReturn(1.50);

        Triplet<String, Double, String> actualOutput = machineUnderTest.buyProduct(product);
        String actualMessage = actualOutput.getValue(VendingMachine.ParameterOrder.MESSAGE.ordinal()).toString();
        Double actualChangeReturn = (Double)actualOutput.getValue(VendingMachine.ParameterOrder.MONEY_RETURN.ordinal());
        String actualProductOut = actualOutput.getValue(VendingMachine.ParameterOrder.PRODUCT.ordinal()).toString();

        String expectedMessage = "CANNOT FIND PRODUCT";
        Double expectedChangeReturn = 1.50;
        String expectedProductOut = "NONE";
        assertEquals(expectedMessage, actualMessage);
        assertEquals(expectedChangeReturn, actualChangeReturn);
        assertEquals(expectedProductOut, actualProductOut);
    }

    @Test
    void return_back_all_money() {
        when(coinInsertionModule.returnAllAcceptedAmount()).thenReturn(1.50);

        Double actualMoneyReturn = machineUnderTest.returnBackAllMoney();

        Double expectedMoneyReturn = 1.50;
        assertEquals(expectedMoneyReturn, actualMoneyReturn);
    }
}
