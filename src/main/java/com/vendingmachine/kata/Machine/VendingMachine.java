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

        String message = getMessageAfterCoinInsertion();

        List<String> rejectedCoins = new ArrayList<>();
        if(!isAllCoinsAccepted) {
            rejectedCoins = coinInsertionModule.returnRejectedCoinsBack();
        }

        return Pair.with(message, rejectedCoins);
    }

    private String getMessageAfterCoinInsertion() {
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
        String message;
        Double moneyReturn;
        String productOut;
        boolean isSelectSuccessful = productSelectionModule.selectProduct(product);
        Double moneyDifference = coinInsertionModule.getAcceptedAmount() - productSelectionModule.getSelectedProductPrice();
        message = getMessageAfterBuyingAttempt(moneyDifference, isSelectSuccessful);
        moneyReturn = getMoneyChangeAfterBuyingAttempt(moneyDifference, isSelectSuccessful);
        productOut = getProductOutAfterBuyingAttempt(moneyDifference, product, isSelectSuccessful);
        if(! productOut.equals(NO_PRODUCT_OUT)) productSelectionModule.disposeSelectedProduct();
        return Triplet.with(message, moneyReturn, productOut);
    }
    private String getMessageAfterBuyingAttempt(Double moneyDifference, boolean isSelectSuccessful) {
        String message;
        if(isSelectSuccessful == false) {
            message = INVALID_PRODUCT_MESSAGE;
        }
        else if(moneyDifference < 0.0) {
            message = INSERTION_PROMPT_MESSAGE;
        }
        else if(moneyDifference > moneyChangeModule.getAvailableMoney()) {
            message = NO_MONEY_FOR_CHANGE_MESSAGE;
        }
        else {
            message = SUCCESS_MESSAGE;
        }
        return message;
    }

    private Double getMoneyChangeAfterBuyingAttempt(Double moneyDifference, boolean isSelectSuccessful) {
        Double moneyReturn;
        if(isSelectSuccessful == false || moneyDifference > moneyChangeModule.getAvailableMoney()) {
            moneyReturn = coinInsertionModule.returnAllAcceptedAmount();
        }
        else if(moneyDifference < 0.0) {
            moneyReturn = 0.0;
        }
        else {
            moneyReturn = moneyDifference;
            moneyChangeModule.decreaseMoneyBy(moneyDifference);
        }
        return moneyReturn;
    }

    private String getProductOutAfterBuyingAttempt(Double moneyDifference, String product, boolean isSelectSuccessful) {
        String productOut;
        if(isSelectSuccessful && (moneyDifference == 0.0 || (moneyDifference > 0.0 && moneyDifference < moneyChangeModule.getAvailableMoney()))) {
            productOut = product;
        }
        else {
            productOut = NO_PRODUCT_OUT;
        }
        return productOut;
    }

    public Double returnBackAllMoney() {
        return coinInsertionModule.returnAllAcceptedAmount();
    }
}