package com.example.thu.realtysocial.Fragment;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.thu.realtysocial.Profile.FriendsActivity;
import com.example.thu.realtysocial.R;
import com.example.thu.realtysocial.Profile.ProfileActivity;
import com.example.thu.realtysocial.activity.FriendRequestActivity;
import com.example.thu.realtysocial.activity.LoginActivity;
import com.example.thu.realtysocial.feedFunction.TimeLineIntentActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {
    LinearLayout profile_logout;
    LinearLayout friend;
    LinearLayout friend_request;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth mAuth;
    private DatabaseReference userReference;
    String online_user_id="";

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        firebaseAuth = FirebaseAuth.getInstance();
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        LinearLayout home = view.findViewById(R.id.home);
        profile_logout = view.findViewById(R.id.logout);
        friend = view.findViewById(R.id.friend);
        friend_request = view.findViewById(R.id.friend_request);

        firebaseAuth = FirebaseAuth.getInstance();
        final FirebaseUser user= firebaseAuth.getCurrentUser();
        online_user_id=firebaseAuth.getCurrentUser().getUid();

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        int[] startingLocation = new int[2];
                        v.getLocationOnScreen(startingLocation);
                        startingLocation[0] += v.getWidth() / 2;
                        ProfileActivity.startUserProfileFromLocation(startingLocation,getActivity(),online_user_id);
//                        overridePendingTransition(0, 0);
                    }
                }, 200);
                //
//                Intent ProfileIntent = new Intent(getContext(), ProfileActivity.class);
//                startActivity(ProfileIntent);
            }
        });
        friend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ProfileIntent = new Intent(getContext(), FriendsActivity.class);
                startActivity(ProfileIntent);
//                Intent ProfileIntent = new Intent(getContext(), TimeLineIntentActivity.class);
//                startActivity(ProfileIntent);
            }
        });
        friend_request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ProfileIntent = new Intent(getContext(), FriendRequestActivity.class);
                startActivity(ProfileIntent);
            }
        });
        profile_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(getActivity(), "jhsdj", Toast.LENGTH_SHORT).show();
                userReference= FirebaseDatabase.getInstance().getReference().child("users").child(online_user_id);
                userReference.child("online").setValue(ServerValue.TIMESTAMP);
                Log.d("AAa", user + "");
                firebaseAuth.signOut();


                Intent loginIntent = new Intent(getActivity(), LoginActivity.class);
                loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(loginIntent);
                getActivity().finish();
            }
        });
        return view;
    }

}
