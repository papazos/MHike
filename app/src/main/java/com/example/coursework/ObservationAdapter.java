package com.example.coursework;

import android.content.Context;
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

public class ObservationAdapter extends RecyclerView.Adapter<ObservationAdapter.ViewHolder> {
    private List<Observation> observationList;
    private HikeDatabaseHelper dbHelper;

    public ObservationAdapter(List<Observation> observationList, HikeDatabaseHelper dbHelper) {
        this.observationList = observationList;
        this.dbHelper = dbHelper;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_observation, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Observation observation = observationList.get(position);
        holder.textName.setText("Observation: " + observation.getObservation());
        holder.textTime.setText("Time: " + observation.getObservationTime());
        holder.textComment.setText("Comment: " + observation.getObservationComments());

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int observationId = observation.getId();
                dbHelper.deleteObservation(observationId);
                observationList.remove(observation);
                notifyDataSetChanged();
                Toast.makeText(view.getContext(), "An observation has been deleted", Toast.LENGTH_SHORT).show();
            }
        });

        holder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(view.getContext(), EditObservationActivity.class);
                i.putExtra("obId", observation.getId());
                view.getContext().startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return observationList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textName, textTime, textComment;
        Button btnEdit, btnDelete;

        public ViewHolder(View itemView) {
            super(itemView);
            textName = itemView.findViewById(R.id.txtObName);
            textTime = itemView.findViewById(R.id.txtObTime);
            textComment = itemView.findViewById(R.id.txtObComment);
            btnEdit = itemView.findViewById(R.id.btnObEdit);
            btnDelete = itemView.findViewById(R.id.btnObDelete);
        }
    }
}
