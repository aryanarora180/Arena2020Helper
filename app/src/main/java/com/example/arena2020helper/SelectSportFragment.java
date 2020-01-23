package com.example.arena2020helper;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class SelectSportFragment extends Fragment {

    public static final int MODE_ADD_SPORT_EVENT = 2100;
    public static final int MODE_UPDATE_SCORES = 2101;

    public SelectSportFragment(int mode) {
        mMode = mode;
    }

    private int mMode;

    RecyclerView recycler;

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

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_select_sport, container, false);

        if(mMode == MODE_ADD_SPORT_EVENT) {
            getActivity().setTitle(getString(R.string.select_sport_add_event));
        }
        else {
            getActivity().setTitle(getString(R.string.select_sport_update_score));
        }

        recycler = root.findViewById(R.id.select_sport_recycler);
        SportAdapter adapter = new SportAdapter();
        recycler.setAdapter(adapter);
        recycler.setLayoutManager(new LinearLayoutManager(getContext()));

        return root;
    }

    private class SportAdapter extends RecyclerView.Adapter<SportAdapter.ViewHolder> {

        SportAdapter() {}

        private String[] sports = {"CRICKET", "FOOTBALL", "VOLLEYBALL", "BASKETBALL", "HOCKEY", "BADMINTON", "TENNIS", "CHESS", "CARROM", "SQUASH", "TABLE TENNIS", "SNOOKER", "THROWBALL", "DUATHLON", "BODY BUIDING", "POWER LIFTING", "FRISBEE" };

        private long getSportCode(int position) {
            switch(position) {
                case 0:
                    return SPORT_CRICKET;
                case 1:
                    return SPORT_FOOTBALL;
                case 2:
                    return SPORT_VOLLEYBALL;
                case 3:
                    return SPORT_BASKETBALL;
                case 4:
                    return SPORT_HOCKEY;
                case 5:
                    return SPORT_BADMINTON;
                case 6:
                    return SPORT_TENNIS;
                case 7:
                    return SPORT_CHESS;
                case 8:
                    return SPORT_CARROM;
                case 9:
                    return SPORT_SQUASH;
                case 10:
                    return SPORT_TABLE_TENNIS;
                case 11:
                    return SPORT_SNOOKER;
                case 12:
                    return SPORT_THROWBALL;
                case 13:
                    return SPORT_DUALTHON;
                case 14:
                    return SPORT_BODY_BUILDING;
                case 15:
                    return SPORT_POWER_LIFTING;
                case 16:
                    return SPORT_FRISBEE;
                default:
                    return 0;
            }
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView sport;
            public ViewHolder(View itemView) {
                super(itemView);
                sport = itemView.findViewById(R.id.sport);
            }
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
            final TextView sport = holder.sport;
            sport.setText(sports[position]);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mMode == MODE_ADD_SPORT_EVENT) {
                        getFragmentManager().beginTransaction().replace(R.id.frame_container, new AddSportEventFragment(getSportCode(position))).commit();
                    }
                    else {
                        getFragmentManager().beginTransaction().replace(R.id.frame_container, new SelectEventForUpdateFragment(getSportCode(position))).commit();
                    }
                }
            });

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
        public int getItemCount() {
            return sports.length;
        }
    }

}