package model.Calculations;

import model.entities.Cart;
import model.entities.Product;

import java.util.ArrayList;
import java.util.List;

public class CartCalculation {
    public static double calculateTotal(List<Product> products) {
        double total = 0;
        List<Double> prices = calculateItemPrices(new ArrayList<>(products));
        int n = prices.size();
        for (int i = 0; i < n; i++) {
            total += prices.get(i);
        }
        total = Math.round(total * 100.0) / 100.0;
        return total;
    }

    public static List<Double> calculateItemPrices(List<Product> products) {
        List<Double> result = new ArrayList<>();
        List<Integer> quantities = calculateQuantities(new ArrayList<>(products));
        int n = quantities.size();
        for (int i = 0; i < n; i++) {
            Product product = products.get(i + quantities.get(i) - 1);
            result.add(Math.round(product.getPrice() * quantities.get(i) * 100.0) / 100.0);
        }
        return result;
    }

    public static List<Integer> calculateQuantities(List<Product> products){
        List<Integer> result = new ArrayList<>();
        int n = products.size();
        while (n > 0) {
            int quant = 1;
            Product product = products.get(0);
            products.remove(0);
            n--;
            for (int j = 0; j < n; j++) {
                if (product.getName().equals(products.get(j).getName())
                        && product.getType().equals(products.get(j).getType())) {
                    quant++;
                    products.remove(j);
                    n--;
                    j--;
                }
            }
            result.add(quant);
        }
        return result;
    }
}
