package com.example.vetappdraft;

import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class ChangeAppearanceFragment extends Fragment {

  private Spinner mSpinChoice;
  private Button btnSaveChanges;
  private Button btnBack;
  private VetDatabase mDB;


  //***************************************************************************
  // Method:      ChangeAppearanceFragment
  //
  // Description: Default constructor for the fragment
  //
  // Parameters:  None
  //
  // Returned:    None
  //***************************************************************************
  public ChangeAppearanceFragment() {
    // Required empty public constructor
  }

  //***************************************************************************
  // Method:      onCreateView
  //
  // Description: Inflates the fragment's layout and initializes UI components
  //
  // Parameters:  inflater - layout inflater
  //              container - parent view that the fragment's UI is attached to
  //              savedInstanceState - saved instance state
  //
  // Returned:    View - the created fragment view
  //***************************************************************************
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_change_appearance, container, false);

    mDB = VetDatabase.getInstance(requireContext());

    mSpinChoice = view.findViewById(R.id.spnBranch);
    btnSaveChanges = view.findViewById(R.id.btnSaveChanges);
    btnBack = view.findViewById (R.id.btnBack);

    btnBack.setOnClickListener(v -> {
      requireActivity().onBackPressed();
    });

    // Initialize spinner
    String[] choiceArray = new String[]{"Army", "Marine Corps", "Navy", "Air Force", "Coast Guard", "Grey Scale"};
    ArrayAdapter<String> choiceAdapter = new ArrayAdapter<>(
        requireContext(), android.R.layout.simple_spinner_dropdown_item, choiceArray);
    choiceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    mSpinChoice.setAdapter(choiceAdapter);

    // Set current selection if available
    String currentBranch = requireContext().getSharedPreferences("AppPreferences",
            requireContext().MODE_PRIVATE)
        .getString("selectedBranch", "Army");
    int position = choiceAdapter.getPosition(currentBranch);
    if (position >= 0) {
      mSpinChoice.setSelection(position);
    }

    mSpinChoice.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
      @Override
      public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String selectedBranch = (String) adapterView.getItemAtPosition(i);
        updateColorScheme(selectedBranch);
      }

      @Override
      public void onNothingSelected(AdapterView<?> adapterView) {
      }
    });

    btnSaveChanges.setOnClickListener(v -> {
      String selectedBranch = mSpinChoice.getSelectedItem().toString();
      // Save the selected branch to preferences
      requireContext().getSharedPreferences("AppPreferences",
              requireContext().MODE_PRIVATE)
          .edit()
          .putString("selectedBranch", selectedBranch)
          .apply();
          new Thread(() -> {
            VetDAO dao = mDB.vetDAO ();
            VetUser user = dao.getAll ().get (0);
            user.setMcBranch (selectedBranch);
            dao.update (user);
          }).start ();
      // Go back to previous fragment
      requireActivity().onBackPressed();
    });

    return view;
  }

  //***************************************************************************
  // Method:      updateColorScheme
  //
  // Description: Updates the app's color scheme based on the selected branch
  //
  // Parameters:  selectedBranch - the selected branch name
  //
  // Returned:    None
  //***************************************************************************
  private void updateColorScheme(String selectedBranch) {
    int[] colors;
    switch (selectedBranch) {
      case "Army":
        colors = new int[]{0xFFFEFEFE, 0xFFFDE37D};
        break;
      case "Marine Corps":
        colors = new int[]{0xFFFDF3DE, 0xFFBE2321};
        break;
      case "Navy":
        colors = new int[]{0xFFFFFEFC, 0xFFF3C881};
        break;
      case "Air Force":
        colors = new int[]{0xFFFEF5E0, 0xFFC1871F};
        break;
      case "Coast Guard":
        colors = new int[]{0xFFFFFFFF, 0xFF205698};
        break;
      case "Grey Scale":
        colors = new int[]{0xFFFEFEFE, 0xFF59595B};
        break;
      default:
        colors = new int[]{0xFFFFFFFF, 0xFFFFFFFF};
        break;
    }

    GradientDrawable gradientDrawable = new GradientDrawable(
        GradientDrawable.Orientation.TOP_BOTTOM,
        colors
    );

    requireActivity().getWindow().getDecorView().setBackground(gradientDrawable);
  }
}