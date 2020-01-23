package com.example.arena2020helper;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


public class UpdateScoreFragment extends Fragment {

    String mDocumentId;
    long mSportCode;

    public static final long SPORT_CRICKET = 100;
    public static final long SPORT_FOOTBALL = 101;
    public static final long SPORT_VOLLEYBALL = 102;
    public static final long SPORT_BASKETBALL = 103;
    public static final long SPORT_HOCKEY = 104;
    public static final long SPORT_BADMINTON = 105;
    public static final long SPORT_TENNIS = 106;
    public static final long SPORT_CHESS = 107;
    public static final long SPORT_CARROM = 108;
    public static final long SPORT_SQUASH = 109;
    public static final long SPORT_TABLE_TENNIS = 110;
    public static final long SPORT_SNOOKER = 111;
    public static final long SPORT_THROWBALL = 112;
    public static final long SPORT_DUALTHON = 113;
    public static final long SPORT_BODY_BUILDING = 114;
    public static final long SPORT_POWER_LIFTING = 115;
    public static final long SPORT_FRISBEE = 116;

    public static final long SPORT_TYPE_ONE = 151;
    public static final long SPORT_TYPE_TWO = 152;
    public static final long SPORT_TYPE_THREE = 153;
    public static final long SPORT_TYPE_NO_LIVE = 154;

    public static final long MATCH_NOT_STARTED = 1000;
    public static final long MATCH_IN_PROGRESS = 1001;
    public static final long MATCH_COMPLETED = 1002;

    public long getSportType() {
        if (mSportCode == SPORT_FOOTBALL || mSportCode == SPORT_BASKETBALL || mSportCode == SPORT_HOCKEY || mSportCode == SPORT_THROWBALL)
            return SPORT_TYPE_ONE;
        else if (mSportCode == SPORT_CRICKET)
            return SPORT_TYPE_TWO;
        else if (mSportCode == SPORT_TENNIS || mSportCode == SPORT_BADMINTON || mSportCode == SPORT_SQUASH || mSportCode == SPORT_TABLE_TENNIS)
            return SPORT_TYPE_THREE;
        else return SPORT_TYPE_NO_LIVE;
    }

    public UpdateScoreFragment(String documentId, long sportCode) {
        mDocumentId = documentId;
        mSportCode = sportCode;
    }

    private ChipGroup statusChipGroup;
    private Chip matchStatusScheduled;
    private Chip matchStatusLive;
    private Chip matchStatusComplete;

    private TextView teamAName;
    private TextView teamBName;
    private TextView teamAScore;
    private TextView teamBScore;
    private TextView teamAOvers;
    private TextView teamBOvers;
    private TextView currentSet;
    private TextView setOneWinner;
    private TextView setTwoWinner;
    private TextView setThreeWinner;
    private TextView combinedScore;
    private TextView winner;
    private Button update;

    private FirebaseFirestore db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        db = FirebaseFirestore.getInstance();

        if (getSportType() == SPORT_TYPE_ONE) {

            View root = inflater.inflate(R.layout.fragment_update_score_one, container, false);

            matchStatusScheduled = root.findViewById(R.id.match_status_chip_scheduled);
            matchStatusLive = root.findViewById(R.id.match_status_chip_live);
            matchStatusComplete = root.findViewById(R.id.match_status_chip_complete);
            teamAName = root.findViewById(R.id.update_score_team_a_name_edit_text);
            teamBName = root.findViewById(R.id.update_score_team_b_name_edit_text);
            teamAScore = root.findViewById(R.id.update_score_team_a_score_edit_text);
            teamBScore = root.findViewById(R.id.update_score_team_b_score_edit_text);
            update = root.findViewById(R.id.update_scores_button);
            statusChipGroup = root.findViewById(R.id.match_status_chip_group);

            db.collection(getString(R.string.firebase_collection_schedule)).document(mDocumentId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        long matchStatus = document.getLong(getString(R.string.firebase_collection_schedule_field_matchStatus));
                        if (matchStatus == MATCH_IN_PROGRESS)
                            matchStatusLive.setChecked(true);
                        else if (matchStatus == MATCH_COMPLETED)
                            matchStatusComplete.setChecked(true);
                        else
                            matchStatusScheduled.setChecked(true);
                        teamAName.setText(document.getString(getString(R.string.firebase_collection_schedule_field_name_team_a)));
                        teamBName.setText(document.getString(getString(R.string.firebase_collection_schedule_field_name_team_b)));
                        teamAScore.setText(Long.toString(document.getLong(getString(R.string.firebase_collection_schedule_field_score_team_a))));
                        teamBScore.setText(Long.toString(document.getLong(getString(R.string.firebase_collection_schedule_field_score_team_b))));
                    } else {
                        Toast.makeText(getContext(), "Error getting details", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            update.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DocumentReference reference = db.collection(getString(R.string.firebase_collection_schedule)).document(mDocumentId);

                    if (statusChipGroup.getCheckedChipId() == R.id.match_status_chip_live)
                        reference.update(getString(R.string.firebase_collection_schedule_field_matchStatus), MATCH_IN_PROGRESS);
                    else if (statusChipGroup.getCheckedChipId() == R.id.match_status_chip_complete)
                        reference.update(getString(R.string.firebase_collection_schedule_field_matchStatus), MATCH_COMPLETED);
                    else
                        reference.update(getString(R.string.firebase_collection_schedule_field_matchStatus), MATCH_NOT_STARTED);

                    reference.update(getString(R.string.firebase_collection_schedule_field_name_team_a), teamAName.getText().toString());
                    reference.update(getString(R.string.firebase_collection_schedule_field_name_team_b), teamBName.getText().toString());
                    reference.update(getString(R.string.firebase_collection_schedule_field_score_team_a), Long.parseLong(teamAScore.getText().toString()));
                    reference.update(getString(R.string.firebase_collection_schedule_field_score_team_b), Long.parseLong(teamBScore.getText().toString()))
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(getContext(), "Updated Successfully", Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            });
            return root;
        } else if (getSportType() == SPORT_TYPE_TWO) {

            View root = inflater.inflate(R.layout.fragment_update_score_two, container, false);

            statusChipGroup = root.findViewById(R.id.match_status_chip_group);
            matchStatusScheduled = root.findViewById(R.id.match_status_chip_scheduled);
            matchStatusLive = root.findViewById(R.id.match_status_chip_live);
            matchStatusComplete = root.findViewById(R.id.match_status_chip_complete);
            teamAName = root.findViewById(R.id.update_score_team_a_name_edit_text);
            teamBName = root.findViewById(R.id.update_score_team_b_name_edit_text);
            teamAScore = root.findViewById(R.id.update_score_team_a_score_edit_text);
            teamBScore = root.findViewById(R.id.update_score_team_b_score_edit_text);
            teamAOvers = root.findViewById(R.id.update_score_team_a_overs_edit_text);
            teamBOvers = root.findViewById(R.id.update_score_team_b_overs_edit_text);
            update = root.findViewById(R.id.update_scores_button);

            db.collection(getString(R.string.firebase_collection_schedule)).document(mDocumentId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        long matchStatus = document.getLong(getString(R.string.firebase_collection_schedule_field_matchStatus));
                        if (matchStatus == MATCH_IN_PROGRESS)
                            matchStatusLive.setChecked(true);
                        else if (matchStatus == MATCH_COMPLETED)
                            matchStatusComplete.setChecked(true);
                        else
                            matchStatusScheduled.setChecked(true);
                        teamAName.setText(document.getString(getString(R.string.firebase_collection_schedule_field_name_team_a)));
                        teamBName.setText(document.getString(getString(R.string.firebase_collection_schedule_field_name_team_b)));
                        teamAScore.setText(Long.toString(document.getLong(getString(R.string.firebase_collection_schedule_field_score_team_a))));
                        teamBScore.setText(Long.toString(document.getLong(getString(R.string.firebase_collection_schedule_field_score_team_b))));
                        teamAOvers.setText(document.getString(getString(R.string.firebase_collection_schedule_field_overs_team_a)));
                        teamBOvers.setText(document.getString(getString(R.string.firebase_collection_schedule_field_overs_team_b)));
                    } else {
                        Toast.makeText(getContext(), "Error getting details", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            update.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DocumentReference reference = db.collection(getString(R.string.firebase_collection_schedule)).document(mDocumentId);

                    if (statusChipGroup.getCheckedChipId() == R.id.match_status_chip_live)
                        reference.update(getString(R.string.firebase_collection_schedule_field_matchStatus), MATCH_IN_PROGRESS);
                    else if (statusChipGroup.getCheckedChipId() == R.id.match_status_chip_complete)
                        reference.update(getString(R.string.firebase_collection_schedule_field_matchStatus), MATCH_COMPLETED);
                    else
                        reference.update(getString(R.string.firebase_collection_schedule_field_matchStatus), MATCH_NOT_STARTED);

                    reference.update(getString(R.string.firebase_collection_schedule_field_name_team_a), teamAName.getText().toString());
                    reference.update(getString(R.string.firebase_collection_schedule_field_name_team_b), teamBName.getText().toString());
                    reference.update(getString(R.string.firebase_collection_schedule_field_overs_team_a), teamAOvers.getText().toString());
                    reference.update(getString(R.string.firebase_collection_schedule_field_overs_team_b), teamBOvers.getText().toString());
                    reference.update(getString(R.string.firebase_collection_schedule_field_score_team_a), Long.parseLong(teamAScore.getText().toString()));
                    reference.update(getString(R.string.firebase_collection_schedule_field_score_team_b), Long.parseLong(teamBScore.getText().toString()))
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(getContext(), "Updated Successfully", Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            });
            return root;

        } else if (getSportType() == SPORT_TYPE_THREE) {

            View root = inflater.inflate(R.layout.fragment_update_score_two, container, false);

            statusChipGroup = root.findViewById(R.id.match_status_chip_group);
            matchStatusScheduled = root.findViewById(R.id.match_status_chip_scheduled);
            matchStatusLive = root.findViewById(R.id.match_status_chip_live);
            matchStatusComplete = root.findViewById(R.id.match_status_chip_complete);
            teamAName = root.findViewById(R.id.update_score_team_a_name_edit_text);
            teamBName = root.findViewById(R.id.update_score_team_b_name_edit_text);
            currentSet = root.findViewById(R.id.update_current_set_edit_text);
            setOneWinner = root.findViewById(R.id.update_set_one_winner_edit_text);
            setTwoWinner = root.findViewById(R.id.update_set_two_winner_edit_text);
            setThreeWinner = root.findViewById(R.id.update_set_three_winner_edit_text);
            combinedScore = root.findViewById(R.id.update_combined_score_team_edit_text);
            update = root.findViewById(R.id.update_scores_button);

            db.collection(getString(R.string.firebase_collection_schedule)).document(mDocumentId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        long matchStatus = document.getLong(getString(R.string.firebase_collection_schedule_field_matchStatus));
                        if (matchStatus == MATCH_IN_PROGRESS)
                            matchStatusLive.setChecked(true);
                        else if (matchStatus == MATCH_COMPLETED)
                            matchStatusComplete.setChecked(true);
                        else
                            matchStatusScheduled.setChecked(true);
                        long currentSetNumber = document.getLong(getString(R.string.firebase_collection_schedule_field_currentSet));
                        teamAName.setText(document.getString(getString(R.string.firebase_collection_schedule_field_name_team_a)));
                        teamBName.setText(document.getString(getString(R.string.firebase_collection_schedule_field_name_team_b)));
                        currentSet.setText(Long.toString(currentSetNumber));
                        setOneWinner.setText(document.getString(getString(R.string.firebase_collection_schedule_field_setOneWinner)));
                        setTwoWinner.setText(document.getString(getString(R.string.firebase_collection_schedule_field_setTwoWinner)));
                        setThreeWinner.setText(document.getString(getString(R.string.firebase_collection_schedule_field_setThreeWinner)));
                        if (currentSetNumber == 2L)
                            combinedScore.setText(document.getString(getString(R.string.firebase_collection_schedule_field_setTwoScore)));
                        else if (currentSetNumber == 3L)
                            combinedScore.setText(document.getString(getString(R.string.firebase_collection_schedule_field_setThreeScore)));
                        else
                            combinedScore.setText(document.getString(getString(R.string.firebase_collection_schedule_field_setOneScore)));
                    } else {
                        Toast.makeText(getContext(), "Error getting details", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            update.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DocumentReference reference = db.collection(getString(R.string.firebase_collection_schedule)).document(mDocumentId);

                    if (statusChipGroup.getCheckedChipId() == R.id.match_status_chip_live)
                        reference.update(getString(R.string.firebase_collection_schedule_field_matchStatus), MATCH_IN_PROGRESS);
                    else if (statusChipGroup.getCheckedChipId() == R.id.match_status_chip_complete)
                        reference.update(getString(R.string.firebase_collection_schedule_field_matchStatus), MATCH_COMPLETED);
                    else
                        reference.update(getString(R.string.firebase_collection_schedule_field_matchStatus), MATCH_NOT_STARTED);

                    reference.update(getString(R.string.firebase_collection_schedule_field_name_team_a), teamAName.getText().toString());
                    reference.update(getString(R.string.firebase_collection_schedule_field_name_team_b), teamBName.getText().toString());
                    reference.update(getString(R.string.firebase_collection_schedule_field_currentSet), Long.parseLong(currentSet.getText().toString()));
                    reference.update(getString(R.string.firebase_collection_schedule_field_setOneWinner), setOneWinner.getText().toString());
                    reference.update(getString(R.string.firebase_collection_schedule_field_setTwoWinner), setTwoWinner.getText().toString());
                    reference.update(getString(R.string.firebase_collection_schedule_field_setThreeWinner), setThreeWinner.getText().toString());

                    long currentSetNumber = Long.parseLong(currentSet.getText().toString());
                    if (currentSetNumber == 2L)
                        reference.update(getString(R.string.firebase_collection_schedule_field_setTwoScore), combinedScore.getText().toString());
                    else if (currentSetNumber == 3L)
                        reference.update(getString(R.string.firebase_collection_schedule_field_setThreeScore), combinedScore.getText().toString());
                    else
                        reference.update(getString(R.string.firebase_collection_schedule_field_setOneScore), combinedScore.getText().toString())
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(getContext(), "Updated Successfully", Toast.LENGTH_SHORT).show();
                                    }
                                });
                }
            });
            return root;
        } else {
            View root = inflater.inflate(R.layout.fragment_update_score_four, container, false);

            statusChipGroup = root.findViewById(R.id.match_status_chip_group);
            matchStatusScheduled = root.findViewById(R.id.match_status_chip_scheduled);
            matchStatusLive = root.findViewById(R.id.match_status_chip_live);
            matchStatusComplete = root.findViewById(R.id.match_status_chip_complete);
            teamAName = root.findViewById(R.id.update_score_team_a_name_edit_text);
            teamBName = root.findViewById(R.id.update_score_team_b_name_edit_text);
            winner = root.findViewById(R.id.update_score_winner_edit_text);
            update = root.findViewById(R.id.update_scores_button);

            db.collection(getString(R.string.firebase_collection_schedule)).document(mDocumentId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        long matchStatus = document.getLong(getString(R.string.firebase_collection_schedule_field_matchStatus));
                        if (matchStatus == MATCH_IN_PROGRESS)
                            matchStatusLive.setChecked(true);
                        else if (matchStatus == MATCH_COMPLETED)
                            matchStatusComplete.setChecked(true);
                        else
                            matchStatusScheduled.setChecked(true);
                        teamAName.setText(document.getString(getString(R.string.firebase_collection_schedule_field_name_team_a)));
                        teamBName.setText(document.getString(getString(R.string.firebase_collection_schedule_field_name_team_b)));
                        winner.setText(document.getString(getString(R.string.firebase_collection_schedule_field_winner)));

                    } else {
                        Toast.makeText(getContext(), "Error getting details", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            update.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DocumentReference reference = db.collection(getString(R.string.firebase_collection_schedule)).document(mDocumentId);

                    if (statusChipGroup.getCheckedChipId() == R.id.match_status_chip_live)
                        reference.update(getString(R.string.firebase_collection_schedule_field_matchStatus), MATCH_IN_PROGRESS);
                    else if (statusChipGroup.getCheckedChipId() == R.id.match_status_chip_complete)
                        reference.update(getString(R.string.firebase_collection_schedule_field_matchStatus), MATCH_COMPLETED);
                    else
                        reference.update(getString(R.string.firebase_collection_schedule_field_matchStatus), MATCH_NOT_STARTED);

                    reference.update(getString(R.string.firebase_collection_schedule_field_name_team_a), teamAName.getText().toString());
                    reference.update(getString(R.string.firebase_collection_schedule_field_name_team_b), teamBName.getText().toString());
                    reference.update(getString(R.string.firebase_collection_schedule_field_winner), winner.getText().toString());
                }
            });

            return root;
        }
    }

}
