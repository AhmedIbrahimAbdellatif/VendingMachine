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
    private static final String NO_PRODUCT_OUT = "NONE";

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
        String message; Double changeReturn; String productOut;
        boolean isSelectSuccessful = productSelectionModule.selectProduct(product);
        Double moneyChange = coinInsertionModule.getAcceptedAmount() - productSelectionModule.getSelectedProductPrice();
        message = updateMessageForBuyingProduct(moneyChange, isSelectSuccessful);
        changeReturn = updateChangeReturnForBuyingProduct(moneyChange, isSelectSuccessful);
        productOut = updateProductOutForBuyingProduct(moneyChange, product, isSelectSuccessful);
        return Triplet.with(message, changeReturn, productOut);
    }
    private String updateMessageForBuyingProduct(Double moneyChange, boolean isSelectSuccessful) {
        String message;
        if(isSelectSuccessful == false) {
            message = INVALID_PRODUCT_MESSAGE;
        }
        else if(moneyChange < 0.0) {
            message = INSERTION_PROMPT_MESSAGE;
        }
        else if(moneyChange > moneyChangeModule.getAvailableMoney()) {
            message = NO_MONEY_FOR_CHANGE_MESSAGE;
        }
        else {
            message = SUCCESS_MESSAGE;
        }
        return message;
    }

    private Double updateChangeReturnForBuyingProduct(Double moneyChange, boolean isSelectSuccessful) {
        Double changeReturn;
        if(isSelectSuccessful == false || moneyChange > moneyChangeModule.getAvailableMoney()) {
            changeReturn = coinInsertionModule.returnAllAcceptedAmount();
        }
        else if(moneyChange < 0.0) {
            changeReturn = 0.0;
        }
        else {
            changeReturn = moneyChange;
            moneyChangeModule.decreaseMoneyBy(moneyChange);
        }
        return changeReturn;
    }

    private String updateProductOutForBuyingProduct(Double moneyChange, String product, boolean isSelectSuccessful) {
        String productOut;
        if(isSelectSuccessful && (moneyChange == 0.0 || (moneyChange > 0.0 && moneyChange < moneyChangeModule.getAvailableMoney()))) {
            productOut = product;
        }
        else {
            productOut = NO_PRODUCT_OUT;
        }
        return productOut;
    }
}