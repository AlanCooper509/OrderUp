package com.example.orderup;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

public class MainActivity extends AppCompatActivity {

    private SqliteHandler sqliteHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // handle db setup
        sqliteHandler = setupDB();

        // click on login button
        Button loginButton = (Button) findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                renderChoicePage();
            }
        });
    }

    private void renderChoicePage() {
        // move to food/time choice page
        setContentView(R.layout.filter_choice);

        // click on food button
        Button foodButton = (Button) findViewById(R.id.foodButton);
        foodButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                renderFoodChoicePage();
            }
        });

        // click on time button
        Button timeButton = (Button) findViewById(R.id.timeButton);
        timeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                renderTimeChoicePage();
            }
        });
    }

    private void renderFoodChoicePage() {
        // move to food choices page
        setContentView(R.layout.food_sort);

        // group together the checkbox objects by their ids
        final ArrayList<CheckBox> checkboxes = new ArrayList<CheckBox>();
        checkboxes.add((CheckBox) findViewById(R.id.checkBox1));
        checkboxes.add((CheckBox) findViewById(R.id.checkBox2));
        checkboxes.add((CheckBox) findViewById(R.id.checkBox3));
        checkboxes.add((CheckBox) findViewById(R.id.checkBox4));
        checkboxes.add((CheckBox) findViewById(R.id.checkBox5));

        // set onClick for each checkbox
        for(CheckBox checkbox : checkboxes) {
            checkbox.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    updateCards(checkboxes);
                }
            });
        }
    }

    private void renderTimeChoicePage() {
        // move to time choices page
        setContentView(R.layout.time_sort);

        // get an organized list from sqlite in sqlitehandler file and return the list
        ArrayList<ArrayList<String>> restaurants = sqliteHandler.minFirst();

        // from list, loop through and print restaurants out in order of minimum to maximum time (add wait time + distance)
        for (int i = 0; i < restaurants.size(); i++) {

            // add and display a new card for every restaurant in list
            String restaurantName = restaurants.get( i ).get( 0 );
            String stars = restaurants.get( i ).get( 1 );
            String distance = restaurants.get( i ).get( 2 );
            String waitTime = restaurants.get( i ).get( 3 );
            String image_id = restaurants.get( i ).get( 4 );

            // add card with the information from the DB
            addCard( Integer.parseInt(image_id), restaurantName, stars, distance, waitTime);
        }
    }

    private void updateCards(ArrayList<CheckBox> checkboxes) {
        // remove all cards in the results view
        LinearLayout linearLayout = (LinearLayout)findViewById(R.id.resultsLinearLayout);
        linearLayout.removeAllViews();

        // get label names of checked elements
        ArrayList<String> categories = new ArrayList<String>();
        for(CheckBox checkbox:checkboxes) {
            if (checkbox.isChecked()) {
                categories.add((String)checkbox.getText());
            }
        }

        // get category from checkbox and search database for restaurants with the same category
        ArrayList<ArrayList<String>> restaurants = sqliteHandler.getResults(categories);
        for (int i = 0; i < restaurants.size(); i++) {

            // add and display a new card for every restaurant in list
            String restaurantName = restaurants.get( i ).get( 0 );
            String stars = restaurants.get( i ).get( 1 );
            String distance = restaurants.get( i ).get( 2 );
            String waitTime = restaurants.get( i ).get( 3 );
            String image_id = restaurants.get( i ).get( 4 );

            // add card with the information from the DB
            addCard(Integer.parseInt(image_id), restaurantName, stars, distance, waitTime);
        }
    }

    private void addCard(int imageID, final String restaurantName, String stars, String distance, String waitTime) {
        // add a CardView as a child view to the LinearLayout parent view
        LinearLayout linearLayout = (LinearLayout)findViewById(R.id.resultsLinearLayout);
        CardView cardView = new CardView(this);
        linearLayout.addView(cardView);

        // modify the layout params for the added CardView
        LinearLayout.LayoutParams linearLayoutParams = (LinearLayout.LayoutParams)cardView.getLayoutParams();
        float DENSITY = cardView.getContext().getResources().getDisplayMetrics().density; // dp/px ratio
        linearLayoutParams.height = (int) (110.0*DENSITY); // px to dp
        linearLayoutParams.width = MATCH_PARENT;
        linearLayoutParams.bottomMargin = (int) (15.0*DENSITY); // px to dp
        cardView.setLayoutParams(linearLayoutParams);

        // set corner radius of CardView
        cardView.setRadius(25*DENSITY);

        // add the listener for pulling up the restaurant information
        cardView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                renderInfoPage(restaurantName);
            }
        });

        // add the restaurant image to the cardView
        addImageView(imageID, cardView);

        // add the restaurant name to the cardView
        addNameTextView(restaurantName, cardView);

        // add the number of stars to the CardView
        addStarsTextView(stars, cardView);

        // add the distance to the CardView
        addDistanceTextView(distance, cardView);

        // add the wait time to the CardView
        addWaitTimeTextView(waitTime, cardView);
    }

    private void renderInfoPage(String restaurantName) {
        setContentView(R.layout.restaurant_info);

        // LIST OF KEYS (see the "colNames" variable in the "setupDB()" method):
        // "restaurant_id", "name", "address", "number", "website", "rating", "distance",
        // "category", "current_wait", "average_wait", "tables", "orders", "image_id"
        HashMap<String, String> restaurantInfo = sqliteHandler.getResult(restaurantName);

        // pass data to the appropriate IDs
        TextView name = (TextView)findViewById(R.id.name);
        name.setText(restaurantInfo.get("name"));

        TextView rating = (TextView)findViewById(R.id.rating);
        rating.setText(restaurantInfo.get("rating") + " stars");

        TextView distance = (TextView)findViewById(R.id.distance);
        distance.setText("Distance Away: " + restaurantInfo.get("distance"));

        TextView average_wait = (TextView)findViewById(R.id.average_wait);
        average_wait.setText("Avg. serving speed: " + restaurantInfo.get("average_wait") + " min");

        TextView current_wait = (TextView)findViewById(R.id.current_wait);
        current_wait.setText(restaurantInfo.get("current_wait") + " min");

        TextView tables = (TextView)findViewById(R.id.tables);
        tables.setText("Available tables: " + restaurantInfo.get("tables"));

        TextView orders = (TextView)findViewById(R.id.orders);
        orders.setText("Currently handling orders: " + restaurantInfo.get("orders"));

        TextView number = (TextView)findViewById(R.id.number);
        number.setText(restaurantInfo.get("number"));

        TextView address = (TextView)findViewById(R.id.address);
        address.setText(restaurantInfo.get("address"));

        TextView website = (TextView)findViewById(R.id.website);
        website.setText(restaurantInfo.get("website"));

        ImageView imageView = (ImageView)findViewById(R.id.restaurant_image);
        imageView.setImageResource(Integer.parseInt(restaurantInfo.get("image_id")));
    }

    private void addImageView(int imageID, CardView cardView) {
        float DENSITY = cardView.getContext().getResources().getDisplayMetrics().density; // dp/px ratio

        // add ImageView to CardView
        ImageView imageView = new ImageView(this);
        imageView.setImageResource(imageID);
        cardView.addView(imageView);
        imageView.setPadding((int)(8*DENSITY), (int)(8*DENSITY), (int)(8*DENSITY), (int)(8*DENSITY));

        // modify the layout params for the added ImageView
        CardView.LayoutParams cardLayoutParams = (CardView.LayoutParams)imageView.getLayoutParams();
        cardLayoutParams.height = MATCH_PARENT;
        cardLayoutParams.width = (int) (135.0*DENSITY);
        imageView.setLayoutParams(cardLayoutParams);
    };

    private void addNameTextView(String restaurantName, CardView cardView) {
        float DENSITY = cardView.getContext().getResources().getDisplayMetrics().density; // dp/px ratio

        // add the NAME TextView to CardView
        TextView textView = new TextView(this);
        textView.setText(restaurantName);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        textView.setTextColor(Color.parseColor("#000000"));
        cardView.addView(textView);

        // modify the layout params for the added TextView
        CardView.LayoutParams cardLayoutParams = (CardView.LayoutParams)textView.getLayoutParams();
        cardLayoutParams.height = (int) (30.0*DENSITY);
        cardLayoutParams.width = (int) (175.0*DENSITY);
        cardLayoutParams.leftMargin = (int) (125*DENSITY);
        cardLayoutParams.topMargin = (int) (5*DENSITY);
        textView.setLayoutParams(cardLayoutParams);
    }

    private void addStarsTextView(String stars, CardView cardView) {
        float DENSITY = cardView.getContext().getResources().getDisplayMetrics().density; // dp/px ratio

        // add the STARS TextView to CardView
        TextView textView = new TextView(this);
        textView.setText(stars + " stars");
        cardView.addView(textView);

        // modify the layout params for the added TextView
        CardView.LayoutParams cardLayoutParams = (CardView.LayoutParams)textView.getLayoutParams();
        cardLayoutParams.height = (int) (30.0*DENSITY);
        cardLayoutParams.width = (int) (175.0*DENSITY);
        cardLayoutParams.leftMargin = (int) (125*DENSITY);
        cardLayoutParams.topMargin = (int) (30*DENSITY);
        textView.setLayoutParams(cardLayoutParams);
    }

    private void addWaitTimeTextView(String waitTime, CardView cardView) {
        float DENSITY = cardView.getContext().getResources().getDisplayMetrics().density; // dp/px ratio

        // add the STARS TextView to CardView
        TextView textView = new TextView(this);
        textView.setText(waitTime + " minutes of waiting");
        if(Float.parseFloat(waitTime) < 15.0) textView.setTextColor(Color.parseColor("#4CBB17"));
        else if(Float.parseFloat(waitTime) < 30.0) textView.setTextColor(Color.parseColor("#E3AE01"));
        else textView.setTextColor(Color.parseColor("#FF0000"));
        cardView.addView(textView);

        // modify the layout params for the added TextView
        CardView.LayoutParams cardLayoutParams = (CardView.LayoutParams)textView.getLayoutParams();
        cardLayoutParams.height = (int) (30.0*DENSITY);
        cardLayoutParams.width = (int) (175.0*DENSITY);
        cardLayoutParams.leftMargin = (int) (125*DENSITY);
        cardLayoutParams.topMargin = (int) (60*DENSITY);
        textView.setLayoutParams(cardLayoutParams);
    }

    private void addDistanceTextView(String distance, CardView cardView) {
        float DENSITY = cardView.getContext().getResources().getDisplayMetrics().density; // dp/px ratio

        // add the STARS TextView to CardView
        TextView textView = new TextView(this);
        textView.setText(distance + " mi");
        cardView.addView(textView);

        // modify the layout params for the added TextView
        CardView.LayoutParams cardLayoutParams = (CardView.LayoutParams)textView.getLayoutParams();
        cardLayoutParams.height = (int) (30.0*DENSITY);
        cardLayoutParams.width = (int) (175.0*DENSITY);
        cardLayoutParams.leftMargin = (int) (125*DENSITY);
        cardLayoutParams.topMargin = (int) (80*DENSITY);
        textView.setLayoutParams(cardLayoutParams);
    }

    private SqliteHandler setupDB() {
        // TODO: add actual file path for DB instead of placing it in :memory:
        SqliteHandler sqliteHandler = new SqliteHandler();
        String tablename = "restaurant";
        String[] colNames = new String[] {
                "name", "address", "number", "website", "rating", "distance",
                "category", "current_wait", "average_wait", "tables", "orders", "image_id"
        };
        String[] colTypes = new String[] {
                "TEXT", "TEXT", "TEXT", "TEXT", "INTEGER", "REAL",
                "TEXT", "REAL", "REAL", "INTEGER", "INTEGER", "TEXT"
        };

        // TODO: creates company table since it's being placed in :memory: for each run
        // TODO: currently ignores colTypes and defaults to TEXT
        sqliteHandler.createTable( tablename, colNames, colTypes );

        // TODO: inserting dummy data for hackathon testing
        sqliteHandler.insertRow( colNames, new String[]{
                "Fine Eats", "1500 W CANNON DR", "(832) 123-1234", "WWW.HACKRICE.COM", "4.42", "1.4",
                "Category 1", "24.5", "35.7", "4", "2", "2131230868"
        });
        sqliteHandler.insertRow( colNames, new String[]{
                "Big Endian Cafe", "1420 W CANNON DR", "(832) 435-1231", "WWW.HACKRICE.COM", "3.47", "1.2",
                "Category 2", "14.5", "13.1", "7", "0", "2131230837"
        });
        sqliteHandler.insertRow( colNames, new String[]{
                "Big Byte", "1234 HACKERS ONLY", "(832) 555-0000", "WWW.HACKRICE.COM", "4.99", "3.4",
                "Category 3", "21.2", "18.2", "1", "5", "2131230871"
        });
        sqliteHandler.insertRow( colNames, new String[]{
                "The Motherboard", "9999 MY ADDRESS", "(832) 999-9999", "WWW.HACKRICE.COM", "2.42", "O.4",
                "Category 4", "35.9", "47.8", "10", "0", "2131230867"
        });
        sqliteHandler.insertRow( colNames, new String[]{
                "Cyberspace Cuisine", "1200 OUTER SPACE", "(832) D4T4-5C1", "WWW.HACKRICE.COM", "1.42", "99.12",
                "Category 5", "194.5", "135.7", "1", "5", "2131230815"
        });

        return sqliteHandler;
    }
}