package com.example.thu.realtysocial.postNews.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.thu.realtysocial.R;
import com.example.thu.realtysocial.postNews.RefreshImageListener;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private static final String TAG = "RecyclerViewAdapter";

    //vars
//    private ArrayList<String> mNames = new ArrayList<>();
    private ArrayList<String> mImageUrls = new ArrayList<>();
    private ArrayList<Bitmap> mImageBitmap = new ArrayList<>();
    private RefreshImageListener listener;
    private Context mContext;
    private Boolean test = false;

    public RecyclerViewAdapter(Context mContext, ArrayList<String> mImageUrls, RefreshImageListener listener) {
        this.mContext = mContext;
        this.mImageUrls = mImageUrls;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_array_image_post, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: called.");

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true; //Chỉ đọc thông tin ảnh, không đọc dữ liwwuj
//        BitmapFactory.decodeFile(mImageUrls.get(position),options); //Đọc thông tin ảnh
        options.inSampleSize = 4; //Scale bitmap xuống 4 lần
        options.inJustDecodeBounds = false; //Cho phép đọc dữ liệu ảnh ảnh
        Bitmap image = BitmapFactory.decodeFile(mImageUrls.get(position), options);
        holder.item_image.setImageBitmap(image);
        holder.item_image_cover_black.setVisibility(View.VISIBLE);
        holder.item_image_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                test = true;
                mImageUrls.remove(position);
                mImageBitmap.removeAll(mImageBitmap);
                Log.d(TAG, "onClick: xoa het chua - "+mImageBitmap.size());
                listener.onImageBitmap(1,mImageBitmap);
                listener.onRemoveItemImage(mImageUrls);
            }
        });
        holder.item_image.setDrawingCacheEnabled(true);
        holder.item_image.buildDrawingCache();
        Bitmap bitmap = ((BitmapDrawable) holder.item_image.getDrawable()).getBitmap();
        mImageBitmap.add(bitmap);
        Log.d(TAG, "onBindViewHolder: mImageUrls" + mImageUrls.size() + "");
        Log.d(TAG, "onBindViewHolder: mImageBitmap " + mImageBitmap.size() + "");
        Log.d(TAG, "onBindViewHolder: i  " + position + "");
        if (mImageUrls.size() == (position + 1)) {
            Log.d(TAG, "onBindViewHolder: mImageBitmap.size()==position  ");
            listener.onImageBitmap(2,mImageBitmap);
        }
        test = false;
//


//        holder.item_image_cover_black.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent t = new Intent(mContext, MainActivity_LearnSpeak.class);
//                t.putExtra("id_Ipa", holder.txt_idIpa.getText().toString());
////                Toast.makeText(mContext, holder.txt_idIpa.getText().toString(), Toast.LENGTH_SHORT).show();
//
//                mContext.startActivity(t);
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return mImageUrls.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView item_summuber;
        ImageView item_image;
        ImageView item_image_close;
        ImageView item_image_cover_black;


        public ViewHolder(View itemView) {
            super(itemView);
            item_summuber = itemView.findViewById(R.id.item_summuber);
            item_image = itemView.findViewById(R.id.item_image);
            item_image_close = itemView.findViewById(R.id.item_image_close);
            item_image_cover_black = itemView.findViewById(R.id.item_image_cover_black);

        }
    }
}