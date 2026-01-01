package com.example.quickbites.adapters;

import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.quickbites.R;
import com.example.quickbites.models.GroceryItem;
import java.util.List;

public class GroceryAdapter extends RecyclerView.Adapter<GroceryAdapter.GroceryViewHolder> {
    private List<GroceryItem> groceryItems;
    private final OnItemActionListener listener;

    public interface OnItemActionListener {
        void onItemChecked(GroceryItem item, int position);
        void onItemRemove(GroceryItem item, int position);
    }

    public GroceryAdapter(List<GroceryItem> groceryItems, OnItemActionListener listener) {
        this.groceryItems = groceryItems;
        this.listener = listener;
    }

    @NonNull
    @Override
    public GroceryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_grocery, parent, false);
        return new GroceryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GroceryViewHolder holder, int position) {
        GroceryItem item = groceryItems.get(position);
        holder.bind(item, listener);
    }

    @Override
    public int getItemCount() {
        return groceryItems.size();
    }

    public void updateItems(List<GroceryItem> newItems) {
        this.groceryItems = newItems;
        notifyDataSetChanged();
    }

    public static class GroceryViewHolder extends RecyclerView.ViewHolder {
        private final CheckBox cbItem;
        private final TextView tvItemName;
        private final Button btnRemove;

        public GroceryViewHolder(@NonNull View itemView) {
            super(itemView);
            cbItem = itemView.findViewById(R.id.cbItem);
            tvItemName = itemView.findViewById(R.id.tvItemName);
            btnRemove = itemView.findViewById(R.id.btnRemove);
        }

        public void bind(GroceryItem item, OnItemActionListener listener) {
            tvItemName.setText(item.getName());
            cbItem.setChecked(item.isChecked());

            // strike through if checked
            if (item.isChecked()) {
                tvItemName.setPaintFlags(tvItemName.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                tvItemName.setTextColor(0xFF999999);
            } else {
                tvItemName.setPaintFlags(tvItemName.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                tvItemName.setTextColor(0xFF333333);
            }

            cbItem.setOnCheckedChangeListener((buttonView, isChecked) -> {
                int position = getBindingAdapterPosition();
                if (position != RecyclerView.NO_POSITION && listener != null) {
                    item.setChecked(isChecked);
                    listener.onItemChecked(item, position);
                }
            });

            btnRemove.setOnClickListener(v -> {
                int position = getBindingAdapterPosition();
                if (position != RecyclerView.NO_POSITION && listener != null) {
                    listener.onItemRemove(item, position);
                }
            });
        }
    }
}
