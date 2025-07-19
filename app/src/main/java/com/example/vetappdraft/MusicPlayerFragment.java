package com.example.vetappdraft;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 *
 *
 *
 */
public class MusicPlayerFragment extends Fragment {

  private VetDatabase mcDB;

  public MusicPlayerFragment() {
    super(R.layout.fragment_music_player); // empty layout that you'll dynamically fill
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    mcDB = VetDatabase.getInstance(requireContext());

    new Thread(() -> {
      String pref = mcDB.vetDAO().getMcMusicPreference();

      Fragment playerFragment;
      switch (pref) {
        case "YOUTUBE":
          playerFragment = new YoutubePlayerFragment(); break;
        case "SPOTIFY":
          playerFragment = new SpotifyPlayerFragment(); break;
        default:
          playerFragment = new LocalMusicPlayerFragment(); break;
      }

      Fragment finalFragment = playerFragment;
      requireActivity().runOnUiThread(() -> {
        getChildFragmentManager().beginTransaction()
            .replace(R.id.music_player_container, finalFragment)
            .commit();
      });
    }).start();
  }
}