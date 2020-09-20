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
                        // move to food choices page
                        setContentView(R.layout.food_sort);
                        // add restaurant info to each restaurant
                        TextView restOne = (TextView) findViewById(R.id.restOne);
                        restOne.setText("hello hello doo doo doo");

                        TextView restTwo = (TextView) findViewById(R.id.restTwo);
                        restTwo.setText("beeboop");

                        TextView restThree = (TextView) findViewById(R.id.restThree);
                        restThree.setText("yeehaw");

                        TextView restFour = (TextView) findViewById(R.id.restFour);
                        restFour.setText("bing bong");

                        TextView restFive = (TextView) findViewById(R.id.restFive);
                        restFive.setText("achoo");
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
    }
}