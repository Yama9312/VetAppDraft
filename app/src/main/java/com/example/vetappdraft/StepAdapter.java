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

    /***************************************************************************
     * Constructor: StepAdapter
     *
     * Description: Constructs the adapter with the given data.
     *
     * Parameters: data - the data to be displayed by the views
     *
     * Returned: None
     **************************************************************************/
    public StepAdapter(ArrayList<Page> data) {
        this.mcData = data;
    }

    /***************************************************************************
     * Method: onCreateViewHolder
     *
     * Description: Creates the view from the XML file by inflating.
     *
     * Parameters: parent - the parent view group
     *             viewType - the type of the view
     *
     * Returned: ViewHolder - a new ViewHolder instance
     **************************************************************************/
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

    /***************************************************************************
     * Method: onBindViewHolder
     *
     * Description: Binds the data to the screen at the given position.
     *
     * Parameters: holder - the ViewHolder instance
     *             position - the position in the array list to be displayed
     *
     * Returned: None
     **************************************************************************/
    @Override
    public void onBindViewHolder (
            @NonNull StepAdapter.ViewHolder holder, int position)
    {
        holder.mcCourse = mcData.get(position);
        holder.bindData();
    }

    /***************************************************************************
     * Method: getItemCount
     *
     * Description: Returns the total number of items in the data set.
     *
     * Parameters: None
     *
     * Returned: int - the number of items
     **************************************************************************/
    @Override
    public int getItemCount ()
    {
        return mcData.size();
    }

    /***************************************************************************
     * Class: ViewHolder
     *
     * Description: Represents the piece of XML on the screen and assigns
     *              course data to each widget.
     **************************************************************************/
    public class ViewHolder extends RecyclerView.ViewHolder
    {
        private Page mcCourse;
        private TextView mcTVPageInfo;

        /***************************************************************************
         * Constructor: ViewHolder
         *
         * Description: Constructs a ViewHolder for the item view.
         *
         * Parameters: itemView - the view of the item
         *
         * Returned: None
         **************************************************************************/
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        /***************************************************************************
         * Method: bindData
         *
         * Description: Binds data to the corresponding UI components.
         *
         * Parameters: None
         *
         * Returned: None
         **************************************************************************/
        public void bindData() {
            if (null == mcTVPageInfo) {
                mcTVPageInfo = (TextView) itemView.findViewById(R.id.tvStep);
            }
            mcTVPageInfo.setText(mcCourse.getInstructions());
        }
    }
}
