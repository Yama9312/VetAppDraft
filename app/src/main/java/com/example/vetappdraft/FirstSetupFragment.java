package com.example.vetappdraft;

import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
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
  private Vector<Page> thePages = new Vector<>();

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

    thePages.add(new Page("step 1", MainActivity.class, "", "take a deep breath"));
    thePages.add(new Page("step 2", MainActivity.class, "", "review reasons for living\nPhone a friend"));
    thePages.add(new Page("step 3", MainActivity.class, "", "H.A.L.T"));
    thePages.add(new Page("step 4", MainActivity.class, "", "2 minute mindful breathing"));
    thePages.add(new Page("step 5", MainActivity.class, "", "safety SOP"));
    thePages.add(new Page("step 6", MainActivity.class, "", "body scan meditation"));
    thePages.add(new Page("step 7", MainActivity.class, "", "listen to music"));
    thePages.add(new Page("step 8", MainActivity.class, "", "call someone\nsee who is online"));
    thePages.add(new Page("step 9", MainActivity.class, "", "call hotlines"));
    thePages.add(new Page("step 10", MainActivity.class, "", "put weapons down"));
    thePages.add(new Page("step 11", MainActivity.class, "", "observe & describe"));
    thePages.add(new Page("step 12", MainActivity.class, "", "leaves on a stream"));
    thePages.add(new Page("step 13", MainActivity.class, "", "identify values"));
    thePages.add(new Page("step 14", MainActivity.class, "", "mindful walking"));
    thePages.add(new Page("step 15", MainActivity.class, "", "progressive muscle relaxation"));
    thePages.add(new Page("step 16", MainActivity.class, "", "distract yourself\n(test on the screen)"));
    thePages.add(new Page("step 17", MainActivity.class, "", "awareness of thoughts"));
    thePages.add(new Page("step 18", MainActivity.class, "", "follow the circle"));
    thePages.add(new Page("step 19", MainActivity.class, "", "urge surfing"));
    thePages.add(new Page("step 20", MainActivity.class, "", "S.O.B.E.R\nStop\nobserve\nbreath\nexpand\nrespond"));
    thePages.add(new Page("step 21", MainActivity.class, "", "four squared breathing"));
    thePages.add(new Page("step 22", MainActivity.class, "", "diaphragmatic breathing"));
    thePages.add(new Page("step 23", MainActivity.class, "", "5 senses"));
    thePages.add(new Page("step 24", MainActivity.class, "", "mountain meditation"));
    thePages.add(new Page("step 25", MainActivity.class, "", "distractions"));
    thePages.add(new Page("step 26", MainActivity.class, "", "whole body breathing"));
    thePages.add(new Page("step 27", MainActivity.class, "", "breathing space"));
    thePages.add(new Page("step 28", MainActivity.class, "", "breathing body"));



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
      });
      startActivity(intent);
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