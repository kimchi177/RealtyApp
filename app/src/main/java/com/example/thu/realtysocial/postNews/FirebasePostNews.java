package com.example.thu.realtysocial.postNews;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.example.thu.realtysocial.Fragment.ProfileFragmentPackage.TimeLineFragment;
import com.example.thu.realtysocial.Profile.ProfileActivity;
import com.example.thu.realtysocial.R;
import com.example.thu.realtysocial.Retrofit.ClassData.User;
import com.example.thu.realtysocial.Retrofit.GXClient;
import com.example.thu.realtysocial.model.PostNews;
import com.example.thu.realtysocial.model.UserFI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FirebasePostNews {
    //top
    private static final String TAG = "FirebasePostNews";

    //firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;
    private String userID;
    Context mContext;
    RefreshImageListener listener;
    StorageReference firebaseStorage;

    //    private ProgressDialog loadPrDia;
    public FirebasePostNews(Context context, RefreshImageListener listener) {
        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();
//        mStorageReference = FirebaseStorage.getInstance().getReference();
        mContext = context;
        this.listener = listener;
//        firebaseStorage = FirebaseStorage.getInstance().getReference().child("image_news_post");
        if (mAuth.getCurrentUser() != null) {
            userID = mAuth.getCurrentUser().getUid();
        }
    }

    public void addNews(String typeNews, String content, String address, Double price) {
        //Double date, Double time
//        String device_token = FirebaseInstanceId.getInstance().getToken();
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MMMM-yyyy");
//        final String saveCurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentDateTime = new SimpleDateFormat("dd-MMMM-yyyy hh:mm:ss");
        final String saveCurrentDateTime = currentDateTime.format(calendar.getTime());
//       Long timestamp =Long.parseLong(ServerValue.TIMESTAMP+"") ;
//        PostNews postNews = new PostNews(typeNews, content, address, price, saveCurrentDateTime, timestamp);
//        myRef.child(mContext.getString(R.string.dbname_news))
//                .child(userID).push()
//                .setValue(postNews, new DatabaseReference.CompletionListener() {
//                    @Override
//                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
//                        if (databaseError != null) {
//                            Log.d(TAG, databaseError.getMessage().toString());
//                            Toast.makeText(mContext, "your news post fail.", Toast.LENGTH_SHORT).show();
//                        } else {
////                            String valuePush = myRef.child(mContext.getString(R.string.dbname_news)).child(userID).push().getKey();
////                            Toast.makeText(mContext, "your news post suceesful.", Toast.LENGTH_SHORT).show();
//                            listener.onPostImage(saveCurrentDateTime);
//                            Log.d(TAG, "onComplete: " + saveCurrentDateTime);
////
//                        }
//                    }
//                });
//        Log.d(TAG, "addNews: " +t);
        Map messageTextBody = new HashMap();
        messageTextBody.put("typeNews", typeNews);
        messageTextBody.put("content", content);
        messageTextBody.put("address", address);
        messageTextBody.put("price", price);
        messageTextBody.put("date", saveCurrentDateTime);
        messageTextBody.put("time", ServerValue.TIMESTAMP);

        myRef.child(mContext.getString(R.string.dbname_news))
                .child(userID).push().updateChildren(messageTextBody, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError != null) {
                    Log.d(TAG, databaseError.getMessage().toString());
                    Toast.makeText(mContext, "your news post fail.", Toast.LENGTH_SHORT).show();
                } else {
//                            String valuePush = myRef.child(mContext.getString(R.string.dbname_news)).child(userID).push().getKey();
//                            Toast.makeText(mContext, "your news post suceesful.", Toast.LENGTH_SHORT).show();
                    listener.onPostImage(saveCurrentDateTime);
                    Log.d(TAG, "onComplete: " + saveCurrentDateTime);
//
                }
            }
        });
    }
}
