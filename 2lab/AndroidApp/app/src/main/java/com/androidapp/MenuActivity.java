package com.androidapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import com.androidapp.entities.Cart;
import com.androidapp.entities.Customer;
import com.androidapp.helpers.Rest;
import com.androidapp.jsonserializers.LocalDateSerializer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.time.LocalDate;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import static com.androidapp.helpers.Constants.*;

public class MenuActivity extends AppCompatActivity {
    Customer connectedCustomer;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        Intent intent = getIntent();
        String customerInfo = intent.getStringExtra("customerObject");
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(LocalDate.class, new LocalDateSerializer());
        Gson customerGson = builder.create();
        connectedCustomer = customerGson.fromJson(customerInfo, Customer.class);

        System.out.println(connectedCustomer);
    }

    public void openProductsPage(View view) {
        Executor executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(()->{
            try {
                String response = Rest.sendGet(GET_CUSTOMER_BY_ID + connectedCustomer.getId());
                System.out.println(response);
                handler.post(()->{
                    try {
                        if (!response.equals("Error")) {
                            Intent intent = new Intent(MenuActivity.this, ProductsActivity.class);
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

    public void openCartPage(View view) {
        Executor executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(()->{
            try {
                String response = Rest.sendGet(GET_CUSTOMER_BY_ID + connectedCustomer.getId());
                System.out.println(response);
                handler.post(()->{
                    try {
                        if (!response.equals("Error")) {
                            Intent intent = new Intent(MenuActivity.this, CartActivity.class);
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

    public void openSettingsPage(View view) {
        Executor executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(()->{
            try {
                String response = Rest.sendGet(GET_ALL_CUSTOMERS);
                System.out.println(response);
                handler.post(()->{
                    try {
                        if (!response.equals("Error")) {
                            Intent intent = new Intent(MenuActivity.this, SettingsActivity.class);
                            intent.putExtra("customerObject", response);
                            intent.putExtra("id", connectedCustomer.getId());
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

    public void logout(View view) {
        Intent intent = new Intent(MenuActivity.this, MainActivity.class);
        startActivity(intent);
    }
}
