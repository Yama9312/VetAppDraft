package com.example.vetappdraft;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

public class TopNavBarFragment extends Fragment {

  private ImageButton btnSettings;
  private ImageButton btnEmergency;

  //***************************************************************************
  // Method:      TopNavBarFragment
  //
  // Description: Required empty public constructor
  //
  // Parameters:  None
  //
  // Returned:    None
  //***************************************************************************
  public TopNavBarFragment () {
    // Required empty public constructor
  }

  //***************************************************************************
  // Method:      onCreate
  //
  // Description: Called when the fragment is created
  //
  // Parameters:  savedInstanceState - Bundle containing the saved instance state
  //
  // Returned:    None
  //***************************************************************************
  @Override
  public void onCreate (Bundle savedInstanceState) {
    super.onCreate (savedInstanceState);
  }

  //***************************************************************************
  // Method:      onViewCreated
  //
  // Description: Initializes the view elements and sets up button listeners
  //
  // Parameters:  view - the created view
  //              savedInstanceState - Bundle containing the saved instance state
  //
  // Returned:    None
  //***************************************************************************
  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    btnSettings =  view.findViewById(R.id.settingsButton);

    btnSettings.setOnClickListener(v -> {
      Fragment currentFragment = requireActivity()
          .getSupportFragmentManager()
          .findFragmentById(R.id.fragment_container);

      if (currentFragment instanceof SettingsFragment) {
        requireActivity().getSupportFragmentManager().popBackStack();
      } else {
        FragmentTransaction transaction = requireActivity()
            .getSupportFragmentManager()
            .beginTransaction();
        transaction.replace(R.id.fragment_container, new SettingsFragment());
        transaction.addToBackStack("SettingsFragment");
        transaction.commit();
      }
    });


    btnEmergency =  view.findViewById(R.id.emergencyButton);

    btnEmergency.setOnClickListener(v -> {
      Fragment currentFragment = requireActivity()
          .getSupportFragmentManager()
          .findFragmentById(R.id.fragment_container);

      if (currentFragment instanceof EmergencyFragment) {
        requireActivity().getSupportFragmentManager().popBackStack();
      } else {
        FragmentTransaction transaction = requireActivity()
            .getSupportFragmentManager()
            .beginTransaction();
        transaction.replace(R.id.fragment_container, new EmergencyFragment());
        transaction.addToBackStack("EmergencyFragment");
        transaction.commit();
      }
    });

  }

  //***************************************************************************
  // Method:      onCreateView
  //
  // Description: Inflates the layout for this fragment
  //
  // Parameters:  inflater - LayoutInflater object to inflate the view
  //              container - parent view to attach the fragment's view
  //              savedInstanceState - Bundle containing the saved instance state
  //
  // Returned:    The inflated view
  //***************************************************************************
  @Nullable
  @Override
  public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    return inflater.inflate (R.layout.fragment_top_nav_bar, container, false);
  }
}
