package com.example.thu.realtysocial.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.thu.realtysocial.Profile.FriendsActivity;
import com.example.thu.realtysocial.Profile.ProfileActivity;
import com.example.thu.realtysocial.R;
import com.example.thu.realtysocial.model.Friend;
import com.example.thu.realtysocial.model.FriendFrequest;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class FriendRequestActivity extends AppCompatActivity {

    @BindView(R.id.number_friend_request)
    TextView number_friend_request;
    @BindView(R.id.fr_recyc)
    RecyclerView fr_recyc;
    private DatabaseReference FriendreFerence;
    private DatabaseReference usereFerence;
    private DatabaseReference FriendRequestDatabase;
    private DatabaseReference ListFreferenceRequest;
    private FirebaseAuth mAuth;
    String online_user_id = "";
    Integer countFriends = 0;
    DatabaseReference ProfileFriendFreference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_request);
        ButterKnife.bind(this);
        InitFribase();
        InitView();
    }

    private void InitView() {
        fr_recyc.setLayoutManager(new LinearLayoutManager(FriendRequestActivity.this));
    }

    private void InitFribase() {
        mAuth = FirebaseAuth.getInstance();
        online_user_id = mAuth.getCurrentUser().getUid();
        FriendreFerence = FirebaseDatabase.getInstance().getReference().child("List_Friend_Request").child(online_user_id);
        FriendreFerence.keepSynced(true);
        usereFerence = FirebaseDatabase.getInstance().getReference().child("users");
        usereFerence.keepSynced(true);
        ProfileFriendFreference = FirebaseDatabase.getInstance().getReference().child("Friends");
        ProfileFriendFreference.keepSynced(true);
        FriendRequestDatabase = FirebaseDatabase.getInstance().getReference().child("Friend_Request");
        FriendRequestDatabase.keepSynced(true);
        ListFreferenceRequest = FirebaseDatabase.getInstance().getReference().child("List_Friend_Request");
        ListFreferenceRequest.keepSynced(true);
    }

    @Override
    public void onStart() {
        super.onStart();
        final FirebaseRecyclerAdapter<FriendFrequest, AlFriendsRequestViewHolder> allFriendRequest_firebase =
                new FirebaseRecyclerAdapter<FriendFrequest, AlFriendsRequestViewHolder>(
                        FriendFrequest.class,
                        R.layout.item_friend_request,
                        AlFriendsRequestViewHolder.class,
                        FriendreFerence
                ) {


                    @Override
                    protected void populateViewHolder(final AlFriendsRequestViewHolder viewHolder, final FriendFrequest model, final int position) {


                        final String list_user_id = getRef(position).getKey();
                        countFriends++;
                        usereFerence.child(list_user_id).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                String username = dataSnapshot.child("username").getValue().toString();
                                String ImageAvartar = dataSnapshot.child("imageAvartar").getValue().toString();
                                viewHolder.SetUserName(username);
                                viewHolder.SetAvarta_image(ImageAvartar);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                        viewHolder.mView.findViewById(R.id.item_btn_accept).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Toast.makeText(FriendRequestActivity.this, list_user_id, Toast.LENGTH_SHORT).show();
                                AcceptRequestMethod(list_user_id);
                            }
                        });
                        viewHolder.mView.findViewById(R.id.item_btn_delete).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Toast.makeText(FriendRequestActivity.this, list_user_id, Toast.LENGTH_SHORT).show();
//                                CancelItemListFriend(list_user_id);
                                cancelRequestMethod(list_user_id);
                            }
                        });
                    }
                };
        fr_recyc.setAdapter(allFriendRequest_firebase);
        number_friend_request.setText(countFriends + "");
//        allFriend_firebase.notifyDataSetChanged();
    }

    private void AcceptRequestMethod(final String userIdFriend) {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MMMM-yyyy");
        final String saveCurrentDate = currentDate.format(calendar.getTime());
        ProfileFriendFreference.child(online_user_id).child(userIdFriend).child("date").setValue(saveCurrentDate)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        ProfileFriendFreference.child(userIdFriend).child(online_user_id).child("date").setValue(saveCurrentDate)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        cancelRequestMethod( userIdFriend);

                                    }
                                });
                    }
                });
    }

    private void cancelRequestMethod(final String userIdFriend) {
        FriendRequestDatabase.child(online_user_id).child(userIdFriend)
                .removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()) {
                            FriendRequestDatabase.child(userIdFriend).child(online_user_id).removeValue()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                CancelItemListFriend(userIdFriend);
                                            }
                                        }
                                    });
                        }


                    }
                });
    }

    private void CancelItemListFriend(String userIdFriend) {
        ListFreferenceRequest.child(online_user_id).child(userIdFriend).removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d("CancelItemListFriend", "Succe");
                        }
                    }
                });

    }

    public static class AlFriendsRequestViewHolder extends RecyclerView.ViewHolder {
        View mView;

        public AlFriendsRequestViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void SetUserName(String username) {
            TextView all_users_name = mView.findViewById(R.id.item_txt_name);
            all_users_name.setText(username);
        }

        public void SetAvarta_image(final String avarta_image) {
            final ImageView all_users_circle = mView.findViewById(R.id.item_imv_friend);
            try {

                Picasso.get().load(avarta_image).networkPolicy(NetworkPolicy.OFFLINE).placeholder(R.drawable.noticon).into(all_users_circle, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError(Exception e) {
                        Picasso.get().load(avarta_image).placeholder(R.drawable.noticon).into(all_users_circle);
                    }
                });
                Picasso.get().load(avarta_image).into(all_users_circle);
            } catch (Exception e) {
            }
        }

    }
}
