package com.example.thu.realtysocial.Profile;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.thu.realtysocial.Profile.EditProfile.EditAdressActivity;
import com.example.thu.realtysocial.Profile.EditProfile.EditBrithdayActivity;
import com.example.thu.realtysocial.Profile.EditProfile.EditSexActivity;
import com.example.thu.realtysocial.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EditProfileActivity extends AppCompatActivity {
    private static  final  String EditProfileActivity="EditProfileActivity";
    @BindView(R.id.baseinfo)
    RelativeLayout baseinfo;
    @BindView(R.id.baseinfo_bri)
    RelativeLayout baseinfo_bri;
    @BindView(R.id.baseinfo_live)
    RelativeLayout baseinfo_live;

    @BindView(R.id.edit_txtsex_intent)
    TextView edit_txtsex_intent;
    @BindView(R.id.edit_txtbriday_intent)
    TextView edit_txtbriday_intent;
    @BindView(R.id.edit_txtlive_intent)
    TextView edit_txtlive_intent;

    private String sexIntent,brithdayIntent,liveIntent;
    //    firebase
    DatabaseReference firebaseDatabase;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        ButterKnife.bind(this);
        InitFirebase();
        LoadDatatoFirebase();
    }

    private void InitFirebase() {
        firebaseAuth = FirebaseAuth.getInstance();
        String online_user_id = firebaseAuth.getCurrentUser().getUid();
        firebaseDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(online_user_id);
        firebaseDatabase.keepSynced(true);
    }

    private void LoadDatatoFirebase() {
        firebaseDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String sex = dataSnapshot.child("sex").getValue().toString();
                String briday = dataSnapshot.child("briday").getValue().toString();
                String address = dataSnapshot.child("address").getValue().toString();

                edit_txtsex_intent.setText(sex);
                edit_txtbriday_intent.setText(briday);
                edit_txtlive_intent.setText(address);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @OnClick(R.id.baseinfo)
    public void ClickChanceSex()
    {
        Log.d("EditProfileActivity","ClickChanceSex");
        Intent editSexIntent=new Intent(EditProfileActivity.this, EditSexActivity.class);
        editSexIntent.putExtra("edit_txtsex_intent",edit_txtsex_intent.getText().toString());
        startActivity(editSexIntent);
    }

    @OnClick(R.id.baseinfo_bri)
    public void ClickChanceBrithday() {
        Log.d("EditProfileActivity", "ClickChanceBrithday");
        Intent editBrithdayIntent = new Intent(EditProfileActivity.this, EditBrithdayActivity.class);
        startActivity(editBrithdayIntent);
    }

    @OnClick(R.id.baseinfo_live)
    public void ClickChanceLive() {
        Log.d("EditProfileActivity", "ClickChanceLive");
        Intent editLiveIntent = new Intent(EditProfileActivity.this, EditAdressActivity.class);
        startActivity(editLiveIntent);
    }
}
