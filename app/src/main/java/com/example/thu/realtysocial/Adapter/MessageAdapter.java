package com.example.thu.realtysocial.Adapter;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.thu.realtysocial.R;
import com.example.thu.realtysocial.model.Messages;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {
    private List<Messages> userMessageList;
    private FirebaseAuth mAuth;
    private DatabaseReference userDatabaseRef;

    public MessageAdapter(List<Messages> userMessageList) {
        this.userMessageList = userMessageList;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.messages_layout_of_user, parent, false);
        mAuth=FirebaseAuth.getInstance();
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MessageViewHolder holder, int position) {
        String message_sender_id=mAuth.getCurrentUser().getUid();
        final Messages messages = userMessageList.get(position);
        String fromUserId=messages.getFrom();
        String fromMessageType=messages.getType();
        userDatabaseRef= FirebaseDatabase.getInstance().getReference().child("users").child(fromUserId);
        userDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final String username = dataSnapshot.child("username").getValue().toString();
                final String ImageAvartar = dataSnapshot.child("imageAvartar").getValue().toString();
                try {

                    Picasso.get().load(ImageAvartar).networkPolicy(NetworkPolicy.OFFLINE).placeholder(R.drawable.noticon).into(holder.message_circle_image, new Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError(Exception e) {
                            Picasso.get().load(ImageAvartar).placeholder(R.drawable.noticon).into(holder.message_circle_image);
                        }
                    });
                    Picasso.get().load(ImageAvartar).into(holder.message_circle_image);

                } catch (Exception e) {
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        if(fromMessageType.equals("text"))
        {
            holder.message_txt_content.setVisibility(View.VISIBLE);
            holder.message_txt_content.setPadding(0,0,0,0);
            holder.message_image_view.setVisibility(View.GONE);
            if(fromUserId.equals(message_sender_id))
            {
                holder.message_txt_content.setBackgroundResource(R.drawable.message_text_background_two);
                holder.message_txt_content.setTextColor(Color.BLACK);
                holder.message_txt_content.setGravity(Gravity.RIGHT);
            }
            else
            {
                holder.message_txt_content.setBackgroundResource(R.drawable.message_text_background);
                holder.message_txt_content.setTextColor(Color.WHITE);
                holder.message_txt_content.setGravity(Gravity.LEFT);
            }
            holder.message_txt_content.setText(messages.getMessage());
        }
        else
        {
            try {

                Picasso.get().load(messages.getMessage()).networkPolicy(NetworkPolicy.OFFLINE).placeholder(R.drawable.noticon).into(holder.message_image_view, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError(Exception e) {
                        Picasso.get().load(messages.getMessage()).placeholder(R.drawable.noticon).into(holder.message_image_view);
                    }
                });
                Picasso.get().load(messages.getMessage()).into(holder.message_image_view);
                holder.message_image_view.setVisibility(View.VISIBLE);
                holder.message_txt_content.setVisibility(View.GONE);

            } catch (Exception e) {
            }
        }
    }

    @Override
    public int getItemCount() {
        return userMessageList.size();
    }

    public class MessageViewHolder extends RecyclerView.ViewHolder {
        public TextView message_txt_content;
        public CircleImageView message_circle_image;
        public ImageView message_image_view;

        public MessageViewHolder(View itemView) {
            super(itemView);
            message_circle_image = itemView.findViewById(R.id.message_circle_image);
            message_image_view = itemView.findViewById(R.id.message_image_view);
            message_txt_content = itemView.findViewById(R.id.message_txt_content);
        }
    }
}
