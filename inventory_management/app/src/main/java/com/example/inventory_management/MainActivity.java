package com.example.inventory_management;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private TextView tvTotalProducts, tvLowStockCount, tvTotalValue;
    private CardView cardAddProduct, cardProductList, cardSaleProduct, cardAddVendor, cardVendorList;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        // Initialize database helper
        dbHelper = new DBHelper(this);

        //  Wiring UI elements from XML
        tvTotalProducts = findViewById(R.id.tvTotalProducts);
        tvLowStockCount = findViewById(R.id.tvLowStockCount);
        tvTotalValue = findViewById(R.id.tvTotalValue);

        cardAddProduct = findViewById(R.id.cardAddProduct);
        cardProductList = findViewById(R.id.cardProductList);
        cardSaleProduct = findViewById(R.id.cardSaleProduct);
        cardAddVendor = findViewById(R.id.cardAddVendor);
        cardVendorList = findViewById(R.id.cardVendorList);

        //  Setup Click Listeners for Navigation
        cardAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, add_product.class);
                startActivity(intent);
            }
        });

        cardProductList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, product_list.class);
                startActivity(intent);
            }
        });

        cardSaleProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, sale_product.class);
                startActivity(intent);
            }
        });

        cardAddVendor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, add_vendor.class);
                startActivity(intent);
            }
        });

        cardVendorList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, vendor_list.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // every time it return to the dashboard, updating the counts and total value automatically
        loadDashboardData();
    }

    // Simple method to fetch and calculate metrics from SQLite database
    private void loadDashboardData() {
        int totalProducts = 0;
        int lowStockCount = 0;
        double totalValue = 0.0;

        Cursor cursor = dbHelper.getAllProducts(); // Uses standard SQLite Cursor reading
        if (cursor != null && cursor.moveToFirst()) {
            do {
                totalProducts++;

                // Assuming the products table columns:
                // Index 0 = id, Index 1 = name, Index 2 = price, Index 3 = quantity
                try {
                    double price = cursor.getDouble(2);
                    int quantity = cursor.getInt(3);

                    // Calculate total value (Price * Quantity)
                    totalValue += (price * quantity);

                    // Low stock condition (e.g. quantity less than 5)
                    if (quantity <5 ) {
                        lowStockCount++;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } while (cursor.moveToNext());
            cursor.close();
        }

        // Set values to dashboard text views
        tvTotalProducts.setText(String.valueOf(totalProducts));
        tvLowStockCount.setText(String.valueOf(lowStockCount));
        tvTotalValue.setText(String.format(Locale.getDefault(), "LKR %,.2f", totalValue));
    }
}