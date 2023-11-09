package com.example.coursework;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class HikeDatabaseHelper extends SQLiteOpenHelper {
    private static final String HIKE_TABLE_NAME = "hike_details";
    private static final String ID_COLUMN_NAME = "hike_id";
    private static final String NAME_COLUMN_NAME = "name";
    private static final String LOCATION_COLUMN_NAME = "location";
    private static final String DATE_COLUMN_NAME = "date";
    private static final String PARKING_COLUMN_NAME = "parking_available";
    private static final String LENGTH_COLUMN_NAME = "length";
    private static final String DIFFICULTY_COLUMN_NAME = "difficulty";
    private static final String DESCRIPTION_COLUMN_NAME = "description";

    private static final String OBSERVATION_TABLE_NAME = "observation";
    private static final String OBSERVATION_NAME_COLUMN_NAME = "observation_name";
    private static final String OBSERVATION_ID_COLUMN_NAME = "observation_id";
    private static final String OBSERVATION_TIME_COLUMN_NAME = "observation_time";
    private static final String ADDITIONAL_COMMENTS_COLUMN_NAME = "additional_comments";
    private SQLiteDatabase database;
    private static final String CREATE_HIKE_TABLE = String.format(
            "CREATE TABLE %s (" +
            "%s INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "%s TEXT, " +
            "%s TEXT, " +
            "%s TEXT, " +
            "%s INTEGER, " +
            "%s TEXT, " +
            "%s TEXT, " +
            "%s TEXT)", HIKE_TABLE_NAME, ID_COLUMN_NAME, NAME_COLUMN_NAME, LOCATION_COLUMN_NAME, DATE_COLUMN_NAME, PARKING_COLUMN_NAME, LENGTH_COLUMN_NAME, DIFFICULTY_COLUMN_NAME, DESCRIPTION_COLUMN_NAME);
    private static final String CREATE_OBSERVATION_TABLE = String.format(
            "CREATE TABLE %s (" +
            "%s INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "%s INTEGER, " +
            "%s TEXT, " +
            "%s TEXT, " +
            "%s TEXT, " +
            "FOREIGN KEY (%s) REFERENCES %s(%s))",
            OBSERVATION_TABLE_NAME, OBSERVATION_ID_COLUMN_NAME, ID_COLUMN_NAME, OBSERVATION_NAME_COLUMN_NAME, OBSERVATION_TIME_COLUMN_NAME, ADDITIONAL_COMMENTS_COLUMN_NAME, ID_COLUMN_NAME, HIKE_TABLE_NAME, ID_COLUMN_NAME);

    public HikeDatabaseHelper(Context context) {
        super(context, HIKE_TABLE_NAME, null, 1);
        database = getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_HIKE_TABLE);
        db.execSQL(CREATE_OBSERVATION_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + HIKE_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + OBSERVATION_TABLE_NAME);
        Log.w(this.getClass().getName(), HIKE_TABLE_NAME + " database upgrade to version " + newVersion + " - old data lost");
        onCreate(db);
    }

    public long insertDetails(String name, String location, String date, int parking, String length, String difficulty, String description) {
        ContentValues rowValues = new ContentValues();
        rowValues.put(NAME_COLUMN_NAME, name);
        rowValues.put(LOCATION_COLUMN_NAME, location);
        rowValues.put(DATE_COLUMN_NAME, date);
        rowValues.put(PARKING_COLUMN_NAME, parking);
        rowValues.put(LENGTH_COLUMN_NAME, length);
        rowValues.put(DIFFICULTY_COLUMN_NAME, difficulty);
        rowValues.put(DESCRIPTION_COLUMN_NAME, description);
        return database.insertOrThrow(HIKE_TABLE_NAME, null, rowValues);
    }

    public Hike getHikeDetails(int hikeId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Hike hike = null;

        String selection = ID_COLUMN_NAME + " = ?";
        String[] selectionArgs = { String.valueOf(hikeId) };

        Cursor cursor = db.query(HIKE_TABLE_NAME, new String[] {
                "hike_id", "name", "location", "date", "parking_available", "length", "difficulty", "description"
        }, selection, selectionArgs, null, null, null);

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                int id = cursor.getInt(0);
                String name = cursor.getString(1);
                String location = cursor.getString(2);
                String date = cursor.getString(3);
                int parking = cursor.getInt(4);
                String length = cursor.getString(5);
                String difficulty = cursor.getString(6);
                String description = cursor.getString(7);

                hike = new Hike(id, name, location, date, parking, length, difficulty, description);
            }
            cursor.close();
        }
        return hike;
    }

    public List<Hike> getAllDetails() {
        List<Hike> listHike = new ArrayList<>();

        Cursor results = database.query(HIKE_TABLE_NAME, new String[] {"hike_id", "name", "location", "date", "parking_available", "length", "difficulty", "description"}, null, null, null, null, "hike_id DESC");
        results.moveToFirst();

        while(!results.isAfterLast()) {
            int id = results.getInt(0);
            String name = results.getString(1);
            String location = results.getString(2);
            String date = results.getString(3);
            int parking = results.getInt(4);
            String length = results.getString(5);
            String difficulty = results.getString(6);
            String description = results.getString(7);

            listHike.add(new Hike(id, name, location, date, parking, length, difficulty, description));
            results.moveToNext();
        }
        results.close();
        return listHike;
    }

    public void editHike(int hikeId, String newName, String newLocation, String newDate, int newParking, String newLength, String newDifficulty, String newDescription) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(NAME_COLUMN_NAME, newName);
        values.put(LOCATION_COLUMN_NAME, newLocation);
        values.put(DATE_COLUMN_NAME, newDate);
        values.put(PARKING_COLUMN_NAME, newParking);
        values.put(LENGTH_COLUMN_NAME, newLength);
        values.put(DIFFICULTY_COLUMN_NAME, newDifficulty);
        values.put(DESCRIPTION_COLUMN_NAME, newDescription);

        String whereClause = ID_COLUMN_NAME + " = ?";
        String[] whereArgs = { String.valueOf(hikeId) };

        db.update(HIKE_TABLE_NAME, values, whereClause, whereArgs);
        db.close();
    }

    public void deleteHike(int hikeId) {
        SQLiteDatabase db = this.getWritableDatabase();
        String whereClause = ID_COLUMN_NAME + " = ?";
        String[] whereArgs = { String.valueOf(hikeId) };
        db.delete(HIKE_TABLE_NAME, whereClause, whereArgs);
        db.close();
    }

    public void deleteAllHikes() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(HIKE_TABLE_NAME, null, null);
        db.close();
    }


    public long insertObservation(int hikeId, String observation, String observationTime, String additionalComments) {
        ContentValues rowValues = new ContentValues();
        rowValues.put(ID_COLUMN_NAME, hikeId);
        rowValues.put(OBSERVATION_NAME_COLUMN_NAME, observation);
        rowValues.put(OBSERVATION_TIME_COLUMN_NAME, observationTime);
        rowValues.put(ADDITIONAL_COMMENTS_COLUMN_NAME, additionalComments);
        return database.insertOrThrow(OBSERVATION_TABLE_NAME, null, rowValues);
    }


    public Observation getObservationDetails(int observationId) {
        Observation observation = null;

        String selection = ID_COLUMN_NAME + " = ?";
        String[] selectionArgs = { String.valueOf(observationId) };

        Cursor cursor = database.query(OBSERVATION_TABLE_NAME, new String[] {
                ID_COLUMN_NAME, ID_COLUMN_NAME, OBSERVATION_NAME_COLUMN_NAME, OBSERVATION_TIME_COLUMN_NAME, ADDITIONAL_COMMENTS_COLUMN_NAME
        }, selection, selectionArgs, null, null, null);

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                int id = cursor.getInt(0);
                int hikeId = cursor.getInt(1);
                String observationText = cursor.getString(2);
                String observationTime = cursor.getString(3);
                String additionalComments = cursor.getString(4);

                observation = new Observation(id, hikeId, observationText, observationTime, additionalComments);
            }
            cursor.close();
        }

        return observation;
    }
    public List<Observation> getObservationsForHike(int hikeId) {
        List<Observation> observationList = new ArrayList<>();

        String selection = ID_COLUMN_NAME + " = ?";
        String[] selectionArgs = { String.valueOf(hikeId) };

        Cursor cursor = database.query(OBSERVATION_TABLE_NAME, new String[] {
                ID_COLUMN_NAME, ID_COLUMN_NAME, OBSERVATION_NAME_COLUMN_NAME, OBSERVATION_TIME_COLUMN_NAME, ADDITIONAL_COMMENTS_COLUMN_NAME
        }, selection, selectionArgs, null, null, null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                int id = cursor.getInt(0);
                int observedHikeId = cursor.getInt(1);
                String observation = cursor.getString(2);
                String observationTime = cursor.getString(3);
                String additionalComments = cursor.getString(4);

                observationList.add(new Observation(id, observedHikeId, observation, observationTime, additionalComments));
            }
            cursor.close();
        }
        return observationList;
    }

    public void editObservation(int observationId, String newObservation, String newObservationTime, String newAdditionalComments) {
        ContentValues values = new ContentValues();
        values.put(OBSERVATION_NAME_COLUMN_NAME, newObservation);
        values.put(OBSERVATION_TIME_COLUMN_NAME, newObservationTime);
        values.put(ADDITIONAL_COMMENTS_COLUMN_NAME, newAdditionalComments);

        String whereClause = ID_COLUMN_NAME + " = ?";
        String[] whereArgs = { String.valueOf(observationId) };

        database.update(OBSERVATION_TABLE_NAME, values, whereClause, whereArgs);
    }

    public void deleteObservation(int observationId) {
        String whereClause = ID_COLUMN_NAME + " = ?";
        String[] whereArgs = { String.valueOf(observationId) };
        database.delete(OBSERVATION_TABLE_NAME, whereClause, whereArgs);
    }

    public void deleteAllObservationsForHike(int hikeId) {
        String whereClause = ID_COLUMN_NAME + " = ?";
        String[] whereArgs = { String.valueOf(hikeId) };
        database.delete(OBSERVATION_TABLE_NAME, whereClause, whereArgs);
    }
}
