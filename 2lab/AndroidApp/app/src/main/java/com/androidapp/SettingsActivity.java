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
import com.androidapp.entities.Customer;
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
import java.util.regex.Pattern;

import static com.androidapp.helpers.Constants.*;

public class SettingsActivity extends AppCompatActivity {
    Customer connectedCustomer;
    List<Customer> allCustomers;

    @SuppressLint("NewApi")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Intent intent = getIntent();
        String customersInfo = intent.getStringExtra("customerObject");
        int id = intent.getIntExtra("id",0);
        if (id == 0) System.out.println("ERROR WRONG ID");

        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(LocalDate.class, new LocalDateSerializer());
        Gson customerGson = builder.create();
        Type customersListType = new TypeToken<List<Customer>>(){}.getType();
        allCustomers = customerGson.fromJson(customersInfo, customersListType);
        for (Customer customer: allCustomers) {
            if (customer.getId() == id) {
                connectedCustomer = customer;
                break;
            }
        }
        System.out.println("Settings page");
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

        String alertText = "ERROR\n";
        boolean check = false;
        if (name.getText().toString().isEmpty()) {
            alertText += "No name\n";
            check = true;
        }
        if (surname.getText().toString().isEmpty()) {
            alertText += "No surname\n";
            check = true;
        }
        if (name.getText().toString().isEmpty()) {
            alertText += "No name\n";
            check = true;
        }
        for (Customer customer : allCustomers) {
            if (customer.getUsername().equals(username.getText().toString()) && !connectedCustomer.getUsername().equals(username.getText().toString())) {
                alertText += "Username already exist\n";
                check = true;
                break;
            } else if (username.getText().toString().isEmpty()) {
                alertText += "No username\n";
                check = true;
                break;
            }
        }
        if (!newPassword.getText().toString().equals(newPasswordR.getText().toString())) {
            alertText += "Passwords do not match\n";
            check = true;
        }
        if (gmail.getText().toString().isEmpty()) {
            alertText += "No email\n";
            check = true;
        } else if (!Pattern.matches(".+@.+\\..+", gmail.getText().toString())) {
            alertText += "Wrong email\n";
            check = true;
        }
        if (birthDate.getText().toString().isEmpty()) {
            alertText += "No birthDate\n";
            check = true;
        } else if (!Pattern.matches("[0-9]{4}-[0-9]{2}-[0-9]{2}", birthDate.getText().toString())) {
            alertText += "Wrong birthDate\n";
            check = true;
        }
        if (!Pattern.matches("[0-9]{4}-[0-9]{4}-[0-9]{4}-[0-9]{4}", cardNo.getText().toString()) && !cardNo.getText().toString().isEmpty()) {
            alertText += "Wrong cardNo\n";
            check = true;
        }

        if (check) {
            AlertDialog.Builder alert = new AlertDialog.Builder(SettingsActivity.this);
            alert.setMessage(alertText);
            alert.setCancelable(true);
            alert.setNeutralButton("OK", (dialog, which) -> dialog.cancel());
            AlertDialog alertPopup = alert.create();
            alertPopup.show();
            return;
        }

        if (!newPassword.getText().toString().equals(newPasswordR.getText().toString())) {
            AlertDialog.Builder alert = new AlertDialog.Builder(SettingsActivity.this);
            alert.setMessage("ERROR\nPasswords do not match");
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
