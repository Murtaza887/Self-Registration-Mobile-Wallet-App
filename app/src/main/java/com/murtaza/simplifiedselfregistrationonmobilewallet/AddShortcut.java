package com.murtaza.simplifiedselfregistrationonmobilewallet;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class AddShortcut extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        setContentView(R.layout.activity_add_shortcut);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;
        getWindow().setLayout((int)(width * 0.8), (int)(height * 0.6));

        RadioGroup radioGroup = findViewById(R.id.radioGroup);
        RadioButton radioButton1 = findViewById(R.id.radio_button_1);
        RadioButton radioButton2 = findViewById(R.id.radio_button_2);
        RadioButton radioButton3 = findViewById(R.id.radio_button_3);
        RadioButton radioButton4 = findViewById(R.id.radio_button_4);
        RadioButton radioButton5 = findViewById(R.id.radio_button_5);

        Button button = findViewById(R.id.ok);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (radioButton1.isChecked()) {
                    Shortcut.shortcut = "Send Money";
                }
                if (radioButton2.isChecked()) {
                    Shortcut.shortcut = "Add Money";
                }
                if (radioButton3.isChecked()) {
                    Shortcut.shortcut = "Sign Out";
                }
                if (radioButton4.isChecked()) {
                    Shortcut.shortcut = "Account Information";
                }
                if (radioButton5.isChecked()) {
                    Shortcut.shortcut = "Settings";
                }
            }
        });
    }
}