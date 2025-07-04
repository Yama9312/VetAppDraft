//***************************************************************************
// File name:   FirstSetupFragment
// Author:      Berglund Center Coding team
// Date:        4/3/25
// Purpose:     Handles the initial setup process for the app
//***************************************************************************
package com.example.vetappdraft;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.room.Room;

import android.telephony.PhoneNumberFormattingTextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.text.TextWatcher;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

public class FirstSetupFragment extends Fragment {

  public enum MusicPreference {
    YOUTUBE,
    SPOTIFY,
    LOCAL
  }

  private Spinner mSpinChoice;
  private Button btnSubmit;
  private TextView tvContact;
  private ImageButton btnSettings, btnEmergency;

  public String sBranch;
  private String eContact;

  private VetDatabase mcDB;
  private VetDAO mcDAO;

  //***************************************************************************
  // Method:      FirstSetupFragment
  //
  // Description: Default constructor for the fragment
  //
  // Parameters:  None
  //
  // Returned:    None
  //***************************************************************************
  public FirstSetupFragment () {
    // Required empty public constructor
  }

  //***************************************************************************
  // Method:      onCreate
  //
  // Description: Runs when the fragment is created
  //
  // Parameters:  savedInstanceState - the current instance state
  //
  // Returned:    None
  //***************************************************************************
  @Override
  public void onCreate (Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
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
  @SuppressLint("MissingInflatedId")
  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater,
                           @Nullable ViewGroup container,
                           @Nullable Bundle savedInstanceState) {

    View view = inflater.inflate(R.layout.fragment_first_setup, container, false);

    btnSettings = view.findViewById(R.id.settingsButton);
    btnSubmit = view.findViewById(R.id.btnSubBranch);
    mSpinChoice = view.findViewById(R.id.spnBranch);
    tvContact = view.findViewById(R.id.phEContact1);

    tvContact.addTextChangedListener(new PhoneNumberFormattingTextWatcher ());

    mcDB = VetDatabase.getInstance(requireContext());
    mcDAO = mcDB.vetDAO();

    btnSubmit.setOnClickListener(v -> {
      sBranch = mSpinChoice.getSelectedItem().toString();
      eContact = tvContact.getText().toString().replaceAll("[^\\d]", "");

      if (eContact.length() != 10) {
        tvContact.setError("Enter a valid 10-digit U.S. phone number");
        return;
      }

      Executors.newSingleThreadExecutor().execute(() -> {
        if (mcDAO.getSize() == 0) {
          VetUser newUser = new VetUser(sBranch, eContact);
          List<Integer> numbers = new ArrayList<> (List.of(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27));
          newUser.setMcPageIndexes (numbers);
          mcDAO.insert(newUser);
        }
        requireActivity().runOnUiThread(() -> {
          FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
          transaction.replace(R.id.fragment_container, new MusicSetupFragment());
          transaction.commit();
        });
      });
    });

    // Initialize spinner
    String[] choiceArray = new String[]{"Army", "Marine Corps", "Navy", "Air Force", "Coast Guard", "Grey Scale"};
    ArrayAdapter<String> choiceAdapter = new ArrayAdapter<>(
            requireContext(), android.R.layout.simple_spinner_dropdown_item, choiceArray);
    choiceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    mSpinChoice.setAdapter(choiceAdapter);

    mSpinChoice.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
      @Override
      public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String selectedBranch = (String) adapterView.getItemAtPosition(i);
        requireContext().getSharedPreferences("AppPreferences",
                        requireContext().MODE_PRIVATE)
                .edit()
                .putString("selectedBranch", selectedBranch)
                .apply();
        updateColorScheme(selectedBranch);
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

      @Override
      public void onNothingSelected(AdapterView<?> adapterView) {
      }
    });

    return view;
  }
}
