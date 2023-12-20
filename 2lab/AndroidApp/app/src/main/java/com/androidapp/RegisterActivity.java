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

import static com.androidapp.helpers.Constants.ADD_CUSTOMER;

public class RegisterActivity extends AppCompatActivity {
    List<Customer> allCustomers;
    @SuppressLint("NewApi")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Intent intent = getIntent();
        String customersInfo = intent.getStringExtra("customerObject");
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(LocalDate.class, new LocalDateSerializer());
        Gson customerGson = builder.create();
        Type customersListType = new TypeToken<List<Customer>>(){}.getType();
        allCustomers = customerGson.fromJson(customersInfo, customersListType);
    }

    public void goToLoginPage(View view) {
        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
        startActivity(intent);
    }

    @SuppressLint("NewApi")
    public void register(View view) {
        TextView name = findViewById(R.id.name2);
        TextView surname = findViewById(R.id.surname2);
        TextView username = findViewById(R.id.username2);
        TextView newPassword = findViewById(R.id.newPassword2);
        TextView newPasswordR = findViewById(R.id.newPasswordR2);
        TextView gmail = findViewById(R.id.email2);
        TextView birthDate = findViewById(R.id.birthDate2);
        TextView cardNo = findViewById(R.id.cardNo2);

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
            if (customer.getUsername().equals(username.getText().toString())) {
                alertText += "Username already exist\n";
                check = true;
                break;
            } else if (username.getText().toString().isEmpty()) {
                alertText += "No username\n";
                check = true;
                break;
            }
        }
        if (!newPassword.getText().toString().equals(newPasswordR.getText().toString()) || newPassword.getText().toString().isEmpty()) {
            alertText += "Passwords do not match or are empty\n";
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
            AlertDialog.Builder alert = new AlertDialog.Builder(RegisterActivity.this);
            alert.setMessage(alertText);
            alert.setCancelable(true);
            alert.setNeutralButton("OK", (dialog, which) -> dialog.cancel());
            AlertDialog alertPopup = alert.create();
            alertPopup.show();
            return;
        }

        Customer customer = new Customer(
                username.getText().toString(),
                newPassword.getText().toString(),
                name.getText().toString(),
                surname.getText().toString(),
                gmail.getText().toString(),
                LocalDate.parse(birthDate.getText().toString()),
                cardNo.getText().toString());

        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(LocalDate.class, new LocalDateSerializer());
        Gson customerGson = builder.create();

        String customerInfo = customerGson.toJson(customer);

        Executor executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(()->{
            try {
                String response = Rest.sendPost(ADD_CUSTOMER, customerInfo);
                System.out.println(response);
                handler.post(()->{
                    try {
                        if (!response.equals("Error")) {
                            System.out.println(customerGson.fromJson(response, Customer.class));
                            AlertDialog.Builder alert = new AlertDialog.Builder(RegisterActivity.this);
                            alert.setMessage("Customer account created");
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
