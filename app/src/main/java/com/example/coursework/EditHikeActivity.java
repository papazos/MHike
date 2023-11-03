package com.example.coursework;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

public class EditHikeActivity extends AppCompatActivity {

    private String[] difficultyArray  = {"Easy", "Moderate", "Difficult"};
    private EditText hikeName, hikeLocation, hikeLength, hikeDescription;
    private Spinner hikeDifficulty;
    private Switch parkingSwitch;
    private DatePicker hikeDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_hike);
        int hikeId = getIntent().getIntExtra("hikeId", -1); // -1 is a default value in case the extra is not found

        DatabaseHelper db = new DatabaseHelper(this);
        Hike hike = db.getHikeDetails(hikeId);

        // Get references to the EditText fields in your layout
        hikeName = findViewById(R.id.hikeName);
        hikeLocation = findViewById(R.id.hikeLocation);
        hikeLength = findViewById(R.id.hikeLength);
        hikeDifficulty = findViewById(R.id.hikeDifficulty);
        parkingSwitch = findViewById(R.id.parkingAvailable);
        hikeDescription = findViewById(R.id.hikeDescription);
        hikeDate = findViewById(R.id.hikeDate);

        Button btnEdit = findViewById(R.id.btnSubmit);

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, difficultyArray);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        hikeDifficulty.setAdapter(dataAdapter);
        // Repeat this for other EditText fields

        // Set the text of each EditText to the corresponding detail from the retrieved Hike object
        if (hike != null) {
            hikeName.setText(hike.getName());
            hikeLocation.setText(hike.getLocation());
            hikeLength.setText(hike.getLength());
            parkingSwitch.setChecked(getParkingBoolean(hike.getParkingAvailable()));
            hikeDescription.setText(hike.getDescription());

            String difficulty = hike.getDifficulty();
            int difficultyIndex = getDifficultyIndex(difficulty);
            if (difficultyIndex != -1) {
                hikeDifficulty.setSelection(difficultyIndex);
            }

            // Set the date in the DatePicker
            String date = hike.getDate();
            setDatePickerFromDate(hikeDate, date);

            btnEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (validateForm() == false) {
                        return;
                    } else {
                        showConfirmation(hikeId);
                    }
                }
            });
        }
    }

    private void showConfirmation(int hikeId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirmation");
        builder.setMessage("Hike Name: " + hikeName.getText().toString() + "\n" +
                "Location: " + hikeLocation.getText().toString() + "\n" +
                "Length: " + hikeLength.getText().toString() + "\n" +
                "Description: " + hikeDescription.getText().toString() + "\n" +
                "Difficulty: " + difficultyArray[hikeDifficulty.getSelectedItemPosition()] + "\n" +
                "Date: " + hikeDate.getDayOfMonth() + "/" + (hikeDate.getMonth() + 1) + "/" + hikeDate.getYear() + "\n" +
                "Parking Available: " + (parkingSwitch.isChecked() ? "Yes" : "No"));
        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                updateDetails(hikeId);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    private boolean validateForm() {
        boolean isValid = true;

        if (hikeName.getText().toString().isEmpty()) {
            hikeName.setError("Name is required");
            isValid = false;
        }

        if (hikeLocation.getText().toString().isEmpty()) {
            hikeLocation.setError("Location is required");
            isValid = false;
        }

        if (hikeLength.getText().toString().isEmpty()) {
            hikeLength.setError("Length is required");
            isValid = false;
        }
        return isValid;
    }

    private void updateDetails(int hikeId) {
        DatabaseHelper dbHelper = new DatabaseHelper(getApplicationContext());
        String name = hikeName.getText().toString();
        String location = hikeLocation.getText().toString();
        String length = hikeLength.getText().toString();
        String description = hikeDescription.getText().toString();
        String selectedDifficulty = difficultyArray[hikeDifficulty.getSelectedItemPosition()];
        String date = hikeDate.getDayOfMonth() + "/" + (hikeDate.getMonth() + 1) + "/" + hikeDate.getYear();
        boolean isParkingAvailable = parkingSwitch.isChecked();

        // Convert the boolean to an integer (0 for false, 1 for true)
        int parkingAvailability = isParkingAvailable ? 1 : 0;

        dbHelper.editHike(hikeId,name,location,date,parkingAvailability,length,selectedDifficulty,description);

        Toast.makeText(this, "Updated", Toast.LENGTH_LONG).show();

        Intent i = new Intent(this, DetailsActivity.class);
        startActivity(i);
    }

    private int getDifficultyIndex(String difficulty) {
        for (int i = 0; i < difficultyArray.length; i++) {
            if (difficultyArray[i].equals(difficulty)) {
                return i;
            }
        }
        return -1; // Difficulty not found in the array
    }

    private boolean getParkingBoolean(int parkingAvailable) {
        if (parkingAvailable == 0) {
            return false;
        }
        else return true;
    }

    private void setDatePickerFromDate(DatePicker datePicker, String date) {
        String[] parts = date.split("/");
        if (parts.length == 3) {
            int day = Integer.parseInt(parts[0]);
            int month = Integer.parseInt(parts[1]) - 1;
            int year = Integer.parseInt(parts[2]);
            datePicker.updateDate(year, month, day);
        }
    }
}