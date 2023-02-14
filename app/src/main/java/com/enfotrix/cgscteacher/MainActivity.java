package com.enfotrix.cgscteacher;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.MenuItem;

import com.enfotrix.cgscteacher.fragments.HomeFragment;
import com.enfotrix.cgscteacher.fragments.NotificationsFragment;
import com.enfotrix.cgscteacher.fragments.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        navigationView = findViewById(R.id.navigation);
        navigationView.setOnNavigationItemSelectedListener(selectedListener);


        loadFragment(new HomeFragment());


    }

    private BottomNavigationView.OnNavigationItemSelectedListener selectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            switch (menuItem.getItemId()) {

                case R.id.nav_home:
                    loadFragment(new HomeFragment());
                    return true;


                case R.id.nav_profile:

                    loadFragment(new ProfileFragment());
                    return true;


                case R.id.nav_noti:

                    loadFragment(new NotificationsFragment());
                    return true;

            }
            return false;
        }
    };

    public void loadFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.content, fragment, "");
        fragmentTransaction.commit();
    }


}