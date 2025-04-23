package com.example.vetappdraft;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.LinkedList;

/**
 * Fragment to handle changing the order of steps.
 * Converted from previous BaseActivity implementation.
 *
 * @author ania
 */

public class ChangeStepOrderFragment extends Fragment
{
    private Button mBtnApply;
    private ImageButton mBtnGoBack;

    private LinkedList<Page> theSteps;
    private RecyclerView rvOrder;
    private StepAdapter mcAdapter;

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

        // back button click implementation (return to the previous fragment)
        mBtnGoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requireActivity().getSupportFragmentManager().popBackStack();
            }
        });

        theSteps = new LinkedList<>();
        theSteps.add(new Page("Step1", Page.PageType.TEXT, "take a deep breath", ""));

        // Adapter initialization (idk bro this was here so i left it)
        // mcAdapter = new StepAdapter(theSteps);
        // rvOrder.setAdapter(mcAdapter);

        return view;
    }
}