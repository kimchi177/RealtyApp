package com.example.thu.realtysocial.Profile.EditProfile;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.Toast;

import com.example.thu.realtysocial.Profile.EditProfileActivity;
import com.example.thu.realtysocial.R;
import com.example.thu.realtysocial.activity.CalendarActivity;
import com.example.thu.realtysocial.activity.RegisterActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EditBrithdayActivity extends AppCompatActivity {
    private static final String EditBrithdayActivity = "EditBrithdayActivity";
    @BindView(R.id.eb_calendar)
    CalendarView calendarView;
    @BindView(R.id.eb_btn_cancel)
    Button eb_btn_cancel;
    @BindView(R.id.eb_btn_save)
    Button eb_btn_save;

    String date="";
    ProgressDialog loadProgressDialog;

    //Firebase
    DatabaseReference statusDatabaseReference;
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_brithday);
        ButterKnife.bind(this);
        InitFirebase();
        Init();
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                 date = (month + 1) + "/" + dayOfMonth + "/" + year;
//                Intent dateIntent=new Intent(EditBrithdayActivity.this,EditProfileActivity.class);
//                dateIntent.putExtra("date",date);
//                startActivity(dateIntent);
//                finish();
            }
        });
    }

    @OnClick(R.id.eb_btn_save)
    public void ClickSave() {
        Log.d(EditBrithdayActivity, "EditBrithdayActivity");
        eb_btn_cancel.setBackgroundResource(R.color.edit_sex_background);
        eb_btn_save.setBackgroundResource(R.color.colortextProA);
        ChanceSexate(date);
    }

    private void ChanceSexate(final String date) {
        if (date.equals("")) {
            Toast.makeText(this, "please wait your check", Toast.LENGTH_SHORT).show();
        } else {
            loadProgressDialog.setTitle("chance profile brithday");
            loadProgressDialog.setMessage("plase wait, we are updating your brithday.....");
            loadProgressDialog.show();
            statusDatabaseReference.child("briday").setValue(date).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        loadProgressDialog.dismiss();
                        Toast.makeText(EditBrithdayActivity.this, "Profile brithday update susscessfull.", Toast.LENGTH_SHORT).show();
                        Intent sexIntent = new Intent(
                                EditBrithdayActivity.this, EditProfileActivity.class
                        );
//                        sexIntent.putExtra("date",date);
                        startActivity(sexIntent);
                        finish();
                    } else
                        Toast.makeText(EditBrithdayActivity.this, "what is reason ? you can't update. sorry!!!", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @OnClick(R.id.eb_btn_cancel)
    public void ClickCancel() {
        Log.d(EditBrithdayActivity, "EditBrithdayActivity");
        eb_btn_save.setBackgroundResource(R.color.edit_sex_background);
        eb_btn_cancel.setBackgroundResource(R.color.colortextProA);
        finish();
    }
    private void Init() {
        eb_btn_save.setBackgroundResource(R.color.colortextProA);
        loadProgressDialog = new ProgressDialog(this);
    }
    private void InitFirebase() {
        mAuth = FirebaseAuth.getInstance();
        String U_id = mAuth.getCurrentUser().getUid();
        statusDatabaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(U_id);
    }
}
