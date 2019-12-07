package com.techbatu;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


public class Syllabus extends AppCompatActivity implements View.OnClickListener {

    private Button bt_m1,bt_m2,bt_chemistry,bt_mechanics,bt_cp,bt_workshop,bt_electrical_electronics,bt_cpL,bt_chemistryL,bt_mechanicsL;
    private Button bt_physics,bt_graphics,bt_cs,bt_eee,bt_civil_mechanical,bt_physicsL,bt_graphicsL,bt_csL;
    private String subname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_syllabus);
        initvariables();
        initListners();
    }

    private void initListners() {
        bt_m1.setOnClickListener(this);
        bt_m2.setOnClickListener(this);
        bt_chemistry.setOnClickListener(this);
        bt_mechanics.setOnClickListener(this);
        bt_cp.setOnClickListener(this);
        bt_workshop.setOnClickListener(this);
        bt_electrical_electronics.setOnClickListener(this);
        bt_cpL.setOnClickListener(this);
        bt_chemistry.setOnClickListener(this);
        bt_chemistryL.setOnClickListener(this);
        bt_mechanicsL.setOnClickListener(this);

        bt_physics.setOnClickListener(this);
        bt_graphics.setOnClickListener(this);
        bt_cs.setOnClickListener(this);
        bt_eee.setOnClickListener(this);
        bt_civil_mechanical.setOnClickListener(this);
        bt_physicsL.setOnClickListener(this);
        bt_graphicsL.setOnClickListener(this);
        bt_csL.setOnClickListener(this);

    }

    private void initvariables() {
        bt_m1 = findViewById(R.id.bt_m1);
        bt_m2 = findViewById(R.id.bt_m2);
        bt_chemistry = findViewById(R.id.bt_chemistry);
        bt_mechanics = findViewById(R.id.bt_mechanics);
        bt_cp = findViewById(R.id.bt_cp);
        bt_workshop = findViewById(R.id.bt_workshop);
        bt_electrical_electronics = findViewById(R.id.bt_electrical_electronics);
        bt_cpL = findViewById(R.id.bt_cpL);
        bt_chemistryL = findViewById(R.id.bt_chemistryL);
        bt_mechanicsL = findViewById(R.id.bt_mechanicsL);

        bt_physics = findViewById(R.id.bt_physics);
        bt_graphics = findViewById(R.id.bt_graphics);
        bt_cs = findViewById(R.id.bt_cs);
        bt_eee = findViewById(R.id.bt_eee);
        bt_civil_mechanical = findViewById(R.id.bt_civil_mechanical);
        bt_physicsL = findViewById(R.id.bt_physicsL);
        bt_graphicsL = findViewById(R.id.bt_graphicsL);
        bt_csL = findViewById(R.id.bt_csL);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch(id)
        {
            case R.id.bt_m1:
                subname = "enggmath1";
                createSharedPref();
                gotoLogin();
                break;

            case R.id.bt_m2:
                subname = "enggmath2";
                createSharedPref();
                gotoLogin();
                break;

            case R.id.bt_chemistry:
                subname = "enggchemistry";
                createSharedPref();
                gotoLogin();
                break;

            case R.id.bt_mechanics:
                subname = "enggmechanics";
                createSharedPref();
                gotoLogin();
                break;

            case R.id.bt_cp:
                subname = "compprogramc";
                createSharedPref();
                gotoLogin();
                break;

            case R.id.bt_workshop:
                subname = "workshop";
                createSharedPref();
                gotoLogin();
                break;

            case R.id.bt_electrical_electronics:
                subname = "electextcengg";
                createSharedPref();
                gotoLogin();
                break;

            case R.id.bt_cpL:
                subname = "compprogramclab";
                createSharedPref();
                gotoLogin();
                break;

            case R.id.bt_chemistryL:
                subname = "enggchemistrylab";
                createSharedPref();
                gotoLogin();
                break;

            case R.id.bt_mechanicsL:
                subname = "enggmechanicslab";
                createSharedPref();
                gotoLogin();
                break;

            case R.id.bt_physics:
                subname = "enggphysics";
                createSharedPref();
                gotoLogin();
                break;

            case R.id.bt_graphics:
                subname = "engggraphics";
                createSharedPref();
                gotoLogin();
                break;

            case R.id.bt_cs:
                subname = "communicationskills";
                createSharedPref();
                gotoLogin();
                break;

            case R.id.bt_eee:
                subname = "energyenvironmentengg";
                createSharedPref();
                gotoLogin();
                break;

            case R.id.bt_civil_mechanical:
                subname = "civilmechengg";
                createSharedPref();
                gotoLogin();
                break;

            case R.id.bt_physicsL:
                subname = "enggphysicslab";
                createSharedPref();
                gotoLogin();
                break;

            case R.id.bt_graphicsL:
                subname = "engggraphicslab";
                createSharedPref();
                gotoLogin();
                break;

            case R.id.bt_csL:
                subname = "communicationskillslab";
                createSharedPref();
                gotoLogin();
                break;



            default:
                Toast.makeText(Syllabus.this, "Something went wrong", Toast.LENGTH_SHORT).show();
        }


    }

    private void gotoLogin() {
        startActivity(new Intent(Syllabus.this,viewSyllabus.class));
    }

    private void createSharedPref() {
        SharedPreferences sharedPreferences = getSharedPreferences("basic_student_info",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("subname",subname);
        editor.commit();
    }
}
