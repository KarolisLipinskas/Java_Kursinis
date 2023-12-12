package com.androidapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
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

public class SettingsActivity extends AppCompatActivity {
    String customerInfo;
    Customer connectedCustomer;
    @SuppressLint("NewApi")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Intent intent = getIntent();
        customerInfo = intent.getStringExtra("customerObject");
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(LocalDate.class, new LocalDateSerializer());
        Gson customerGson = builder.create();
        connectedCustomer = customerGson.fromJson(customerInfo, Customer.class);
        System.out.println("Products page");
        System.out.println(connectedCustomer);

        TextView name = findViewById(R.id.name);
        TextView surname = findViewById(R.id.surname);
        TextView username = findViewById(R.id.username);
        TextView gmail = findViewById(R.id.email);
        TextView birthDate = findViewById(R.id.birthDate);
        TextView cardNo = findViewById(R.id.cardNo);

        name.setText(connectedCustomer.getName());
        surname.setText(connectedCustomer.getSurname());
        username.setText(connectedCustomer.getUsername());
        gmail.setText(connectedCustomer.getGmail());
        birthDate.setText(connectedCustomer.getBirthDate().toString());
        cardNo.setText(connectedCustomer.getCardNo());
    }

    public void openMenuPageFromSettings(View view) {
        Executor executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {
            try {
                String response = Rest.sendGet(GET_CUSTOMER_BY_ID + connectedCustomer.getId());
                System.out.println(response);
                handler.post(() -> {
                    try {
                        if (!response.equals("Error")) {
                            Intent intent = new Intent(SettingsActivity.this, MenuActivity.class);
                            intent.putExtra("customerObject", response);
                            startActivity(intent);
                        } else {
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
    public void saveChanges(View view) {
        TextView name = findViewById(R.id.name);
        TextView surname = findViewById(R.id.surname);
        TextView username = findViewById(R.id.username);
        TextView newPassword = findViewById(R.id.newPassword);
        TextView newPasswordR = findViewById(R.id.newPasswordR);
        TextView gmail = findViewById(R.id.email);
        TextView birthDate = findViewById(R.id.birthDate);
        TextView cardNo = findViewById(R.id.cardNo);

        if (!newPassword.getText().toString().equals(newPasswordR.getText().toString())) {
            AlertDialog.Builder alert = new AlertDialog.Builder(SettingsActivity.this);
            alert.setMessage("ERROR\nPasswords do not match or are empty");
            alert.setCancelable(true);
            alert.setNeutralButton("OK", (dialog, which) -> dialog.cancel());
            AlertDialog alertPopup = alert.create();
            alertPopup.show();
            return;
        }

        Customer customer = connectedCustomer;
        customer.setName(name.getText().toString());
        customer.setSurname(surname.getText().toString());
        customer.setUsername(username.getText().toString());
        if (!newPassword.getText().toString().isEmpty()) {
            customer.setPassword(newPassword.getText().toString());
        }
        customer.setGmail(gmail.getText().toString());
        customer.setBirthDate(LocalDate.parse(birthDate.getText().toString()));
        customer.setCardNo(cardNo.getText().toString());

        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(LocalDate.class, new LocalDateSerializer());
        Gson customerGson = builder.create();

        String customerInfo = customerGson.toJson(customer);

        Executor executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(()->{
            try {
                String response = Rest.sendPut(UPDATE_CUSTOMER, customerInfo);
                System.out.println(response);
                handler.post(()->{
                    try {
                        if (!response.equals("Error")) {
                            System.out.println(customerGson.fromJson(response, Customer.class));
                            AlertDialog.Builder alert = new AlertDialog.Builder(SettingsActivity.this);
                            alert.setMessage("Updated customer information");
                            alert.setCancelable(true);
                            alert.setNeutralButton("OK", (dialog, which) -> dialog.cancel());
                            AlertDialog alertPopup = alert.create();
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
}
