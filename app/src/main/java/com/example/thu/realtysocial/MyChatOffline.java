package com.example.thu.realtysocial;


import android.app.Application;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

public class MyChatOffline extends Application {
    private DatabaseReference userReference;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUsers;
    @Override
    public void onCreate() {
        super.onCreate();
        //Load image offline with Picasso.
        LoadImageOffile();
        InitFirebase();
    }

    private void InitFirebase() {
        mAuth=FirebaseAuth.getInstance();
        currentUsers=mAuth.getCurrentUser();
      if(currentUsers!=null)
      {
          String online_user_id=mAuth.getCurrentUser().getUid();
          userReference=FirebaseDatabase.getInstance().getReference().child("users").child(online_user_id);
          userReference.addValueEventListener(new ValueEventListener() {
              @Override
              public void onDataChange(DataSnapshot dataSnapshot) {
                  userReference.child("online").onDisconnect().setValue(ServerValue.TIMESTAMP);
//                  userReference.child("online").onDisconnect().setValue(false);
//                  userReference.child("online").setValue(true);
              }

              @Override
              public void onCancelled(DatabaseError databaseError) {

              }
          });
      }
    }

    private void LoadImageOffile() {
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
//        Create an OkHttp3Downloader instance wrapping your OkHttpClient or Call.Factory and pass it to downloader
        Picasso.Builder builder=new Picasso.Builder(this);
        builder.downloader(new OkHttp3Downloader(this,Integer.MAX_VALUE));
        Picasso built=builder.build();
        built.setIndicatorsEnabled(true);
        built.setLoggingEnabled(true);
        Picasso.setSingletonInstance(built);
    }
}

