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
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import android.Manifest;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.net.URI;

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
  private VetDatabase mDB;
  private Button mFidgetButton;
  private Button mMathButton;
  private Button mCrosswordButton;

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

    mDB = VetDatabase.getInstance(requireContext());

    mTitleTextView = view.findViewById(R.id.pageTitle);
    mContentTextView = view.findViewById(R.id.pageContent);
    mPreviousButton = view.findViewById(R.id.previousButton);
    mNextButton = view.findViewById(R.id.nextButton);
    mPlayButton = view.findViewById(R.id.playButton);
    mCallButton = view.findViewById(R.id.callButton);
    mFidgetButton = view.findViewById(R.id.fidgetButton);
    mMathButton = view.findViewById(R.id.mathButton);
    mCrosswordButton = view.findViewById(R.id.crosswordButton);

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
    ImageView gifImageView = view.findViewById(R.id.gifImageView);


    if (mPage.getContent ().equals ("follow the circle")) {
      gifImageView.setVisibility(View.VISIBLE);
      Glide.with(requireContext())
          .asGif()
          .load(R.drawable.circle_breathing)
          .into(gifImageView);
    }
    else if (mPage.getContent ().equals ("four squared breathing")) {
      gifImageView.setVisibility(View.VISIBLE);
      Glide.with(requireContext())
          .asGif()
          .load(R.drawable.square_breathing)
          .into(gifImageView);
    }
    else if (mPage.getContent ().equals ("diaphragmatic breathing")) {
      gifImageView.setVisibility(View.VISIBLE);
      Glide.with(requireContext())
              .load(R.drawable.diaphragmic_breathing)
              .into(gifImageView);
    }
    else {
      gifImageView.setVisibility(View.GONE);
    }

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
    if (mPage.getCallFlag()) {
      mCallButton.setVisibility(View.VISIBLE);
      mCallButton.setOnClickListener(v -> {
        String phone = mDB.vetDAO().getContact ();

        if (phone != null && !phone.isEmpty()) {
          Intent callIntent = new Intent(Intent.ACTION_CALL);
          callIntent.setData(Uri.parse("tel:" + phone));

          if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CALL_PHONE)
              != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(),
                new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL_PERMISSION);
          } else {
            startActivity(callIntent);
          }
        }
      });
    } else {
      mCallButton.setVisibility(View.GONE);
    }


    // show fidget button only when on distractions page
    if (mPage.getLinks()) {
      mFidgetButton.setVisibility(View.VISIBLE);
      mMathButton.setVisibility(View.VISIBLE);
      mCrosswordButton.setVisibility(View.VISIBLE);
      mFidgetButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          goToUrl("https://ffffidget.com/");
        }
      });
      mMathButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          goToUrl("https://www.wolframalpha.com/problem-generator/quiz/?category=Arithmetic&topic=AddOrSubtractSummary");
        }
      });
      mCrosswordButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          goToUrl("https://www.boatloadpuzzles.com/playcrossword");
        }
      });
    } else {
      mFidgetButton.setVisibility(View.GONE);
      mMathButton.setVisibility(View.GONE);
      mCrosswordButton.setVisibility(View.GONE);
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

  void goToUrl (String linkURL) {
    try {
      Uri url = Uri.parse(linkURL);
      startActivity(new Intent(Intent.ACTION_VIEW, url));
    } catch (Exception e) {
      Toast.makeText(getContext(), "No Website Linked", Toast.LENGTH_SHORT).show();
    }
  }
}
