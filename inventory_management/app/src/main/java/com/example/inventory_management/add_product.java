package com.example.inventory_management;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class add_product extends AppCompatActivity {

    private EditText ProductName, ProductPrice, ProductQuantity;
    private Spinner spinnerVendor;
    private Button SaveProduct;
    private DBHelper dbHelper;
    private ImageView btnBack;

    private ArrayList<String> vendorList;
    private int editingProductId = -1; //editing purpose

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_product);
        // Initialize DB Helper
        dbHelper = new DBHelper(this);


       ProductName = findViewById(R.id.etProductName);
        ProductPrice = findViewById(R.id.etProductPrice);
        ProductQuantity = findViewById(R.id.etProductQuantity);
        spinnerVendor = findViewById(R.id.spinnerVendor);
        SaveProduct = findViewById(R.id.btnSaveProduct);
        btnBack = findViewById(R.id.btnBack);

        // Load Vendors to Spinner Dropdown
        loadVendorSpinner();

        if (getIntent().hasExtra("PRODUCT_ID")) {
            //  When editing, the previous screen sends the product ID and name using an Intent.
            editingProductId = getIntent().getIntExtra("PRODUCT_ID", -1);
            String passedName = getIntent().getStringExtra("PRODUCT_NAME");

            // Pre-fill ONLY the product name field
            if (passedName != null) {
                ProductName.setText(passedName);
            }

            SaveProduct.setText("Update Product");
        } else {
            // ADD MODE: Ensure everything starts completely blank
            editingProductId = -1;
            ProductName.setText("");
            ProductPrice.setText("");
            ProductQuantity.setText("");
            SaveProduct.setText("Save Product");
        }

        // Save Button Click Listener
       SaveProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = ProductName.getText().toString().trim();
                String priceStr = ProductPrice.getText().toString().trim();
                String qtyStr = ProductQuantity.getText().toString().trim();

                // selected vendor from spinner
                String selectedVendor = "";
                if (spinnerVendor.getSelectedItem() != null) {
                    selectedVendor = spinnerVendor.getSelectedItem().toString();
                }

                // Input Validation
                if (name.isEmpty() || priceStr.isEmpty() || qtyStr.isEmpty()) {
                    Toast.makeText(add_product.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (selectedVendor.equals("No vendors available") || selectedVendor.isEmpty()) {
                    Toast.makeText(add_product.this, "Please add a vendor first!", Toast.LENGTH_SHORT).show();
                    return;
                }

                double price = Double.parseDouble(priceStr);
                int quantity = Integer.parseInt(qtyStr);

                if (editingProductId == -1) {
                // Insert into Database
                long rowId = dbHelper.insertProduct(name, price, quantity, selectedVendor);

                if (rowId != -1) {
                    Toast.makeText(add_product.this, "Product Saved Successfully!", Toast.LENGTH_SHORT).show();

                    //clear the form after successfully added the one product
                    ProductName.setText("");
                    ProductPrice.setText("");
                    ProductQuantity.setText("");
                } else {
                    Toast.makeText(add_product.this, "Failed to save product", Toast.LENGTH_SHORT).show();
                }
            } else {
               //  update existing product
               boolean updated = dbHelper.updateProduct(editingProductId, name, price, quantity, selectedVendor);

               if (updated) {
                   Toast.makeText(add_product.this, "Product Updated Successfully!", Toast.LENGTH_SHORT).show();
                   finish();
               } else {
                   Toast.makeText(add_product.this, "Failed to update product", Toast.LENGTH_SHORT).show();
               }
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

    //  method to select  the vendor dropdown spinner from SQLite
    private void loadVendorSpinner() {
        vendorList = new ArrayList<>();
        Cursor cursor = dbHelper.getAllVendors();

        if (cursor != null && cursor.moveToFirst()) {
            do {
                //  column index 1 is the vendor name string
                String vendorName = cursor.getString(1);
                vendorList.add(vendorName);
            } while (cursor.moveToNext());
            cursor.close();
        }

        if (vendorList.isEmpty()) {
            vendorList.add("No vendors available");
        }

        // Bind data to ArrayAdapter and set to Spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, vendorList);
        spinnerVendor.setAdapter(adapter);
    }
}