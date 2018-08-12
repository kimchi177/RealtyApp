package com.example.thu.realtysocial.Profile;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.thu.realtysocial.postNews.PostNewsFragment;
import com.example.thu.realtysocial.Fragment.ProfileFragmentPackage.TimeLineFragment;
import com.example.thu.realtysocial.R;
import com.example.thu.realtysocial.RevealBackgroundView;
import com.example.thu.realtysocial.UserProfileAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity implements RevealBackgroundView.OnStateChangeListener {
    private static final String ProfileActivity = "ProfileActivity";
    private static final String TAG = "ProfileActivity";
    private static final Integer Grallery = 123;
    //    private static final Integer GralleryCOVER = 123;
    private Integer value = 1;
    //bitmap
    Bitmap thumBitmap = null;

    private ProgressDialog loadPrDia;
    @BindView(R.id.proA_editifo)
    LinearLayout proA_editifo;
    @BindView(R.id.linear_add_friends)
    LinearLayout linear_add_friends;
    @BindView(R.id.proA_chat_add)
    LinearLayout proA_chat_add;
    //    @BindView(R.id.ep_editP_cover)
//    RelativeLayout ep_editP_cover;
    @BindView(R.id.ep_editP_avarta)
    RelativeLayout ep_editP_avarta;
    @BindView(R.id.linear_show_accept_decline)
    RelativeLayout linear_show_accept_decline;

    //    @BindView(R.id.proA_sex)
//    TextView proA_sex;
//    @BindView(R.id.profileA_brithday)
//    TextView profileA_brithday;
//    @BindView(R.id.profileA_phone)
//    TextView profileA_phone;
//    @BindView(R.id.profileA_live)
//    TextView profileA_live;
//    @BindView(R.id.profileA_love)
//    TextView profileA_love;
//    @BindView(R.id.profileA_notlove)
//    TextView profileA_notlove;
    @BindView(R.id.proA_name)
    TextView proA_name;
    @BindView(R.id.proA_status)
    TextView proA_status;
    @BindView(R.id.txt_add_friends)
    TextView txt_add_friends;
    @BindView(R.id.txt_request_to_friend)
    TextView txt_request_to_friend;

    //    @BindView(R.id.proA_sexicon)
//    ImageView proA_sexicon;
//    @BindView(R.id.proA_brithdayicon)
//    ImageView proA_brithdayicon;
//    @BindView(R.id.proA_phoneicon)
//    ImageView proA_phoneicon;
//    @BindView(R.id.proA_liveicon)
//    ImageView proA_liveicon;
//    @BindView(R.id.proA_loveicon)
//    ImageView proA_loveicon;
//    @BindView(R.id.proA_notloveicon)
//    ImageView proA_notloveicon;
    @BindView(R.id.ep_avarta)
    CircleImageView ep_avarta;
    //    @BindView(R.id.ep_cover)
//    ImageView ep_cover;
    @BindView(R.id.btn_decline)
    Button btn_decline;
    @BindView(R.id.btn_Accept)
    Button btn_Accept;


    //    firebase
    //    firebase
    DatabaseReference firebaseDatabase;
    DatabaseReference FriendFreference;
    DatabaseReference ListFreferenceRequest;
    DatabaseReference profileDatabase;
    //    DatabaseReference FriendFreference;
    FirebaseAuth firebaseAuth;
    StorageReference firebaseStorage;
    String userIdFriend = "";
    String online_user_id = "";
    String CURRENT_STATE = "";
    private DatabaseReference FriendRequestDatabase;
//    StorageReference thumFirebaseStorage;

    //    interface
    public static final String ARG_REVEAL_START_LOCATION = "reveal_start_location";

    private static final int USER_OPTIONS_ANIMATION_DELAY = 300;
    private static final Interpolator INTERPOLATOR = new DecelerateInterpolator();

    @BindView(R.id.vRevealBackground)
    RevealBackgroundView vRevealBackground;
//    @BindView(R.id.rvUserProfile)
//    RecyclerView rvUserProfile;

    @BindView(R.id.tlUserProfileTabs)
    BottomNavigationView tlUserProfileTabs;
    @BindView(R.id.profile_fragment)
    FrameLayout profile_fragment;
// @BindView(R.id.tlUserProfileTabs)
//    TabLayout tlUserProfileTabs;

    ////    @BindView(R.id.ivUserProfilePhoto)
////    ImageView ivUserProfilePhoto;
    @BindView(R.id.vUserDetails)
    View vUserDetails;
    @BindView(R.id.vUserStats)
    View vUserStats;
    @BindView(R.id.vUserProfileRoot)
    View vUserProfileRoot;
    @BindView(R.id.acti_profile_toolbar)
    android.support.v7.widget.Toolbar acti_profile_toolbar;
//    @BindView(R.id.profile_bar_layout)
//    android.support.v7.widget.Toolbar profile_bar_layout;
//    TextView name_txt_username;
    private int avatarSize;
    private String profilePhoto;
    private UserProfileAdapter userPhotosAdapter;

    //    Fragment
    private PostNewsFragment postNewsFragment;
    private TimeLineFragment timeLineFragment;

    public static void startUserProfileFromLocation(int[] startingLocation, Activity startingActivity, String user_id) {
        Intent intent = new Intent(startingActivity, ProfileActivity.class);
        intent.putExtra(ARG_REVEAL_START_LOCATION, startingLocation);
        intent.putExtra("user_id", user_id);
        startingActivity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);
        postNewsFragment=new PostNewsFragment();
        timeLineFragment=new TimeLineFragment();
        setFragment(timeLineFragment);
        setTabbar();

//        setupTabs();
//        setupUserProfileGrid();
        setupRevealBackground(savedInstanceState);

        loadPrDia = new ProgressDialog(this);
        firebaseAuth = FirebaseAuth.getInstance();
        online_user_id = firebaseAuth.getCurrentUser().getUid();
        userIdFriend = online_user_id;
        try {
            userIdFriend = getIntent().getExtras().get("user_id").toString(); /// cai muon gui
        } catch (Exception e) {
        }
        Log.d("AAABEMEO", userIdFriend);
//        GoneView();
        InitFirebase();
        AddEvent();
        Init();
        tlUserProfileTabs.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
              return ChooseBottomNavi(item);
            }
        });
    }

    private void setTabbar() {
        setSupportActionBar(acti_profile_toolbar);
//        setSupportActionBar(profile_bar_layout);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowCustomEnabled(true);
//        LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        View action_bar_view = layoutInflater.inflate(R.layout.profile_custom_bar, null);
//        actionBar.setCustomView(action_bar_view);
//        name_txt_username = findViewById(R.id.name_txt_username);
    }

    private boolean ChooseBottomNavi(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_post_news:
                tlUserProfileTabs.setItemBackgroundResource(R.color.colorPrimary);
                setFragment(postNewsFragment);
                return true;
            case R.id.nav_time_line:
                tlUserProfileTabs.setItemBackgroundResource(R.color.colorPrimary);
                setFragment(timeLineFragment);
                return true;
            case R.id.nav_image_profile:
                tlUserProfileTabs.setItemBackgroundResource(R.color.colorAccent);
                setFragment(postNewsFragment);
                return true;
            default:
                return false;
        }
    }

    private void setFragment(Fragment fragment) {
        android.support.v4.app.FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.profile_fragment,fragment);
        fragmentTransaction.commit();
    }

    //
//    private void setupTabs() {
//        tlUserProfileTabs.addTab(tlUserProfileTabs.newTab().setIcon(R.drawable.ic_dislike));
//        tlUserProfileTabs.addTab(tlUserProfileTabs.newTab().setIcon(R.drawable.ic_dislike));
//        tlUserProfileTabs.addTab(tlUserProfileTabs.newTab().setIcon(R.drawable.ic_dislike));
//        tlUserProfileTabs.addTab(tlUserProfileTabs.newTab().setIcon(R.drawable.ic_dislike));
//    }

//    private void setupUserProfileGrid() {
//        final StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
//        rvUserProfile.setLayoutManager(layoutManager);
//        rvUserProfile.setOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//                userPhotosAdapter.setLockedAnimations(true);
//            }
//        });
//    }

    private void setupRevealBackground(Bundle savedInstanceState) {
        vRevealBackground.setOnStateChangeListener(this);
        if (savedInstanceState == null) {
            final int[] startingLocation = getIntent().getIntArrayExtra(ARG_REVEAL_START_LOCATION);
            vRevealBackground.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    vRevealBackground.getViewTreeObserver().removeOnPreDrawListener(this);
                    vRevealBackground.startFromLocation(startingLocation);
                    return true;
                }
            });
        } else {
            vRevealBackground.setToFinishedFrame();
            userPhotosAdapter.setLockedAnimations(true);
        }
    }

    @Override
    public void onStateChange(int state) {
        if (RevealBackgroundView.STATE_FINISHED == state) {
//            rvUserProfile.setVisibility(View.VISIBLE);
            tlUserProfileTabs.setVisibility(View.VISIBLE);
            ep_avarta.setVisibility(View.VISIBLE);
            userPhotosAdapter = new UserProfileAdapter(this);
//            rvUserProfile.setAdapter(userPhotosAdapter);
            animateUserProfileOptions();
            animateUserProfileHeader();
        }
// else {
//            tlUserProfileTabs.setVisibility(View.INVISIBLE);
//            rvUserProfile.setVisibility(View.INVISIBLE);
//            vUserProfileRoot.setVisibility(View.INVISIBLE);
//        }
    }

    private void animateUserProfileOptions() {
        tlUserProfileTabs.setTranslationY(-tlUserProfileTabs.getHeight());
        tlUserProfileTabs.animate().translationY(0).setDuration(300).setStartDelay(USER_OPTIONS_ANIMATION_DELAY).setInterpolator(INTERPOLATOR);
    }

    private void animateUserProfileHeader() {
        vUserProfileRoot.setTranslationY(-vUserProfileRoot.getHeight());
        ep_avarta.setTranslationY(-ep_avarta.getHeight());
        vUserDetails.setTranslationY(-vUserDetails.getHeight());
        vUserStats.setAlpha(0);

        vUserProfileRoot.animate().translationY(0).setDuration(300).setInterpolator(INTERPOLATOR);
        ep_avarta.animate().translationY(0).setDuration(300).setStartDelay(100).setInterpolator(INTERPOLATOR);
        vUserDetails.animate().translationY(0).setDuration(300).setStartDelay(200).setInterpolator(INTERPOLATOR);
        vUserStats.animate().alpha(1).setDuration(200).setStartDelay(400).setInterpolator(INTERPOLATOR).start();
    }

    //
    private void Init() {
        CURRENT_STATE = "not_friends";
        linear_show_accept_decline.setVisibility(View.GONE);
        btn_decline.setVisibility(View.INVISIBLE);
        btn_decline.setEnabled(false);
        //avoid myseft send to myseft.
        if (!online_user_id.equals(userIdFriend)) {
            AddButtonSend();
//            ep_editP_cover.setVisibility(View.INVISIBLE);
            proA_editifo.setVisibility(View.GONE);
            ep_editP_avarta.setVisibility(View.INVISIBLE);
            proA_chat_add.setVisibility(View.VISIBLE);
            firebaseDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(userIdFriend);
            firebaseDatabase.keepSynced(true);
        } else {
            btn_decline.setVisibility(View.INVISIBLE);
            linear_add_friends.setVisibility(View.INVISIBLE);
            proA_chat_add.setVisibility(View.GONE);
            firebaseDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(online_user_id);
            firebaseDatabase.keepSynced(true);
        }
        LoadDatatoFirebase();
    }

    private void InitFirebase() {
        //init value

//        thumFirebaseStorage = FirebaseStorage.getInstance().getReference().child("photo_avarta");
        FriendRequestDatabase = FirebaseDatabase.getInstance().getReference().child("Friend_Request");
        FriendRequestDatabase.keepSynced(true);
        ListFreferenceRequest = FirebaseDatabase.getInstance().getReference().child("List_Friend_Request");
        ListFreferenceRequest.keepSynced(true);
        FriendFreference = FirebaseDatabase.getInstance().getReference().child("Friends");
        FriendFreference.keepSynced(true);
        profileDatabase = FirebaseDatabase.getInstance().getReference().child("users");
//        firebaseDatabase.keepSynced(true);
//        if (!online_user_id.equals(userIdFriend)) {
//            ep_editP_cover.setVisibility(View.INVISIBLE);
//            proA_editifo.setVisibility(View.GONE);
//            ep_editP_avarta.setVisibility(View.INVISIBLE);
//            proA_chat_add.setVisibility(View.VISIBLE);
//            firebaseDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(userIdFriend);
//            firebaseDatabase.keepSynced(true);
//        } else {
//            proA_chat_add.setVisibility(View.GONE);
//            firebaseDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(online_user_id);
//            firebaseDatabase.keepSynced(true);
//        }
//        LoadDatatoFirebase();

    }

//    private void GoneView() {
//        if (proA_sex.getText().toString().equals("")) {
//            proA_sex.setVisibility(View.GONE);
//            proA_sexicon.setVisibility(View.GONE);
//        } else {
//            proA_sex.setVisibility(View.VISIBLE);
//            proA_sexicon.setVisibility(View.VISIBLE);
//        }
//        if (profileA_brithday.getText().toString().equals("")) {
//            profileA_brithday.setVisibility(View.GONE);
//            proA_brithdayicon.setVisibility(View.GONE);
//        } else {
//            profileA_brithday.setVisibility(View.VISIBLE);
//            proA_brithdayicon.setVisibility(View.VISIBLE);
//        }
//        if (profileA_phone.getText().toString().equals("")) {
//            profileA_phone.setVisibility(View.GONE);
//            proA_phoneicon.setVisibility(View.GONE);
//        } else {
//            profileA_phone.setVisibility(View.VISIBLE);
//            proA_phoneicon.setVisibility(View.VISIBLE);
//        }
//        if (profileA_live.getText().toString().equals("")) {
//            profileA_live.setVisibility(View.GONE);
//            proA_liveicon.setVisibility(View.GONE);
//        } else {
//            profileA_live.setVisibility(View.VISIBLE);
//            proA_liveicon.setVisibility(View.VISIBLE);
//        }
//        if (proA_status.getText().toString().equals("")) {
//            proA_status.setVisibility(View.GONE);
//        } else {
//            proA_status.setVisibility(View.VISIBLE);
//        }
//    }

    @OnClick(R.id.proA_editifo)
    public void ClickEditInfo() {
        Intent editIntent = new Intent(ProfileActivity.this, EditProfileActivity.class);
        startActivity(editIntent);
    }

    @OnClick(R.id.btn_Accept)
    public void Accept() {
        AcceptRequestMethod();
    }

    private void LoadDatatoFirebase() {
        firebaseDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

//                loadChild(dataSnapshot);
                try {
                    String username = dataSnapshot.child("username").getValue().toString();
                    String status = dataSnapshot.child("status").getValue().toString();
                    String sex = dataSnapshot.child("sex").getValue().toString();
                    String briday = dataSnapshot.child("briday").getValue().toString();
                    String phone_number = dataSnapshot.child("phone_number").getValue().toString();
                    String address = dataSnapshot.child("address").getValue().toString();
                    String starLove = dataSnapshot.child("starLove").getValue().toString();
                    String starHate = dataSnapshot.child("starHate").getValue().toString();

                    proA_name.setText(username);
                    proA_status.setText(status);
//                    proA_sex.setText(sex);
//                    profileA_brithday.setText(briday);
//                    profileA_phone.setText(phone_number);
//                    profileA_live.setText(address);
//                    profileA_love.setText(starLove);
//                    profileA_notlove.setText(starHate);
//                    GoneView();
                    try {
                        final String ImageAvartar = dataSnapshot.child("imageAvartar").getValue().toString();
                        Picasso.get().load(ImageAvartar).networkPolicy(NetworkPolicy.OFFLINE).
                                into(ep_avarta, new Callback() {
                                    @Override
                                    public void onSuccess() {

                                    }

                                    @Override
                                    public void onError(Exception e) {
                                        Picasso.get().load(ImageAvartar).placeholder(R.drawable.noavarta).into(ep_avarta);
                                    }
                                });
//                    Picasso.get().load(image).into(circle_setting);
                    } catch (Exception e) {
                    }

//                    try {
//                        final String ImageCover = dataSnapshot.child("imageCover").getValue().toString();
//                        Picasso.get().load(ImageCover).networkPolicy(NetworkPolicy.OFFLINE).
//                                into(ep_cover, new Callback() {
//                                    @Override
//                                    public void onSuccess() {
//
//                                    }
//
//                                    @Override
//                                    public void onError(Exception e) {
//                                        Picasso.get().load(ImageCover).placeholder(R.drawable.nocover).into(ep_cover);
//                                    }
//                                });
////                    Picasso.get().load(image).into(circle_setting);
//                    } catch (Exception e) {
//                    }
                } catch (Exception e) {
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

//    @OnClick(R.id.ep_editP_cover)
//    public void EditPhotoCover() {
//        Log.d(ProfileActivity, "ProfileActivity");
//        firebaseStorage = FirebaseStorage.getInstance().getReference().child("photo_cover");
//        value = 3;
//        ChooseImage();
//    }


    @OnClick(R.id.ep_editP_avarta)
    public void EditPhotoAvarta() {
        Log.d(ProfileActivity, "ProfileActivity");
        firebaseStorage = FirebaseStorage.getInstance().getReference().child("photo_avarta");
        value = 2;
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
            Uri uriImage = data.getData();
//            Log.d(TAG, "onActivityResult: " +resultUri+"");
            CropImage.activity().setGuidelines(CropImageView.Guidelines.ON).
                    setAspectRatio(1, 1).start(this);
        }
//        if (requestCode == GralleryCOVER && resultCode == RESULT_OK && data != null) {
//            Uri uriImage = data.getData();
//            CropImage.activity().setGuidelines(CropImageView.Guidelines.ON).
//                    setAspectRatio(1, 1).start(this);
//        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                loadPrDia.setTitle("Update profile image");
                loadPrDia.setMessage("Please wait, we updating your profile");
                loadPrDia.show();
                Uri resultUri = result.getUri();
                Log.d(TAG, "onActivityResult: " +resultUri+"");
                String user_id = firebaseAuth.getCurrentUser().getUid();
                StorageReference filePath = firebaseStorage.child(user_id + ".jpg");
//                final StorageReference thumStorageReference = thumFirebaseStorage.child(user_id + ".jpg");
                filePath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(ProfileActivity.this, "save successfull", Toast.LENGTH_SHORT).show();
                            final String dowloadDataURL = task.getResult().getDownloadUrl().toString();
                            if (value == 2) {
                                firebaseDatabase.child("imageAvartar").setValue(dowloadDataURL).
                                        addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                Toast.makeText(ProfileActivity.this, "profile update succsessful", Toast.LENGTH_SHORT).show();
                                                loadPrDia.dismiss();
                                            }
                                        });
                            } else {
                                firebaseDatabase.child("imageCover").setValue(dowloadDataURL).
                                        addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                Toast.makeText(ProfileActivity.this, "profile update succsessful", Toast.LENGTH_SHORT).show();
                                                loadPrDia.dismiss();
                                            }
                                        });
                            }

                        } else {
                            Toast.makeText(ProfileActivity.this, "save erro", Toast.LENGTH_SHORT).show();
                            loadPrDia.dismiss();
                        }
                    }
                });
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }

        }
    }

    //    @OnClick(R.id.btn_decline)
    public void ClickProfile_btnDecline() {
        btn_decline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelRequestMethod("not_friends", "send friend request");
                CancelItemListFriend();
            }
        });
    }

    //    @OnClick(R.id.linear_add_friends)
    public void AddButtonSend() {
        linear_add_friends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linear_add_friends.setEnabled(false);
                if (CURRENT_STATE.equals("not_friends")) {
                    sendRequestMethod();
                }
                if (CURRENT_STATE.equals("request_send")) {
                    cancelRequestMethod("not_friends", "Send friend request");
                }
                if (CURRENT_STATE.equals("request_receiver")) {
                    AcceptRequestMethod();
                }
                if (CURRENT_STATE.equals("friends")) {
                    UnfriendRequestMethod();
                }
            }
        });
    }

    private void UnfriendRequestMethod() {
        FriendFreference.child(online_user_id).child(userIdFriend).removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            FriendFreference.child(userIdFriend).child(online_user_id).removeValue()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                linear_add_friends.setEnabled(true);
                                                CURRENT_STATE = "not_friends";
                                                txt_add_friends.setText("send friend request");

                                                linear_show_accept_decline.setVisibility(View.GONE);
                                                btn_decline.setVisibility(View.INVISIBLE);
                                                btn_decline.setEnabled(false);
                                            }
                                        }
                                    });
                        }
                    }
                });
    }

    private void AcceptRequestMethod() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MMMM-yyyy");
        final String saveCurrentDate = currentDate.format(calendar.getTime());
        FriendFreference.child(online_user_id).child(userIdFriend).child("date").setValue(saveCurrentDate)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        FriendFreference.child(userIdFriend).child(online_user_id).child("date").setValue(saveCurrentDate)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        cancelRequestMethod("friends", "Unfriend");

                                    }
                                });
                    }
                });
    }

    private void cancelRequestMethod(final String value, final String btnSend) {
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
                                                linear_add_friends.setEnabled(true);
                                                CURRENT_STATE = value;
                                                txt_add_friends.setText(btnSend);

                                                linear_show_accept_decline.setVisibility(View.GONE);
                                                btn_decline.setVisibility(View.INVISIBLE);
                                                btn_decline.setEnabled(false);
                                                CancelItemListFriend();
                                            }
                                        }
                                    });
                        }


                    }
                });
    }

    private void CancelItemListFriend() {
        ListFreferenceRequest.child(userIdFriend).child(online_user_id).removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d("CancelItemListFriend", "Succe");
                        }
                    }
                });

    }

    private void sendRequestMethod() {
        FriendRequestDatabase.child(online_user_id).child(userIdFriend)
                .child("request_type").setValue("sent")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            FriendRequestDatabase.child(userIdFriend).child(online_user_id)
                                    .child("request_type").setValue("receiver").
                                    addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
//                                                ChanceStatus("request_send", "Cancel request");
                                                linear_add_friends.setEnabled(true);
                                                CURRENT_STATE = "request_send";
                                                txt_add_friends.setText("cancel friend request");

                                                linear_show_accept_decline.setVisibility(View.GONE);
                                                btn_decline.setVisibility(View.INVISIBLE);
                                                btn_decline.setEnabled(false);
                                                InsertListRequestFriend();
                                            }
                                        }
                                    });
                        }
                    }
                });
    }

    private void InsertListRequestFriend() {
        ListFreferenceRequest.child(userIdFriend)
                .child(online_user_id).child("idFriendRequest").setValue(online_user_id)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d("InsertListRequestFriend", "Succe");
                        }
                    }
                });
    }

    private void AddEvent() {
        profileDatabase.child(userIdFriend).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                FriendRequestDatabase.child(online_user_id).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            if (dataSnapshot.hasChild(userIdFriend)) {
                                String reg_type = dataSnapshot.child(userIdFriend).child("request_type").getValue().toString();
                                if (reg_type.equals("sent")) {
                                    CURRENT_STATE = "request_send";
                                    txt_add_friends.setText("cancel friend request");

                                    linear_show_accept_decline.setVisibility(View.GONE);
                                    btn_decline.setVisibility(View.INVISIBLE);
                                    btn_decline.setEnabled(false);
                                } else {
                                    if (reg_type.equals("receiver")) {
                                        CURRENT_STATE = "request_receiver";
                                        txt_add_friends.setText("accept friend request");

                                        btn_decline.setVisibility(View.VISIBLE);
                                        btn_decline.setEnabled(true);
                                        linear_show_accept_decline.setVisibility(View.VISIBLE);
                                        txt_request_to_friend.setText(proA_name.getText().toString() + "have request friend to you");
//haha
                                        ClickProfile_btnDecline();
                                    }
                                }

                            }
                        } else {
                            FriendFreference.child(online_user_id)
                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            if (dataSnapshot.hasChild(userIdFriend)) {
                                                CURRENT_STATE = "friends";
                                                txt_add_friends.setText("Unfriend friend");

                                                btn_decline.setVisibility(View.INVISIBLE);
                                                btn_decline.setEnabled(false);
                                                linear_show_accept_decline.setVisibility(View.GONE);
                                            }
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        AddButtonSend();
    }

}
