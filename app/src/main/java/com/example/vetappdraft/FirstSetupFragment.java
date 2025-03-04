package com.example.vetappdraft;

import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.room.Room;

import android.os.health.PackageHealthStats;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Vector;
import java.util.concurrent.Executors;

public class FirstSetupFragment extends Fragment {

  private Spinner mSpinChoice;
  private Button btnSubmit;
  private TextView tvContact;
  private ImageButton btnSettings, btnEmergency;

  public String sBranch;
  private String eContact;

  private VetDatabase mcDB;
  private VetDAO mcDAO;

  public FirstSetupFragment ()
  {
    // Required empty public constructor
  }

  @Override
  public void onCreate (Bundle savedInstanceState)
  {
    super.onCreate (savedInstanceState);
  }

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

    mcDB = Room.databaseBuilder(requireContext(),
        VetDatabase.class, "VET-DB").build();
    mcDAO = mcDB.vetDAO();

    Intent intent = new Intent(requireActivity(), ChangeStepOrder.class);

    new Thread(() -> {
      int userCount = mcDAO.getSize();

      requireActivity().runOnUiThread(() -> {
        if (userCount > 0) {
          startActivity(intent);
        }
      });
    }).start();

    btnSubmit.setOnClickListener(v -> {
      sBranch = mSpinChoice.getSelectedItem().toString();
      eContact = tvContact.getText().toString();
      Executors.newSingleThreadExecutor().execute(() -> {
        VetUser newUser = new VetUser(sBranch, eContact);
        mcDAO.insert(newUser);

        // Switch to DynamicPageFragment after database insertion
        requireActivity().runOnUiThread(() -> {
          FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
          transaction.replace(R.id.fragment_container, DynamicPageFragment.newInstance(0)); // Start at page 0
          transaction.commit();
        });
      });
      // startActivity(intent);
    });

    // initializing spinner
    String[] choiceArray = new String[]{
        "Army", "Marine Corps", "Navy", "Air Force",
        "Coast Guard", "Grey Scale"
    };
    ArrayAdapter<String> choiceAdapter = new ArrayAdapter<>(
        requireContext(), android.R.layout.simple_spinner_dropdown_item, choiceArray);
    choiceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    mSpinChoice.setAdapter(choiceAdapter);

    // spinner with branches
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

      private void updateColorScheme(String selectedBranch) {
        // change color scheme depending on choice of branch
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
        // First option by default
      }
    });

    return view;
  }

  @Override
  public void onViewCreated (@NonNull View view,
      @Nullable Bundle savedInstanceState)
  {
    super.onViewCreated (view, savedInstanceState);
  }

  @Override
  public void onDestroyView ()
  {
    super.onDestroyView ();
  }
}