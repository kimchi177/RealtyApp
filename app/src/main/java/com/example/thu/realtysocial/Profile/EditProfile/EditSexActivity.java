package com.example.thu.realtysocial.Profile.EditProfile;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.thu.realtysocial.Profile.EditProfileActivity;
import com.example.thu.realtysocial.Profile.ProfileActivity;
import com.example.thu.realtysocial.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EditSexActivity extends AppCompatActivity {

    private static final String EditSexActivity = "EditSexActivity";
    @BindView(R.id.linear_female)
    RelativeLayout linear_female;
    @BindView(R.id.linear_male)
    RelativeLayout linear_male;
    @BindView(R.id.es_imv_check_female)
    ImageView es_imv_check_female;
    @BindView(R.id.es_imv_check_male)
    ImageView es_imv_check_male;
    @BindView(R.id.ex_btn_cancel)
    Button ex_btn_cancel;
    @BindView(R.id.ex_btn_save)
    Button ex_btn_save;

    //value
    private String chooseSex = "";
    ProgressDialog loadProgressDialog;

    //Firebase
    DatabaseReference statusDatabaseReference;
    FirebaseAuth mAuth;

    FirebaseUpdateData firebaseUpdateData=new FirebaseUpdateData(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_sex);
        ButterKnife.bind(this);
       Init();
       InitFirebase();
    }

    private void InitFirebase() {
        mAuth = FirebaseAuth.getInstance();
        String U_id = mAuth.getCurrentUser().getUid();
        statusDatabaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(U_id);
    }

    private void Init() {
        Intent valueIntent=getIntent();
        String value= valueIntent.getStringExtra("edit_txtsex_intent");
        if(value.equals("Female"))
        {
            es_imv_check_female.setVisibility(View.VISIBLE);
            es_imv_check_male.setVisibility(View.GONE);
        }
        else
        {
            if(value.equals("Male"))
            {
                es_imv_check_female.setVisibility(View.GONE);
                es_imv_check_male.setVisibility(View.VISIBLE);
            }
            else
            {
                es_imv_check_female.setVisibility(View.GONE);
                es_imv_check_male.setVisibility(View.GONE);
            }
        }
        ex_btn_save.setBackgroundResource(R.color.colortextProA);
        loadProgressDialog = new ProgressDialog(this);
    }

    @OnClick(R.id.ex_btn_cancel)
    public void ClickCancel() {
        Log.d("EditSexActivity", "ClickCancel");
        ex_btn_save.setBackgroundResource(R.color.edit_sex_background);
        ex_btn_cancel.setBackgroundResource(R.color.colortextProA);
        finish();
    }

    @OnClick(R.id.linear_female)
    public void linear_female() {
        chooseSex = "Female";
        es_imv_check_female.setVisibility(View.VISIBLE);
        es_imv_check_male.setVisibility(View.GONE);
    }

    @OnClick(R.id.linear_male)
    public void linear_male() {
        chooseSex = "Male";
        es_imv_check_female.setVisibility(View.GONE);
        es_imv_check_male.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.ex_btn_save)
    public void ClickSave() {
        Log.d("EditSexActivity", "ClickSave");
        ex_btn_cancel.setBackgroundResource(R.color.edit_sex_background);
        ex_btn_save.setBackgroundResource(R.color.colortextProA);
        firebaseUpdateData.UpdateData("sex",chooseSex);
        finish();
//        ChanceSex(chooseSex);
    }

//    private void ChanceSex(final String chooseSex) {
//        if (chooseSex.equals("")) {
//            Toast.makeText(this, "please wait your check", Toast.LENGTH_SHORT).show();
//        } else {
//            loadProgressDialog.setTitle("chance profile sex");
//            loadProgressDialog.setMessage("plase wait, we are updating your sex.....");
//            loadProgressDialog.show();
//            statusDatabaseReference.child("sex").setValue(chooseSex).addOnCompleteListener(new OnCompleteListener<Void>() {
//                @Override
//                public void onComplete(@NonNull Task<Void> task) {
//                    if (task.isSuccessful()) {
//                        loadProgressDialog.dismiss();
//                        Toast.makeText(EditSexActivity.this, "Profile sex update susscessfull.", Toast.LENGTH_SHORT).show();
//                        Intent sexIntent = new Intent(
//                                EditSexActivity.this, EditProfileActivity.class
//                        );
////                        sexIntent.putExtra("sex",chooseSex);
//                        startActivity(sexIntent);
//                        finish();
//                    } else
//                        Toast.makeText(EditSexActivity.this, "what is reason ? you can't update. sorry!!!", Toast.LENGTH_SHORT).show();
//                }
//            });
//        }
//    }
}
