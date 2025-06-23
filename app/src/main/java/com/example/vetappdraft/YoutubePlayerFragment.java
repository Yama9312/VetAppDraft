package com.example.vetappdraft;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class YoutubePlayerFragment extends Fragment
{

  public YoutubePlayerFragment ()
  {
    super (R.layout.fragment_youtube_player);
  }

  @Override
  public void onViewCreated (@NonNull View view,
      @Nullable Bundle savedInstanceState)
  {
    super.onViewCreated (view, savedInstanceState);

    // Open YouTube app
    Intent intent = new Intent (Intent.ACTION_VIEW);
    intent.setData (Uri.parse ("https://www.youtube.com/"));
    intent.setPackage ("com.google.android.youtube");

    if (intent.resolveActivity (requireContext ().getPackageManager ()) != null)
    {
      startActivity (intent);
    }
    else
    {
      // fallback to browser
      startActivity (new Intent (Intent.ACTION_VIEW,
          Uri.parse ("https://www.youtube.com/")));
    }
  }
}
