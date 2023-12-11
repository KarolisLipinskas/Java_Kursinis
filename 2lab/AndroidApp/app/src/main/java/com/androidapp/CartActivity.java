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

public class CartActivity extends AppCompatActivity {
    String customerInfo;
    Customer connectedCustomer;

    @SuppressLint("NewApi")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        Intent intent = getIntent();
        customerInfo = intent.getStringExtra("customerObject");
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(LocalDate.class, new LocalDateSerializer());
        Gson customerGson = builder.create();
        connectedCustomer = customerGson.fromJson(customerInfo, Customer.class);

        System.out.println(connectedCustomer.getCartList().get(connectedCustomer.getCartList().size()-1));
    }

    public void openMenuPage(View view) {
        Intent intent = new Intent(CartActivity.this, MenuActivity.class);
        intent.putExtra("customerObject", customerInfo);
        startActivity(intent);
    }
}
