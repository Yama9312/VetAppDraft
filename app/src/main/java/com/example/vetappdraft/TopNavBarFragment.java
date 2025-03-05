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

public class TopNavBarFragment extends Fragment
{

  public TopNavBarFragment ()
  {
    // Required empty public constructor
  }

  @Override
  public void onCreate (Bundle savedInstanceState)
  {
    super.onCreate (savedInstanceState);
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    ImageButton btnSettings =  view.findViewById(R.id.settingsButton);
    btnSettings.setOnClickListener(v -> {
      FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
      transaction.replace(R.id.fragment_container, new SettingsFragment());
      transaction.addToBackStack(null);
      transaction.commit();
    });

//    view.findViewById(R.id.someButton).setOnClickListener(v -> {
//      Fragment newFragment = new SomeOtherFragment();
//      if (getActivity() instanceof MainActivity) {
//        ((MainActivity) getActivity()).loadContentFragment(newFragment);
//      }
//    });
  }

  @Nullable
  @Override
  public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
  {
    // Inflate the layout for this fragment
    return inflater.inflate (R.layout.fragment_top_nav_bar, container, false);
  }
}