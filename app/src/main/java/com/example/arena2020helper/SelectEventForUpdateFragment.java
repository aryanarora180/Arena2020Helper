package com.example.arena2020helper;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.EventLog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class SelectEventForUpdateFragment extends Fragment {

    private long mSportCode;

    private FirebaseFirestore db;

    public SelectEventForUpdateFragment(long sportCode) {
        mSportCode = sportCode;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_select_sport_update, container, false);

        final RecyclerView eventRecycler = root.findViewById(R.id.select_event_recycler);
        db = FirebaseFirestore.getInstance();

        db.collection(getString(R.string.firebase_collection_schedule))
                .orderBy(getString(R.string.firebase_collection_schedule_field_date))
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            ArrayList<Event> events = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if(mSportCode == document.getLong(getString(R.string.firebase_collection_schedule_field_sportCode))) {
                                    String desc = document.getString(getString(R.string.firebase_collection_schedule_field_name_team_a)) + " vs " + document.getString(getString(R.string.firebase_collection_schedule_field_name_team_b)) + " @ " + formatDate(document.getDate(getString(R.string.firebase_collection_schedule_field_date)));
                                    events.add(new Event(document.getId(), desc));
                                }
                            }
                            EventAdapter adapter = new EventAdapter(events);
                            eventRecycler.setAdapter(adapter);
                            eventRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
                        } else {
                            Toast.makeText(getContext(), "An error occurred", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        return root;
    }

    private class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolder> {

        EventAdapter(ArrayList<Event> events) {
            mDataset = events;
        }

        ArrayList<Event> mDataset;

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView event;
            public ViewHolder(View itemView) {
                super(itemView);
                event = itemView.findViewById(R.id.sport);
            }
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            View listItemView = inflater.inflate(R.layout.add_sport_item, parent, false);
            ViewHolder viewHolder = new ViewHolder(listItemView);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
            final TextView sport = holder.event;
            sport.setText(mDataset.get(position).getDocumentDetails());

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getFragmentManager().beginTransaction().replace(R.id.frame_container, new UpdateScoreFragment(mDataset.get(position).getDocumentID(), mSportCode)).commit();
                }
            });

        }

        @Override
        public int getItemCount() {
            return mDataset.size();
        }
    }

    private class Event{

        String documentID;
        String documentDetails;

        public Event(String documentID, String documentDetails) {
            this.documentID = documentID;
            this.documentDetails = documentDetails;
        }

        public String getDocumentID() {
            return documentID;
        }

        public String getDocumentDetails() {
            return documentDetails;
        }
    }

    private String formatDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy HH:mm");
        return sdf.format(date);
    }

}
