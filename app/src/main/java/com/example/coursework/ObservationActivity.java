package com.example.coursework;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class ObservationActivity extends AppCompatActivity {
    int hikeId;
    private EditText obName, obTime, obComment;
    private Button btnAdd;
    private ObservationAdapter observationAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_observation);

        obName = findViewById(R.id.observation);
        obTime = findViewById(R.id.obTime);
        obComment = findViewById(R.id.obComments);
        btnAdd = findViewById(R.id.btn_addOb);

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
        builder.setTitle("Confirmation");
        builder.setMessage("Observation Name: " + obName.getText().toString() + "\n" +
                "Time: " + obTime.getText().toString() + "\n" +
                "Comment: " + obComment.getText().toString());
        builder.setPositiveButton("Confirm", (dialog, which) -> {
            saveObDetails();
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> {
        });
        builder.show();
    }

    private void saveObDetails() {
        ObservationDatabaseHelper dbHelper = new ObservationDatabaseHelper(getApplicationContext());
        String name = obName.getText().toString();
        String time = obTime.getText().toString();
        String comment = obComment.getText().toString();
        int hikeId = getIntent().getIntExtra("hikeId", -1);

        dbHelper.insertObservation(hikeId, name, time, comment);

        Toast.makeText(this, "Observation added successfully", Toast.LENGTH_SHORT).show();

        // Finish the current activity
        finish();
    }

    private boolean validateForm() {
        boolean isValid = true;

        if (obName.getText().toString().isEmpty()) {
            obName.setError("Observation Name is required");
            isValid = false;
        }

        if (obTime.getText().toString().isEmpty()) {
            obTime.setError("Time is required");
            isValid = false;
        }

        if (obComment.getText().toString().isEmpty()) {
            obComment.setError("Comment is required");
            isValid = false;
        }

        return isValid;
    }
}
