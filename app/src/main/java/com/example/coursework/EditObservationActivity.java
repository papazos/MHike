package com.example.coursework;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class EditObservationActivity extends AppCompatActivity {
    private EditText obName, obTime, obComment;
    private Button btnUpdate;
    private ObservationAdapter observationAdapter;
    private ObservationDatabaseHelper db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_observation);

        int obId = getIntent().getIntExtra("obId", -1);

        obName = findViewById(R.id.observation);
        obTime = findViewById(R.id.obTime);
        obComment = findViewById(R.id.obComments);
        btnUpdate = findViewById(R.id.btn_updateOb);

        db = new ObservationDatabaseHelper(getApplicationContext());
        Observation ob = db.getObservationDetails(obId);

        if(ob != null){
            obName.setText(ob.getObservation());
            obTime.setText(ob.getObservationTime());
            obComment.setText(ob.getObservationComments());

            btnUpdate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (validateForm()) {
                        showConfirmation(obId);
                    }
                }
            });
        }
    }

    private void showConfirmation(int obId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirmation");
        builder.setMessage("Observation Name: " + obName.getText().toString() + "\n" +
                "Time: " + obTime.getText().toString() + "\n" +
                "Comment: " + obComment.getText().toString());
        builder.setPositiveButton("Confirm", (dialog, which) -> {
            updateObDetails();
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> {
        });
        builder.show();
    }

    private void updateObDetails() {
        ObservationDatabaseHelper dbHelper = new ObservationDatabaseHelper(getApplicationContext());
        String name = obName.getText().toString();
        String time = obTime.getText().toString();
        String comment = obComment.getText().toString();

        int obId = getIntent().getIntExtra("obId", -1);
        dbHelper.editObservation(obId, name, time, comment);

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