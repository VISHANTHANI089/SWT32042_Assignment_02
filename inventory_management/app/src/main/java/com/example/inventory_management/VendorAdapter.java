package com.example.inventory_management;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;



public class VendorAdapter extends RecyclerView.Adapter<VendorAdapter.ViewHolder> {

    private Context context;
    private ArrayList<VendorModel> vendorList;
    private DBHelper dbHelper;

    public VendorAdapter(Context context, ArrayList<VendorModel> vendorList) {
        this.context = context;
        this.vendorList = vendorList;
        this.dbHelper = new DBHelper(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.single_vendor_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        VendorModel vendor = vendorList.get(position);

        holder.tvName.setText(vendor.getName());
        holder.tvPhone.setText("Phone: " + vendor.getPhone());
        holder.tvAddress.setText("Address: " + vendor.getAddress());

        //delete

        holder.btnDeleteVendor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos = holder.getBindingAdapterPosition();
                if (pos != RecyclerView.NO_POSITION) {
                    VendorModel vendor = vendorList.get(pos);

                    // Pass both ID and Name to check for linked products details before deleting
                    boolean deleted = dbHelper.deleteVendor(vendor.getId(), vendor.getName());

                    if (deleted) {
                        // Remove from list and refresh RecyclerView
                        vendorList.remove(pos);
                        notifyItemRemoved(pos);
                        notifyItemRangeChanged(pos, vendorList.size());
                        Toast.makeText(context, "Vendor Deleted", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "Cannot delete! Products are linked to this vendor.", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        holder.btnEditVendor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int adapterPosition = holder.getAdapterPosition();
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    VendorModel currentVendor = vendorList.get(adapterPosition);

                    Intent intent = new Intent(context, edit_vendor.class);
                    intent.putExtra("VENDOR_ID", currentVendor.getId());
                    context.startActivity(intent);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return vendorList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvPhone, tvAddress;
        Button btnDeleteVendor,btnEditVendor;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvVendorName);
            tvPhone = itemView.findViewById(R.id.tvVendorPhone);
            tvAddress = itemView.findViewById(R.id.tvVendorAddress);
            btnEditVendor = itemView.findViewById(R.id.btnEditVendor);
            btnDeleteVendor = itemView.findViewById(R.id.btnDeleteVendor);
        }
    }
}
