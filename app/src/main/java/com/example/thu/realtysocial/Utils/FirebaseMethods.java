package com.example.thu.realtysocial.Utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.example.thu.realtysocial.Retrofit.GXClient;
import com.example.thu.realtysocial.activity.RegisterActivity;
import com.example.thu.realtysocial.Retrofit.ClassData.User;
import com.example.thu.realtysocial.R;
import com.example.thu.realtysocial.model.UserFI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.example.thu.realtysocial.model.UserAccountSettings;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FirebaseMethods {
    //top
    private static final String TAG = "FirebaseMethods";

    //firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;
    private String userID;

    //vars
    private Context mContext;
    private double mPhotoUploadProgress = 0;

    public FirebaseMethods(Context context) {
        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();
//        mStorageReference = FirebaseStorage.getInstance().getReference();
        mContext = context;

        if(mAuth.getCurrentUser() != null){
            userID = mAuth.getCurrentUser().getUid();
        }
    }
    public void registerNewEmail(final String email, String password, final String username){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Toast.makeText(mContext, R.string.auth_failed,
                                    Toast.LENGTH_SHORT).show();

                        }
                        else if(task.isSuccessful()){
                            //send verificaton email
                            sendVerificationEmail();

                            userID = mAuth.getCurrentUser().getUid();

                            Log.d(TAG, "onComplete: Authstate changed: " + userID);
                        }

                    }
                });
    }
    public void sendVerificationEmail(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if(user != null){
            user.sendEmailVerification()
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){

                            }else{
                                Toast.makeText(mContext, "couldn't send verification email.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }
    public void addNewUser( String username, String email, String phone_number, String sex, String imageAvartar,String imageCover
                           ,String briday, String typeUser, String address, String status,
                            double starLove,double starHate){
        String device_token = FirebaseInstanceId.getInstance().getToken();
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MMMM-yyyy");
        final String saveCurrentDate = currentDate.format(calendar.getTime());

        UserFI user = new UserFI( userID,  username,  email,phone_number,sex,imageAvartar,imageCover,briday,typeUser,address,status,device_token,
                starLove,starHate,saveCurrentDate);

        myRef.child(mContext.getString(R.string.dbname_users))
                .child(userID)
                .setValue(user);

//        userID,username, email, briday, imageAvartar, phone_number,sex,
//                starHate, starLove, status, address, saveCurrentDate,device_token,typeUser,
        //add phpmyadmin.
        GXClient.getInstance().InsertData(userID,username, email, briday, imageAvartar, phone_number,sex,
                1.0, 1.0, status, address, saveCurrentDate,device_token,typeUser,
                new Callback<List<User>>() {
                    @Override
                    public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                        Log.d("THUTHU", response.body().size() + "");
                    }

                    @Override
                    public void onFailure(Call<List<User>> call, Throwable t) {

                    }
                });


//        UserAccountSettings settings = new UserAccountSettings(
//                description,
//                username,
//                status,
//                profile_photo,
//                StringManipulation.condenseUsername(username),
//                thum_image,
//                device_token,
//                userID
//        );
//
//        myRef.child(mContext.getString(R.string.dbname_user_account_settings))
//                .child(userID)
//                .setValue(user);

    }
}
