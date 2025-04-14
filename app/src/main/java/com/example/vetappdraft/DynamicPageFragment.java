//***************************************************************************
// File name:   DynamicPageFragment
// Author:      Berglund Center Coding team
// Date:        4/3/25
// Purpose:     Create the page fragment for the app
//***************************************************************************
package com.example.vetappdraft;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class DynamicPageFragment extends Fragment {

  private Page mPage;
  private TextView mTitleTextView;
  private TextView mContentTextView;
  private Button mPreviousButton;
  private Button mNextButton;

  private int mPageIndex;
  private static final String ARG_PAGE_INDEX = "page_index";

  //***************************************************************************
  // Method:      newInstance
  //
  // Description: Creates a new instance of a page fragment
  //
  // Parameters:  pageIndex - the index of the selected page
  //
  // Returned:    Fragment - new page fragment instance
  //***************************************************************************
  public static Fragment newInstance(int pageIndex) {
    DynamicPageFragment fragment = new DynamicPageFragment();
    Bundle args = new Bundle();
    args.putInt(ARG_PAGE_INDEX, pageIndex);
    fragment.setArguments(args);
    return fragment;
  }

  //***************************************************************************
  // Method:      onCreate
  //
  // Description: Runs when the fragment is created
  //
  // Parameters:  savedInstanceState - the current instance state
  //
  // Returned:    None
  //***************************************************************************
  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (getArguments() != null) {
      mPageIndex = getArguments().getInt(ARG_PAGE_INDEX);
    }
  }

  //***************************************************************************
  // Method:      onCreateView
  //
  // Description: Runs to create and set up the fragment's UI
  //
  // Parameters:  inflater - layout inflater
  //              container - parent view that the fragment's UI is attached to
  //              savedInstanceState - saved instance state
  //
  // Returned:    View - the created fragment view
  //***************************************************************************
  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_dynamic_page, container, false);

    mTitleTextView = view.findViewById(R.id.pageTitle);
    mContentTextView = view.findViewById(R.id.pageContent);
    mPreviousButton = view.findViewById(R.id.previousButton);
    mNextButton = view.findViewById(R.id.nextButton);

    return view;
  }

  //***************************************************************************
  // Method:      onViewCreated
  //
  // Description: Runs after the view has been created to initialize content
  //
  // Parameters:  view - the fragment's root view
  //              savedInstanceState - saved instance state
  //
  // Returned:    None
  //***************************************************************************
  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    // Get the page based on the index
    mPage = ((MainActivity) requireActivity()).getPages().get(mPageIndex);

    // Populate UI
    mTitleTextView.setText(mPage.getName());

    switch (mPage.getType()) {
      case TEXT:
        mContentTextView.setText(mPage.getContent());
        break;
      case AUDIO:
        // Implement audio player logic using mPage.getContent()
        mContentTextView.setText("Audio Player Here");
        break;
      case LINK:
        // Implement link handling using mPage.getContent()
        mContentTextView.setText("Link Here: " + mPage.getContent());
        break;
      case IMAGE:
        // Implement image logic.
        mContentTextView.setText("Image here");
        break;
    }

    // Navigation
    mPreviousButton.setOnClickListener(v -> navigate(-1));
    mNextButton.setOnClickListener(v -> navigate(1));

    // Disable previous/next buttons if at the beginning/end
    mPreviousButton.setEnabled(mPageIndex > 0);
    mNextButton.setEnabled(mPageIndex < ((MainActivity) requireActivity()).getPages().size() - 1);
  }

  //***************************************************************************
  // Method:      navigate
  //
  // Description: Navigates to the previous or next page
  //
  // Parameters:  direction - integer representing direction (-1 for previous, 1 for next)
  //
  // Returned:    None
  //***************************************************************************
  private void navigate(int direction) {
    mPageIndex += direction;
    requireActivity().getSupportFragmentManager().beginTransaction()
            .replace(R.id.fragment_container, DynamicPageFragment.newInstance(mPageIndex))
            .commit();
  }
}
