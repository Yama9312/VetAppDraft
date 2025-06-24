package com.example.vetappdraft;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

/**
 * EmergencyFragment displays emergency contacts like 911 etc.
 */
public class EmergencyFragment extends Fragment {

  private Button btnReturn;

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_emergency, container, false);
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    // Setup click listeners for fixed emergency numbers
    setupPhoneClick(view, R.id.textViewEMS, "911");
    setupPhoneClick(view, R.id.textViewPolice, "123-456-7890");
    setupPhoneClick(view, R.id.textViewHotline, "988");
    btnReturn = view.findViewById (R.id.returnToStepsButton);

    btnReturn.setOnClickListener(v -> {
      FragmentTransaction transaction = requireActivity()
          .getSupportFragmentManager()
          .beginTransaction();
      transaction.replace(R.id.fragment_container, new DynamicPageFragment ());
      transaction.addToBackStack(null);
      transaction.commit();
    });

    TextView friendTextView = view.findViewById(R.id.textViewFriend);

    // Run database query off the main thread
    new Thread(() -> {
      VetDatabase db = VetDatabase.getInstance(requireContext());
      VetDAO dao = db.vetDAO();

      List<VetUser> contacts = dao.getAll();

      // Post UI updates to main thread
      requireActivity().runOnUiThread(() -> {
        if (contacts != null && !contacts.isEmpty()) {
          String emergencyNumber = contacts.get(0).getMcEContact();
          if (emergencyNumber != null && !emergencyNumber.isEmpty()) {
            friendTextView.setText("Phone a Friend: " + emergencyNumber);
            friendTextView.setOnClickListener(v -> {
              Intent intent = new Intent(Intent.ACTION_DIAL);
              intent.setData(Uri.parse("tel:" + emergencyNumber));
              startActivity(intent);
            });
          } else {
            friendTextView.setText("No emergency contact found");
            friendTextView.setOnClickListener(null);
          }
        } else {
          friendTextView.setText("No emergency contact found");
          friendTextView.setOnClickListener(null);
        }
      });
    }).start();
  }

  /**
   * Helper method to setup click listener on a TextView that dials a phone number.
   *
   * @param rootView     The root view to find the TextView by id
   * @param textViewId   The id of the TextView
   * @param phoneNumber  The phone number to dial on click
   */
  private void setupPhoneClick(View rootView, int textViewId, String phoneNumber) {
    TextView textView = rootView.findViewById(textViewId);
    if (textView != null) {
      textView.setOnClickListener(v -> {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + phoneNumber));
        startActivity(intent);
      });
    }
  }
}
