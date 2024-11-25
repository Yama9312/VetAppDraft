package com.example.vetappdraft;

import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

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
                colors = new int[]{0xFFFEFEFE,0xFFFDE37D};
                break;
            case "Marine Corps":
                colors = new int[]{0xFFFDF3DE, 0xFFBE2321};
                break;
            case "Navy":
                colors = new int[]{0xFFFFFEFC,0xFFF3C881};
                break;
            case "Air Force":
                colors = new int[]{0xFFFEF5E0,0xFFC1871F};
                break;
            case "Coast Guard":
                colors = new int[]{0xFFFFFFFF,0xFF205698};
                break;
            case "Grey Scale":
                colors = new int[]{0xFFFEFEFE,0xFF59595B};
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

}