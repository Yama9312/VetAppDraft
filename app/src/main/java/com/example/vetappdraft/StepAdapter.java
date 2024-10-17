package com.example.vetappdraft;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.Collections;
import java.util.List;

public class StepAdapter extends RecyclerView.Adapter<StepAdapter.ViewHolder> {
    private List<Page> steps;
    private Context context;
    private int selectedIndex = -1;

    public StepAdapter(List<Page> steps, Context context) {
        this.steps = steps;
        this.context = context;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;
        public CheckBox checkBox;

        public ViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.text_view);
            checkBox = itemView.findViewById(R.id.checkbox);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_step, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Page step = steps.get(position);
        holder.textView.setText(step.getName());

        holder.checkBox.setOnCheckedChangeListener(null);
        holder.checkBox.setChecked(position == selectedIndex);

        holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                selectedIndex = holder.getAdapterPosition();
                notifyDataSetChanged();
            } else if (selectedIndex == holder.getAdapterPosition()) {
                selectedIndex = -1;
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return steps.size();
    }

    public int getFirstSelectedIndex() {
        return selectedIndex;
    }

    // Method to handle drag and drop
    public void onItemMove(int fromPosition, int toPosition) {
        Collections.swap(steps, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
    }
}
