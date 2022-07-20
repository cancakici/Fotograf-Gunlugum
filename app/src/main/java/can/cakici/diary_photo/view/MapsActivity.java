package can.cakici.diary_photo.view;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.snackbar.Snackbar;

import can.cakici.diary_photo.R;
import can.cakici.diary_photo.databinding.ActivityMapsBinding;
import can.cakici.diary_photo.model.Place;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,GoogleMap.OnMapLongClickListener {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    LocationManager locationManager;
    ActivityResultLauncher<String> activityResultLauncher;
    LocationListener locationListener;
    boolean trackBoolean;
    SharedPreferences sharedPreferences;
    double getLongitude,getLatitude;
    String sorgu;
    Place sifree;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        sharedPreferences = MapsActivity.this.getSharedPreferences("can.cakici.diary_photo.view",MODE_PRIVATE);
        trackBoolean = false;

        Intent intent=getIntent();
        sorgu=intent.getStringExtra("view");

        Intent intent1=getIntent();
        sifree=(Place) intent1.getSerializableExtra("sifree");

        if (sorgu!=null){
            binding.saveButton.setVisibility(View.GONE);

        }else {
            binding.saveButton.setVisibility(View.VISIBLE);
            ActivityResultLauncherr();

        }
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMapLongClickListener(this);

        if (sorgu!=null){
            LatLng latLng=new LatLng(sifree.latitude,sifree.longitude);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,15));
            mMap.addMarker(new MarkerOptions().position(latLng).title(sifree.baslik));

        }else {
            locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

            locationListener = new LocationListener() {
                @Override
                public void onLocationChanged(android.location.Location location) {
                    getLongitude = location.getLongitude();
                    getLatitude = location.getLatitude();

                    trackBoolean = sharedPreferences.getBoolean("trackBoolean", false);

                    if (!trackBoolean) {
                        LatLng userLocation = new LatLng(location.getLatitude(), location.getLongitude());
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 15));
                        sharedPreferences.edit().putBoolean("trackBoolean", true).apply();
                    }
                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {

                }

                @Override
                public void onProviderEnabled(String provider) {

                }

                @Override
                public void onProviderDisabled(String provider) {

                }
            };

            if (ContextCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                //izn verilmedi
                if (ActivityCompat.shouldShowRequestPermissionRationale(MapsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)) {

                    Snackbar.make(binding.getRoot(), "izin vermek istermisiniz !", Snackbar.LENGTH_INDEFINITE).setAction("İzin ver", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            //izin verildi

                            activityResultLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION);

                        }
                    }).show();

                } else {
                    //izin verildi
                    activityResultLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION);

                }


            } else {//izin verildi
                activityResultLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION);
            }
        }

    }
    public void ActivityResultLauncherr(){
        activityResultLauncher= registerForActivityResult(new ActivityResultContracts.RequestPermission(), new ActivityResultCallback<Boolean>() {
            @Override
            public void onActivityResult(Boolean result) {

                if (result){
                if (ContextCompat.checkSelfPermission(MapsActivity.this,Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED){


                    LocationManager lm = (LocationManager) MapsActivity.this.getSystemService(Context.LOCATION_SERVICE);
                    boolean gps_enabled = false;
                    boolean network_enabled = false;

                    try {
                        gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
                    } catch(Exception ex) {}

                    try {
                        network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
                    } catch(Exception ex) {}

                    if(!gps_enabled && !network_enabled) {
                        Snackbar.make(binding.getRoot(),"Konumu açınız !",Snackbar.LENGTH_INDEFINITE).setAction("izin ver", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                MapsActivity.this.startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));

                            }
                        }).show();
                    }else{

                       // locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,60000,100,locationListener);


                        Location lasLocation=locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        if (lasLocation!=null){
                            LatLng latLng=new LatLng(lasLocation.getLatitude(),lasLocation.getLongitude());
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,15));
                        }

                        mMap.setMyLocationEnabled(true);

                    }
                }
                }
                else{
                    Toast.makeText(MapsActivity.this,"İzin alınamadı",Toast.LENGTH_LONG).show();
                }


            }
        });

    }
    @Override
    public void onMapLongClick(@NonNull LatLng latLng) {
        if (sorgu!=null){}else {

            mMap.clear();
            getLongitude = latLng.longitude;
            getLatitude = latLng.latitude;
            mMap.addMarker(new MarkerOptions().position(latLng));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
        }

    }
    public void save(View view){

        Intent myIntent = new Intent(getApplicationContext(), details_add_activity.class);
        myIntent.putExtra("longitude",getLongitude);
        myIntent.putExtra("latiude",getLatitude);
        myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(myIntent);


    }
    public void cancel(View view){

        if (sorgu!=null){
            Intent myIntent = new Intent(getApplicationContext(), details_add_view_activity.class);
            myIntent.putExtra("sifre",sifree);
            myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(myIntent);


        }else {
            Intent myIntent = new Intent(getApplicationContext(), details_add_activity.class);
            myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(myIntent);

        }
    }




}