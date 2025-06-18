package com.example.vetappdraft;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;

/**
 * Fragment that allows the user to change their music playback preference
 * (YouTube, Spotify, or local files) from the app settings. Reuses the initial
 * setup layout but provides updated behavior for modifying an existing preference.
 */

public class MusicSetupSettingsFragment extends Fragment {

  private VetDatabase mcDB;
  private LinearLayout mStreamingOptionsLayout;
  private RadioButton mRadioStreaming, mRadioLocal, mRadioYouTube, mRadioSpotify;

  public MusicSetupSettingsFragment() {
    super(R.layout.fragment_music_setup); // reusing layout
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    mcDB = VetDatabase.getInstance(requireContext());

    mRadioStreaming = view.findViewById(R.id.radio_streaming);
    mRadioSpotify = view.findViewById(R.id.radio_spotify);
    mRadioYouTube = view.findViewById(R.id.radio_youtube);
    mRadioLocal = view.findViewById(R.id.radio_local);
    mStreamingOptionsLayout = view.findViewById(R.id.streaming_options);
    Button continueButton = view.findViewById(R.id.button_continue);

    continueButton.setText("Done");

    new Thread(() -> {
      String currentPref = mcDB.vetDAO().getMcMusicPreference();

      requireActivity().runOnUiThread(() -> {
        if (currentPref != null) {
          if (currentPref.equalsIgnoreCase("YouTube")) {
            mRadioStreaming.setChecked(true);
            mStreamingOptionsLayout.setVisibility(View.VISIBLE);
            mRadioYouTube.setChecked(true);
          } else if (currentPref.equalsIgnoreCase("Spotify")) {
            mRadioStreaming.setChecked(true);
            mStreamingOptionsLayout.setVisibility(View.VISIBLE);
            mRadioSpotify.setChecked(true);
          } else if (currentPref.equalsIgnoreCase("Local")) {
            mRadioLocal.setChecked(true);
            mStreamingOptionsLayout.setVisibility(View.GONE);
          }
        }
      });
    }).start();


    // Listeners to toggle streaming options
    mRadioStreaming.setOnCheckedChangeListener((buttonView, isChecked) -> {
      if (isChecked) {
        mStreamingOptionsLayout.setVisibility(View.VISIBLE);
        mRadioLocal.setChecked(false);
      }
    });

    mRadioLocal.setOnCheckedChangeListener((buttonView, isChecked) -> {
      if (isChecked) {
        mStreamingOptionsLayout.setVisibility(View.GONE);
        mRadioYouTube.setChecked(false);
        mRadioSpotify.setChecked(false);
        // Save preference immediately
        new Thread(() -> mcDB.vetDAO().updateMcMusicPreference("Local")).start();
      }
    });

    // Listeners for Spotify/YouTube
    mRadioYouTube.setOnCheckedChangeListener((buttonView, isChecked) -> {
      if (isChecked) {
        mRadioSpotify.setChecked(false);
        mRadioStreaming.setChecked(true);
        mRadioLocal.setChecked(false);
        new Thread(() -> mcDB.vetDAO().updateMcMusicPreference("YouTube")).start();
      }
    });

    mRadioSpotify.setOnCheckedChangeListener((buttonView, isChecked) -> {
      if (isChecked) {
        mRadioYouTube.setChecked(false);
        mRadioStreaming.setChecked(true);
        mRadioLocal.setChecked(false);
        new Thread(() -> mcDB.vetDAO().updateMcMusicPreference("Spotify")).start();
      }
    });

    continueButton.setOnClickListener(v -> {
      requireActivity()
          .getSupportFragmentManager()
          .popBackStack(); // go back to previous fragment
    });

  }
}