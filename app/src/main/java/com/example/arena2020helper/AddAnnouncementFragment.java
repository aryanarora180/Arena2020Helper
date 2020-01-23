package com.example.arena2020helper;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class AddAnnouncementFragment extends Fragment {

    private FirebaseFirestore db;

    private TextInputEditText titleEditText;
    private TextInputEditText detailsEditText;

    public AddAnnouncementFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_add_announcement, container, false);

        titleEditText = root.findViewById(R.id.add_announcement_add_title_edit_text);
        detailsEditText = root.findViewById(R.id.add_sport_add_team_b_edit_text);

        getActivity().setTitle(getString(R.string.add_announcement));

        db = FirebaseFirestore.getInstance();

        Button submitButton = root.findViewById(R.id.add_event_button);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Object> dataForFirebase = new HashMap<>();
                dataForFirebase.put(getString(R.string.firebase_collection_announcements_field_name), titleEditText.getText().toString());
                dataForFirebase.put(getString(R.string.firebase_collection_announcements_field_desc), detailsEditText.getText().toString());
                dataForFirebase.put(getString(R.string.firebase_collection_announcements_field_date), new Date());

                addAnnouncementToFirebase(dataForFirebase);
            }
        });

        return root;
    }

    private void showToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    private void addAnnouncementToFirebase(Map<String, Object> data) {
        db.collection(getString(R.string.firebase_collection_announcements))
                .add(data)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        showToast(getString(R.string.add_announcement_success));
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        showToast(getString(R.string.add_announcement_failed));
                    }
                });
    }



}
