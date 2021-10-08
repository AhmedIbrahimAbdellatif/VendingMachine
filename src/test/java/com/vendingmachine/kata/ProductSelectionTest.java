package com.vendingmachine.kata;

import com.vendingmachine.kata.ProductSelection.ProductSelectionModuleImpl;
import com.vendingmachine.kata.Repo.ProductRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
}
