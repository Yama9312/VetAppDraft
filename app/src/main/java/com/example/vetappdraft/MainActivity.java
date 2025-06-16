package com.example.vetappdraft;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.LinkedList;
import java.util.List;

public class MainActivity extends BaseActivity {

    private LinkedList<Page> pages = new LinkedList<>();

    /**
     * Retrieves the list of pages in the app.

     * @return A linkedlist containing the pages.
     */
    public LinkedList<Page> getPages(){
        return pages;
    }

    /**
     * Updates the list of pages with a new ordered list.
     * @param newPages The new list of pages selected by the user.
     */
    public void updatePages(LinkedList<Page> newPages) {
        pages.clear();
        pages.addAll(newPages);
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
        pages.add(new Page("step 2", Page.PageType.TEXT,
            "review reasons for living\nPhone a friend", "", -999, true));
        pages.add(new Page("step 3", Page.PageType.TEXT, "H.A.L.T", ""));
        pages.add(new Page("step 4", Page.PageType.TEXT, "2 minute mindful breathing", "",
            R.raw.two_minutes_of_mindful_breathing_vf));
        pages.add(new Page("step 5", Page.PageType.TEXT, "safetySOP", ""));
        pages.add(new Page("step 6", Page.PageType.TEXT, "body scan meditation",
            "", R.raw.body_scan_v2));
        pages.add(new Page("step 7", Page.PageType.TEXT, "listen to music", ""));
        pages.add(new Page("step 8", Page.PageType.TEXT,  "call someone\nsee who is online",
            "", -999, true));
        pages.add(new Page("step 9", Page.PageType.TEXT, "call hotlines",
            "", -999, true));
        pages.add(new Page("step 10", Page.PageType.TEXT, "put weapons down", ""));

        pages.add(new Page("step 11", Page.PageType.TEXT, "observe & describe", "", R.raw.observe_and_describe_vf));
        pages.add(new Page("step 12", Page.PageType.TEXT, "leaves on a stream", "", R.raw.leaves_on_a_stream_vf));
        pages.add(new Page("step 13", Page.PageType.TEXT, "identify values", ""));
        pages.add(new Page("step 14", Page.PageType.TEXT, "mindful walking", "",
            R.raw.mindful_walking_vf));
        pages.add(new Page("step 15", Page.PageType.TEXT, "progressive muscle\nrelaxation",
            "", R.raw.progressive_muscle_relaxing_vf));
        pages.add(new Page("step 16", Page.PageType.TEXT, "distract yourself", ""));
        pages.add(new Page("step 17", Page.PageType.TEXT, "awareness of thought",
            "", R.raw.awareness_of_thoughts_vf));
        pages.add(new Page("step 18", Page.PageType.TEXT, "follow the circle", ""));
        pages.add(new Page("step 19", Page.PageType.TEXT, "urge surfing",
            "", R.raw.urge_surfing_vf));
        pages.add(new Page("step 20", Page.PageType.TEXT,
            "SOBER\n-Stop\n-observe\n-breath\n-expand\n-respond", ""));
        pages.add(new Page("step 21", Page.PageType.TEXT, "four squared breathing",
            "", R.raw.four_square_breathing_vf));
        pages.add(new Page("step 22", Page.PageType.TEXT, "diaphragmatic breathing", "",
            R.raw.diagramic_breathing_vf));
        pages.add(new Page("step 23", Page.PageType.TEXT, "5 senses",
            "", R.raw.five_senses_vf));
        pages.add(new Page("step 24", Page.PageType.TEXT, "mountain meditation",
            "", R.raw.mountian_meditation_vf));
        pages.add(new Page("step 25", Page.PageType.TEXT, "distractions", "", true));
        pages.add(new Page("step 26", Page.PageType.TEXT, "whole body breathing",
            "", R.raw.whole_body_breathing_meditation_vf));
        pages.add(new Page("step 27", Page.PageType.TEXT, "breathing space",
            "", R.raw.breathing_space_vf));
        pages.add(new Page("step 28", Page.PageType.TEXT, "breathing body",
            "", R.raw.breathing_body_vf));


        // Add NavigationBarFragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.navigation_bar_container, new TopNavBarFragment());
        transaction.commit();

        VetDatabase db = VetDatabase.getInstance(this);
        VetDAO dao = db.vetDAO();

        new Thread(() -> {
            List<VetUser> users = dao.getAll();

            Fragment initialFragment;
            if (users.isEmpty()) {
                initialFragment = new FirstSetupFragment();
            } else if (users.get(0).getMcMusicPreference () == null) {
                initialFragment = new MusicSetupFragment();
            } else {
                initialFragment = new DynamicPageFragment();
            }

            Fragment finalInitialFragment = initialFragment;
            runOnUiThread(() -> {
                loadContentFragment(finalInitialFragment);
            });
        }).start();

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
