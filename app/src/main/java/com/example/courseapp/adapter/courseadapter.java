package com.example.courseapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.courseapp.R;
import com.example.courseapp.domain.coursedomain;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class courseadapter extends RecyclerView.Adapter<courseadapter.Viewholder> {

    ArrayList<coursedomain> items;
    Context context;
    private OnItemClickListener onItemClickListener;

    public courseadapter(ArrayList<coursedomain> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public courseadapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflator = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewlist, parent, false);
        context = parent.getContext();
        return new Viewholder(inflator);
    }

    @Override
    public void onBindViewHolder(@NonNull courseadapter.Viewholder holder, int position) {
        coursedomain course = items.get(position);
        holder.title.setText(course.getTitle());
        holder.price.setText("$" + course.getPrice());
        holder.pic.setImageResource(course.getPicpath());

        // Xử lý sự kiện click vào item
        holder.itemView.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(course);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        TextView title, price;
        ImageView pic, background_img;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            price = itemView.findViewById(R.id.price);
            pic = itemView.findViewById(R.id.pic);
            background_img = itemView.findViewById(R.id.background_img);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(coursedomain course);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public void updateData(ArrayList<coursedomain> newItems) {
        this.items.clear();
        this.items.addAll(newItems);
        notifyDataSetChanged();
    }
}
