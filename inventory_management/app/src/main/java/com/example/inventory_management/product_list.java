package com.example.inventory_management;

import android.database.Cursor;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class product_list extends AppCompatActivity {

    private RecyclerView recyclerView;
    private DBHelper dbHelper;
    private ArrayList<ProductModel> productList;
    private ProductAdapter adapter;
    private ImageView btnBack;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);

        btnBack = findViewById(R.id.btnBack);
        recyclerView = findViewById(R.id.recyclerViewProducts);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        dbHelper = new DBHelper(this);
        productList = new ArrayList<>();

        loadProductsFromDB();

        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
        // Refresh list when returning from other screens
        loadProductsFromDB();
    }

    private void loadProductsFromDB() {
        productList.clear();
        Cursor cursor = dbHelper.getAllProducts();

        if (cursor != null && cursor.moveToFirst()) {
            do {
                // Table columns: 0=id, 1=name, 2=price, 3=quantity, 4=vendor
                int id = cursor.getInt(0);
                String name = cursor.getString(1);
                double price = cursor.getDouble(2);
                int quantity = cursor.getInt(3);
                String vendor = cursor.getString(4);

                productList.add(new ProductModel(id, name, price, quantity, vendor));
            } while (cursor.moveToNext());
            cursor.close();
        }

        // Pass context and productList (ArrayList)
        adapter = new ProductAdapter(this, productList);
        recyclerView.setAdapter(adapter);
    }


}