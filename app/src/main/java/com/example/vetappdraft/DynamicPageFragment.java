//***************************************************************************
// File name:   DynamicPageFragment.java
// Author:      Berglund Center Coding team
// Date:        4/3/25
// Purpose:     Create the page fragment for the app, with conditional audio playback
//***************************************************************************

package com.example.vetappdraft;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import android.Manifest;

public class DynamicPageFragment extends Fragment {

  private static final int REQUEST_CALL_PERMISSION = 1001;

  private Page mPage;
  private TextView mTitleTextView;
  private TextView mContentTextView;
  private Button mPreviousButton;
  private Button mNextButton;
  private Button mPlayButton;
  private Button mCallButton;
  private MediaPlayer mMediaPlayer;

  private int mPageIndex;
  private static final String ARG_PAGE_INDEX = "page_index";

  public static Fragment newInstance(int pageIndex) {
    DynamicPageFragment fragment = new DynamicPageFragment();
    Bundle args = new Bundle();
    args.putInt(ARG_PAGE_INDEX, pageIndex);
    fragment.setArguments(args);
    return fragment;
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (getArguments() != null) {
      mPageIndex = getArguments().getInt(ARG_PAGE_INDEX);
    }
  }

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_dynamic_page, container, false);

    mTitleTextView = view.findViewById(R.id.pageTitle);
    mContentTextView = view.findViewById(R.id.pageContent);
    mPreviousButton = view.findViewById(R.id.previousButton);
    mNextButton = view.findViewById(R.id.nextButton);
    mPlayButton = view.findViewById(R.id.playButton);
    mCallButton = view.findViewById(R.id.callButton);

    return view;
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    // Get current page
    mPage = ((MainActivity) requireActivity()).getPages().get(mPageIndex);

    // Set page content
    mTitleTextView.setText(mPage.getName());
    mContentTextView.setText(mPage.getContent());

    // Only show the play button if audio exists
    if (mPage.getAudioResId() != -999) {
      mPlayButton.setVisibility(View.VISIBLE);
      mPlayButton.setOnClickListener(v -> {
        if (mMediaPlayer != null) {
          mMediaPlayer.release();
        }

        mMediaPlayer = MediaPlayer.create(requireContext(), mPage.getAudioResId());
        if (mMediaPlayer != null) {
          mMediaPlayer.start();
        }
      });
    } else {
      mPlayButton.setVisibility(View.GONE);
    }

    // show call button only when intend to call
    if (mPage.getCallFlag ()) {
      mCallButton.setVisibility (View.VISIBLE);
      mPlayButton.setOnClickListener(v -> {
        Intent callIntent = new Intent (Intent.ACTION_DIAL);

        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CALL_PHONE)
            != PackageManager.PERMISSION_GRANTED) {
          ActivityCompat.requestPermissions(requireActivity(),
              new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL_PERMISSION);
        } else {
          callIntent.setData (Uri.parse ("tel:1234567890"));
          startActivity(callIntent);
        }
      });
    } else {
      mCallButton.setVisibility (View.GONE);
    }

    // Navigation buttons
    mPreviousButton.setOnClickListener(v -> navigate(-1));
    mNextButton.setOnClickListener(v -> navigate(1));

    mPreviousButton.setEnabled(mPageIndex > 0);
    mNextButton.setEnabled(mPageIndex < ((MainActivity) requireActivity()).getPages().size() - 1);
  }

  private void navigate(int direction) {
    mPageIndex += direction;
    requireActivity().getSupportFragmentManager().beginTransaction()
            .replace(R.id.fragment_container, DynamicPageFragment.newInstance(mPageIndex))
            .commit();
  }

  @Override
  public void onDestroyView() {
    if (mMediaPlayer != null) {
      mMediaPlayer.release();
      mMediaPlayer = null;
    }
    super.onDestroyView();
  }
}
