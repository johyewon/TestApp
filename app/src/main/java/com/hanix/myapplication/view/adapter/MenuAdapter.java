package com.hanix.myapplication.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.hanix.myapplication.R;

import java.util.List;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.Holder> {

    public interface ItemClick {
        void onClick(View view, int position);
    }

    private ItemClick itemClick;

    public void setItemClick(ItemClick itemClick) {
        this.itemClick = itemClick;
    }

    Context context;
    List<String> items;

    public MenuAdapter(List<String> items, Context context) {
        this.items = items;
        this.context = context;
    }

    public static class Holder extends RecyclerView.ViewHolder {
        protected ImageView menuImage;
        protected TextView menuName;
        protected ConstraintLayout menuLayout;

        public Holder(@NonNull View itemView) {
            super(itemView);
            menuImage = itemView.findViewById(R.id.menuImage);
            menuName = itemView.findViewById(R.id.menuName);
            menuLayout = itemView.findViewById(R.id.menuLayout);
        }
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_menu, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MenuAdapter.Holder holder, int i) {
        holder.menuName.setText(items.get(i));

        final int num = i;
        holder.menuLayout.setOnClickListener((view) -> {
            if (itemClick != null)
                itemClick.onClick(view, num);
        });

        holder.menuName.setOnClickListener((view) -> {
            if (itemClick != null)
                itemClick.onClick(view, num);
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public Object getItem(int position) {
        return items.get(position);
    }

}
