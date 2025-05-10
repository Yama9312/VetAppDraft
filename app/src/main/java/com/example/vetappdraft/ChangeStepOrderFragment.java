package com.example.vetappdraft;

import android.os.Bundle;
import android.util.Log;
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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_change_step_order, container, false);

        mBtnGoBack = view.findViewById(R.id.imBtnGoBack);
        mBtnGoBack.setOnClickListener(v -> requireActivity().getSupportFragmentManager().popBackStack());

        mBtnApply = view.findViewById(R.id.btnApply);
        mBtnApply.setOnClickListener(v -> {
            for (String step : theSteps) {
                Log.d("StepOrder", step);
            }
        });

        theSteps = new ArrayList<>();
        theSteps.add("Step 1: Take a deep breath");
        theSteps.add("Step 2: Relax");
        theSteps.add("Step 3: Breathe deeply");
        theSteps.add("Step 4: Focus");

        rvOrder = view.findViewById(R.id.recyclerView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        rvOrder.setLayoutManager(layoutManager);

        mcAdapter = new StringAdapter(theSteps);
        rvOrder.setAdapter(mcAdapter);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT, 0) {

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView,
                                  @NonNull RecyclerView.ViewHolder viewHolder,
                                  @NonNull RecyclerView.ViewHolder target) {

                int fromPosition = viewHolder.getAdapterPosition();
                int toPosition = target.getAdapterPosition();

                Log.d("ItemTouchHelper", "Moving from " + fromPosition + " to " + toPosition);

                Collections.swap(theSteps, fromPosition, toPosition);
                mcAdapter.notifyItemMoved(fromPosition, toPosition);
                return true;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                //nothing
            }

            @Override
            public boolean isLongPressDragEnabled() {
                return true;
            }
        });

        itemTouchHelper.attachToRecyclerView(rvOrder);

        return view;
    }
}
