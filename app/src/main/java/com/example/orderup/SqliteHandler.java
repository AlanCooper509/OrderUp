package com.example.orderup;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class SqliteHandler {
    private SQLiteDatabase db;
    private String tablename = "restaurant";

    public SqliteHandler() {
        db = SQLiteDatabase.openOrCreateDatabase(":memory:", null);
    }

    public void createTable(String new_tablename, String[] colnames, String[] coltypes) {
        String query = "CREATE TABLE IF NOT EXISTS " + new_tablename + " (";
        query += new_tablename + "_id INTEGER PRIMARY KEY,";

        // generate column name list with types
        // TODO: currently just using TEXT for coltypes to make everything easier
        for (int i = 0; i < colnames.length; i++) {
            query += colnames[i] + ' ' + "TEXT"; // TEXT instaed of coltypes[i] for now;
            if( i < colnames.length - 1) query += ", ";
        }
        query += ");";

        // execute the statement
        db.execSQL(query.toString());
    }

    public void insertRow(String[] colnames, String[] rowEntry) {
        String query = "INSERT INTO " + tablename + " (";

        // generate column name list
        for (int i = 0; i < colnames.length; i++) {
            query += colnames[i];
            if( i < colnames.length - 1) query += ", ";
        }
        query += ") VALUES (";

        // generate value list
        for (int i = 0; i < rowEntry.length; i++) {
            query += "'" + rowEntry[i] + "'";
            if( i < rowEntry.length - 1) query += ", ";
        }
        query += ");";

        // execute the statement
        db.execSQL(query.toString());
    }

    public void printRows() {
        String query = "SELECT * FROM " + tablename;
        Cursor cursor = db.rawQuery(query.toString(), null);
        while (cursor.moveToNext()) {
            String[] colnames = cursor.getColumnNames();
            for (int i = 0; i < colnames.length; i++) {
                Log.i( "orderup", cursor.getColumnName(i) + ": " + cursor.getString(i));
            }
            Log.i("orderup", "---------------");
        }
    }

    public void printTables() {
        ArrayList<ArrayList<String>> a = new ArrayList<ArrayList<String>>();
        String query = "SELECT name FROM sqlite_master WHERE type = 'table' ";
        query += "AND name NOT LIKE 'sqlite_%' ORDER BY 1;";
        Cursor cursor = db.rawQuery(query.toString(), null);
        while ( cursor.moveToNext() ) {
            Log.i("orderup", cursor.getString(0));
        }
    }
}
