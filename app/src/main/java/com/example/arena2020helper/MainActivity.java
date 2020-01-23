package com.example.arena2020helper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        putFragment(new AddAnnouncementFragment());

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.add_announcement_menu_item:
                        putFragment(new AddAnnouncementFragment());
                        return true;
                    case R.id.update_live_scores_menu_item:
                        putFragment(new SelectSportFragment(SelectSportFragment.MODE_UPDATE_SCORES));
                        return true;
                    case R.id.add_sports_event:
                        putFragment(new SelectSportFragment(SelectSportFragment.MODE_ADD_SPORT_EVENT));
                        return true;
                }
                return false;
            }
        });

    }

    private void putFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, fragment).commit();
    }

}
