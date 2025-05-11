package com.example.vetappdraft;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.Arrays;
import java.util.List;

public class ChangeStepOrderFragment extends Fragment {

    private Button mBtnApply;
    private ImageButton mBtnGoBack;

    private Spinner[] spinners = new Spinner[10];

    private final List<String> stepOptions = Arrays.asList(
            "Step 1: Take a deep breath",
            "Step 2: Relax",
            "Step 3: Breathe deeply",
            "Step 4: Focus",
            "Step 5: Hydrate",
            "Step 6: Stretch",
            "Step 7: Meditate",
            "Step 8: Visualize success",
            "Step 9: Commit",
            "Step 10: Act"
    );

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

            for (int i = 0; i < spinners.length; i++) {
                String selected = (String) spinners[i].getSelectedItem();

            }
        });


        for (int i = 0; i < spinners.length; i++) {
            int resId = getResources().getIdentifier("spinner" + (i + 1), "id", requireContext().getPackageName());
            spinners[i] = view.findViewById(resId);
            ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, stepOptions);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinners[i].setAdapter(adapter);
        }

        return view;
    }
}
