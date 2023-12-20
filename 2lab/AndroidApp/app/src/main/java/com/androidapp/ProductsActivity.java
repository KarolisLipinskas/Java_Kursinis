package com.androidapp;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.androidapp.entities.Cart;
import com.androidapp.entities.Customer;
import com.androidapp.entities.Product;
import com.androidapp.helpers.Rest;
import com.androidapp.jsonserializers.LocalDateSerializer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import static com.androidapp.helpers.Constants.*;

public class ProductsActivity extends AppCompatActivity {

    String customerInfo;
    Customer connectedCustomer;
    String[] types = {"All", "Bike", "Brakes", "Fork", "Frame", "Handlebars", "Pedals", "Shock", "Wheels"};
    AutoCompleteTextView autoCompleteTextView;
    ArrayAdapter<String> adapterItems;
    List<Product> productListFromJson;
    Product selection = null;

    @SuppressLint("NewApi")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);

        Intent intent = getIntent();
        customerInfo = intent.getStringExtra("customerObject");
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(LocalDate.class, new LocalDateSerializer());
        Gson customerGson = builder.create();
        connectedCustomer = customerGson.fromJson(customerInfo, Customer.class);
        System.out.println("Products page");
        System.out.println(connectedCustomer);

        autoCompleteTextView = findViewById(R.id.typeSelection);
        adapterItems = new ArrayAdapter<>(this, R.layout.list_item, types);

        autoCompleteTextView.setAdapter(adapterItems);

        autoCompleteTextView.setOnItemClickListener((adapterView, view, i, l) -> {
            String item = adapterView.getItemAtPosition(i).toString();
            Toast.makeText(ProductsActivity.this, "Item: " + item, Toast.LENGTH_SHORT).show();
        });

        Executor executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(()->{
            try {
                String response = Rest.sendGet(GET_ALL_PRODUCTS);
                System.out.println(response);
                handler.post(()->{
                    try {
                        if (!response.equals("Error")) {
                            Gson productsGson = new Gson();
                            Type productListType = new TypeToken<List<Product>>(){}.getType();
                            productListFromJson = productsGson.fromJson(response, productListType);

                            setProductList(productListFromJson);
                        }
                        else {
                            System.out.println("Error");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        System.out.println(autoCompleteTextView.getText().toString());
    }

    public void setProductList(List<Product> list) {
        ListView listView = findViewById(R.id.productList);

        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener((parent, view, position, id) -> {
            selection = list.get(position);
            System.out.println(selection);

            AlertDialog.Builder builder = new AlertDialog.Builder(ProductsActivity.this);
            builder.setMessage("Selected item: " + selection.getName());
            builder.setCancelable(true);
            builder.setNeutralButton("OK", (dialog, which) -> dialog.cancel());
            AlertDialog alertPopup = builder.create();
            alertPopup.show();
        });
    }

    public void filterProducts(View view) {
        TextView priceMin = findViewById(R.id.priceMin);
        TextView priceMax = findViewById(R.id.priceMax);

        String type = autoCompleteTextView.getText().toString();

        double min = 0.0;
        if (!priceMin.getText().toString().isEmpty()) min = Double.parseDouble(priceMin.getText().toString());

        double max = 999999.9;
        if (!priceMax.getText().toString().isEmpty()) max = Double.parseDouble(priceMax.getText().toString());

        System.out.println(type + "--" + min + "--" + max);

        List<Product> filteredList = new ArrayList<>();
        for (Product product: productListFromJson) {
            if (type.equals("All")) {
                if (product.getPrice() >= min && product.getPrice() <= max) filteredList.add(product);
            }
            else if(product.getType().equals(type) && product.getPrice() >= min && product.getPrice() <= max) filteredList.add(product);
        }

        setProductList(filteredList);
    }

    public void openMenuPagefromProducts(View view) {
        Executor executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(()->{
            try {
                String response = Rest.sendGet(GET_CUSTOMER_BY_ID + connectedCustomer.getId());
                System.out.println(response);
                handler.post(()->{
                    try {
                        if (!response.equals("Error")) {
                            Intent intent = new Intent(ProductsActivity.this, MenuActivity.class);
                            intent.putExtra("customerObject", response);
                            startActivity(intent);
                        }
                        else {
                            System.out.println("Error");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    @SuppressLint("NewApi")
    public void addToCart(View view) {
        System.out.println("TEST: +++ " + connectedCustomer.getCartList().get(connectedCustomer.getCartList().size()-1));

        if (selection == null) return;

        Cart cart = connectedCustomer.getCartList().get(connectedCustomer.getCartList().size()-1);

        Product product = selection;
        product.setId(0);
        product.setCart(cart);
        String newProductInfo = new Gson().toJson(product);

        Executor executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());
        executor.execute(()->{
            try {
                String response = Rest.sendPost(ADD_NEW_PRODUCT, newProductInfo);
                System.out.println(response);
                handler.post(()->{
                    try {
                        if (!response.equals("Error")) {
                            Gson cartGson = new Gson();
                            Product product1 = cartGson.fromJson(response, Product.class);
                            System.out.println("PRODUCT: " + product1);

                            updateCart(cart);

                            AlertDialog.Builder builder = new AlertDialog.Builder(ProductsActivity.this);
                            builder.setMessage("Added item: " + product1.getName());
                            builder.setCancelable(true);
                            builder.setNeutralButton("OK", (dialog, which) -> dialog.cancel());
                            AlertDialog alertPopup = builder.create();
                            alertPopup.show();
                        }
                        else {
                            System.out.println("Error");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public void updateCart(Cart cart) {
        String lastCartInfo = new Gson().toJson(cart);

        Executor executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(()->{
            try {
                String response = Rest.sendPut(UPDATE_CART, lastCartInfo);
                System.out.println(response);
                handler.post(()->{
                    try {
                        if (!response.equals("Error")) {
                            Gson cartGson = new Gson();
                            System.out.println(cartGson.fromJson(response, Cart.class));
                        }
                        else {
                            System.out.println("Error");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
