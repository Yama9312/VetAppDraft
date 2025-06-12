package com.example.vetappdraft;

import android.app.AlertDialog;
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

    List<Integer> mcPageIndexes = new ArrayList<>();
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

    /**
     * Creates and returns the view hierarchy associated with the fragment.
     * @param inflater The LayoutInflater object that can be used to inflate any views.
     * @param container If non-null, this is the parent view to attach the fragment's UI to.
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous state.
     * @return The root view for the fragment's UI.
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_change_step_order, container, false);

        // Buttons
        ImageButton btnGoBack = rootView.findViewById(R.id.imBtnGoBack);
        Button btnApply = rootView.findViewById(R.id.btnApply);

        // Button listeners
        btnGoBack.setOnClickListener(v -> exitFragment());
        btnApply.setOnClickListener(v -> {
            if (hasDuplicateSelections()) {
                showDuplicateWarning();
            } else {
                buildPagesFromSpinners();

                new Thread(() -> {
                    VetDatabase db = VetDatabase.getInstance(requireContext());
                    VetDAO dao = db.vetDAO();

                    VetUser user = dao.getAll ().get (0);
                    if (user != null) {
                        user.setMcPageIndexes (mcPageIndexes);
                        dao.update(user);
                    }

                    requireActivity().runOnUiThread(() -> {
                        if (getActivity() instanceof MainActivity) {
                            ((MainActivity) getActivity()).updatePages(pages);
                        }
                        exitFragment();
                    });
                }).start();
            }
        });

        // Spinner and label IDs
        for (int i = 1; i <= 10; i++) {
            int spinnerId = getResources().getIdentifier("spinner" + i, "id", requireContext().getPackageName());
            int labelId = getResources().getIdentifier("step" + i, "id", requireContext().getPackageName());
            spinners.add(rootView.findViewById(spinnerId));
            stepLabels.add(rootView.findViewById(labelId));
        }

        setupSpinners();
        return rootView;
    }

    /**
     * Initializes the spinners with step options and sets default selections.
     */
    private void setupSpinners() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_spinner_item, stepOptions);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        for (int i = 0; i < spinners.size(); i++) {
            Spinner spinner = spinners.get(i);
            spinner.setAdapter(adapter);

            if (i < stepOptions.size()) {
                spinner.setSelection(i);
            }
        }
    }

    /**
     * Creates a Page object based on the selected item in the spinner and adds it to the list.
     * @param spinner The spinner whose selected item will be used to create a Page.
     */
    private void createPageFromSpinnerSelection(Spinner spinner) {
        String spinnerIdString = getResources().getResourceEntryName(spinner.getId());
        String stepLabel = spinnerIdToStepLabel(spinnerIdString);
        String selectedText = spinner.getSelectedItem().toString();

        pages.add(new Page(stepLabel, Page.PageType.TEXT, selectedText, ""));
        for (int i = 0; i < stepLabels.size(); i++) {
            if (stepLabels.get(i).getText().toString().equals(stepLabel)) {
                mcPageIndexes.add (i);
                break;
            }
        }
    }

    /**
     * Builds the list of pages from the current spinner selections.
     */
    private void buildPagesFromSpinners() {
        pages.clear();
        for (Spinner spinner : spinners) {
            createPageFromSpinnerSelection(spinner);
        }
    }

    /**
     * Checks whether there are any duplicate step selections in the spinners.
     * @return True if duplicates exist, false otherwise.
     */
    private boolean hasDuplicateSelections() {
        List<String> selectedSteps = new ArrayList<>();
        for (Spinner spinner : spinners) {
            String selectedText = spinner.getSelectedItem().toString();
            if (selectedSteps.contains(selectedText)) {
                return true;
            }
            selectedSteps.add(selectedText);
        }
        return false;
    }

    /**
     * Displays an alert dialog warning the user about duplicate step selections.
     */
    private void showDuplicateWarning() {
        new AlertDialog.Builder(requireContext())
                .setTitle("Duplicate Steps Found")
                .setMessage("Please remove duplicate steps before applying changes.")
                .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                .show();
    }

    /**
     * Converts a spinner ID string into a corresponding step label.
     * @param spinnerId The resource ID string of the spinner (e.g., "spinner1").
     * @return A formatted step label (e.g., "Step 1").
     */
    private String spinnerIdToStepLabel(String spinnerId) {
        String number = spinnerId.replaceAll("\\D+", "");
        return "Step " + number;
    }

    /**
     * Exits the current fragment by popping it from the back stack.
     */
    private void exitFragment() {
        requireActivity().getSupportFragmentManager().popBackStack();
    }

    /**
     * Called when the fragment is paused. Updates the main activity's pages.
     */
    @Override
    public void onPause() {
        super.onPause();
        buildPagesFromSpinners();
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).updatePages(pages);
        }
    }

    /**
     * Returns the list of Page objects built from spinner selections.
     * @return A LinkedList of Page objects.
     */
    public LinkedList<Page> getPages() {
        return pages;
    }
}
