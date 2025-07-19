package com.example.vetappdraft;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
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
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LocalMusicPlayerFragment extends Fragment {
  private MediaPlayer mediaPlayer;
  private VetDatabase db;
  private List<MusicFile> musicFiles = new ArrayList<>(); // Initialize empty list
  private int currentSongIndex = 0;
  private TextView songTitle;
  private Button playButton, stopButton, nextButton;

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    db = VetDatabase.getInstance(requireContext());
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_local_music_player, container, false);
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    songTitle = view.findViewById(R.id.song_title);
    playButton = view.findViewById(R.id.play_button);
    stopButton = view.findViewById(R.id.stop_button);
    nextButton = view.findViewById(R.id.next_button);

    setupButtonListeners();
    observeMusicFiles();
  }

  private void setupButtonListeners() {
    playButton.setOnClickListener(v -> {
      if (!musicFiles.isEmpty()) {
        playCurrentSong();
      } else {
        Toast.makeText(requireContext(), "No songs available", Toast.LENGTH_SHORT).show();
      }
    });

    stopButton.setOnClickListener(v -> {
      if (mediaPlayer != null && mediaPlayer.isPlaying()) {
        mediaPlayer.pause();
        mediaPlayer.seekTo(0);
      }
    });

    nextButton.setOnClickListener(v -> playNextSong());
  }

  private void observeMusicFiles() {
    db.musicFileDAO().getAllMusicFiles().observe(getViewLifecycleOwner(), files -> {
      if (files != null && !files.isEmpty()) {
        musicFiles = files;
        currentSongIndex = 0;
        updateCurrentSong();
        Log.d("MusicPlayer", "Loaded " + files.size() + " songs"); // Verify count
      } else {
        songTitle.setText("No songs available");
        playButton.setEnabled(false);
        nextButton.setEnabled(false);
      }
    });
  }

  private void updateCurrentSong() {
    if (!musicFiles.isEmpty()) {
      songTitle.setText(musicFiles.get(currentSongIndex).getFileName());
    }
  }

  private void playCurrentSong() {
    try {
      if (mediaPlayer != null) {
        mediaPlayer.release();
      }

      MusicFile currentSong = musicFiles.get(currentSongIndex);
      mediaPlayer = new MediaPlayer();
      mediaPlayer.setDataSource(currentSong.getFilePath());
      mediaPlayer.prepareAsync();

      mediaPlayer.setOnPreparedListener(mp -> {
        mp.start();
        Toast.makeText(requireContext(), "Playing: " + currentSong.getFileName(), Toast.LENGTH_SHORT).show();
      });

      mediaPlayer.setOnCompletionListener(mp -> playNextSong());
      mediaPlayer.setOnErrorListener((mp, what, extra) -> {
        Toast.makeText(requireContext(), "Playback error", Toast.LENGTH_SHORT).show();
        return false;
      });
    } catch (IOException e) {
      Toast.makeText(requireContext(), "Error playing file", Toast.LENGTH_SHORT).show();
    }
  }

  private void playNextSong() {
    if (musicFiles.isEmpty()) return;

    currentSongIndex = (currentSongIndex + 1) % musicFiles.size();
    updateCurrentSong();
    playCurrentSong();
  }

  @Override
  public void onDestroy() {
    if (mediaPlayer != null) {
      mediaPlayer.release();
    }
    super.onDestroy();
  }
}
