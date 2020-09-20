package com.example.orderup;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SqliteHandler sqliteHandler = setupDB();

        Button button = (Button) findViewById(R.id.loginButton);


        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setContentView(R.layout.activity_main2);
            }
        });
    }

    private SqliteHandler setupDB() {
        // TODO: add actual file path for DB instead of placing it in :memory:
        SqliteHandler sqliteHandler = new SqliteHandler();
        String tablename = "restaurant";
        String[] colNames = new String[] {
                "name", "address", "number", "website", "rating", "distance",
                "category", "current_wait", "average_wait", "tables", "orders"
        };
        String[] colTypes = new String[] {
                "TEXT", "TEXT", "TEXT", "TEXT", "INTEGER", "REAL",
                "TEXT", "REAL", "REAL", "INTEGER", "INTEGER"
        };

        // TODO: creates company table since it's being placed in :memory: for each run
        // TODO: currently ignores colTypes and defaults to TEXT
        sqliteHandler.createTable( tablename, colNames, colTypes );

        // TODO: inserting dummy data for hackathon testing
        sqliteHandler.insertRow( colNames, new String[]{
                "Fine Eats", "1500 W CANNON DR", "(832) 123-1234", "WWW.HACKRICE.COM", "4.42", "1.4",
                "Category 1", "24.5", "35.7", "4", "2"
        });
        sqliteHandler.insertRow( colNames, new String[]{
                "Big Endian Cafe", "1420 W CANNON DR", "(832) 435-1231", "WWW.HACKRICE.COM", "3.47", "1.2",
                "Category 2", "14.5", "22.2", "7", "0"
        });
        sqliteHandler.insertRow( colNames, new String[]{
                "Big Byte", "1234 HACKERS ONLY", "(832) 555-0000", "WWW.HACKRICE.COM", "4.99", "3.4",
                "Category 3", "21.2", "18.2", "1", "5"
        });
        sqliteHandler.insertRow( colNames, new String[]{
                "The Motherboard", "9999 MY ADDRESS", "(832) 999-9999", "WWW.HACKRICE.COM", "2.42", "O.4",
                "Category 4", "35.9", "47.8", "10", "0"
        });
        sqliteHandler.insertRow( colNames, new String[]{
                "Cyberspace Cuisine", "1200 OUTER SPACE", "(832) D4T4-5C1", "WWW.HACKRICE.COM", "1.42", "99.12",
                "Category 5", "194.5", "135.7", "1", "5"
        });

        return sqliteHandler;
    }
}