package com.example.coursework;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class HikeAdapter extends RecyclerView.Adapter<HikeAdapter.ViewHolder> {
    private List<Hike> hikeList;
    private HikeDatabaseHelper dbHelper;

    public HikeAdapter(List<Hike> hikeList, HikeDatabaseHelper dbHelper) {
        this.hikeList = hikeList;
        this.dbHelper = dbHelper;
    }

    public void setFilteredList(List<Hike> filteredList) {
        this.hikeList = filteredList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_hike, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Hike hike = hikeList.get(position);
        holder.textName.setText("Name: " + hike.getName());
        holder.textLocation.setText("Location: " + hike.getLocation());
        holder.textDate.setText("Date: " + hike.getDate());
        String parkingValue = getParkingValue(hike.getParkingAvailable());
        holder.textParking.setText("Parking Available: " + parkingValue);
        holder.textLength.setText("Length the bike: " + hike.getLength());
        holder.textDifficulty.setText("Level of difficulty: " + hike.getDifficulty());
        holder.textDescription.setText("Description: " + hike.getDescription());
        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int hikeId = hike.getId();
                dbHelper.deleteHike(hikeId);
                Toast.makeText(view.getContext(), "A hike information with id: " + hikeId + " has been deleted", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(view.getContext(), CreateHikeActivity.class);
                view.getContext().startActivity(i);
            }
        });
        holder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(view.getContext(), EditHikeActivity.class);
                i.putExtra("hikeId", hike.getId());
                view.getContext().startActivity(i);
            }
        });
    }

    private String getParkingValue(int parkingAvailable) {
        if (parkingAvailable == 0) {
            return "No";
        } else return "Yes";
    }

    @Override
    public int getItemCount() {
        return hikeList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textName, textLocation, textDate, textParking, textLength, textDifficulty, textDescription;
        Button btnEdit, btnDelete;

        public ViewHolder(View itemView) {
            super(itemView);
            textName = itemView.findViewById(R.id.txtName);
            textLocation = itemView.findViewById(R.id.txtLocation);
            textDate = itemView.findViewById(R.id.txtDate);
            textParking = itemView.findViewById(R.id.txtParking);
            textLength = itemView.findViewById(R.id.txtLength);
            textDifficulty = itemView.findViewById(R.id.txtDifficulty);
            textDescription = itemView.findViewById(R.id.txtDescription);
            btnDelete = itemView.findViewById(R.id.btnDeleteHike);
            btnEdit = itemView.findViewById(R.id.btnEditHike);
        }
    }
}
