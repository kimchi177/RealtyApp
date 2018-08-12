package com.example.thu.realtysocial.Fragment;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.thu.realtysocial.Profile.EditProfileActivity;
import com.example.thu.realtysocial.Profile.ProfileActivity;
import com.example.thu.realtysocial.R;
import com.example.thu.realtysocial.model.UserFI;
import com.example.thu.realtysocial.model.UserFI;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Queue;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddFriendsFragment extends Fragment {

    //view
    RecyclerView all_recyc;
    EditText sr_edt;
    ImageView sr_imv;

    // ArrayList
    ArrayList<UserFI> userArrayList;

    //firebase
    DatabaseReference AllUserDatabase;
    FirebaseAuth firebaseAuth;

    public AddFriendsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_friends, container, false);
        InitFire();
        all_recyc = view.findViewById(R.id.all_recyc);
        sr_edt = view.findViewById(R.id.sr_edt);
        sr_imv = view.findViewById(R.id.sr_imv);
        Init();
        sr_imv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String valueName=sr_edt.getText().toString();
                if(TextUtils.isEmpty(valueName))
                {
                    Toast.makeText(getActivity(), "Please wait, you need to write information ...", Toast.LENGTH_SHORT).show();
                }
                searchUser(valueName);
            }
        });
        sr_edt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String valueName=sr_edt.getText().toString();
                if(!valueName.equals(""))
                {
                    searchUser(valueName);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        return view;
    }

    private void Init() {
        all_recyc.setHasFixedSize(true);
        all_recyc.setLayoutManager(new LinearLayoutManager(getActivity()));
        userArrayList = new ArrayList<>();
    }

    private void InitFire() {
        AllUserDatabase = FirebaseDatabase.getInstance().getReference().child("users");
        AllUserDatabase.keepSynced(true);
    }

//    @Override
//    public void onStart() {
//        super.onStart();
    public  void searchUser(String valueName){
//        Toast.makeText(getActivity(), "Searching...", Toast.LENGTH_SHORT).show();
        Query searchFriendAndPeople=AllUserDatabase.orderByChild("username")
                .startAt(valueName).endAt(valueName+'\uf8ff');
        FirebaseRecyclerAdapter<UserFI, AllUserViewHolder> allUser_firebase =
                new FirebaseRecyclerAdapter<UserFI, AllUserViewHolder>(
                        UserFI.class,
                        R.layout.item_list_user,
                        AllUserViewHolder.class,
                        searchFriendAndPeople
                ) {
                    @Override
                    protected void populateViewHolder(AllUserViewHolder viewHolder, UserFI model, final int position) {
                        viewHolder.SetUserName(model.getUsername());
                        viewHolder.SetAvarta_image(model.getImageAvartar());
                        Log.d("AAAgetUsername", model.getUsername()+"");
                        Log.d("AAAgetImageAvartar", model.getImageAvartar()+"");
                        Log.d("AAAgetgetImageCover", model.getImageCover()+"");
                        Log.d("AAAgetPhone_number", model.getPhone_number()+"");

                        viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(final View v) {
                                final String user_id = getRef(position).getKey();
//                                Intent profileIntent = new Intent(getActivity(), ProfileActivity.class);
//                                profileIntent.putExtra("user_id", user_id);
//                                startActivity(profileIntent);
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        int[] startingLocation = new int[2];
                                        v.getLocationOnScreen(startingLocation);
                                        startingLocation[0] += v.getWidth() / 2;
                                        ProfileActivity.startUserProfileFromLocation(startingLocation,getActivity(),user_id);
//                        overridePendingTransition(0, 0);
                                    }
                                }, 200);
                            }
                        });
                    }


                };
        all_recyc.setAdapter(allUser_firebase);
        allUser_firebase.notifyDataSetChanged();
    }

    public static class AllUserViewHolder extends RecyclerView.ViewHolder {
        View mView;

        public AllUserViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void SetUserName(String username) {
            TextView all_users_name = mView.findViewById(R.id.all_users_name);
            all_users_name.setText(username);
        }
        public void SetAvarta_image(final String avarta_image) {
            final CircleImageView all_users_circle = mView.findViewById(R.id.all_users_circle);
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
//            ImageAsynTask imageAsynTask=new ImageAsynTask();
//            imageAsynTask.execute(avarta_image);
        }
//        class ImageAsynTask extends AsyncTask<String, Void, Bitmap> {
//            @Override
//            protected Bitmap doInBackground(String... integers) {
//                try {
//                    String link = integers[0];
//                    Bitmap bitmap= BitmapFactory.decodeStream((InputStream) new URL(link).getContent());
//                    return bitmap;
//
//                } catch (Exception e) {
//                    Log.d("LOI", e.toString());
//                }
//                return null;
//            }
//
//            @Override
//            protected void onPreExecute() {
////                diaglog.show();
//                super.onPreExecute();
//            }
//
//            @Override
//            protected void onPostExecute(Bitmap bitmap) {
//                super.onPostExecute(bitmap);
//                final CircleImageView all_users_circle = mView.findViewById(R.id.all_users_circle);
////                all_users_circle.setImageBitmap(bitmap);
////                diaglog.dismiss();  //an di , nh]xng vân còn hoạt ddoojjng, vãn dùng được. cancle mạnh hơn.
//                ByteArrayOutputStream baos = new ByteArrayOutputStream();
//                bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
//                byte[] b = baos.toByteArray();
//                final String temp = Base64.encodeToString(b, Base64.DEFAULT);
//                try {
//
//                Picasso.get().load(temp).networkPolicy(NetworkPolicy.OFFLINE).into(all_users_circle, new Callback() {
//                    @Override
//                    public void onSuccess() {
//
//                    }
//
//                    @Override
//                    public void onError(Exception e) {
//                        Picasso.get().load(temp).networkPolicy(NetworkPolicy.OFFLINE).placeholder(R.drawable.noticon).into(all_users_circle);
//                    }
//                });
////                Picasso.get().load(thum_image).into(all_users_circle);
//            } catch (Exception e) {
//            }
//            }
//
//            @Override
//            protected void onProgressUpdate(Void... values) {
//                super.onProgressUpdate(values);
//            }  //Void vi khong biet khi nao tai xong.
//
//
//        }
    }
}
