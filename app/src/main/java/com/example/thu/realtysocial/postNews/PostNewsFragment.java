package com.example.thu.realtysocial.postNews;


import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.thu.realtysocial.Fragment.ProfileFragmentPackage.TimeLineFragment;
import com.example.thu.realtysocial.R;
import com.example.thu.realtysocial.activity.MainActivity;
import com.example.thu.realtysocial.postNews.adapters.RecyclerViewAdapter;
import com.example.thu.realtysocial.postNews.model.Image;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class PostNewsFragment extends Fragment implements RefreshImageListener {
    private static final String TAG = "PostNewsFragment";
    TextView textView;
    public ArrayList<String> mImageUrls = new ArrayList<>();
    public ArrayList<Bitmap> mImageBitmap = new ArrayList<>();
    RecyclerView recyclerView;
    RadioGroup frag_post_RG;
    private String chooseType = "";
    Button frag_post_btn_share;
    FirebasePostNews firebasePostNews;
    EditText frag_post_post, input_price;
    RadioButton frag_post_ra_Lease, frag_post_ra_sele, frag_post_ra_Seller;
    private DatabaseReference myRef;
    private DatabaseReference myRefSaveDateTime;
    private FirebaseAuth mAuth;
    private String userID;
    StorageReference firebaseStorage = FirebaseStorage.getInstance().getReference().child("image_news_post");
    private String mValuePush = "";
    private int code = 4;
    RelativeLayout frag_post_phovi;
    private ProgressDialog progressDialog;
    private TimeLineFragment timeLineFragment;

    public PostNewsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_post_news, container, false);
        timeLineFragment=new TimeLineFragment();
        progressDialog = new ProgressDialog(getActivity());
        firebasePostNews = new FirebasePostNews(getActivity(), this);

        frag_post_RG = view.findViewById(R.id.frag_post_RG);
        frag_post_post = view.findViewById(R.id.frag_post_post);
        input_price = view.findViewById(R.id.input_price);
        RelativeLayout frag_selection = view.findViewById(R.id.frag_selection);
        frag_post_phovi = view.findViewById(R.id.frag_post_phovi);
        Button frag_post_btn_cancel = view.findViewById(R.id.frag_post_btn_cancel);
        frag_post_btn_share = view.findViewById(R.id.frag_post_btn_share);
        recyclerView = view.findViewById(R.id.frag_post_recyclerView);
        frag_post_ra_Lease = view.findViewById(R.id.frag_post_ra_Lease);
        frag_post_ra_sele = view.findViewById(R.id.frag_post_ra_sele);
        frag_post_ra_Seller = view.findViewById(R.id.frag_post_ra_Seller);

        mAuth = FirebaseAuth.getInstance();
        userID = mAuth.getCurrentUser().getUid();

        frag_post_phovi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent chosseimageIntent = new Intent(getActivity(), AlbumSelectActivity.class);
//                startActivity(chosseimageIntent);
                Intent intent = new Intent(getActivity(), AlbumSelectActivity.class);
                intent.putExtra(Constants.INTENT_EXTRA_LIMIT, code);
                startActivityForResult(intent, Constants.REQUEST_CODE);
            }
        });
        CheckTypeUser();
        ClickShare();
        return view;
    }

    private void setFragment(Fragment fragment) {
        android.support.v4.app.FragmentTransaction fragmentTransaction=getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.profile_fragment,fragment);
        fragmentTransaction.commit();
    }

    private void ClickShare() {
        frag_post_btn_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = frag_post_post.getText().toString();
                String price = input_price.getText().toString();
                if (chooseType.equals("") && TextUtils.isEmpty(content) && TextUtils.isEmpty(price))//||(chooseType.equals("") || TextUtils.isEmpty(content) || TextUtils.isEmpty(price)))
                {
                    Toast.makeText(getActivity(), "you need input full information.", Toast.LENGTH_SHORT).show();
                } else {
                    progressDialog.setTitle("Please wailt...");
                    progressDialog.setMessage("Wifi will strong speed upload.");
                    progressDialog.show();
                    firebasePostNews.addNews(chooseType, content, "dfdf", Double.parseDouble(price));
                    Log.d(TAG, "onClick: " + mValuePush);
//                    if (!mValuePush.equals("")) {
//                        AddImage();
//                        frag_post_post.setText("");
//                        input_price.setText("");
//                        frag_post_ra_Lease.setChecked(false);
//                        frag_post_ra_sele.setChecked(false);
//                        frag_post_ra_Seller.setChecked(false);
//                        recyclerView.setVisibility(View.GONE);
//                    }
                }
            }
        });
    }

    private void AddImage(final String date) {
        // Get the data from an ImageView as bytes
//      final   int mControl;
        myRef = FirebaseDatabase.getInstance().getReference().child("image_post_news").child(userID).push();
        for (int i = 0, l = mImageBitmap.size(); i < l; i++) {
            Log.d(TAG, "AddImage: " + mImageBitmap.size() + "");
            final int mControl = i;
            Calendar calendar = Calendar.getInstance();
            StorageReference mountainsRef = firebaseStorage.child("image" + calendar.getTimeInMillis() + "png");
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            mImageBitmap.get(i).compress(Bitmap.CompressFormat.PNG, 100, baos);
            byte[] data = baos.toByteArray();

            UploadTask uploadTask = mountainsRef.putBytes(data);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle unsuccessful uploads
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                    // ...
                    Uri dowloadUri = taskSnapshot.getDownloadUrl();
                    Log.d("AAA", dowloadUri + "");
//                    InsertImage(dowloadUri);
                    Log.d(TAG, "onSuccess: " + date);
                    myRef.child("image_" + mControl).setValue(dowloadUri + "", new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                            if (databaseError != null) {
                                Toast.makeText(getActivity(), "save image and link Fail", Toast.LENGTH_SHORT).show();
                                Log.d(TAG, "onComplete: save image and link Fail");
                            } else {
                                Log.d(TAG, "onComplete: ave image and link Successful");
                            }
                        }
                    });
                }
            });
        }
        myRef.child("date").setValue(date, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError != null) {
                    Toast.makeText(getActivity(), "your news post Fail", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "your news post suceesful.", Toast.LENGTH_SHORT).show();
//                    Intent intentTimeLine = new Intent(getActivity(), TimeLineFragment.class);
//                getActivity().startActivity(intentTimeLine);
//                getActivity().finish();
                    setFragment(timeLineFragment);
                }
//                Intent intentTimeLine = new Intent(getActivity(), TimeLineFragment.class);
//                getActivity().startActivity(intentTimeLine);
//                getActivity().finish();

            }
        });
    }

    private void CheckTypeUser() {
        frag_post_RG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.frag_post_ra_Lease:
                        chooseType = "Lease";
                        break;
                    case R.id.frag_post_ra_sele:
                        chooseType = "Seller and Lease";
                        break;
                    case R.id.frag_post_ra_Seller:
                        chooseType = "Seller";
                        break;
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            ArrayList<Image> images = data.getParcelableArrayListExtra(Constants.INTENT_EXTRA_IMAGES);
            StringBuffer stringBuffer = new StringBuffer();
            for (int i = 0, l = images.size(); i < l; i++) {
                stringBuffer.append(images.get(i).path + "\n");
                mImageUrls.add(images.get(i).path);
            }
            mImageBitmap.removeAll(mImageBitmap);
            if (images.size() < code) {
                code = 4 - mImageUrls.size();
                frag_post_phovi.setAlpha(1f);
                frag_post_phovi.setEnabled(true);
            } else {
                frag_post_phovi.setAlpha(0.3f);
                frag_post_phovi.setEnabled(false);
            }
            if (mImageUrls.size() > 0)
                recyclerView.setVisibility(View.VISIBLE);
            else
                recyclerView.setVisibility(View.GONE);
            Log.d(TAG, "onActivityResult: " + mImageUrls.size() + "");
//            textView.setText(stringBuffer.toString());
            PushDataImage(mImageUrls);

        }
    }

    private void PushDataImage(ArrayList<String> image) {

        if (image.size() > 0)
            recyclerView.setVisibility(View.VISIBLE);
        else
            recyclerView.setVisibility(View.GONE);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(getActivity(), image, this);
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onRemoveItemImage(ArrayList<String> image) {
//        Log.d(TAG, "onRemoveItemImage: "+image.size());
//        mImageUrls.removeAll(mImageUrls);
//        for (int i = 0, l = image.size(); i < l; i++) {
//            mImageUrls.add(image.get(i));
//            Log.d(TAG, "onRemoveItemImage: "+mImageUrls.size());
//        }
        Log.d(TAG, "onRemoveItemImage: mImageUrls " + mImageUrls.size());
        code = 4 - image.size();
        frag_post_phovi.setAlpha(1f);
        frag_post_phovi.setEnabled(true);
        PushDataImage(image);
    }

    //suceesful listener.
    @Override
    public void onPostImage(String valuePush) {
        mValuePush = valuePush;
        Log.d(TAG, "onPostImage: " + valuePush);
        Log.d(TAG, "onPostImage: mValuePush" + mValuePush);
        AddImage(valuePush);
        progressDialog.dismiss();
        frag_post_post.setText("");
        input_price.setText("");
        frag_post_ra_Lease.setChecked(false);
        frag_post_ra_sele.setChecked(false);
        frag_post_ra_Seller.setChecked(false);
        recyclerView.setVisibility(View.GONE);
        mImageUrls.removeAll(mImageUrls);
        mImageBitmap.removeAll(mImageBitmap);
        frag_post_phovi.setAlpha(1f);
        frag_post_phovi.setEnabled(true);
        Log.d(TAG, "onPostImage: " + mImageUrls.size());
        code = 4;
    }

    @Override
    public void onImageBitmap(int value, ArrayList<Bitmap> bitmaps) {
        Log.d(TAG, "onImageBitmap: " + bitmaps.size());
//        if (value == 1) {
//            bitmaps.removeAll(bitmaps);
//            mImageBitmap.removeAll(mImageBitmap);
//        } else {
//            for (int i = 0, l = bitmaps.size(); i < l; i++) {
//                mImageBitmap.add(bitmaps.get(i));
//                Log.d(TAG, "onImageBitmap: i" + bitmaps.get(i));
//            }
//        }
//        bitmaps.removeAll(bitmaps);
//        mImageBitmap.removeAll(mImageBitmap);
        mImageBitmap.removeAll(mImageBitmap);
        for (int i = 0, l = bitmaps.size(); i < l; i++) {
            mImageBitmap.add(bitmaps.get(i));
            Log.d(TAG, "onImageBitmap: i" + bitmaps.get(i));
        }
        Log.d(TAG, "onImageBitmap: mImageBitmap" + mImageBitmap.size());
    }
}
