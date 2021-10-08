package com.vendingmachine.kata;

import com.vendingmachine.kata.ProductSelection.ProductSelectionModuleImpl;
import com.vendingmachine.kata.Repo.ProductRepo;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

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

        verify(productRepo, times(1)).select("cola");
        assertTrue(actual);
    }

    @Test
    void test_selecting_valid_unavailable_product() {
        when(productRepo.isProductAvailable(anyString())).thenReturn(false);

        boolean actual = moduleUnderTest.selectProduct("cola");

        verify(productRepo, times(1)).select("NONE");
        assertFalse(actual);
    }

    @Test
    void test_selecting_invalid_product() {
        boolean actual = moduleUnderTest.selectProduct("ice cream");

        verifyNoInteractions(productRepo);
        assertFalse(actual);
    }

    @ParameterizedTest
    @CsvSource({"cola, 1.00", "chips, 0.5", "NONE, 0.0"})
    void test_get_selected_product_price(String product, Double expectedPrice) {
        when(productRepo.getSelected()).thenReturn(product);

        Double actualPrice = moduleUnderTest.getSelectedProductPrice();

        assertEquals(expectedPrice, actualPrice);
    }
}
