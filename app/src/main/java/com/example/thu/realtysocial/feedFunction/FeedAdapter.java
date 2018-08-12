package com.example.thu.realtysocial.feedFunction;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextSwitcher;
import android.widget.TextView;

import com.example.thu.realtysocial.Adapter.MessageAdapter;
import com.example.thu.realtysocial.LastSeenTime;
import com.example.thu.realtysocial.R;
import com.example.thu.realtysocial.activity.MainActivity;
import com.example.thu.realtysocial.model.Messages;
import com.example.thu.realtysocial.model.PostNews;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class FeedAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final String ACTION_LIKE_BUTTON_CLICKED = "action_like_button_button";
    public static final String ACTION_LIKE_IMAGE_CLICKED = "action_like_image_button";

    public static final int VIEW_TYPE_DEFAULT = 1;
    public static final int VIEW_TYPE_LOADER = 2;

    //    private final List<FeedItem> feedItems = new ArrayList<>();
    private List<PostNews> postNewsList = new ArrayList<>();

    private Context context;
    private OnFeedItemClickListener onFeedItemClickListener;

    private boolean showLoadingView = false;
    private String name, linkAvarta;

    public FeedAdapter(Context context, List<PostNews> postNewsList,String name,String linkAvarta) {
        this.postNewsList = postNewsList;
        this.context = context;
        this.name = name;
        this.linkAvarta = linkAvarta;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_DEFAULT) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_feed, parent, false);
            CellFeedViewHolder cellFeedViewHolder = new CellFeedViewHolder(view);
            setupClickableViews(view, cellFeedViewHolder);
            return cellFeedViewHolder;
        } else if (viewType == VIEW_TYPE_LOADER) {
            LoadingFeedItemView view = new LoadingFeedItemView(context);
            view.setLayoutParams(new LinearLayoutCompat.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT)
            );
            return new LoadingCellFeedViewHolder(view);
        }

        return null;
    }

    private void setupClickableViews(final View view, final CellFeedViewHolder cellFeedViewHolder) {
        cellFeedViewHolder.btnComments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                onFeedItemClickListener.onCommentsClick(view, cellFeedViewHolder.getAdapterPosition());
            }
        });
        cellFeedViewHolder.btnMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                onFeedItemClickListener.onMoreClick(v, cellFeedViewHolder.getAdapterPosition());
            }
        });
        cellFeedViewHolder.ivFeedCenter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                int adapterPosition = cellFeedViewHolder.getAdapterPosition();
//                feedItems.get(adapterPosition).likesCount++;
//                notifyItemChanged(adapterPosition, ACTION_LIKE_IMAGE_CLICKED);
//                if (context instanceof TimeLineIntentActivity) {
//                    ((TimeLineIntentActivity) context).showLikedSnackbar();
//                }
            }
        });
        cellFeedViewHolder.btnLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                int adapterPosition = cellFeedViewHolder.getAdapterPosition();
//                feedItems.get(adapterPosition).likesCount++;
//                notifyItemChanged(adapterPosition, ACTION_LIKE_BUTTON_CLICKED);
//                if (context instanceof TimeLineIntentActivity) {
//                    ((TimeLineIntentActivity) context).showLikedSnackbar();
//                }
            }
        });
        cellFeedViewHolder.ivUserProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                onFeedItemClickListener.onProfileClick(view);
            }
        });
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        Log.d("THUTHU", "onBindViewHolder: " +position);
        PostNews postNews = postNewsList.get(position);
//        ((CellFeedViewHolder) viewHolder).bindView(feedItems.get(position));
        ((CellFeedViewHolder) viewHolder).setAddress (postNews.getAddress());
        LastSeenTime getTime = new LastSeenTime();
        Long last_seen = Long.parseLong(postNews.getTime()+"");
        String lastGreenOnline = getTime.getTimeAgo(last_seen).toString();
        ((CellFeedViewHolder) viewHolder).setTime (lastGreenOnline);
        DecimalFormat dec = new DecimalFormat("###,###,###,###");
        String credits = dec.format(postNews.getPrice());
        ((CellFeedViewHolder) viewHolder).setTypePrice (credits);
        ((CellFeedViewHolder) viewHolder).setTypeUSers (postNews.getTypeNews());
        ((CellFeedViewHolder) viewHolder).setContent (postNews.getContent());
        ((CellFeedViewHolder) viewHolder).setUserName (name);
        ((CellFeedViewHolder) viewHolder).SetAvarta_image (linkAvarta);
//        item_feed_name.setText(postNews.getDate());
//        cellFeedViewHolder.item_feed_content.setText(postNews.getContent());
//        cellFeedViewHolder.item_feed_type_users.setText(postNews.getTypeNews());
//        cellFeedViewHolder.item_feed_type_price.setText(postNews.getPrice() + "");

        if (getItemViewType(position) == VIEW_TYPE_LOADER) {
            bindLoadingFeedItem((LoadingCellFeedViewHolder) viewHolder);
        }

    }

    private void bindLoadingFeedItem(final LoadingCellFeedViewHolder holder) {
        holder.loadingFeedItemView.setOnLoadingFinishedListener(new LoadingFeedItemView.OnLoadingFinishedListener() {
            @Override
            public void onLoadingFinished() {
                showLoadingView = false;
                notifyItemChanged(0);
            }
        });
        holder.loadingFeedItemView.startLoading();
    }

    @Override
    public int getItemViewType(int position) {
        if (showLoadingView && position == 0) {
            return VIEW_TYPE_LOADER;
        } else {
            return VIEW_TYPE_DEFAULT;
        }
    }

    @Override
    public int getItemCount() {
        return postNewsList.size();
    }

//    public void updateItems(boolean animated) {
//        feedItems.clear();
//        feedItems.addAll(Arrays.asList(
//                new FeedItem(33, false),
//                new FeedItem(1, false),
//                new FeedItem(223, false),
//                new FeedItem(2, false),
//                new FeedItem(6, false),
//                new FeedItem(8, false),
//                new FeedItem(99, false)
//        ));
//        if (animated) {
//            notifyItemRangeInserted(0, feedItems.size());
//        } else {
//            notifyDataSetChanged();
//        }
//    }

    public void setOnFeedItemClickListener(OnFeedItemClickListener onFeedItemClickListener) {
        this.onFeedItemClickListener = onFeedItemClickListener;
    }

    public void showLoadingView() {
        showLoadingView = true;
        notifyItemChanged(0);
    }

    public static class CellFeedViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.ivFeedCenter)
        ImageView ivFeedCenter;
        @BindView(R.id.ivFeedBottom)
        ImageView ivFeedBottom;
        @BindView(R.id.btnComments)
        ImageButton btnComments;
        @BindView(R.id.btnLike)
        ImageButton btnLike;
        @BindView(R.id.btnMore)
        ImageButton btnMore;
        //        @BindView(R.id.vBgLike)
//        View vBgLike;
//        @BindView(R.id.ivLike)
//        ImageView ivLike;
        @BindView(R.id.tsLikesCounter)
        TextSwitcher tsLikesCounter;
        @BindView(R.id.ivUserProfileA)
        CircleImageView ivUserProfile;
//        @BindView(R.id.vImageRoot)
//        FrameLayout vImageRoot;

        @BindView(R.id.item_feed_name)
        TextView item_feed_name;
        @BindView(R.id.item_feed_content)
        TextView item_feed_content;
        @BindView(R.id.item_feed_type_users)
        TextView item_feed_type_users;
        @BindView(R.id.item_feed_type_price)
        TextView item_feed_type_price;
        @BindView(R.id.item_feed_time)
        TextView item_feed_time;
        @BindView(R.id.item_feed_address)
        TextView item_feed_address;

        //        FeedItem feedItem;
        public CellFeedViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

//        public void bindView(FeedItem feedItem) {
//            this.feedItem = feedItem;
//            int adapterPosition = getAdapterPosition();
//            ivFeedCenter.setImageResource(adapterPosition % 2 == 0 ? R.drawable.img_feed_center_1 : R.drawable.img_feed_center_2);
//            ivFeedBottom.setImageResource(adapterPosition % 2 == 0 ? R.drawable.img_feed_bottom_1 : R.drawable.img_feed_bottom_2);
//            btnLike.setImageResource(feedItem.isLiked ? R.drawable.ic_heart_red : R.drawable.ic_heart_outline_grey);
//            tsLikesCounter.setCurrentText(vImageRoot.getResources().getQuantityString(
//                    R.plurals.likes_count, feedItem.likesCount, feedItem.likesCount
//            ));
//        }

        public void setUserName(String setUserName) {
            item_feed_name.setText(setUserName);
        }

        public void setTypeUSers(String setUserName) {
            item_feed_type_users.setText(setUserName);
        }

        public void setTypePrice(String setTypePrice) {
            item_feed_type_price.setText(setTypePrice + "");
        }

        public void setContent(String setContent) {
            item_feed_content.setText(setContent);
        }

        public void setTime(String setTime) {
            item_feed_time.setText(setTime);
        }

        public void setAddress(String setAddress) {
            item_feed_address.setText(setAddress);
        }
        public void SetAvarta_image(final String avarta_image) {
            try {

                Picasso.get().load(avarta_image).networkPolicy(NetworkPolicy.OFFLINE).placeholder(R.drawable.noticon).into(ivUserProfile, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError(Exception e) {
                        Picasso.get().load(avarta_image).placeholder(R.drawable.noticon).into(ivUserProfile);
                    }
                });
                Picasso.get().load(avarta_image).into(ivUserProfile);
            } catch (Exception e) {
            }
        }
//        public FeedItem getFeedItem() {
//            return feedItem;
//        }
    }

    public static class LoadingCellFeedViewHolder extends CellFeedViewHolder {

        LoadingFeedItemView loadingFeedItemView;

        public LoadingCellFeedViewHolder(LoadingFeedItemView view) {
            super(view);
            this.loadingFeedItemView = view;
        }

//        @Override
//        public void bindView(FeedItem feedItem) {
//            super.bindView(feedItem);
//        }
    }

//    public static class FeedItem {
//        public int likesCount;
//        public boolean isLiked;
//
//        public FeedItem(int likesCount, boolean isLiked) {
//            this.likesCount = likesCount;
//            this.isLiked = isLiked;
//        }
//    }

    public interface OnFeedItemClickListener {
        void onCommentsClick(View v, int position);

        void onMoreClick(View v, int position);

        void onProfileClick(View v);
    }
}