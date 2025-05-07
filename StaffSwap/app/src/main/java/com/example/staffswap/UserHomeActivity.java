package com.example.staffswap;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.staffswap.navigations.AddNoteFragment;
import com.example.staffswap.navigations.DashboardFragment;
import com.example.staffswap.navigations.RequestLeaveFragment;
import com.example.staffswap.navigations.TimeTableFragment;
import com.google.android.material.navigation.NavigationView;

import androidx.appcompat.widget.Toolbar;

public class UserHomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_home);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.drawerLayout1), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });



        DrawerLayout drawerLayout1 = findViewById(R.id.drawerLayout1);
        NavigationView navigationView1 = findViewById(R.id.navigationView1);
        Toolbar toolbar =  findViewById(R.id.toolbar1);
//        setSupportActionBar(toolbar);

        loadFragment(new DashboardFragment());
        toolbar.setTitle("Dashboard");


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout1.openDrawer(navigationView1);
            }
        });


        navigationView1.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment = null;
                String title = "";

                int id = item.getItemId();

                if (id == R.id.menu_dashboard) {
                    fragment = new DashboardFragment();
                    title = "Dashboard";
                } else if (id == R.id.menu_req_leave) {
                    fragment = new RequestLeaveFragment();
                    title = "Request Leave";
                } else if (id == R.id.menu_time_table) {
                    fragment = new TimeTableFragment();
                    title = "Time Table";
                } else if (id == R.id.menu_note) {
                    fragment = new AddNoteFragment();
                    title = "Add Note";
                }

                if (fragment != null) {
                    loadFragment(fragment);
                    toolbar.setTitle(title);
                }

                drawerLayout1.closeDrawers();
                return true;
            }
        });





    }


    private void loadFragment(Fragment fragment){

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container_view1, fragment,null)
                .setReorderingAllowed(true)
                .commit();
    }
}