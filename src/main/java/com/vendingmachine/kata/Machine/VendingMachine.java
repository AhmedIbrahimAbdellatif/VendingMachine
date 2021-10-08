package com.vendingmachine.kata.Machine;

import java.util.ArrayList;
import java.util.List;

import com.vendingmachine.kata.CoinInsertion.CoinInsertionModule;
import com.vendingmachine.kata.MoneyChange.MoneyChangeModule;
import com.vendingmachine.kata.ProductSelection.ProductSelectionModule;
import org.javatuples.Pair;
import org.javatuples.Triplet;

public class VendingMachine {
    private static final String DEFAULT_MESSAGE = "INSERT COIN";
    private static final String NO_MONEY_FOR_CHANGE_MESSAGE = "EXACT CHANGE ONLY";
    private static final String INSERTION_PROMPT_MESSAGE = "INSERT COIN";
    private static final String SUCCESS_MESSAGE = "THANK YOU";
    private static final String INVALID_PRODUCT_MESSAGE = "CANNOT FIND PRODUCT";


    public static enum ParameterOrder {
        MESSAGE,
        MONEY_RETURN,
        PRODUCT
    }

    private final CoinInsertionModule coinInsertionModule;
    private final MoneyChangeModule moneyChangeModule;
    private final ProductSelectionModule productSelectionModule;

    public VendingMachine(CoinInsertionModule coinInsertionModule, MoneyChangeModule moneyChangeModule, ProductSelectionModule productSelectionModule) {
        this.coinInsertionModule = coinInsertionModule;
        this.moneyChangeModule = moneyChangeModule;
        this.productSelectionModule = productSelectionModule;
    }

    public String start() {
        String message;
        if(moneyChangeModule.isMoneyAvailable()) {
            message = DEFAULT_MESSAGE;
        }
        else {
            message = NO_MONEY_FOR_CHANGE_MESSAGE;
        }
        return message;
    }

    public Pair<String, List<String>> insertCoins(List<String> coins) {

        boolean isAllCoinsAccepted = coinInsertionModule.acceptCoins(coins);

        String message = updateCoinInsertionMessage();

        List<String> rejectedCoins = new ArrayList<>();
        if(!isAllCoinsAccepted) {
            rejectedCoins = coinInsertionModule.takeRejectedCoinsBack();
        }

        return Pair.with(message, rejectedCoins);
    }

    private String updateCoinInsertionMessage() {
        String message;
        if(coinInsertionModule.getAcceptedAmount() == 0.0) {
            message = INSERTION_PROMPT_MESSAGE;
        }
        else {
            message = "$" + coinInsertionModule.getAcceptedAmount().toString();
        }
        return message;
    }

    public Triplet<String, Double, String> buyProduct(String product) {
        String message = "";
        Double changeReturn = 0.0;
        String productOut = "";
        boolean isSelectSuccessful = productSelectionModule.selectProduct(product);
        if(isSelectSuccessful) {
            Double moneyChange = coinInsertionModule.getAcceptedAmount() - productSelectionModule.getSelectedProductPrice();
            if(moneyChange == 0.0) {
                message = SUCCESS_MESSAGE;
                changeReturn = moneyChange;
                productOut = product;
            }
        }
        return Triplet.with(message, changeReturn, productOut);
    }
}
