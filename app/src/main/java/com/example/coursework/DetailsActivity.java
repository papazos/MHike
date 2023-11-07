package com.example.coursework;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;

import java.util.ArrayList;
import java.util.List;

public class DetailsActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private List<Hike> hikeList;
    private HikeDatabaseHelper dbHelper;
    private HikeAdapter hikeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        dbHelper = new HikeDatabaseHelper(getApplicationContext());

        // Retrieve data from the database and populate the originalHikeList
        hikeList = dbHelper.getAllDetails();
        hikeAdapter = new HikeAdapter(hikeList, dbHelper);

        // Set the adapter on the RecyclerView
        recyclerView.setAdapter(hikeAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_details, menu);
        MenuItem i = menu.findItem(R.id.itemDelete);
        i.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(@NonNull MenuItem menuItem) {
                showConfirmation();
                return false;
            }
        });

        MenuItem search = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) search.getActionView();
        searchView.setQueryHint("Type here to search...");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterList(newText);
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    private void filterList(String text) {
        List<Hike> filteredList = new ArrayList<>();

        for (Hike hike : hikeList) {
            if (hike.getName().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(hike);
            }
        }
        hikeAdapter.setFilteredList(filteredList);
    }

    private void showConfirmation() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirmation");
        builder.setMessage("You are going to delete all the hike information, are you sure about this?");
        builder.setPositiveButton("Confirm", (dialog, which) -> {
            handleDeleteAllDatabase();
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    private void handleDeleteAllDatabase() {
        HikeDatabaseHelper dbHelper = new HikeDatabaseHelper(getApplicationContext());
        dbHelper.deleteAllHikes();

        Intent i = new Intent(this, MainActivity.class); // Change to your root activity's name
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // This flag clears the back stack
        startActivity(i);
    }
}