package com.androidapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.androidapp.entities.Cart;
import com.androidapp.entities.Customer;
import com.androidapp.entities.Product;
import com.androidapp.helpers.Rest;
import com.androidapp.jsonserializers.LocalDateSerializer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import static com.androidapp.helpers.Constants.*;

public class CartActivity extends AppCompatActivity {
    String customerInfo;
    Customer connectedCustomer;
    Product selection = null;
    Cart cart;

    @SuppressLint("NewApi")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        Intent intent = getIntent();
        customerInfo = intent.getStringExtra("customerObject");
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(LocalDate.class, new LocalDateSerializer());
        Gson gson = builder.create();
        connectedCustomer = gson.fromJson(customerInfo, Customer.class);

        cart = connectedCustomer.getCartList().get(connectedCustomer.getCartList().size()-1);
        System.out.println("HERE: === " + cart);

        //irasomi duomenys
        setProductList(cart.getProducts());
        TextView status = findViewById(R.id.status);
        TextView totalPrice = findViewById(R.id.cartPrice);
        status.setText(cart.getStatus());
        totalPrice.setText(Double.toString(cart.getPrice()));

    }

    @SuppressLint("NewApi")
    public void updateCart(Cart cart) {
        String lastCartInfo = new Gson().toJson(cart);

        System.out.println(lastCartInfo);

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
                            refreshPage();
                        }
                        else {
                            System.out.println("Error in update cart");
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

    public void refreshPage() {
        Executor executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(()->{
            try {
                String response = Rest.sendGet(GET_CUSTOMER_BY_ID + connectedCustomer.getId());
                System.out.println(response);
                handler.post(()->{
                    try {
                        if (!response.equals("Error")) {
                            Intent intent = new Intent(CartActivity.this, CartActivity.class);
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

    public void setProductList(List<Product> list) {
        ListView listView = findViewById(R.id.productListInCart);

        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener((parent, view, position, id) -> {
            selection = list.get(position);
            System.out.println(selection);

            AlertDialog.Builder builder = new AlertDialog.Builder(CartActivity.this);
            builder.setMessage("Selected item: " + selection.getName());
            builder.setCancelable(true);
            builder.setNeutralButton("OK", (dialog, which) -> dialog.cancel());
            AlertDialog alertPopup = builder.create();
            alertPopup.show();
        });
    }

    public void openMenuPageFromCart(View view) {
        Executor executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(()->{
            try {
                String response = Rest.sendGet(GET_CUSTOMER_BY_ID + connectedCustomer.getId());
                System.out.println(response);
                handler.post(()->{
                    try {
                        if (!response.equals("Error")) {
                            createOpenCartIfNoExist(response);
                            Intent intent = new Intent(CartActivity.this, MenuActivity.class);
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

    public void removeItem(View view) {
        if (selection == null) return;
        Product product = selection;

        Executor executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());
        executor.execute(()->{
            try {
                String response = Rest.sendDelete(DELETE_PRODUCT + product.getId());
                System.out.println(response);
                handler.post(()->{
                    try {
                        if (!response.equals("Error")) {
                            updateCart(cart);
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

    public void checkout(View view) {
        if (!cart.getStatus().equals("open")) return;

        Cart tempCart = cart;
        tempCart.setStatus("Paid");
        updateCart(tempCart);
    }

    @SuppressLint("NewApi")
    public void createOpenCartIfNoExist(String input) {

        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(LocalDate.class, new LocalDateSerializer());
        Gson gson = builder.create();
        Customer connectedCustomer = gson.fromJson(input, Customer.class);

        System.out.println("Checking");
        if (connectedCustomer.getCartList().isEmpty() || !connectedCustomer.getCartList().get(connectedCustomer.getCartList().size()-1).getStatus().equals("open")) {


            Cart tempCart = new Cart();
            tempCart.setCustomer(connectedCustomer);
            tempCart.setStatus("open");
            String newCartInfo = gson.toJson(tempCart);

            Executor executor = Executors.newSingleThreadExecutor();
            Handler handler = new Handler(Looper.getMainLooper());
            executor.execute(()->{
                try {
                    String response = Rest.sendPost(ADD_NEW_CART, newCartInfo);
                    System.out.println(response);
                    handler.post(()->{
                        try {
                            if (!response.equals("Error")) {
                                Gson cartGson = new Gson();
                                Cart cart = cartGson.fromJson(response, Cart.class);
                                System.out.println("CART ID: " + cart.getId());
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
            System.out.println("Vygdo");
        }
    }
}
