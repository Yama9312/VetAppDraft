package com.example.vetappdraft;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class ChangeStepOrderFragment extends Fragment {

    private View rootView;
    private List<Spinner> spinners = new ArrayList<>();
    private List<TextView> stepLabels = new ArrayList<>();
    private LinkedList<Page> pages = new LinkedList<>();

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
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_change_step_order, container, false);

        //buttons
        ImageButton btnGoBack = rootView.findViewById(R.id.imBtnGoBack);
        Button btnApply = rootView.findViewById(R.id.btnApply);

        //button listeners
        btnGoBack.setOnClickListener(v -> exitFragment());
        btnApply.setOnClickListener(v -> {
            buildPagesFromSpinners();
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).updatePages(pages);
            }
            exitFragment();
        });

        //spinner and label id's
        for (int i = 1; i <= 10; i++) {
            int spinnerId = getResources().getIdentifier("spinner" + i, "id", requireContext().getPackageName());
            int labelId = getResources().getIdentifier("step" + i, "id", requireContext().getPackageName());
            spinners.add(rootView.findViewById(spinnerId));
            stepLabels.add(rootView.findViewById(labelId));
        }

        //setup the spinners
        setupSpinners();

        return rootView;
    }

    private void setupSpinners() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_spinner_item, stepOptions);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        for (Spinner spinner : spinners) {
            spinner.setAdapter(adapter);
        }
    }

    private void createPageFromSpinnerSelection(Spinner spinner) {
        String spinnerIdString = getResources().getResourceEntryName(spinner.getId());
        String stepLabel = spinnerIdToStepLabel(spinnerIdString);
        String selectedText = spinner.getSelectedItem().toString();
        Page newPage = new Page(stepLabel, Page.PageType.TEXT, selectedText, "");
        pages.add(newPage);
    }

    private void buildPagesFromSpinners() {
        pages.clear();
        for (Spinner spinner : spinners) {
            createPageFromSpinnerSelection(spinner);
        }
    }

    private String spinnerIdToStepLabel(String spinnerId) {
        String number = spinnerId.replaceAll("\\D+", "");
        return "step " + number;
    }

    private void exitFragment() {
        requireActivity().getSupportFragmentManager().popBackStack();
    }

    @Override
    public void onPause() {
        super.onPause();
        buildPagesFromSpinners();
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).updatePages(pages);
        }
    }

    public LinkedList<Page> getPages() {
        return pages;
    }
}
