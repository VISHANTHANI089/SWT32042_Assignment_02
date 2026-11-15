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
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Locale;

public class sale_product extends AppCompatActivity {

    private Spinner spinnerProducts;
    private EditText SaleQuantity;
    private Button CompleteSale;
    private DBHelper dbHelper;

    // Lists to keep track of product IDs, names, current stock, and prices
    private ArrayList<String> productDisplayList;
    private ArrayList<Integer> productIdList;
    private ArrayList<Integer> productStockList;
    private ArrayList<Double> productPriceList;
    private ImageView btnBack;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sale_product);

        dbHelper = new DBHelper(this);

        btnBack = findViewById(R.id.btnBack);
        spinnerProducts = findViewById(R.id.spinnerProducts);
      SaleQuantity = findViewById(R.id.etSaleQuantity);
      CompleteSale = findViewById(R.id.btnCompleteSale);

        // Load products into the Spinner
        loadProductSpinner();

        CompleteSale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedPosition = spinnerProducts.getSelectedItemPosition();
                String qtyStr = SaleQuantity.getText().toString().trim();

                if (productDisplayList.isEmpty() || productDisplayList.get(0).equals("No products available")) {
                    Toast.makeText(sale_product.this, "No products available for sale", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (qtyStr.isEmpty()) {
                    Toast.makeText(sale_product.this, "Please enter quantity to sell", Toast.LENGTH_SHORT).show();
                    return;
                }

                int saleQty = Integer.parseInt(qtyStr);
                if (saleQty <= 0) {
                    Toast.makeText(sale_product.this, "Quantity must be greater than 0", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Get selected product details using spinner position
                int productId = productIdList.get(selectedPosition);
                int currentStock = productStockList.get(selectedPosition);
                double unitPrice = productPriceList.get(selectedPosition);
                String productName = productDisplayList.get(selectedPosition);

                //  Check if stock is sufficient
                if (saleQty > currentStock) {
                    Toast.makeText(sale_product.this, "Insufficient stock! Only " + currentStock + " left.", Toast.LENGTH_LONG).show();
                    return;
                }

                //  Calculate new stock and total price
                int newStock = currentStock - saleQty;
                double totalSalePrice = saleQty * unitPrice;

                // Update database with reduced quantity
                boolean updated = dbHelper.updateProductStock(productId, newStock);

                if (updated) {
                    Toast.makeText(sale_product.this,
                            "Sale Successful! " , Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    Toast.makeText(sale_product.this, "Sale failed to process", Toast.LENGTH_SHORT).show();
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

    private void loadProductSpinner() {
        productDisplayList = new ArrayList<>();
        productIdList = new ArrayList<>();
        productStockList = new ArrayList<>();
        productPriceList = new ArrayList<>();

        Cursor cursor = dbHelper.getAllProductsForSale();

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String name = cursor.getString(1);
                double price = cursor.getDouble(2);
                int stock = cursor.getInt(3);

                productIdList.add(id);
                productStockList.add(stock);
                productPriceList.add(price);
                productDisplayList.add(name + " (Stock: " + stock + " | LKR " + price + ")");
            } while (cursor.moveToNext());
            cursor.close();
        }

        if (productDisplayList.isEmpty()) {
            productDisplayList.add("No products available");
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, productDisplayList);
        spinnerProducts.setAdapter(adapter);
    }
}