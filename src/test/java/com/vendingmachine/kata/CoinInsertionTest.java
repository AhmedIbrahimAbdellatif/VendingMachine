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
    void accept_coins_all_valid() {
        List<String> coins = List.of("nickle", "dime", "quarter");

        boolean actual = moduleUnderTest.acceptCoins(coins);

        verify(coinStorageRepo, times(1)).add("nickle", 0.05);
        verify(coinStorageRepo, times(1)).add("dime", 0.1);
        verify(coinStorageRepo, times(1)).add("quarter", 0.25);
        verifyNoMoreInteractions(coinStorageRepo);
        assertTrue(actual);
    }

    @Test
    void accept_coins_detect_invalid() {
        List<String> coins = List.of("nickle", "dime", "quarter", "cent");

        boolean actual = moduleUnderTest.acceptCoins(coins);

        verify(coinStorageRepo, times(1)).add("nickle", 0.05);
        verify(coinStorageRepo, times(1)).add("dime", 0.1);
        verify(coinStorageRepo, times(1)).add("quarter", 0.25);
        verifyNoMoreInteractions(coinStorageRepo);
        assertFalse(actual);
    }
}
