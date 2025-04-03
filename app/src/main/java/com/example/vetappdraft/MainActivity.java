package com.example.vetappdraft;

import static java.nio.charset.StandardCharsets.UTF_8;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.net.URLEncoder;
import java.util.Vector;
import java.util.concurrent.Executors;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class MainActivity extends BaseActivity {

    private Vector<Page> pages = new Vector<>();

    /**
     * Retrieves the list of pages in the app.
     * @return A vector containing the pages.
     */
    public Vector<Page> getPages(){
        return pages;
    }

    /**
     * Initializes the activity, sets up the layout, and loads initial fragments.
     * @param savedInstanceState The previously saved instance state, if available.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Add predefined pages
        pages.add(new Page("step 1", Page.PageType.TEXT, "take a deep breath", ""));
        pages.add(new Page("step 2", Page.PageType.TEXT, "review reasons for living\nPhone a friend", ""));
        pages.add(new Page("step 3", Page.PageType.TEXT, "H.A.L.T", ""));
        pages.add(new Page("step 4", Page.PageType.TEXT, "2 minute mindful breathing", ""));
        pages.add(new Page("step 5", Page.PageType.TEXT, "safetySOP", ""));
        pages.add(new Page("step 6", Page.PageType.TEXT, "body scan meditation", ""));
        pages.add(new Page("step 7", Page.PageType.TEXT, "listen to music", ""));
        pages.add(new Page("step 8", Page.PageType.TEXT, "call someone\nsee who is online", ""));
        pages.add(new Page("step 9", Page.PageType.TEXT, "call hotlines", ""));
        pages.add(new Page("step 10", Page.PageType.TEXT, "put weapons down", ""));
        pages.add(new Page("step 11", Page.PageType.TEXT, "observe & describe", ""));
        pages.add(new Page("step 12", Page.PageType.TEXT, "leaves on a stream", ""));
        pages.add(new Page("step 13", Page.PageType.TEXT, "identify values", ""));
        pages.add(new Page("step 14", Page.PageType.TEXT, "mindful walking", ""));
        pages.add(new Page("step 15", Page.PageType.TEXT, "progressive muscle\nrelaxation", ""));
        pages.add(new Page("step 16", Page.PageType.TEXT, "distract yourself", ""));
        pages.add(new Page("step 17", Page.PageType.TEXT, "awareness of thought", ""));
        pages.add(new Page("step 18", Page.PageType.TEXT, "follow the circle", ""));
        pages.add(new Page("step 19", Page.PageType.TEXT, "urge surfing", ""));
        pages.add(new Page("step 20", Page.PageType.TEXT, "SOBER\n-Stop\n-observe\n-breath\n-expand\n-respond", ""));
        pages.add(new Page("step 21", Page.PageType.TEXT, "four squared breathing", ""));
        pages.add(new Page("step 22", Page.PageType.TEXT, "diaphragmatic breathing", ""));
        pages.add(new Page("step 23", Page.PageType.TEXT, "5 senses", ""));
        pages.add(new Page("step 24", Page.PageType.TEXT, "mountain meditation", ""));
        pages.add(new Page("step 25", Page.PageType.TEXT, "distractions", ""));
        pages.add(new Page("step 26", Page.PageType.TEXT, "whole body breathing", ""));
        pages.add(new Page("step 27", Page.PageType.TEXT, "breathing space", ""));
        pages.add(new Page("step 28", Page.PageType.TEXT, "breathing body", ""));

        // Add NavigationBarFragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.navigation_bar_container, new TopNavBarFragment());
        transaction.commit();

        // Load initial content fragment
        loadContentFragment(new FirstSetupFragment());
    }

    /**
     * Loads the specified content fragment into the main fragment container.
     * @param fragment The fragment to be loaded.
     */
    public void loadContentFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.commit();
    }
}
