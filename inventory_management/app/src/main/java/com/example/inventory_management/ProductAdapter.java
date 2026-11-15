package com.example.inventory_management;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Locale;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {

    private Context context;
    private ArrayList<ProductModel> productList;
    private DBHelper dbHelper;

    public ProductAdapter(Context context, ArrayList<ProductModel> productList) {
        this.context = context;
        this.productList = productList;
        this.dbHelper = new DBHelper(context);
    }

    @NonNull
    @Override
    public ProductAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.single_product_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductAdapter.ViewHolder holder, int position) {
        ProductModel product = productList.get(position);

        holder.Name.setText(product.getName());
        holder.Price.setText(String.format(Locale.getDefault(), "Price: LKR %,.2f", product.getPrice()));
        holder.Qty.setText("Qty: " + product.getQuantity());
        holder.Vendor.setText("Vendor: " + product.getVendor());

        // Delete Button Click Listener
        holder.Delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int adapterPosition = holder.getAdapterPosition();

                if (adapterPosition != RecyclerView.NO_POSITION) {
                    // Changed 'product' to 'currentProduct' to avoid the duplicate scope error
                    ProductModel currentProduct = productList.get(adapterPosition);

                    boolean deleted = dbHelper.deleteProduct(currentProduct.getId());

                    if (deleted) {
                        productList.remove(adapterPosition);
                        notifyItemRemoved(adapterPosition);
                        notifyItemRangeChanged(adapterPosition, productList.size());
                        Toast.makeText(context, "Product Deleted", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "Delete Failed", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });


        // Edit, it ( redirect to the Add/Edit screen)
        holder.Edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, add_product.class);
                intent.putExtra("PRODUCT_ID", product.getId());
                intent.putExtra("PRODUCT_NAME", product.getName());
                context.startActivity(intent);
            }
        });


        //5 is the threshold to detect the Stock availability
        if (product.getQuantity() < 5) {
            holder.StockStatus.setText("Low Stock");
            holder.StockStatus.setTextColor(Color.parseColor("#DC3545")); // Red
        } else {
            holder.StockStatus.setText("In Stock");
            holder.StockStatus.setTextColor(Color.parseColor("#198754")); // Green
        }

    }


    @Override
    public int getItemCount() {
        return productList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView Name, Price, Qty, Vendor,StockStatus;
        Button Edit, Delete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            Name = itemView.findViewById(R.id.tvProductName);
            Price = itemView.findViewById(R.id.tvProductPrice);
            Qty = itemView.findViewById(R.id.tvProductQty);
            Vendor = itemView.findViewById(R.id.tvProductVendor);
            StockStatus=itemView.findViewById(R.id.tvStockStatus);
            Edit = itemView.findViewById(R.id.btnEdit);
            Delete = itemView.findViewById(R.id.btnDelete);
        }
    }
}