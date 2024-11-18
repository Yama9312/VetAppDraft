package com.example.vetappdraft;

import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class BaseActivity extends AppCompatActivity {
    private TextView mcTitle;
    private LinearLayout mcBar;
    private ImageButton mcBtnSettings, mcBtnEmergency;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        applyChosenColorScheme();
    }

    private void applyChosenColorScheme() {
        String selectedBranch = getSharedPreferences("AppPreferences", MODE_PRIVATE)
                .getString("selectedBranch", "Default");

        int[] colors;
        switch(selectedBranch) {
            case "Army":
                colors = new int[]{0xFFF6F1E6,0xFFFFC107};
                break;
            case "Marine Corps":
                colors = new int[]{0xFFF4DF19, 0xFFF50001};
                break;
            case "Navy":
                colors = new int[]{0xFFFFFF06,0xFF00007B};
                break;
            case "Air Force":
                colors = new int[]{0xFFF8FBFA,0xFF144D80};
                break;
            default:
                colors = new int[]{0xFFFFFFFF,0xFFFFFFFF};
                break;
        }

        GradientDrawable gradientDrawable = new GradientDrawable(
                GradientDrawable.Orientation.TOP_BOTTOM,
                colors
        );

        getWindow().getDecorView().setBackground(gradientDrawable);
    }

//    protected void addTopBar() {
//        //View topBar = LayoutInflater.from(this).inflate(R.layout.top_bar, null);
//
//        mcTitle = findViewById(R.id.tvCoreValues);
//        // mcTitle.setText(title);
//
//        mcBtnEmergency = findViewById(R.id.imageBtnEmergency);
//        emergencyButton.setOnClickListener(v -> {
//            // start an emergency activity
//        });
//
//        mcBtnSettings = findViewById(R.id.imageBtnSettings);
//        settingsButton.setOnClickListener(v -> {
//            // start a settings activity
//        });
//
//        //setContentView(topBar);
//    }
}