package com.androidapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.androidapp.entities.Customer;
import com.androidapp.helpers.Rest;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import static com.androidapp.helpers.Constants.GET_ALL_CUSTOMERS;
import static com.androidapp.helpers.Constants.GET_CUSTOMER_BY_LOGIN;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void test_GET(View view) {
        Executor executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(()->{
            try {
                String response = Rest.sendGet(GET_ALL_CUSTOMERS);
                System.out.println(response);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
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
}