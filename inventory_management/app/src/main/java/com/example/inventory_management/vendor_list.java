package com.example.inventory_management;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class vendor_list extends AppCompatActivity {


    private RecyclerView recyclerView;
    private DBHelper dbHelper;
    private ArrayList<VendorModel> vendorList;
   private ImageView btnBack;

    private VendorAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_list);

        btnBack = findViewById(R.id.btnBack);
        recyclerView = findViewById(R.id.recyclerViewVendors);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        dbHelper = new DBHelper(this);
        vendorList = new ArrayList<>();

        loadVendorsFromDB();

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
        loadVendorsFromDB();
    }

    private void loadVendorsFromDB() {
        vendorList.clear();
        Cursor cursor = dbHelper.getAllVendors();

        if (cursor != null && cursor.moveToFirst()) {
            do {
                // Table columns: 0=id, 1=name, 2=phone, 3=address
                int id = cursor.getInt(0);
                String name = cursor.getString(1);
                String phone = cursor.getString(2);
                String address = cursor.getString(3);

                vendorList.add(new VendorModel(id, name, phone, address));
            } while (cursor.moveToNext());
            cursor.close();
        }

        adapter = new VendorAdapter(this, vendorList);
        recyclerView.setAdapter(adapter);
    }
}