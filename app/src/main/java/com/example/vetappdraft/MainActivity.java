package com.example.vetappdraft;

import static java.nio.charset.StandardCharsets.UTF_8;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
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
import androidx.room.Room;

import java.net.URLEncoder;
import java.util.Vector;
import java.util.concurrent.Executors;

public class MainActivity extends BaseActivity
{

    private Vector<Page> pages = new Vector<>();

    public Vector<Page> getPages(){
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

//        thePages.add(new Page("step 1", MainActivity.class, "", "take a deep breath"));
//        thePages.add(new Page("step 2", MainActivity.class, "", "review reasons for living\nPhone a friend"));
//        thePages.add(new Page("step 3", MainActivity.class, "", "H.A.L.T"));
//        thePages.add(new Page("step 4", MainActivity.class, "", "2 minute mindful breathing"));
//        thePages.add(new Page("step 5", MainActivity.class, "", "safety SOP"));
//        thePages.add(new Page("step 6", MainActivity.class, "", "body scan meditation"));
//        thePages.add(new Page("step 7", MainActivity.class, "", "listen to music"));
//        thePages.add(new Page("step 8", MainActivity.class, "", "call someone\nsee who is online"));
//        thePages.add(new Page("step 9", MainActivity.class, "", "call hotlines"));
//        thePages.add(new Page("step 10", MainActivity.class, "", "put weapons down"));
//        thePages.add(new Page("step 11", MainActivity.class, "", "observe & describe"));
//        thePages.add(new Page("step 12", MainActivity.class, "", "leaves on a stream"));
//        thePages.add(new Page("step 13", MainActivity.class, "", "identify values"));
//        thePages.add(new Page("step 14", MainActivity.class, "", "mindful walking"));
//        thePages.add(new Page("step 15", MainActivity.class, "", "progressive muscle relaxation"));
//        thePages.add(new Page("step 16", MainActivity.class, "", "distract yourself\n(test on the screen)"));
//        thePages.add(new Page("step 17", MainActivity.class, "", "awareness of thoughts"));
//        thePages.add(new Page("step 18", MainActivity.class, "", "follow the circle"));
//        thePages.add(new Page("step 19", MainActivity.class, "", "urge surfing"));
//        thePages.add(new Page("step 20", MainActivity.class, "", "S.O.B.E.R\nStop\nobserve\nbreath\nexpand\nrespond"));
//        thePages.add(new Page("step 21", MainActivity.class, "", "four squared breathing"));
//        thePages.add(new Page("step 22", MainActivity.class, "", "diaphragmatic breathing"));
//        thePages.add(new Page("step 23", MainActivity.class, "", "5 senses"));
//        thePages.add(new Page("step 24", MainActivity.class, "", "mountain meditation"));
//        thePages.add(new Page("step 25", MainActivity.class, "", "distractions"));
//        thePages.add(new Page("step 26", MainActivity.class, "", "whole body breathing"));
//        thePages.add(new Page("step 27", MainActivity.class, "", "breathing space"));
//        thePages.add(new Page("step 28", MainActivity.class, "", "breathing body"));

        // Add NavigationBarFragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.navigation_bar_container, new TopNavBarFragment ());
        transaction.commit();

        // Load initial content fragment
        loadContentFragment(new FirstSetupFragment ());
    }

    public void loadContentFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.commit();
    }
}