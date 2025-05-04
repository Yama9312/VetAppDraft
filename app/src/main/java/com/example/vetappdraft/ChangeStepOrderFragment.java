package com.example.vetappdraft;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ChangeStepOrderFragment extends Fragment {

    private Button mBtnApply;
    private ImageButton mBtnGoBack;
    private RecyclerView rvOrder;
    private StringAdapter mcAdapter;
    private List<String> theSteps;

    /**
     * Inflates the ChangeStepOrderFragment layout and sets up UI elements.
     *
     * @param inflater Used to inflate the layout XML.
     * @param container Optional parent view for the fragment's UI.
     * @param savedInstanceState Saved state, if any.
     * @return The root view of the inflated layout.
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_change_step_order, container, false);

        mBtnGoBack = view.findViewById(R.id.imBtnGoBack);
        mBtnGoBack.setOnClickListener(v -> requireActivity().getSupportFragmentManager().popBackStack());


        theSteps = new ArrayList<>();
        theSteps.add("Step 1: Take a deep breath");
        theSteps.add("Step 2: Relax");
        theSteps.add("Step 3: Breathe deeply");
        theSteps.add("Step 4: Focus");

        rvOrder = view.findViewById(R.id.recyclerView);
        rvOrder.setLayoutManager(new LinearLayoutManager(getActivity()));

        mcAdapter = new StringAdapter(theSteps);
        rvOrder.setAdapter(mcAdapter);


        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(
                ItemTouchHelper.UP | ItemTouchHelper.DOWN,
                0) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {

                int fromPosition = viewHolder.getAdapterPosition();
                int toPosition = target.getAdapterPosition();


                Collections.swap(theSteps, fromPosition, toPosition);


                mcAdapter.notifyItemMoved(fromPosition, toPosition);
                return true;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

            }
        });

        itemTouchHelper.attachToRecyclerView(rvOrder);


        return view;
    }
}
