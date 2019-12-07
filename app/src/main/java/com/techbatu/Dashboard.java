package com.techbatu;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.techbatu.timeline.GetDetails;
import com.techbatu.timeline.NewsFeed;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.techbatu.timeline.ProfilePage;

public class Dashboard extends Activity implements View.OnClickListener {
    private Button bt_syllabus,bt_sign_in,bt_website,bt_formfilling,bt_logout,bt_profile;
    private String semester,dept;
    private AdView mAdView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        SharedPreferences sharedPreferences = getSharedPreferences("basic_student_info",MODE_PRIVATE);
        semester = sharedPreferences.getString("semester","");
        dept = sharedPreferences.getString("department","");

        MobileAds.initialize(this, "ca-app-pub-8579970963670342~2634189437");

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        initvariables();

        initlistners();

    }

    private void initvariables() {

        bt_logout = findViewById(R.id.bt_logout);
        bt_formfilling = findViewById(R.id.bt_formfilling);
        bt_syllabus = findViewById(R.id.bt_syllabus);
        bt_sign_in = findViewById(R.id.bt_sign_in);
        bt_website = findViewById(R.id.bt_website);
        bt_profile = findViewById(R.id.bt_profile_dashboard);
    }

    private void initlistners() {

        bt_logout.setOnClickListener(this);
        bt_formfilling.setOnClickListener(this);
        bt_syllabus.setOnClickListener(this);
        bt_sign_in.setOnClickListener(this);
        bt_website.setOnClickListener(this);
        bt_profile.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch(id)
        {
            case R.id.bt_syllabus:
               //start PDF syllabus page
                break;

            case R.id.bt_website:

                startActivity(new Intent(this,WebsiteCollege.class));

                break;

            case R.id.bt_sign_in:
                SharedPreferences LoginStatus = getSharedPreferences("status",MODE_PRIVATE);
                String status = LoginStatus.getString("status","null");
                if(status.equals("true"))
                {
                    startActivity(new Intent(this,NewsFeed.class));
                }
                else
                {
                    startActivity(new Intent(this,LoginPage.class));
                }
                break;

            case R.id.bt_logout:
                SharedPreferences sharedPreferences = getSharedPreferences("status",MODE_PRIVATE);
                SharedPreferences.Editor statouseditor = sharedPreferences.edit();
                statouseditor.putString("status","false");
                statouseditor.commit();
                startActivity(new Intent(this,MainActivity.class));
                break;

            case R.id.bt_formfilling:
                startActivity(new Intent(Dashboard.this,FormFilling.class));
                break;

            case R.id.bt_profile_dashboard:
                startActivity(new Intent(this,ProfilePage.class));
                break;

        }
    }







}
