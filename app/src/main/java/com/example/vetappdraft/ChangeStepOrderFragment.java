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

        switch (selectedText) {
            case "Take a deep breath":
                pages.add(new Page(stepLabel, Page.PageType.TEXT, "take a deep breath", ""));
                break;
            case "Review reasons for living - Phone a friend":
                pages.add(new Page(stepLabel, Page.PageType.TEXT, "review reasons for living\nPhone a friend", ""));
                break;
            case "H.A.L.T":
                pages.add(new Page(stepLabel, Page.PageType.TEXT, "H.A.L.T", ""));
                break;
            case "2 minute mindful breathing":
                pages.add(new Page(stepLabel, Page.PageType.TEXT, "2 minute mindful breathing", "", R.raw.two_minutes_of_mindful_breathing_vf));
                break;
            case "SafetySOP":
                pages.add(new Page(stepLabel, Page.PageType.TEXT, "safetySOP", ""));
                break;
            case "Body scan meditation":
                pages.add(new Page(stepLabel, Page.PageType.TEXT, "body scan meditation", "", R.raw.body_scan_v2));
                break;
            case "Listen to music":
                pages.add(new Page(stepLabel, Page.PageType.TEXT, "listen to music", ""));
                break;
            case "Call someone - See who is online":
                pages.add(new Page(stepLabel, Page.PageType.TEXT, "call someone\nsee who is online", ""));
                break;
            case "Call hotlines":
                pages.add(new Page(stepLabel, Page.PageType.TEXT, "call hotlines", ""));
                break;
            case "Put weapons down":
                pages.add(new Page(stepLabel, Page.PageType.TEXT, "put weapons down", ""));
                break;
            case "Observe & describe":
                pages.add(new Page(stepLabel, Page.PageType.TEXT, "observe & describe", "", R.raw.observe_and_describe_vf));
                break;
            case "Leaves on a stream":
                pages.add(new Page(stepLabel, Page.PageType.TEXT, "leaves on a stream", "", R.raw.leaves_on_a_stream_vf));
                break;
            case "Identify values":
                pages.add(new Page(stepLabel, Page.PageType.TEXT, "identify values", ""));
                break;
            case "Mindful walking":
                pages.add(new Page(stepLabel, Page.PageType.TEXT, "mindful walking", "", R.raw.mindful_walking_vf));
                break;
            case "Progressive muscle relaxation":
                pages.add(new Page(stepLabel, Page.PageType.TEXT, "progressive muscle\nrelaxation", "", R.raw.progressive_muscle_relaxing_vf));
                break;
            case "Distract yourself":
                pages.add(new Page(stepLabel, Page.PageType.TEXT, "distract yourself", ""));
                break;
            case "Awareness of thought":
                pages.add(new Page(stepLabel, Page.PageType.TEXT, "awareness of thought", "", R.raw.awareness_of_thoughts_vf));
                break;
            case "Follow the circle":
                pages.add(new Page(stepLabel, Page.PageType.TEXT, "follow the circle", ""));
                break;
            case "Urge surfing":
                pages.add(new Page(stepLabel, Page.PageType.TEXT, "urge surfing", "", R.raw.urge_surfing_vf));
                break;
            case "SOBER - Stop - Observe - Breathe - Expand - Respond":
                pages.add(new Page(stepLabel, Page.PageType.TEXT, "SOBER\n-Stop\n-observe\n-breath\n-expand\n-respond", ""));
                break;
            case "Four squared breathing":
                pages.add(new Page(stepLabel, Page.PageType.TEXT, "four squared breathing", "", R.raw.four_square_breathing_vf));
                break;
            case "Diaphragmatic breathing":
                pages.add(new Page(stepLabel, Page.PageType.TEXT, "diaphragmatic breathing", "", R.raw.diagramic_breathing_vf));
                break;
            case "5 senses":
                pages.add(new Page(stepLabel, Page.PageType.TEXT, "5 senses", "", R.raw.five_senses_vf));
                break;
            case "Mountain meditation":
                pages.add(new Page(stepLabel, Page.PageType.TEXT, "mountain meditation", "", R.raw.mountian_meditation_vf));
                break;
            case "Distractions":
                pages.add(new Page(stepLabel, Page.PageType.TEXT, "distractions", ""));
                break;
            case "Whole body breathing":
                pages.add(new Page(stepLabel, Page.PageType.TEXT, "whole body breathing", "", R.raw.whole_body_breathing_meditation_vf));
                break;
            case "Breathing space":
                pages.add(new Page(stepLabel, Page.PageType.TEXT, "breathing space", "", R.raw.breathing_space_vf));
                break;
            case "Breathing body":
                pages.add(new Page(stepLabel, Page.PageType.TEXT, "breathing body", "", R.raw.breathing_body_vf));
                break;
            default:
                pages.add(new Page(stepLabel, Page.PageType.TEXT, selectedText, ""));
                break;
        }
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
