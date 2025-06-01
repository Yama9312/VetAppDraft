//***************************************************************************
// File name:   MusicSetupFragment
// Author:      Berglund Center Coding team
// Date:        06/01/2025
// Purpose:     Handles the initial setup process for the music preferences
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
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.text.TextWatcher;
import android.widget.Toast;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Executors;

public class MusicSetupFragment extends Fragment {

  private RadioButton mRadioStreaming, mRadioLocal, mRadioYouTube, mRadioSpotify;
  private LinearLayout mStreamingOptionsLayout;
  private VetDatabase mDB;

  public MusicSetupFragment () {
    // Required empty public constructor
  }

//  @Override
//  public void onCreate (Bundle savedInstanceState) {
//    super.onCreate(savedInstanceState);
//  }

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {

    View view = inflater.inflate(R.layout.fragment_music_setup, container, false);
    mDB = VetDatabase.getInstance(requireContext());

    mRadioStreaming = view.findViewById(R.id.radio_streaming);
    mRadioLocal = view.findViewById(R.id.radio_local);
    mRadioYouTube = view.findViewById(R.id.radio_youtube);
    mRadioSpotify = view.findViewById(R.id.radio_spotify);
    mStreamingOptionsLayout = view.findViewById(R.id.streaming_options);
    Button continueButton = view.findViewById(R.id.button_continue);

    // show streaming options if streaming is selected
    mRadioStreaming.setOnCheckedChangeListener((buttonView, isChecked) -> {
      if (isChecked) {
        mStreamingOptionsLayout.setVisibility(View.VISIBLE);
      }
    });

    mRadioLocal.setOnCheckedChangeListener((buttonView, isChecked) -> {
      if (isChecked) {
        mStreamingOptionsLayout.setVisibility(View.GONE);
      }
    });

    continueButton.setOnClickListener(v -> {
      new Thread(() -> {
        List<VetUser> userList = mDB.vetDAO().getAll();
        if (userList.isEmpty()) {
          requireActivity().runOnUiThread(() ->
              Toast.makeText(requireContext(), "User data missing. Restart the app.", Toast.LENGTH_SHORT).show());
          return;
        }
        VetUser user = userList.get(0);
        if (mRadioStreaming.isChecked()) {
          if (mRadioYouTube.isChecked()) {
            user.setMusicPreference("YOUTUBE");
          } else if (mRadioSpotify.isChecked()) {
            user.setMusicPreference("SPOTIFY");
          } else {
            requireActivity().runOnUiThread(() ->
                Toast.makeText(requireContext(), "Select a streaming service", Toast.LENGTH_SHORT).show());
            return;
          }
        } else if (mRadioLocal.isChecked()) {
          user.setMusicPreference("LOCAL");
        } else {
          requireActivity().runOnUiThread(() ->
              Toast.makeText(requireContext(), "Select a music preference", Toast.LENGTH_SHORT).show());
          return;
        }

        // mDB.vetDAO().update(user);

        requireActivity().runOnUiThread(() -> {
          // Launch your main app fragment or activity
          FragmentTransaction transaction = requireActivity()
              .getSupportFragmentManager()
              .beginTransaction();
          transaction.replace(R.id.fragment_container, DynamicPageFragment.newInstance(0));
          transaction.commit();
        });
      }).start();
    });

    return view;
  }
}
