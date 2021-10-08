package com.vendingmachine.kata.ProductSelection;

import com.vendingmachine.kata.CoinInsertion.CoinInsertionModuleImpl;
import com.vendingmachine.kata.Repo.ProductRepo;
import org.javatuples.Pair;

import java.util.HashMap;
import java.util.HashSet;

public class ProductSelectionModuleImpl implements ProductSelectionModule{
    private static final String UNSELECTED = "NONE";
    private final ProductRepo productRepo;

    public ProductSelectionModuleImpl(ProductRepo productRepo) {
        this.productRepo = productRepo;
    }

    private enum Products {
        cola(Pair.with("cola", 1.00)),
        chips(Pair.with("chips", 0.50)),
        candy(Pair.with("candy", 0.65));

        Pair<String, Double> nameValuePair;
        Products(Pair<String,Double> nameValuePair) {
            this.nameValuePair = nameValuePair;
        }
        private String getName() {
            return (String)this.nameValuePair.getValue(0);
        }

        private Double getValue() {
            return (Double) this.nameValuePair.getValue(1);
        }

        public static HashSet<String> namesSet() {
            HashSet<String> nameSet = new HashSet<>();
            for (Products p: Products.values()) {
                nameSet.add(p.getName());
            }
            return nameSet;
        }

        public static HashMap<String, Double> nameValueSet(){
            HashMap<String, Double> productSet = new HashMap<>();
            for (Products p: Products.values()) {
                productSet.put(p.getName(), p.getValue());
            }
            return productSet;
        }
    }
    private static HashSet<String> validProducts = Products.namesSet();
    @Override
    public boolean selectProduct(String product) {
        boolean result = false;
        if(validProducts.contains(product)) {
            boolean isAvailable = productRepo.isProductAvailable(product);
            String selectedProduct =  isAvailable ? product : UNSELECTED;
            productRepo.select(selectedProduct);
            result = isAvailable;
        }
        return result;
    }

    @Override
    public Double getSelectedProductPrice() {
        return null;
    }
}
