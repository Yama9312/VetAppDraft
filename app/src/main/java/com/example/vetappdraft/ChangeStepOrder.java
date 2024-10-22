package com.example.vetappdraft;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ChangeStepOrder extends AppCompatActivity {
    public StepAdapter theSteps;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_change_step_order);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        theSteps.addPage(new Page("Step1", step1.class, "no", "take a deep breath"));
        theSteps.addPage(new Page("Step2", step2.class, "no", "Review reasons for living"));
        theSteps.addPage(new Page("Step3", step3.class, "no", "H.A.L.T"));
        theSteps.addPage(new Page("Step4", step4.class, "no", "Practice Breathing Skills"));
        theSteps.addPage(new Page("Step5", step5.class, "no", "Identify current thoughts, emotions, and body sensations"));
        theSteps.addPage(new Page("Step6", step6.class, "no", "Do Body Scan Meditation"));
        theSteps.addPage(new Page("Step7", step7.class, "no", "Listen to Music"));
        theSteps.addPage(new Page("Step8", step8.class, "no", "Call Someone / See who is online"));
        theSteps.addPage(new Page("Step9", step9.class, "no", "Call Hotlines"));
        theSteps.addPage(new Page("Step10", step10.class, "no", "Put down the weapon and put both hands on the phone"));
    }
}