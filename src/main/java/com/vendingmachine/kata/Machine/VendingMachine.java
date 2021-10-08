package com.vendingmachine.kata.Machine;

import java.util.ArrayList;
import java.util.List;

import com.vendingmachine.kata.CoinInsertion.CoinInsertionModule;
import com.vendingmachine.kata.MoneyChange.MoneyChangeModule;
import org.javatuples.Pair;

public class VendingMachine {
    private static final String DEFAULT_MESSAGE = "INSERT COIN";
    private static final String NO_MONEY_FOR_CHANGE_MESSAGE = "EXACT CHANGE ONLY";
    private static final String INSERTION_PROMPT_MESSAGE = "INSERT COIN";
    public static enum ParameterOrder {
        MESSAGE,
        COIN_RETURN
    }

    private final CoinInsertionModule coinInsertionModule;
    private final MoneyChangeModule moneyChangeModule;

    public VendingMachine(CoinInsertionModule coinInsertionModule, MoneyChangeModule moneyChangeModule) {
        this.coinInsertionModule = coinInsertionModule;
        this.moneyChangeModule = moneyChangeModule;
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
}
