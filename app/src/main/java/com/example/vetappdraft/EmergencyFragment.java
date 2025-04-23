package com.example.vetappdraft;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * EmergencyFragment displays emergency contacts like 911 etc
 *
 * there's more to add to this fragments - mostly just textviews
 * @author ania
 */

public class EmergencyFragment extends Fragment
{
  /**
   * Inflates the layout for the EmergencyFragment.
   *
   * @param inflater           LayoutInflater to inflate views in fragment
   * @param container          Parent view to attach fragment UI to
   * @param savedInstanceState Previous state, if any, of the fragment
   * @return                   The root view of the inflated layout
   */
  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_emergency, container, false);
    return view;
  }

  /**
   * Called immediately after onCreateView has returned.
   *
   * @param view               The view returned by onCreateView
   * @param savedInstanceState Previous state, if any, of the fragment
   */
  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
  }
}