package com.example.thu.realtysocial.Fragment;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.thu.realtysocial.Adapter.MessageAdapter;
import com.example.thu.realtysocial.LastSeenTime;
import com.example.thu.realtysocial.Profile.FriendsActivity;
import com.example.thu.realtysocial.Profile.ProfileActivity;
import com.example.thu.realtysocial.R;
import com.example.thu.realtysocial.activity.ChatActivity;
import com.example.thu.realtysocial.activity.FriendRequestActivity;
import com.example.thu.realtysocial.model.Chats;
import com.example.thu.realtysocial.model.Friend;
import com.example.thu.realtysocial.model.FriendFrequest;
import com.example.thu.realtysocial.model.Messages;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class MesageFragment extends Fragment {

    private DatabaseReference FriendreFerence;
    private DatabaseReference usereFerence;
    private FirebaseAuth mAuth;
    String online_user_id = "";
    RecyclerView messag_frag_recyc;

    private final List<Messages> messageList = new ArrayList<>();
    private LinearLayoutManager linearLayoutManager;
    private MessageAdapter messageAdapter;

    public MesageFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_mesage, container, false);
        messag_frag_recyc = view.findViewById(R.id.messag_frag_recyc);
        Log.d("MessagesMessages", "onCreateView: ");
        initFirbase();
        return view;
    }

    private void initFirbase() {
        mAuth = FirebaseAuth.getInstance();
        online_user_id = mAuth.getCurrentUser().getUid();
        FriendreFerence = FirebaseDatabase.getInstance().getReference().child("Messages").child(online_user_id);
        FriendreFerence.keepSynced(true);
        usereFerence = FirebaseDatabase.getInstance().getReference().child("users");
        usereFerence.keepSynced(true);

//        linearLayoutManager = new LinearLayoutManager(getContext());
//        linearLayoutManager.setReverseLayout(true);
//        linearLayoutManager.setStackFromEnd(true);
//        messag_frag_recyc.setHasFixedSize(true);
//        messag_frag_recyc.setLayoutManager(linearLayoutManager);
        messag_frag_recyc.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    @Override
    public void onStart() {
        super.onStart();
        super.onStart();
        final FirebaseRecyclerAdapter<Chats, ChatsViewHolder> allFriendRequest_firebase =
                new FirebaseRecyclerAdapter<Chats, ChatsViewHolder>(
                        Chats.class,
                        R.layout.item_list_friend_messages,
                        ChatsViewHolder.class,
                        FriendreFerence
                ) {


                    @Override
                    protected void populateViewHolder(final ChatsViewHolder viewHolder, final Chats model, final int position) {
                        final String list_user_id = getRef(position).getKey();
                        usereFerence.child(list_user_id).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(final DataSnapshot dataSnapshot) {
                                final String username = dataSnapshot.child("username").getValue().toString();
                                String ImageAvartar = dataSnapshot.child("imageAvartar").getValue().toString();
                                String status = dataSnapshot.child("status").getValue().toString();
                                viewHolder.SetUserName(username);
                                viewHolder.SetAvarta_image(ImageAvartar);
                                viewHolder.SetUserStatus(status);
                                if (dataSnapshot.hasChild("online")) {
                                    String online_status = (String) dataSnapshot.child("online").getValue().toString();
                                    viewHolder.setUserOnline(online_status);
                                    viewHolder.setUserTextOnline(online_status);
                                }
                                else
                                {

                                }
                                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        if (dataSnapshot.child("online").exists()) {
                                            Intent chatIntent = new Intent(getActivity(), ChatActivity.class);
                                            chatIntent.putExtra("user_id", list_user_id);
                                            chatIntent.putExtra("username", username);
                                            startActivity(chatIntent);
                                        } else {
                                            usereFerence.child(list_user_id).child("online").setValue(ServerValue.TIMESTAMP)
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            Intent chatIntent = new Intent(getActivity(), ChatActivity.class);
                                                            chatIntent.putExtra("user_id", list_user_id);
                                                            chatIntent.putExtra("username", username);
                                                            startActivity(chatIntent);
                                                        }
                                                    });
                                        }


                                    }
                                });
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                        //
                    }
                };
        messag_frag_recyc.setAdapter(allFriendRequest_firebase);
    }

    public static class ChatsViewHolder extends RecyclerView.ViewHolder {
        View mView;

        public ChatsViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void SetUserName(String username) {
            TextView all_users_name = mView.findViewById(R.id.all_message_name);
            all_users_name.setText(username);
        }

        public void SetUserStatus(String status) {
            TextView all_users_name = mView.findViewById(R.id.all_messages_date);
            all_users_name.setText(status);
        }

        public void SetAvarta_image(final String avarta_image) {
            final CircleImageView all_users_circle = mView.findViewById(R.id.all_messages_circle);
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
            ImageView item_imv_online = mView.findViewById(R.id.item_friend_online);
            if (online_status.equals("true")) {
                item_imv_online.setVisibility(View.VISIBLE);
            } else {
                item_imv_online.setVisibility(View.GONE);
            }
        }

        public void setUserTextOnline(String online_status) {
            TextView item_imv_online = mView.findViewById(R.id.item_txt_friend_online);
            if (online_status.equals("true")) {
                item_imv_online.setVisibility(View.GONE);
            } else {
                LastSeenTime getTime = new LastSeenTime();
                Long last_seen = Long.parseLong(online_status);
                String lastGreenOnline = getTime.getTimeAgo(last_seen).toString();
                item_imv_online.setText(lastGreenOnline);
                item_imv_online.setVisibility(View.VISIBLE);
            }
        }
    }
}
