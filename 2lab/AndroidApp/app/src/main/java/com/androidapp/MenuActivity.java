package com.androidapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import com.androidapp.entities.Customer;
import com.androidapp.jsonserializers.LocalDateSerializer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.time.LocalDate;

public class MenuActivity extends AppCompatActivity {
    Customer connectedCustomer;
    String customerInfo;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        Intent intent = getIntent();
        customerInfo = intent.getStringExtra("customerObject");
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(LocalDate.class, new LocalDateSerializer());
        Gson customerGson = builder.create();
        connectedCustomer = customerGson.fromJson(customerInfo, Customer.class);

        System.out.println(connectedCustomer);
    }

    public void openProductsPage(View view) {
        Intent intent = new Intent(MenuActivity.this, ProductsActivity.class);
        intent.putExtra("customerObject", customerInfo);
        startActivity(intent);
    }

    public void openCartPage(View view) {
        Intent intent = new Intent(MenuActivity.this, CartActivity.class);
        intent.putExtra("customerObject", customerInfo);
        startActivity(intent);
    }
}
