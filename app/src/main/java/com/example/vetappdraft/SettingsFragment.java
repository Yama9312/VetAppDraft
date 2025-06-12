package com.example.vetappdraft;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.util.Log;

/**
 * SettingsFragment provides navigation to various settings screens within the app.
 *
 * @author ania
 */

public class SettingsFragment extends Fragment {

    private Button changeOrderButton;
    private Button emergencyContactsButton;
    private Button appearanceButton;
    private Button skillToolboxButton;

    /**
     * Inflates the settings layout and initializes button views.
     *
     * @param inflater           LayoutInflater to inflate views in fragment
     * @param container          Parent view to attach fragment UI to
     * @param savedInstanceState Previous state, if any, of the fragment
     * @return                   The root view of the inflated layout
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        changeOrderButton = view.findViewById(R.id.changeOrderButton);
//        emergencyContactsButton = view.findViewById(R.id.emergencyContactsButton);
//        appearanceButton = view.findViewById(R.id.appearanceButton);
//        skillToolboxButton = view.findViewById(R.id.skillToolboxButton);

        return view;
    }

    /**
     * Sets click listeners for navigation buttons after the view has been created.
     *
     * @param view               The view returned by onCreateView
     * @param savedInstanceState Previous state, if any, of the fragment
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //how we get to change order
        changeOrderButton.setOnClickListener(v -> {
            FragmentTransaction transaction = requireActivity()
                .getSupportFragmentManager()
                .beginTransaction();

            Log.d("ButtonClick", "Went in here");

            transaction.replace(R.id.fragment_container, new ChangeStepOrderFragment());
            transaction.addToBackStack(null);
            transaction.commit();
        });

        emergencyContactsButton = view.findViewById(R.id.emergencyContactsButton);

        emergencyContactsButton.setOnClickListener(v -> {
            FragmentTransaction transaction = requireActivity()
                .getSupportFragmentManager()
                .beginTransaction();
            transaction.replace(R.id.fragment_container, new EmergencyFragment());
            transaction.addToBackStack(null);
            transaction.commit();

        });
//
//        appearanceButton.setOnClickListener(v -> {
//            // Navigate to Appearance Settings Fragment
//            // Replace with your actual fragment
//            FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
//            transaction.replace(R.id.fragment_container, new AppearanceSettingsFragment());//AppearanceSettingsFragment is a placeholder.
//            transaction.addToBackStack(null);
//            transaction.commit();
//
//        });
//
//        skillToolboxButton.setOnClickListener(v -> {
//            // Navigate to Skill Toolbox Fragment
//            // Replace with your actual fragment
//            FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
//            transaction.replace(R.id.fragment_container, new SkillToolboxFragment());//SkillToolboxFragment is a placeholder.
//            transaction.addToBackStack(null);
//            transaction.commit();
//
//        });
    }
}