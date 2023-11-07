package com.example.coursework;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

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

import java.util.Calendar;

public class CreateHikeActivity extends AppCompatActivity {
    private ObservationAdapter observationAdapter;
    private String[] difficulty = {"Easy", "Moderate", "Difficult"};
    private EditText hikeName, hikeLocation, hikeLength, hikeDescription;
    private Spinner hikeDifficulty;
    private Switch parkingSwitch;
    private DatePicker hikeDate;
    private Button submitButton, detailsButton;
    DatePicker datePicker;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_hike);

        hikeName = findViewById(R.id.hikeName);
        hikeLocation = findViewById(R.id.hikeLocation);
        hikeLength = findViewById(R.id.hikeLength);
        hikeDescription = findViewById(R.id.hikeDescription);
        hikeDifficulty = findViewById(R.id.hikeDifficulty);
        hikeDate = findViewById(R.id.hikeDate);
        parkingSwitch = findViewById(R.id.parkingAvailable);

        //Spinner
        Spinner spinnerDifficulty = findViewById(R.id.hikeDifficulty);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, difficulty);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDifficulty.setAdapter(dataAdapter);

        datePicker = findViewById(R.id.hikeDate);
        Calendar c = Calendar.getInstance();
        int y = c.get(Calendar.YEAR);
        int m = c.get(Calendar.MONTH);
        int d = c.get(Calendar.DAY_OF_MONTH);
        datePicker.init(y,m,d,null);

        submitButton = findViewById(R.id.btnSubmit);
        submitButton.setOnClickListener(v -> {
            if (validateForm()) {
                showConfirmation();
            }
        });

        detailsButton = findViewById(R.id.btnDetails);
        detailsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(CreateHikeActivity.this, DetailsActivity.class);
                startActivity(i);
            }
        });
    }

    private void showConfirmation() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirmation");
        builder.setMessage("Hike Name: " + hikeName.getText().toString() + "\n" +
                "Location: " + hikeLocation.getText().toString() + "\n" +
                "Length: " + hikeLength.getText().toString() + "\n" +
                "Description: " + hikeDescription.getText().toString() + "\n" +
                "Difficulty: " + difficulty[hikeDifficulty.getSelectedItemPosition()] + "\n" +
                "Date: " + hikeDate.getDayOfMonth() + "/" + (hikeDate.getMonth() + 1) + "/" + hikeDate.getYear() + "\n" +
                "Parking Available: " + (parkingSwitch.isChecked() ? "Yes" : "No"));
        builder.setPositiveButton("Confirm", (dialog, which) -> {
            saveDetails();
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> {
        });
        builder.show();
    }


    private void saveDetails() {
        HikeDatabaseHelper dbHelper = new HikeDatabaseHelper(getApplicationContext());
        String name = hikeName.getText().toString();
        String location = hikeLocation.getText().toString();
        String length = hikeLength.getText().toString();
        String description = hikeDescription.getText().toString();
        String selectedDifficulty = difficulty[hikeDifficulty.getSelectedItemPosition()];
        String date = hikeDate.getDayOfMonth() + "/" + (hikeDate.getMonth() + 1) + "/" + hikeDate.getYear();
        boolean isParkingAvailable = parkingSwitch.isChecked();

        // Convert the boolean to an integer (0 for false, 1 for true)
        int parkingAvailability = isParkingAvailable ? 1 : 0;

        long hikeId = dbHelper.insertDetails(name,location,date,parkingAvailability,length,selectedDifficulty,description);

        Toast.makeText(this, "A new hike has been created with id: " + hikeId, Toast.LENGTH_LONG).show();

        Intent i = new Intent(this, DetailsActivity.class);
        startActivity(i);
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
}