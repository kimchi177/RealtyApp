package com.example.thu.realtysocial.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.thu.realtysocial.Adapter.MessageAdapter;
import com.example.thu.realtysocial.LastSeenTime;
import com.example.thu.realtysocial.R;
import com.example.thu.realtysocial.model.Messages;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends AppCompatActivity {
    private static final Integer Grallery = 123;

    @BindView(R.id.message_imv_send)
    ImageView message_imv_send;
    @BindView(R.id.chat_recyc)
    RecyclerView chat_recyc;
    @BindView(R.id.message_imv_camera)
    ImageView message_imv_camera;
    @BindView(R.id.meeage_txt_input)
    TextView meeage_txt_input;
    //    @BindView(R.id.chat_txt_username)
    TextView chat_txt_username;
    //    @BindView(R.id.chat_txt_last_seen)
    TextView chat_txt_last_seen;
    //    @BindView(R.id.chat_circle)
    CircleImageView chat_circle;
    @BindView(R.id.chat_bar_layout)
    android.support.v7.widget.Toolbar chat_bar_layout;


    private String user_id_intent;
    private String user_name_intent;

    DatabaseReference rootRef;
    private FirebaseAuth mAuth;
    private String messageSenderId;

    private final List<Messages> messageList = new ArrayList<>();
    private LinearLayoutManager linearLayoutManager;
    private MessageAdapter messageAdapter;
    private StorageReference messageImageStorageRef;
    private ProgressDialog loadProgressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ButterKnife.bind(this);

        init();
        user_id_intent = getIntent().getStringExtra("user_id").toString();
        user_name_intent = getIntent().getStringExtra("username").toString();
        chat_txt_username.setText(user_name_intent);
        InitFirbase();
        FetchMessages();
        DisplayStateActive();
    }

    private void FetchMessages() {
        rootRef.child("Messages").child(messageSenderId).child(user_id_intent)
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        Messages messages = dataSnapshot.getValue(Messages.class);
                        messageList.add(messages);
                        messageAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    private void init() {
        loadProgressDialog=new ProgressDialog(this);
        setSupportActionBar(chat_bar_layout);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowCustomEnabled(true);
        LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View action_bar_view = layoutInflater.inflate(R.layout.chat_custom_bar, null);
        actionBar.setCustomView(action_bar_view);
        chat_txt_username = findViewById(R.id.chat_txt_username);
        chat_txt_last_seen = findViewById(R.id.chat_txt_last_seen);
        chat_circle = findViewById(R.id.chat_circle);

        messageAdapter = new MessageAdapter(messageList);
        linearLayoutManager = new LinearLayoutManager(this);
        chat_recyc.setHasFixedSize(true);
        chat_recyc.setLayoutManager(linearLayoutManager);
        chat_recyc.setAdapter(messageAdapter);
    }

    private void DisplayStateActive() {
        rootRef.child("users").child(user_id_intent).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String online = dataSnapshot.child("online").getValue().toString();
                final String imageAvartar = dataSnapshot.child("imageAvartar").getValue().toString();

                try {

                    Picasso.get().load(imageAvartar).networkPolicy(NetworkPolicy.OFFLINE).placeholder(R.drawable.noticon).into(chat_circle, new Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError(Exception e) {
                            Picasso.get().load(imageAvartar).placeholder(R.drawable.noticon).into(chat_circle);
                        }
                    });
                    Picasso.get().load(imageAvartar).into(chat_circle);
                } catch (Exception e) {
                }
                if (online.equals("true")) {
                    chat_txt_last_seen.setText("Online");
                } else {
                    LastSeenTime getTime = new LastSeenTime();
                    Long last_seen = Long.parseLong(online);
                    String lastGreenOnline = getTime.getTimeAgo(last_seen).toString();
                    chat_txt_last_seen.setText(lastGreenOnline);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void InitFirbase() {
        rootRef = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        messageSenderId = mAuth.getCurrentUser().getUid();
        messageImageStorageRef = FirebaseStorage.getInstance().getReference().child("Message_Picture");
    }

    @OnClick(R.id.message_imv_send)
    public void ClickSend() {
        SendMessage();
    }

    private void SendMessage() {
        String messageText = meeage_txt_input.getText().toString();
        if (TextUtils.isEmpty(messageText)) {
            Toast.makeText(this, "please write your message", Toast.LENGTH_SHORT).show();
        } else {
            String message_sender_ref = "Messages/" + messageSenderId + "/" + user_id_intent;
            String message_receiver_ref = "Messages/" + user_id_intent + "/" + messageSenderId;
            DatabaseReference user_message_key = rootRef.child("Messages").child(message_sender_ref).child(message_receiver_ref)
                    .push();
            String message_push_id = user_message_key.getKey();
            Map messageTextBody = new HashMap();
            messageTextBody.put("message", messageText);
            messageTextBody.put("seen", false);
            messageTextBody.put("type", "text");
            messageTextBody.put("time", ServerValue.TIMESTAMP);
            messageTextBody.put("from", messageSenderId);

            Map messageBodyDetail = new HashMap();
            messageBodyDetail.put(message_sender_ref + "/" + message_push_id, messageTextBody);
            messageBodyDetail.put(message_receiver_ref + "/" + message_push_id, messageTextBody);
            rootRef.updateChildren(messageBodyDetail, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                    if (databaseError != null) {
                        Log.d("ChatLog", databaseError.getMessage().toString());
                    }
                    meeage_txt_input.setText("");

                }
            });
        }
    }

    @OnClick(R.id.message_imv_camera)
    public void ClickCamera() {
        ChooseImage();
    }

    private void ChooseImage() {
        Intent intentGrallery = new Intent();
        intentGrallery.setAction(
                Intent.ACTION_GET_CONTENT
        );
        intentGrallery.setType("image/*");
        startActivityForResult(intentGrallery, Grallery);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Grallery && resultCode == RESULT_OK && data != null) {
            loadProgressDialog.setTitle("Sending chat image");
            loadProgressDialog.setMessage("Please wait, while your chat messaging is sending...");
            loadProgressDialog.show();
            Uri imageUri = data.getData();
            final String message_sender_ref = "Messages/" + messageSenderId + "/" + user_id_intent;
            final String message_receiver_ref = "Messages/" + user_id_intent + "/" + messageSenderId;
            DatabaseReference user_message_key = rootRef.child("Messages").child(message_sender_ref).child(message_receiver_ref)
                    .push();
            final String message_push_id = user_message_key.getKey();
            StorageReference filePath = messageImageStorageRef.child(message_push_id + ".jpg");
            filePath.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    if(task.isSuccessful())
                    {
                        final String dowloadUrl=task.getResult().getDownloadUrl().toString();
                        Map messageTextBody = new HashMap();
                        messageTextBody.put("message", dowloadUrl);
                        messageTextBody.put("seen", false);
                        messageTextBody.put("type", "image");
                        messageTextBody.put("time", ServerValue.TIMESTAMP);
                        messageTextBody.put("from", messageSenderId);

                        Map messageBodyDetail = new HashMap();
                        messageBodyDetail.put(message_sender_ref + "/" + message_push_id, messageTextBody);
                        messageBodyDetail.put(message_receiver_ref + "/" + message_push_id, messageTextBody);

                        rootRef.updateChildren(messageBodyDetail, new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                if (databaseError != null) {
                                    Log.d("ChatLog", databaseError.getMessage().toString());
                                }
                                meeage_txt_input.setText("");
                                loadProgressDialog.dismiss();

                            }
                        });
                        loadProgressDialog.dismiss();
                        Toast.makeText(ChatActivity.this, "Picture send suucessful", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        loadProgressDialog.dismiss();
                        Toast.makeText(ChatActivity.this, "Picture not send,Try again!!!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}
