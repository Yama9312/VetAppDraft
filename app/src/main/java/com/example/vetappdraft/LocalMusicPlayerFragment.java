package com.example.vetappdraft;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import android.media.MediaPlayer;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.view.ViewGroup;
import android.view.LayoutInflater;

public class LocalMusicPlayerFragment extends Fragment {

  private MediaPlayer mediaPlayer;

  public LocalMusicPlayerFragment() {
    super(R.layout.fragment_local_music_player); // You'll create this layout
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    TextView songTitle = view.findViewById(R.id.song_title);
    Button playButton = view.findViewById(R.id.play_button);
    Button stopButton = view.findViewById(R.id.stop_button);

    songTitle.setText("Local Song Title"); // You can dynamically load file names

    mediaPlayer = MediaPlayer.create(requireContext(), R.raw.not_you_too_instrumental);

    playButton.setOnClickListener(v -> {
      if (!mediaPlayer.isPlaying()) {
        mediaPlayer.start();
      }
    });

    stopButton.setOnClickListener(v -> {
      if (mediaPlayer.isPlaying()) {
        mediaPlayer.pause();
        mediaPlayer.seekTo(0);
      }
    });
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    if (mediaPlayer != null) {
      mediaPlayer.release();
      mediaPlayer = null;
    }
  }
}