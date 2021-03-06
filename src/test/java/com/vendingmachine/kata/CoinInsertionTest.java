package com.vendingmachine.kata;

import com.vendingmachine.kata.CoinInsertion.CoinInsertionModuleImpl;
import com.vendingmachine.kata.Repo.CoinStorageRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@ExtendWith(MockitoExtension.class)
public class CoinInsertionTest {
    @InjectMocks
    CoinInsertionModuleImpl moduleUnderTest;

    @Mock
    CoinStorageRepo coinStorageRepo;

    @BeforeEach
    void setUp() {
        moduleUnderTest = new CoinInsertionModuleImpl(coinStorageRepo);
    }

    @Test
    void test_accepting_all_valid_coins() {
        List<String> coins = List.of("nickle", "dime", "quarter");

        boolean actual = moduleUnderTest.acceptCoins(coins);

        verify(coinStorageRepo, times(1)).addAccepted("nickle", 0.05);
        verify(coinStorageRepo, times(1)).addAccepted("dime", 0.1);
        verify(coinStorageRepo, times(1)).addAccepted("quarter", 0.25);
        verifyNoMoreInteractions(coinStorageRepo);
        assertTrue(actual);
    }

    @Test
    void test_detecting_invalid_coins() {
        List<String> coins = List.of("nickle", "dime", "quarter", "cent");

        boolean actual = moduleUnderTest.acceptCoins(coins);

        verify(coinStorageRepo, times(1)).addAccepted("nickle", 0.05);
        verify(coinStorageRepo, times(1)).addAccepted("dime", 0.1);
        verify(coinStorageRepo, times(1)).addAccepted("quarter", 0.25);
        verify(coinStorageRepo, times(1)).addRejected("cent");
        verifyNoMoreInteractions(coinStorageRepo);
        assertFalse(actual);
    }

    @Test
    void test_get_accepted_amount() {
        moduleUnderTest.getAcceptedAmount();

        verify(coinStorageRepo, times(1)).getStoredAmount();
        verifyNoMoreInteractions(coinStorageRepo);
    }

    @Test
    void test_get_rejected_coins() {
        moduleUnderTest.returnRejectedCoinsBack();

        verify(coinStorageRepo, times(1)).popRejectedCoins();
        verifyNoMoreInteractions(coinStorageRepo);
    }

    @Test
    void test_returning_accepted_coins() {
        moduleUnderTest.returnAllAcceptedAmount();

        verify(coinStorageRepo, times(1)).popAcceptedAmount();
        verifyNoMoreInteractions(coinStorageRepo);
    }
}
