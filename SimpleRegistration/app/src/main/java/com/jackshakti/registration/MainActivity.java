package com.jackshakti.registration;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import java.io.*;
import java.net.*;

public class MainActivity extends AppCompatActivity {

    EditText nameInput, phoneInput, addressInput;
    Button btnRegister;
    String URL = "http://YOUR_SERVER_IP/register.php"; // use local IP or domain

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nameInput = findViewById(R.id.nameInput);
        phoneInput = findViewById(R.id.phoneInput);
        addressInput = findViewById(R.id.addressInput);
        btnRegister = findViewById(R.id.btnRegister);

        btnRegister.setOnClickListener(v -> {
            String name = nameInput.getText().toString();
            String phone = phoneInput.getText().toString();
            String address = addressInput.getText().toString();

            if (name.isEmpty() || phone.isEmpty() || address.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            new Thread(() -> registerUser(name, phone, address)).start();
        });
    }

    private void registerUser(String name, String phone, String address) {
        try {
            URL url = new URL(URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);

            String data = "name=" + URLEncoder.encode(name, "UTF-8") +
                    "&phone=" + URLEncoder.encode(phone, "UTF-8") +
                    "&address=" + URLEncoder.encode(address, "UTF-8");

            OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());
            writer.write(data);
            writer.flush();

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder result = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null)
                result.append(line);

            runOnUiThread(() -> Toast.makeText(this, "Response: " + result, Toast.LENGTH_LONG).show());

            writer.close();
            reader.close();
        } catch (Exception e) {
            runOnUiThread(() -> Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show());
        }
    }
}
