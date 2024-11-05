package com.example.vetappdraft;

import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class BaseActivity extends AppCompatActivity {

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
                colors = new int[]{0xFFF6F1E6,0xFFFFC107};
                break;
        }

        GradientDrawable gradientDrawable = new GradientDrawable(
                GradientDrawable.Orientation.TOP_BOTTOM,
                colors
        );

        getWindow().getDecorView().setBackground(gradientDrawable);
    }
}