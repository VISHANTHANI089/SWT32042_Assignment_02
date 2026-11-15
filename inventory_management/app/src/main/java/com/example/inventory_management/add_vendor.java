package com.example.inventory_management;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class add_vendor extends AppCompatActivity {

    private EditText VendorName, VendorPhone, VendorAddress;
    private Button SaveVendor;
    private ImageView btnBack;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_vendor);

        // Initialize Database Helper
        dbHelper = new DBHelper(this);


        VendorName = findViewById(R.id.etVendorName);
        VendorPhone = findViewById(R.id.etVendorPhone);
        VendorAddress = findViewById(R.id.etVendorAddress);
        SaveVendor = findViewById(R.id.btnSaveVendor);
        btnBack = findViewById(R.id.btnBack);


        // Save Button Click Listener
        SaveVendor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = VendorName.getText().toString().trim();
                String phone = VendorPhone.getText().toString().trim();
                String address = VendorAddress.getText().toString().trim();

                // Input Validation
                if (name.isEmpty() || phone.isEmpty() || address.isEmpty()) {
                    Toast.makeText(add_vendor.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Insert into Database
                long rowId = dbHelper.insertVendor(name, phone, address);

                if (rowId != -1) {
                    Toast.makeText(add_vendor.this, "Vendor Saved Successfully!", Toast.LENGTH_SHORT).show();
                    finish(); // Closes activity and returns to dashboard
                } else {
                    Toast.makeText(add_vendor.this, "Failed to save vendor", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}