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

/**
 *
 */

public class SettingsFragment extends Fragment {

    private Button changeOrderButton;
    private Button emergencyContactsButton;
    private Button appearanceButton;
    private Button skillToolboxButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

//        changeOrderButton = view.findViewById(R.id.changeOrderButton);
//        emergencyContactsButton = view.findViewById(R.id.emergencyContactsButton);
//        appearanceButton = view.findViewById(R.id.appearanceButton);
//        skillToolboxButton = view.findViewById(R.id.skillToolboxButton);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        changeOrderButton.setOnClickListener(v -> {
//            // Navigate to Change Order of Steps Fragment
//            // Replace with your actual fragment
//            FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
//            transaction.replace(R.id.fragment_container, new ChangeStepOrder()); //ChangeStepOrder is a placeholder.
//            transaction.addToBackStack(null);
//            transaction.commit();
//        });
//
//        emergencyContactsButton.setOnClickListener(v -> {
//            // Navigate to Emergency Contacts Fragment
//            // Replace with your actual fragment
//            FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
//            transaction.replace(R.id.fragment_container, new EmergencyContactsFragment());//EmergencyContactsFragment is a placeholder.
//            transaction.addToBackStack(null);
//            transaction.commit();
//
//        });
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