# Android-simple-registration-example
Simple registration example in Java + PHP + Mysql.


Here's a simple registration example using:
Android Studio with Java frontend
Backend with PHP + MySQL
Fields: name, phone, address


✅ **Step 1: MySQL Table**


CREATE DATABASE userdb;

USE userdb;

CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100),
    phone VARCHAR(15),
    address TEXT
);



✅ **Step 2: PHP API (register.php)**
Save as: register.php

<?php
$host = "localhost";
$user = "root"; // change as per your server
$pass = "";
$db = "userdb";

$conn = new mysqli($host, $user, $pass, $db);

if ($_SERVER["REQUEST_METHOD"] == "POST") {
    $name = $_POST["name"] ?? '';
    $phone = $_POST["phone"] ?? '';
    $address = $_POST["address"] ?? '';

    if ($name && $phone && $address) {
        $stmt = $conn->prepare("INSERT INTO users (name, phone, address) VALUES (?, ?, ?)");
        $stmt->bind_param("sss", $name, $phone, $address);
        if ($stmt->execute()) {
            echo json_encode(["status" => "success", "message" => "Registered successfully"]);
        } else {
            echo json_encode(["status" => "error", "message" => "Database error"]);
        }
        $stmt->close();
    } else {
        echo json_encode(["status" => "error", "message" => "All fields required"]);
    }
}

$conn->close();
?>



✅ **Step 3: Android Studio (Java) UI + API Call**
**a. Layout (activity_main.xml)**


<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:orientation="vertical"
    android:padding="20dp"
    android:layout_width="match_parent"
    android:layout_height="match_parent">

    <EditText android:id="@+id/nameInput"
        android:hint="Name"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"/>

    <EditText android:id="@+id/phoneInput"
        android:hint="Phone"
        android:inputType="phone"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"/>

    <EditText android:id="@+id/addressInput"
        android:hint="Address"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"/>

    <Button android:id="@+id/btnRegister"
        android:text="Register"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"/>
</LinearLayout>



**b. Java Code (MainActivity.java)**

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


✅ **Permissions (AndroidManifest.xml)**

<uses-permission android:name="android.permission.INTERNET"/>






🧪 **Test**
Start Apache + MySQL (e.g., XAMPP or local server).
Place register.php in htdocs folder.
Replace YOUR_SERVER_IP in Java code with your actual IP (e.g., 192.168.1.5).
Run the app on a real device (same network).
Submit form → Check MySQL table.

