//***************************************************************************
// File name:   BaseActivity
// Author:      Berglund Center Coding team
// Date:        4/3/25
// Purpose:     base activity for the Veterans app
//***************************************************************************
package com.example.vetappdraft;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

//***************************************************************************
// Activity:    BaseActivity
//
// Description: Base Activity for the veterans app
//***************************************************************************
public class BaseActivity extends AppCompatActivity {

    //***************************************************************************
    // Method:      onCreate
    //
    // Description: the method that is run on page creation
    //
    // Parameters: 	savedInstanceState - current instance
    //
    // Returned:    None
    //***************************************************************************
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        applyChosenColorScheme();
    }
    //***************************************************************************
    // Method:      applyChosenColorScheme
    //
    // Description: applies users selected color scheme to the page
    //
    // Parameters: 	none
    //
    // Returned:    None
    //***************************************************************************
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