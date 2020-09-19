package com.example.orderup;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button loginButton = (Button) findViewById(R.id.loginButton);

        // click on login button
        loginButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // move to food/time choice page
                setContentView(R.layout.filter_choice);
                // click on food button
                Button foodButton = (Button) findViewById(R.id.foodButton);
                foodButton.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        setContentView(R.layout.food_sort);
                    }
                });
                // click on time button
                Button timeButton = (Button) findViewById(R.id.timeButton);
                timeButton.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        setContentView(R.layout.activity_main);
                    }
                });
            }
        });
        //TextView welcomeTextView = (TextView) findViewById(R.id.welcomeTextView);
        //welcomeTextView.setText("hello hello");
    }
}