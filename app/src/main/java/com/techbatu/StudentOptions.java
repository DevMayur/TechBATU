package com.techbatu;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.techbatu.timeline.NewsFeed;

import java.util.ArrayList;

public class StudentOptions extends AppCompatActivity implements View.OnClickListener {

    private Spinner sp_college_lsit,sp_department,sp_semester;
    private Button bt_start;
    private int status;
    private String college,dept,sem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_options);

        initvariables();
        initListners();

        ArrayAdapter<String> collegeAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.college_list));
        collegeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_college_lsit.setAdapter(collegeAdapter);

        ArrayAdapter<String> deptAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.department_list));
        deptAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_department.setAdapter(deptAdapter);

        ArrayAdapter<String> semAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.semester_list));
        semAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_semester.setAdapter(semAdapter);

        SharedPreferences LoginStatus = getSharedPreferences("status",MODE_PRIVATE);
        String status = LoginStatus.getString("status","null");
        if(status.equals("true"))
        {
            startActivity(new Intent(this,Dashboard.class));
        }


    }

    private void initListners() {

        bt_start.setOnClickListener(this);

    }

    private void initvariables() {
        sp_college_lsit = findViewById(R.id.sp_college_list);
        sp_department = findViewById(R.id.sp_department);
        sp_semester = findViewById(R.id.sp_semester);
        bt_start = findViewById(R.id.bt_start);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        status = 0;
        switch (id)
        {
            case R.id.bt_start:
                checkFields();
                if(status != 1)
            {

                startActivity(new Intent(StudentOptions.this,Dashboard.class));

                storeToSharedPrefs();

                break;
            }
            else
                {
                    Toast.makeText(StudentOptions.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                }
        }
    }

    private void storeToSharedPrefs() {

        SharedPreferences sharedPreferences = getSharedPreferences("basic_student_info",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("college",college);
        editor.putString("department",dept);
        editor.putString("semester",sem);
        editor.commit();
    }

    private void checkFields() {

        int collegeposition = sp_college_lsit.getSelectedItemPosition();

        switch(collegeposition)
        {
            case 0:
                status = 1;
            case 1:
                college = "dbatu";
                break;
            default:
                status = 1;
        }

        int deptposition = sp_department.getSelectedItemPosition();

        switch(deptposition)
        {
            case 0:
                status = 1;
                break;

            case 1:
                dept ="mech";
                break;

            case 2:
                dept = "comp";
                break;

            case 3:
                dept = "civil";
                break;

            case 4:
                dept = "electrical";
                break;

            case 5:
                dept = "it";
                break;

            case 6:
                dept = "extc";
                break;

            case 7:
                dept = "chem";
                break;

            case 8:
                dept = "petro";
                break;
            default:
                status = 1;

        }

        int semposition = sp_semester.getSelectedItemPosition();
        switch(semposition)
        {
            case 0 :
                status = 1;

            case 1:
                sem = "fy";
                break;

            case 2:
                sem = "fy";
                break;
            case 3:
                sem = "3";
                break;
            case 4:
                sem = "4";
                break;
            case 5:
                sem = "5";
                break;
            case 6:
                sem = "6";
                break;
            case 7:
                sem = "7";
                break;
            case 8:
                sem = "8";
                break;
            default:
                status = 1;

        }

    }
}
