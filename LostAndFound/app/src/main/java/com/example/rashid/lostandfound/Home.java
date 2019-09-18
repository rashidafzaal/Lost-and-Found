package com.example.rashid.lostandfound;

import android.app.SearchManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import androidx.core.view.GravityCompat;
import androidx.core.view.MenuItemCompat;
import androidx.viewpager.widget.ViewPager;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.rashid.lostandfound.PostThings.FoundItem;
import com.example.rashid.lostandfound.PostThings.LostItem;
import com.example.rashid.lostandfound.Settings.ProfileSettings;
import com.example.rashid.lostandfound.Start.MainActivity;
import com.example.rashid.lostandfound.Start.ViewPagerAdapter;

import java.util.Locale;



public class Home extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private ViewPager viewPager;
    private DrawerLayout drawer;
    private TabLayout tabLayout;
    private String[] pageTitle = {"LOST", "FOUND"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        viewPager = (ViewPager) findViewById(R.id.view_pager);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        drawer = (DrawerLayout) findViewById(R.id.drawerLayout);

        setSupportActionBar(toolbar);

        //create default navigation drawer toggle
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.app_name, R.string.app_name);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        //setting Tab layout (number of Tabs = number of ViewPager pages)
        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        for (int i = 0; i < 2; i++) {
            tabLayout.addTab(tabLayout.newTab().setText(pageTitle[i]));
        }

        //set gravity for tab bar
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        //handling navigation view item event
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //set viewpager adapter
        ViewPagerAdapter pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);

        //change Tab selection when swipe ViewPager
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        //change ViewPager page when tab is Clicked
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    } //end of onCreate


    public void OnPress_LostBtn(View view)
    {
        Intent i = new Intent(Home.this, LostItem.class);
        startActivity(i);
    }

    public void OnPress_FoundBtn(View view)
    {
        Intent i = new Intent(Home.this, FoundItem.class);
        startActivity(i);
    }


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.drawer_homepage)
        {
            drawer.closeDrawer(GravityCompat.START);
        }
        else if (id == R.id.drawer_settings)
        {
            Intent i = new Intent(Home.this, ProfileSettings.class);
            startActivity(i);
        }
        else if (id == R.id.drawer_logout)
        {
            final AlertDialog d = new AlertDialog.Builder(Home.this).create();
            d.setTitle("Sign Out");
            d.setMessage("Do You Want to Sign Out?");

            d.setButton(d.BUTTON_POSITIVE, "YES", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i)
                {
                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(Home.this);
                    prefs.edit().clear().commit();

                    Intent i2 = new Intent(Home.this, MainActivity.class);
                    startActivity(i2);

                }
            });
            d.setButton(d.BUTTON_NEGATIVE, "NO", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    d.dismiss();
                }
            });
            d.show();
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu1, menu);
        // Retrieve the SearchView and plug it into SearchManager
        MenuItem iSearch = menu.findItem(R.id.action_search);

        SearchManager searchManager = (SearchManager) Home.this.getSystemService(SEARCH_SERVICE);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(iSearch);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(Home.this.getComponentName()));

        // SearchView Listener
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                String text = searchView.getQuery().toString().toLowerCase(Locale.getDefault());
                Fragment1.adapter.filter(text);
                Fragment2.adapter.filter(text);
                return true;
            }
        });

        return true;
    }

    @Override
    public void onBackPressed() {
        assert drawer != null;
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


}
