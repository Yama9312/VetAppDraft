package com.example.vetappdraft;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;

public class StepAdapter extends
    RecyclerView.Adapter<StepAdapter.ViewHolder>
{
    private ArrayList<Page> mcData;
    /***
     * Constructor to build the adapter
     * @param data the data to be displayed by the views
     */
    public StepAdapter(ArrayList<Page> data) {
        this.mcData = data;
    }

    // create the view from the xml file by inflating
    @NonNull
    @Override
    public StepAdapter.ViewHolder onCreateViewHolder (
        @NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.display_step, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    // tells us what position in the array list we need to display and binds data to screen
    @Override
    public void onBindViewHolder (
        @NonNull StepAdapter.ViewHolder holder, int position)
    {
        holder.mcCourse = mcData.get(position);
        holder.bindData();
    }

    @Override
    public int getItemCount ()
    {
        return mcData.size();
    }

    // represents the piece of xml on the screen and puts course data in each widget
    public class ViewHolder extends RecyclerView.ViewHolder
    {
        private Page mcCourse;
        private TextView mcTVPageInfo;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
        public void bindData() {
            if (null == mcTVPageInfo) {
                mcTVPageInfo = (TextView) itemView.findViewById(R.id.tvStep);
            }
            mcTVPageInfo.setText(mcCourse.getInstructions ());
        }
    }
}

