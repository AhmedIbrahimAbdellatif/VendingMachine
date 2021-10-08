package com.vendingmachine.kata;

import com.vendingmachine.kata.ProductSelection.ProductSelectionModuleImpl;
import com.vendingmachine.kata.Repo.ProductRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@ExtendWith(MockitoExtension.class)
public class ProductSelectionTest {
    @InjectMocks
    ProductSelectionModuleImpl moduleUnderTest;
    @Mock
    ProductRepo productRepo;

    @BeforeEach
    void setUp() {
        moduleUnderTest = new ProductSelectionModuleImpl(productRepo);
    }

    @Test
    void test_selecting_valid_available_product() {
        when(productRepo.isProductAvailable(anyString())).thenReturn(true);

        boolean actual = moduleUnderTest.selectProduct("cola");

        assertTrue(actual);
    }

    @Test
    void test_selecting_valid_unavailable_product() {
        when(productRepo.isProductAvailable(anyString())).thenReturn(false);

        boolean actual = moduleUnderTest.selectProduct("cola");

        assertFalse(actual);
    }
}
