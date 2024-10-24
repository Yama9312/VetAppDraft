package com.example.vetappdraft;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class StepAdapter extends RecyclerView.Adapter<StepAdapter.ViewHolder> {
    private ArrayList<Page> steps;
    private Context context;

    public StepAdapter(ArrayList<Page> pages, Context context) {
        this.steps = pages;
        this.context = context;
    }
    public StepAdapter () {
        this.steps = new ArrayList<>();
    }

    public void addPage(Page insert) {
        steps.add(insert);
        notifyItemInserted(steps.size() - 1);
    }

    public void onItemMove(int fromPosition, int toPosition) {
        Page movedPage = steps.remove(fromPosition);
        steps.add(toPosition, movedPage);
        notifyItemMoved(fromPosition, toPosition);
    }

    @NonNull
    @Override
    public StepAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_string, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StepAdapter.ViewHolder holder, int position) {
        Page page = steps.get(position);
        holder.titleTextView.setText(page.getName());
    }

    @Override
    public int getItemCount() {
        return steps.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.textView);
        }
    }
}
