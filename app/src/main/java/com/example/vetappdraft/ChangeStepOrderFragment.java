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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ChangeStepOrderFragment extends Fragment {

    private Button mBtnApply;
    private ImageButton mBtnGoBack;

    private Spinner[] spinners = new Spinner[10];

    private final List<String> stepOptions = Arrays.asList(
            "Take a deep breath",
            "Review reasons for living - Phone a friend",
            "H.A.L.T",
            "2 minute mindful breathing",
            "SafetySOP",
            "Body scan meditation",
            "Listen to music",
            "Call someone - See who is online",
            "Call hotlines",
            "Put weapons down",
            "Observe & describe",
            "Leaves on a stream",
            "Identify values",
            "Mindful walking",
            "Progressive muscle relaxation",
            "Distract yourself",
            "Awareness of thought",
            "Follow the circle",
            "Urge surfing",
            "SOBER - Stop - Observe - Breathe - Expand - Respond",
            "Four squared breathing",
            "Diaphragmatic breathing",
            "5 senses",
            "Mountain meditation",
            "Distractions",
            "Whole body breathing",
            "Breathing space",
            "Breathing body"
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
            List<String> selectedSteps = new ArrayList<>();
            for (Spinner spinner : spinners) {
                String selected = (String) spinner.getSelectedItem();
                selectedSteps.add(selected);
            }

            //handleStepOrder(selectedSteps); where I update the order step

            requireActivity().getSupportFragmentManager().popBackStack();
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

    /*
    private void handleStepOrder(List<String> selectedSteps) {

    }
     */
}
