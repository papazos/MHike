package com.example.coursework;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

public class ObservationActivity extends AppCompatActivity {
    int hikeId;
    private TimePicker timePicker;
    private EditText obName, obComment;
    private Button btnAdd;
    private ObservationAdapter observationAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_observation);

        obName = findViewById(R.id.observation);
        obComment = findViewById(R.id.obComments);
        btnAdd = findViewById(R.id.btn_addOb);

        timePicker = findViewById(R.id.obTime);
        timePicker.setIs24HourView(true);

        // Get the current time
        Calendar currentTime = Calendar.getInstance();
        int currentHour = currentTime.get(Calendar.HOUR_OF_DAY);
        int currentMinute = currentTime.get(Calendar.MINUTE);

        timePicker.setHour(currentHour);
        timePicker.setMinute(currentMinute);


        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateForm()) {
                    showConfirmation();
                }
            }
        });
    }

    private void showConfirmation() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        int selectedHour = timePicker.getHour();
        int selectedMinute = timePicker.getMinute();
        String time = selectedHour + ":" + selectedMinute;

        builder.setTitle("Confirmation");
        builder.setMessage("Observation Name: " + obName.getText().toString() + "\n" +
                "Time: " + time + "\n" +
                "Comment: " + obComment.getText().toString());
        builder.setPositiveButton("Confirm", (dialog, which) -> {
            saveObDetails();
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> {
        });
        builder.show();
    }

    private void saveObDetails() {
        HikeDatabaseHelper dbHelper = new HikeDatabaseHelper(getApplicationContext());

        int selectedHour = timePicker.getHour();
        int selectedMinute = timePicker.getMinute();

        String name = obName.getText().toString();
        String time = selectedHour + ":" + selectedMinute;
        String comment = obComment.getText().toString();
        int hikeId = getIntent().getIntExtra("hikeId", -1);

        dbHelper.insertObservation(hikeId, name, time, comment);

        Toast.makeText(this, "Observation added successfully", Toast.LENGTH_SHORT).show();

        finish();
    }

    private boolean validateForm() {
        boolean isValid = true;

        if (obName.getText().toString().isEmpty()) {
            obName.setError("Observation Name is required");
            isValid = false;
        }

        if (obComment.getText().toString().isEmpty()) {
            obComment.setError("Comment is required");
            isValid = false;
        }

        return isValid;
    }
}
