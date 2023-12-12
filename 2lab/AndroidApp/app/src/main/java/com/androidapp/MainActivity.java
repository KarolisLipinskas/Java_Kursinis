package com.androidapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.androidapp.entities.Cart;
import com.androidapp.entities.Customer;
import com.androidapp.helpers.Rest;
import com.androidapp.jsonserializers.LocalDateSerializer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.time.LocalDate;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import static com.androidapp.helpers.Constants.*;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void validateCustomer(View view) {
        TextView name = findViewById(R.id.nameField);
        TextView password = findViewById(R.id.passwordField);

        Gson gson = new Gson();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("username", name.getText().toString());
        jsonObject.addProperty("password", password.getText().toString());
        String jsonData = gson.toJson(jsonObject);

        Executor executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(()->{
            try {
                String response = Rest.sendPost(GET_CUSTOMER_BY_LOGIN, jsonData);
                System.out.println(response);
                handler.post(()->{
                    try {
                        if (!response.equals("Error")) {
                            /*Gson customerGson = new Gson();
                            Customer connectedCustomer = customerGson.fromJson(response, Customer.class);*/
                            createOpenCartIfNoExist(response);
                            Intent intent = new Intent(MainActivity.this, MenuActivity.class);
                            intent.putExtra("customerObject", response);
                            startActivity(intent);
                        }
                        else {
                            System.out.println("Wrong username or password");
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

    public void goToRegisterPage(View view) {
        Executor executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(()->{
            try {
                String response = Rest.sendGet(GET_ALL_CUSTOMERS);
                System.out.println(response);
                handler.post(()->{
                    try {
                        if (!response.equals("Error")) {
                            Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                            intent.putExtra("customerObject", response);
                            startActivity(intent);
                        }
                        else {
                            System.out.println("Wrong username or password");
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