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
import java.util.LinkedList;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class MainActivity extends BaseActivity {

    private LinkedList<Page> pages = new LinkedList<>();

    public LinkedList<Page> getPages() {
        return pages;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        pages.add(new Page("step 1", Page.PageType.TEXT, "take a deep breath", ""));
        pages.add(new Page("step 2", Page.PageType.TEXT, "review reasons for living\nPhone a friend", ""));
        pages.add(new Page("step 3", Page.PageType.TEXT, "H.A.L.T", ""));
        pages.add(new Page("step 4", Page.PageType.TEXT, "2 minute mindful breathing", ""));
        // Add more pages as needed...

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.navigation_bar_container, new TopNavBarFragment());
        transaction.commit();

        loadContentFragment(new FirstSetupFragment());
    }

    public void loadContentFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.commit();
    }
}