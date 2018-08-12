package com.example.thu.realtysocial.Profile.EditProfile;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.example.thu.realtysocial.Profile.EditProfileActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseUpdateData {
    //Firebase
    DatabaseReference statusDatabaseReference;
    FirebaseAuth mAuth;
//    ProgressDialog loadProgressDialog;
    Context context;
    public FirebaseUpdateData(Context context) {
this.context=context;
    }

    private void InitFirebase() {
        mAuth = FirebaseAuth.getInstance();
        String U_id = mAuth.getCurrentUser().getUid();
        statusDatabaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(U_id);
    }
    public void UpdateData(String property,final String value) {
        InitFirebase();
//        loadProgressDialog = new ProgressDialog(context);
        if (value.equals("")) {
            Toast.makeText(context, "please wait your check", Toast.LENGTH_SHORT).show();
        } else {
//            loadProgressDialog.setTitle("chance profile sex");
//            loadProgressDialog.setMessage("plase wait, we are updating your sex.....");
//            loadProgressDialog.show();
            statusDatabaseReference.child(property).setValue(value).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
//                        loadProgressDialog.dismiss();
                        Toast.makeText(context, "Profile sex update susscessfull.", Toast.LENGTH_SHORT).show();
                        Intent sexIntent = new Intent(
                                context, EditProfileActivity.class
                        );
//                        sexIntent.putExtra("sex",chooseSex);
                        context.startActivity(sexIntent);
                    } else
                        Toast.makeText(context, "what is reason ? you can't update. sorry!!!", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
