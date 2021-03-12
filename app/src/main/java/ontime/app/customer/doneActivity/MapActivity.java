package ontime.app.customer.doneActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import ontime.app.R;
import ontime.app.utils.Common;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback{
    SupportMapFragment mapFragment;
    String REST_LAT = "";
    String REST_LOG = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        REST_LAT = getIntent().getStringExtra("REST_LAT");
        REST_LOG = getIntent().getStringExtra("REST_LOG");

        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d(">>>>>>>",">>>>>" + Common.REST_LAT + " " + Common.REST_LOG);
        if(Common.MERCHANT_TYPE==1){
            googleMap.addMarker(new MarkerOptions()
                    .position(new LatLng(Double.parseDouble(Common.REST_LAT), Double.parseDouble(Common.REST_LOG)))
                    .title(getIntent().getStringExtra( Common.REST_NAME))
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
        }else {
            googleMap.addMarker(new MarkerOptions()
                    .position(new LatLng(Double.parseDouble(Common.REST_LAT), Double.parseDouble(Common.REST_LOG)))
                    .title(getIntent().getStringExtra( Common.REST_NAME))
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
        }
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.parseDouble(Common.REST_LAT), Double.parseDouble(Common.REST_LOG)), 15));
    }
}