package com.example.coursework;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class ObservationDatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "observation_details";
    private static final String ID_COLUMN_NAME = "observation_id";
    private static final String HIKE_ID_COLUMN_NAME = "hike_id";
    private static final String OBSERVATION_COLUMN_NAME = "observation";
    private static final String OBSERVATION_TIME_COLUMN_NAME = "observation_time";
    private static final String ADDITIONAL_COMMENTS_COLUMN_NAME = "additional_comments";
    private SQLiteDatabase database;

    private static final String DATABASE_CREATE_QUERY = String.format(
            "CREATE TABLE %s (" +
                    "%s INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "%s INTEGER, " +
                    "%s TEXT, " +
                    "%s TEXT, " +
                    "%s TEXT)",
            DATABASE_NAME, ID_COLUMN_NAME, HIKE_ID_COLUMN_NAME, OBSERVATION_COLUMN_NAME, OBSERVATION_TIME_COLUMN_NAME, ADDITIONAL_COMMENTS_COLUMN_NAME);

    public ObservationDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
        database = getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE_QUERY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DATABASE_NAME);
        Log.w(this.getClass().getName(), DATABASE_NAME + " database upgrade to version " + newVersion + " - old data lost");
        onCreate(db);
    }

    public long insertObservation(int hikeId, String observation, String observationTime, String additionalComments) {
        ContentValues rowValues = new ContentValues();
        rowValues.put(HIKE_ID_COLUMN_NAME, hikeId);
        rowValues.put(OBSERVATION_COLUMN_NAME, observation);
        rowValues.put(OBSERVATION_TIME_COLUMN_NAME, observationTime);
        rowValues.put(ADDITIONAL_COMMENTS_COLUMN_NAME, additionalComments);
        return database.insertOrThrow(DATABASE_NAME, null, rowValues);
    }


    public Observation getObservationDetails(int observationId) {
        Observation observation = null;

        String selection = ID_COLUMN_NAME + " = ?";
        String[] selectionArgs = { String.valueOf(observationId) };

        Cursor cursor = database.query(DATABASE_NAME, new String[] {
                ID_COLUMN_NAME, HIKE_ID_COLUMN_NAME, OBSERVATION_COLUMN_NAME, OBSERVATION_TIME_COLUMN_NAME, ADDITIONAL_COMMENTS_COLUMN_NAME
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

        String selection = HIKE_ID_COLUMN_NAME + " = ?";
        String[] selectionArgs = { String.valueOf(hikeId) };

        Cursor cursor = database.query(DATABASE_NAME, new String[] {
                ID_COLUMN_NAME, HIKE_ID_COLUMN_NAME, OBSERVATION_COLUMN_NAME, OBSERVATION_TIME_COLUMN_NAME, ADDITIONAL_COMMENTS_COLUMN_NAME
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
        values.put(OBSERVATION_COLUMN_NAME, newObservation);
        values.put(OBSERVATION_TIME_COLUMN_NAME, newObservationTime);
        values.put(ADDITIONAL_COMMENTS_COLUMN_NAME, newAdditionalComments);

        String whereClause = ID_COLUMN_NAME + " = ?";
        String[] whereArgs = { String.valueOf(observationId) };

        database.update(DATABASE_NAME, values, whereClause, whereArgs);
    }

    public void deleteObservation(int observationId) {
        String whereClause = ID_COLUMN_NAME + " = ?";
        String[] whereArgs = { String.valueOf(observationId) };
        database.delete(DATABASE_NAME, whereClause, whereArgs);
    }

    public void deleteAllObservationsForHike(int hikeId) {
        String whereClause = HIKE_ID_COLUMN_NAME + " = ?";
        String[] whereArgs = { String.valueOf(hikeId) };
        database.delete(DATABASE_NAME, whereClause, whereArgs);
    }
}
