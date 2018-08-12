package com.example.thu.realtysocial.activity;

import android.app.FragmentTransaction;
import android.app.NotificationManager;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.example.thu.realtysocial.Fragment.AddFriendsFragment;
import com.example.thu.realtysocial.Fragment.MesageFragment;
import com.example.thu.realtysocial.Fragment.NewsFragment;
import com.example.thu.realtysocial.Fragment.NotificationFragment;
import com.example.thu.realtysocial.Fragment.ProfileFragment;
import com.example.thu.realtysocial.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView mNavi;
    FrameLayout main_fragment;
    FirebaseUser user;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference userReference;

    //
//    Fragment
    private MesageFragment mesageFragment;
    private NewsFragment newsFragment;
    private NotificationFragment notificationManagerFragment;
    private ProfileFragment profileFragment;
    private AddFriendsFragment addFriendsFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        if(user!=null)
        {
            String online_user_id=firebaseAuth.getCurrentUser().getUid();
            userReference= FirebaseDatabase.getInstance().getReference().child("users").child(online_user_id);
        }

        mNavi=findViewById(R.id.main_navi);
        main_fragment=findViewById(R.id.main_fragment);
        newsFragment=new NewsFragment();
        mesageFragment=new MesageFragment();
        notificationManagerFragment=new NotificationFragment();
        profileFragment=new ProfileFragment();
        addFriendsFragment=new AddFriendsFragment();
        setFragment(newsFragment);

        mNavi.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_news:
                       mNavi.setItemBackgroundResource(R.color.colorPrimary);
                       setFragment(newsFragment);
                       return true;
                    case R.id.nav_add_friends:
                        mNavi.setItemBackgroundResource(R.color.colorPrimary);
                        setFragment(addFriendsFragment);
                        return true;
                    case R.id.nav_chat:
                        mNavi.setItemBackgroundResource(R.color.colorAccent);
                        setFragment(mesageFragment);
                        return true;
                    case R.id.nav_noti:
                        mNavi.setItemBackgroundResource(R.color.colorPrimaryDark);
                        setFragment(notificationManagerFragment);
                        return true;
                    case R.id.nav_home:
                        mNavi.setItemBackgroundResource(R.color.coloryellow);
                        setFragment(profileFragment);
                        return true;
                        default:
                            return false;
                }
            }
        });
    }

    private void setFragment(Fragment fragment) {
        android.support.v4.app.FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_fragment,fragment);
        fragmentTransaction.commit();
    }
    @Override
    protected void onStart() {
        super.onStart();
         user = firebaseAuth.getCurrentUser();
        if (user == null) {
            LogoutUser();
        }
        else
        {
            if(user!=null)
            {
                userReference.child("online").setValue("true");
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
//        if(user!=null)
//        {
//            userReference.child("online").setValue(false);
//        }
    }

    private void LogoutUser() {
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
