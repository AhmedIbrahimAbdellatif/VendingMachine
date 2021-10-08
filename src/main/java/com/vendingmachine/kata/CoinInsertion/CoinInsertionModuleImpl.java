package com.vendingmachine.kata.CoinInsertion;

import com.vendingmachine.kata.Repo.CoinStorageRepo;
import org.javatuples.Pair;

import java.util.*;

public class CoinInsertionModuleImpl implements CoinInsertionModule{
    private CoinStorageRepo coinStorageRepo;
    public CoinInsertionModuleImpl(CoinStorageRepo coinStorageRepo) {
        this.coinStorageRepo = coinStorageRepo;
    }

    private enum Coins {
        nickles(Pair.with("nickle", 0.05)),
        dimes(Pair.with("dime", 0.1)),
        quarters(Pair.with("quarter", 0.25));

        Pair<String, Double> nameValuePair;
        Coins(Pair<String, Double> nameValuePair) {
            this.nameValuePair = nameValuePair;
        }

        private String getName() {
            return (String)this.nameValuePair.getValue(0);
        }

        private Double getNameValuePair() {
            return (Double) this.nameValuePair.getValue(1);
        }

        public static HashSet<String> namesSet() {
            HashSet<String> nameSet = new HashSet<>();
            for (Coins c: Coins.values()) {
                nameSet.add(c.getName());
            }
            return nameSet;
        }

        public static HashMap<String, Double> nameValueSet(){
            HashMap<String, Double> coinSet = new HashMap<>();
            for (Coins c: Coins.values()) {
                coinSet.put(c.getName(), c.getNameValuePair());
            }
            return coinSet;
        }
    }

    private static HashSet<String> validCoins = Coins.namesSet();
    private static HashMap<String, Double> coinsNameValue = Coins.nameValueSet();
    @Override
    public boolean acceptCoins(List<String> coins) {
        boolean isAllValid = true;
        for (String coin : coins) {
            if (!validCoins.contains(coin)) {
                isAllValid = false;
            }
            else {
                coinStorageRepo.add(coin, coinsNameValue.get(coin));
            }
        }
        return isAllValid;
    }

    @Override
    public Double getAcceptedAmount() {
        return null;
    }

    @Override
    public List<String> returnRejectedCoinsBack() {
        return null;
    }

    @Override
    public Double returnAllAcceptedAmount() {
        return null;
    }
}
