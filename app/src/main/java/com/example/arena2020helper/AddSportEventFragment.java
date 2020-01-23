package com.example.arena2020helper;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class AddSportEventFragment extends Fragment {

    public AddSportEventFragment(long sportCode) {
        mSportCode = sportCode;
    }

    private FirebaseFirestore db;

    private long mSportCode;

    TextView teamAName;
    TextView teamBName;
    TextView dateAndTime;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_add_sport_event, container, false);

        getActivity().setTitle(getString(R.string.add_sports_event));

        db = FirebaseFirestore.getInstance();

        teamAName = root.findViewById(R.id.add_sport_add_team_a_edit_text);
        teamBName = root.findViewById(R.id.add_sport_add_team_b_edit_text);
        dateAndTime = root.findViewById(R.id.add_sport_add_date_edit_text);
        Button addSportButton = root.findViewById(R.id.add_event_button);

        addSportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Object> data = new HashMap<>();
                data.put(getString(R.string.firebase_collection_schedule_field_date), getDate(dateAndTime.getText().toString()));
                data.put(getString(R.string.firebase_collection_schedule_field_matchStatus), MATCH_NOT_STARTED);
                data.put(getString(R.string.firebase_collection_schedule_field_sportCode), mSportCode);
                data.put(getString(R.string.firebase_collection_schedule_field_name_team_a), teamAName.getText().toString());
                data.put(getString(R.string.firebase_collection_schedule_field_name_team_b), teamBName.getText().toString());
                data.put("winner", "-");

                if(getSportType()==SPORT_TYPE_ONE) {
                    data.put(getString(R.string.firebase_collection_schedule_field_score_team_a), 0L);
                    data.put(getString(R.string.firebase_collection_schedule_field_score_team_b), 0L);
                }
                else if(getSportType()==SPORT_TYPE_TWO) {
                    data.put(getString(R.string.firebase_collection_schedule_field_score_team_a), 0L);
                    data.put(getString(R.string.firebase_collection_schedule_field_score_team_b), 0L);
                    data.put(getString(R.string.firebase_collection_schedule_field_overs_team_a), "0.0");
                    data.put(getString(R.string.firebase_collection_schedule_field_overs_team_b), "0.0");
                }
                else if(getSportType()==SPORT_TYPE_THREE) {
                    data.put(getString(R.string.firebase_collection_schedule_field_currentSet), 1L);
                    data.put(getString(R.string.firebase_collection_schedule_field_setOneScore), "0-0");
                    data.put(getString(R.string.firebase_collection_schedule_field_setTwoScore), "0-0");
                    data.put(getString(R.string.firebase_collection_schedule_field_setThreeScore), "0-0");
                    data.put(getString(R.string.firebase_collection_schedule_field_setOneWinner), "-");
                    data.put(getString(R.string.firebase_collection_schedule_field_setTwoWinner), "-");
                    data.put(getString(R.string.firebase_collection_schedule_field_setThreeWinner), "-");
                }

                db.collection(getString(R.string.firebase_collection_schedule))
                        .add(data)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Toast.makeText(getContext(), getString(R.string.add_event_success), Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getContext(), getString(R.string.add_event_failed), Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
        return root;

    }

    private Date getDate(String date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yy hh:mm");
        try {
            return simpleDateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return new Date();
        }
    }

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
        if (mSportCode == SPORT_FOOTBALL || mSportCode == SPORT_VOLLEYBALL || mSportCode == SPORT_BASKETBALL || mSportCode == SPORT_HOCKEY || mSportCode == SPORT_THROWBALL)
            return SPORT_TYPE_ONE;
        else if (mSportCode == SPORT_CRICKET)
            return SPORT_TYPE_TWO;
        else if (mSportCode == SPORT_TENNIS || mSportCode == SPORT_BADMINTON || mSportCode == SPORT_SQUASH || mSportCode == SPORT_TABLE_TENNIS)
            return SPORT_TYPE_THREE;
        else return SPORT_TYPE_NO_LIVE;
    }

}
