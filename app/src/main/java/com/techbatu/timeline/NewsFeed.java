package com.techbatu.timeline;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


import com.techbatu.Dashboard;
import com.techbatu.LoginPage;
import com.techbatu.R;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class NewsFeed extends AppCompatActivity {

    private BottomNavigationView mainBottomNav;
    private Toolbar news_feed_toolbar;
    private HomeFragment homeFragment;
    private notification notificationFragment;
    private QnA qnA;

    private AdView mAdView;

    @Override
    protected void onStart() {
        super.onStart();

        replaceFragments(homeFragment);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MobileAds.initialize(this, "ca-app-pub-8579970963670342~2634189437");

        mAdView = findViewById(R.id.adViewnewsfeed);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        getMenuInflater().inflate(R.menu.main_menu,menu);

        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId())
        {


        }

        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_feed);


        SharedPreferences sharedPreferences = getSharedPreferences("basic_student_info",MODE_PRIVATE);
        String prn = sharedPreferences.getString("prn","");

        Intent intent = new Intent(this,BlogRecyclerAdapter.class);
        intent.putExtra("prn",prn);

        SharedPreferences LoginStatus = getSharedPreferences("status",MODE_PRIVATE);
        String status = LoginStatus.getString("status","null");
        if(status.equals("false"))
        {
            startActivity(new Intent(this,LoginPage.class));
        }

        news_feed_toolbar = findViewById(R.id.news_feed_toolbar);
        setSupportActionBar(news_feed_toolbar);
        mainBottomNav = findViewById(R.id.mainBottomNav);

        news_feed_toolbar.setVisibility(View.GONE);
        getSupportActionBar().setTitle("DBATU");

        homeFragment = new HomeFragment();
        notificationFragment = new notification();
        qnA  = new QnA();

        mainBottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch(menuItem.getItemId())
                {
                    case R.id.bt_home:

                        replaceFragments(homeFragment);
                        return true;

                    case R.id.qna_main_menu:

                        replaceFragments(qnA);

                        return true;

                    case R.id.bt_notification:
                        replaceFragments(notificationFragment);
                        return true;


                        default:
                            return false;
                }

            }
        });

    }


        private void replaceFragments(Fragment fragment)
        {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.main_container,fragment);
            fragmentTransaction.commit();
        }
}
