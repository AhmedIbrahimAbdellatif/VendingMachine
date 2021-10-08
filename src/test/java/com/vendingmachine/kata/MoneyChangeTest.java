package com.vendingmachine.kata;

import com.vendingmachine.kata.MoneyChange.MoneyChangeModuleImpl;
import com.vendingmachine.kata.Repo.ChangeStorageRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@ExtendWith(MockitoExtension.class)
public class MoneyChangeTest {
    @InjectMocks
    MoneyChangeModuleImpl moduleUnderTest;
    @Mock
    ChangeStorageRepo changeStorageRepo;

    @BeforeEach
    void setUp() {
        moduleUnderTest = new MoneyChangeModuleImpl(changeStorageRepo);
    }

    @ParameterizedTest
    @CsvSource({"0.0, false", "1.50, true"})
    void test_checking_is_money_available(Double storedMoney, boolean expected) {
        when(changeStorageRepo.getStoredMoney()).thenReturn(storedMoney);

        boolean actual = moduleUnderTest.isMoneyAvailable();

        assertEquals(expected, actual);
    }

    @Test
    void test_getting_available_money(){
        moduleUnderTest.getAvailableMoney();

        verify(changeStorageRepo, times(1)).getStoredMoney();
    }

    @Test
    void test_decreasing_money() {
        moduleUnderTest.decreaseMoneyBy(1.00);

        verify(changeStorageRepo, times(1)).decreaseMoney(1.00);
    }
}
