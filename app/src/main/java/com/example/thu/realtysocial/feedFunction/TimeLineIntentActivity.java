package com.example.thu.realtysocial.feedFunction;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.animation.OvershootInterpolator;

import com.example.thu.realtysocial.R;
import com.example.thu.realtysocial.Utils.Utils;
import com.example.thu.realtysocial.model.PostNews;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class TimeLineIntentActivity extends BaseActivity implements FeedAdapter.OnFeedItemClickListener,
        FeedContextMenu.OnFeedContextMenuItemClickListener  {
//    PostNews postNews = postNewsArrayList.get(position);
//        cellFeedViewHolder.item_feed_name.setText(postNews.getDate());
//        cellFeedViewHolder.item_feed_content.setText(postNews.getContent());
//        cellFeedViewHolder.item_feed_type_users.setText(postNews.getTypeNews());
//        cellFeedViewHolder.item_feed_type_price.setText(postNews.getPrice()+"");
//        cellFeedViewHolder.btnComments.setOnClickListener(new View.OnClickListener()
    public static final String ACTION_SHOW_LOADING_ITEM = "action_show_loading_item";

    private static final int ANIM_DURATION_TOOLBAR = 300;
    private static final int ANIM_DURATION_FAB = 400;

    @BindView(R.id.rvFeed)
    RecyclerView rvFeed;
//    @BindView(R.id.btnCreate)
//    FloatingActionButton fabCreate;
    @BindView(R.id.content)
    CoordinatorLayout clContent;

    private FeedAdapter feedAdapter;

    private boolean pendingIntroAnimation;

    //

    ArrayList<PostNews> postNewsArrayList = new ArrayList<>();
    private DatabaseReference feedFerence;
    private DatabaseReference usersFerence;
    private FirebaseAuth mAuth;
    String online_user_id = "";
    String mUsername="";
    String mImageAvartar="";
    String a="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_line_intent);
        initFirebase();
        setupFeed();

        if (savedInstanceState == null) {
            pendingIntroAnimation = true;
        } else {
//            feedAdapter.updateItems(false);
        }
        LoadData();
    }

    private void initFirebase() {
        mAuth = FirebaseAuth.getInstance();
        online_user_id = mAuth.getCurrentUser().getUid();
        feedFerence = FirebaseDatabase.getInstance().getReference().child("post_news").child(online_user_id);
        usersFerence = FirebaseDatabase.getInstance().getReference().child("users").child(online_user_id);
        feedFerence.keepSynced(true);
    }

    private void LoadData() {
        feedFerence.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                PostNews postNews = dataSnapshot.getValue(PostNews.class);
                postNewsArrayList.add(postNews);
                feedAdapter.notifyDataSetChanged();
//                String ImageAvartar = dataSnapshot.child("imageAvartar").getValue().toString();
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

    private void setupFeed() {
        usersFerence.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mUsername = dataSnapshot.child("username").getValue().toString();
                mImageAvartar = dataSnapshot.child("imageAvartar").getValue().toString();
                load(mUsername,mImageAvartar);
                Log.d("KHAKHA", "onDataChange: " +mUsername);
                Log.d("KHAKHA", "onDataChange  a: " +a);
                Log.d("KHAKHA", "onDataChange: " +mImageAvartar);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        Log.d("KHAKHA", "setupFeed: "+a);
        Log.d("KHAKHA", "setupFeed: "+mImageAvartar);
//        feedAdapter = new FeedAdapter(this,postNewsArrayList,mUsername,mImageAvartar);
    }

    private void load(String mUsername,String mImageAvartar) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this) {
            @Override
            protected int getExtraLayoutSpace(RecyclerView.State state) {
                return 300;
            }
        };
        rvFeed.setLayoutManager(linearLayoutManager);
        feedAdapter = new FeedAdapter(this,postNewsArrayList,mUsername,mImageAvartar);
        feedAdapter.setOnFeedItemClickListener(this);
//        rvFeed.setHasFixedSize(true);
        rvFeed.setAdapter(feedAdapter);
        rvFeed.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                FeedContextMenuManager.getInstance().onScrolled(recyclerView, dx, dy);
            }
        });
        rvFeed.setItemAnimator(new FeedItemAnimator());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (ACTION_SHOW_LOADING_ITEM.equals(intent.getAction())) {
            showFeedLoadingItemDelayed();
        }
    }

    private void showFeedLoadingItemDelayed() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                rvFeed.smoothScrollToPosition(0);
                feedAdapter.showLoadingView();
            }
        }, 500);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        if (pendingIntroAnimation) {
            pendingIntroAnimation = false;
            startIntroAnimation();
        }
        return true;
    }

    private void startIntroAnimation() {
//        fabCreate.setTranslationY(2 * getResources().getDimensionPixelOffset(R.dimen.btn_fab_size));

        int actionbarSize = Utils.dpToPx(56);
        getToolbar().setTranslationY(-actionbarSize);
        getIvLogo().setTranslationY(-actionbarSize);
        getInboxMenuItem().getActionView().setTranslationY(-actionbarSize);

        getToolbar().animate()
                .translationY(0)
                .setDuration(ANIM_DURATION_TOOLBAR)
                .setStartDelay(300);
        getIvLogo().animate()
                .translationY(0)
                .setDuration(ANIM_DURATION_TOOLBAR)
                .setStartDelay(400);
        getInboxMenuItem().getActionView().animate()
                .translationY(0)
                .setDuration(ANIM_DURATION_TOOLBAR)
                .setStartDelay(500)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        startContentAnimation();
                    }
                })
                .start();
    }

    private void startContentAnimation() {
//        fabCreate.animate()
//                .translationY(0)
//                .setInterpolator(new OvershootInterpolator(1.f))
//                .setStartDelay(300)
//                .setDuration(ANIM_DURATION_FAB)
//                .start();
//        feedAdapter.updateItems(true);
    }

    @Override
    public void onCommentsClick(View v, int position) {
//        final Intent intent = new Intent(this, CommentsActivity.class);
//        int[] startingLocation = new int[2];
//        v.getLocationOnScreen(startingLocation);
//        intent.putExtra(CommentsActivity.ARG_DRAWING_START_LOCATION, startingLocation[1]);
//        startActivity(intent);
//        overridePendingTransition(0, 0);
    }

    @Override
    public void onMoreClick(View v, int itemPosition) {
        FeedContextMenuManager.getInstance().toggleContextMenuFromView(v, itemPosition, this);
    }

    @Override
    public void onProfileClick(View v) {
//        int[] startingLocation = new int[2];
//        v.getLocationOnScreen(startingLocation);
//        startingLocation[0] += v.getWidth() / 2;
//        UserProfileActivity.startUserProfileFromLocation(startingLocation, this);
//        overridePendingTransition(0, 0);
    }

    @Override
    public void onReportClick(int feedItem) {
        FeedContextMenuManager.getInstance().hideContextMenu();
    }

    @Override
    public void onSharePhotoClick(int feedItem) {
        FeedContextMenuManager.getInstance().hideContextMenu();
    }

    @Override
    public void onCopyShareUrlClick(int feedItem) {
        FeedContextMenuManager.getInstance().hideContextMenu();
    }

    @Override
    public void onCancelClick(int feedItem) {
        FeedContextMenuManager.getInstance().hideContextMenu();
    }

//    @OnClick(R.id.btnCreate)
//    public void onTakePhotoClick() {
////        int[] startingLocation = new int[2];
////        fabCreate.getLocationOnScreen(startingLocation);
////        startingLocation[0] += fabCreate.getWidth() / 2;
////        TakePhotoActivity.startCameraFromLocation(startingLocation, this);
////        overridePendingTransition(0, 0);
//    }

    public void showLikedSnackbar() {
        Snackbar.make(clContent, "Liked!", Snackbar.LENGTH_SHORT).show();
    }
}
