package com.example.thu.realtysocial.Profile.EditProfile;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import com.example.thu.realtysocial.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EditAdressActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {
    private static final String EditAdressActivity = "EditAdressActivity";
    @BindView(R.id.ea_auto_address)
    AutoCompleteTextView ea_auto_address;
    @BindView(R.id.ea_btn_cancel)
    Button ea_btn_cancel;
    @BindView(R.id.ea_btn_save)
    Button ea_btn_save;
    FirebaseUpdateData firebaseUpdateData=new FirebaseUpdateData(this);
    String value="";

    //maps
    private GoogleApiClient mGoogleApiClient;
    private PlaceAutocompleteAdapter mPlaceAutocompleteAdapter;
    private static final LatLngBounds LAT_LNG_BOUNDS = new LatLngBounds(
            new LatLng(-40, -168), new LatLng(71, 136));
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_adress);
        ButterKnife.bind(this);
        AutoComplete();
        ea_btn_save.setBackgroundResource(R.color.colortextProA);
    }
    @OnClick(R.id.ea_btn_cancel)
    public void ClickCancel() {
        Log.d(EditAdressActivity, "ClickCancel");
        ea_btn_save.setBackgroundResource(R.color.edit_sex_background);
        ea_btn_cancel.setBackgroundResource(R.color.colortextProA);
        finish();
    }
    @OnClick(R.id.ea_btn_save)
    public void ClickSave() {
        Log.d(EditAdressActivity, "ClickCancel");
        ea_btn_cancel.setBackgroundResource(R.color.edit_sex_background);
        ea_btn_save.setBackgroundResource(R.color.colortextProA);
//        AutoComplete();
        value =ea_auto_address.getText().toString();
        firebaseUpdateData.UpdateData("address",value);
        finish();
//        ChanceSex(chooseSex);
    }

    private void AutoComplete() {
        mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this, this)
                .build();

        mPlaceAutocompleteAdapter = new PlaceAutocompleteAdapter(this, mGoogleApiClient,
                LAT_LNG_BOUNDS, null);

        ea_auto_address.setAdapter(mPlaceAutocompleteAdapter);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
