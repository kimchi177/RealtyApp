package com.example.thu.realtysocial.Profile;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.thu.realtysocial.Fragment.MesageFragment;
import com.example.thu.realtysocial.ListenOnline;
import com.example.thu.realtysocial.R;

import com.example.thu.realtysocial.activity.ChatActivity;
import com.example.thu.realtysocial.model.Friend;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class FriendsActivity extends AppCompatActivity {
    private static final String TEST = "TEST";
    @BindView(R.id.recyc_friend_request)
    RecyclerView recyc_friend_request;
    private DatabaseReference FriendreFerence;
    private DatabaseReference usereFerence;
    private FirebaseAuth mAuth;
    String online_user_id = "";

    ListenOnline listenOnline;


    //edit
    FirebaseUser userNote;
    private FirebaseAuth firebaseAuthNote;
    private DatabaseReference userReferenceNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);
        ButterKnife.bind(this);
        InitFribase();
        InitView();
    }

    private void InitView() {
        recyc_friend_request.setLayoutManager(new LinearLayoutManager(FriendsActivity.this));
    }

    private void InitFribase() {
        mAuth = FirebaseAuth.getInstance();
        online_user_id = mAuth.getCurrentUser().getUid();
        FriendreFerence = FirebaseDatabase.getInstance().getReference().child("Friends").child(online_user_id);
        FriendreFerence.keepSynced(true);
        usereFerence = FirebaseDatabase.getInstance().getReference().child("users");
        usereFerence.keepSynced(true);
    }

    @Override
    public void onStart() {
//        firebaseAuthNote = FirebaseAuth.getInstance();
//        userNote = firebaseAuthNote.getCurrentUser();
//        String online_user_id=firebaseAuthNote.getCurrentUser().getUid();
//        userReferenceNote= FirebaseDatabase.getInstance().getReference().child("users").child(online_user_id);
//        userReferenceNote.child("online").setValue(true);

        super.onStart();
        Log.d("friend", "onStart: START");
        final FirebaseRecyclerAdapter<Friend, AlFriendsViewHolder> allFriend_firebase =
                new FirebaseRecyclerAdapter<Friend, AlFriendsViewHolder>(
                        Friend.class,
                        R.layout.item_list_friend,
                        AlFriendsViewHolder.class,
                        FriendreFerence
                ) {
                    @Override
                    protected void populateViewHolder(final AlFriendsViewHolder viewHolder, Friend model, int position) {
                        Log.d(TEST, "populateViewHolder 1: ");
                        viewHolder.SetUserDate(model.getDate() + "");

                        try {
                            final String list_user_id = getRef(position).getKey();
                            usereFerence.child(list_user_id).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(final DataSnapshot dataSnapshot) {
                                    final String username = dataSnapshot.child("username").getValue().toString();
                                    String ImageAvartar = dataSnapshot.child("imageAvartar").getValue().toString();

                                    if (dataSnapshot.hasChild("online")) {
                                        String online_status = (String) dataSnapshot.child("online").getValue().toString();
                                        viewHolder.setUserOnline(online_status);
                                    }

                                    viewHolder.SetUserName(username);
                                    viewHolder.SetAvarta_image(ImageAvartar);
                                    viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            CharSequence option[] = new CharSequence[]{
                                                    username + "' sprofile",
                                                    "send message"
                                            };
                                            AlertDialog.Builder builder = new AlertDialog.Builder(FriendsActivity.this);
                                            builder.setTitle("select options");
                                            builder.setItems(option, new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int position) {
                                                    if (position == 0) {
                                                        Intent profileIntent = new Intent(FriendsActivity.this, ProfileActivity.class);
                                                        profileIntent.putExtra("user_id", list_user_id);
                                                        startActivity(profileIntent);
                                                    }
                                                    if (position == 1) {
                                                        if (dataSnapshot.child("online").exists()) {
                                                            Intent chatIntent = new Intent(FriendsActivity.this, ChatActivity.class);
                                                            chatIntent.putExtra("user_id", list_user_id);
                                                            chatIntent.putExtra("username", username);
                                                            startActivity(chatIntent);
                                                        }
                                                        else
                                                        {
                                                            usereFerence.child(list_user_id).child("online").setValue(ServerValue.TIMESTAMP)
                                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                        @Override
                                                                        public void onSuccess(Void aVoid) {
                                                                            Intent chatIntent = new Intent(FriendsActivity.this, ChatActivity.class);
                                                                            chatIntent.putExtra("user_id", list_user_id);
                                                                            chatIntent.putExtra("username", username);
                                                                            startActivity(chatIntent);
                                                                        }
                                                                    });
                                                        }
                                                    }
                                                }
                                            });
                                            builder.show();

                                        }
                                    });
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        } catch (Exception e) {
                        }
                    }
                };
        try {
            recyc_friend_request.setAdapter(allFriend_firebase);
        } catch (Exception e) {
        }
//        allFriend_firebase.notifyDataSetChanged();
        Log.d("friend", "onStart: END");

    }

    public static class AlFriendsViewHolder extends RecyclerView.ViewHolder {
        View mView;

        public AlFriendsViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void SetUserName(String username) {
            TextView all_users_name = mView.findViewById(R.id.all_friend_name);
            all_users_name.setText(username);
        }

        public void SetUserDate(String userDate) {
            TextView all_friend_date = mView.findViewById(R.id.all_friend_date);
            all_friend_date.setText(userDate);
        }

        public void SetAvarta_image(final String avarta_image) {
            final CircleImageView all_users_circle = mView.findViewById(R.id.all_friends_circle);
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

        public void setUserOnline(String online_status) {
            ImageView item_imv_online = mView.findViewById(R.id.item_imv_online);
            if (online_status.equals("true")) {
                item_imv_online.setVisibility(View.VISIBLE);
            } else {
                item_imv_online.setVisibility(View.GONE);
            }
        }
    }
}
