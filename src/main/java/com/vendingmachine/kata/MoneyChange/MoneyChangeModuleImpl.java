package com.vendingmachine.kata.MoneyChange;

import com.vendingmachine.kata.Repo.ChangeStorageRepo;

public class MoneyChangeModuleImpl implements MoneyChangeModule{

    private final ChangeStorageRepo changeStorageRepo;
    public MoneyChangeModuleImpl(ChangeStorageRepo changeStorageRepo) {
        this.changeStorageRepo = changeStorageRepo;
    }

    @Override
    public boolean isMoneyAvailable() {
        return changeStorageRepo.getStoredMoney() > 0.0;
    }

    @Override
    public Double getAvailableMoney() {
        return changeStorageRepo.getStoredMoney();
    }

    @Override
    public void decreaseMoneyBy(Double moneyChange) {
        changeStorageRepo.decreaseMoney(moneyChange);
    }
}
