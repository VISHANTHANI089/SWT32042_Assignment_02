package com.example.inventory_management;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
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

public class edit_vendor extends AppCompatActivity {

    private Spinner spinnerVendorId;
    private EditText Name, Phone, Address;
    private Button UpdateVendor;
    private DBHelper dbHelper;

    // Lists to hold vendor data loaded from DB
    private ArrayList<String> idDisplayList;
    private ArrayList<VendorModel> vendorList;
   private ImageView Back;

    private int selectedVendorId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_edit_vendor);
        dbHelper = new DBHelper(this);

        Back = findViewById(R.id.btnBack);
        spinnerVendorId = findViewById(R.id.spinnerVendorId);
        Name = findViewById(R.id.etEditVendorName);
        Phone = findViewById(R.id.etEditVendorPhone);
        Address = findViewById(R.id.etEditVendorAddress);
        UpdateVendor = findViewById(R.id.btnUpdateVendor);

        // Load all Vendor IDs into the Dropdown
        loadVendorIdsIntoSpinner();


        // If user clicked "Edit" on a specific card in VendorList, it select that ID
        if (getIntent().hasExtra("VENDOR_ID")) {
            int passedId = getIntent().getIntExtra("VENDOR_ID", -1);
            for (int i = 0; i < vendorList.size(); i++) {
                if (vendorList.get(i).getId() == passedId) {
                    spinnerVendorId.setSelection(i);
                    break;
                }
            }
        }

      //select ID for edit the vendor
        spinnerVendorId.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!vendorList.isEmpty()) {
                    VendorModel selectedVendor = vendorList.get(position);
                    selectedVendorId = selectedVendor.getId();
                }else{
                    selectedVendorId = -1;
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        // Update the database when the Update button is clicked
        UpdateVendor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        UpdateVendor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedVendorId == -1) {
                    Toast.makeText(edit_vendor.this, "No vendor selected", Toast.LENGTH_SHORT).show();
                    return;
                }

                String name = Name.getText().toString().trim();
                String phone = Phone.getText().toString().trim();
                String address = Address.getText().toString().trim();

                if (name.isEmpty() || phone.isEmpty() || address.isEmpty()) {
                    Toast.makeText(edit_vendor.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                boolean updated = dbHelper.updateVendor(selectedVendorId, name, phone, address);
                if (updated) {
                    Toast.makeText(edit_vendor.this, "Vendor ID " + selectedVendorId + " Updated!", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(edit_vendor.this, "Update Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });



        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void loadVendorIdsIntoSpinner() {
        idDisplayList = new ArrayList<>();
        vendorList = new ArrayList<>();

        Cursor cursor = dbHelper.getAllVendors();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String name = cursor.getString(1);
                String phone = cursor.getString(2);
                String address = cursor.getString(3);

                vendorList.add(new VendorModel(id, name, phone, address));
                idDisplayList.add("ID: " + id + " (" + name + ")");
            } while (cursor.moveToNext());
            cursor.close();
        }

        if (idDisplayList.isEmpty()) {
            idDisplayList.add("No Vendors Available");
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, idDisplayList);
        spinnerVendorId.setAdapter(adapter);
    }
}