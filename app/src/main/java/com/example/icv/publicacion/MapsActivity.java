package com.example.icv.publicacion;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Toast;
import com.example.icv.R;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.common.collect.Maps;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements  GoogleMap.OnMapLongClickListener,GoogleMap.OnMyLocationClickListener,GoogleMap.OnMyLocationButtonClickListener,OnMapReadyCallback {

    private GoogleMap mMap;
    public String latitud,longitud;
    public boolean restart=false;
    Location nueva;
    Geocoder geocoder;
    List<Address> direccion;
    AlertDialog alert=null;


    static String click1,encendido;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng sydney = new LatLng(13.74816141572074 ,-88.9487836137414);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney,8));
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.setOnMyLocationButtonClickListener(MapsActivity.this);
        mMap.setOnMyLocationClickListener(MapsActivity.this);
        mMap.setOnMapLongClickListener(MapsActivity.this);

        LocationServices.getFusedLocationProviderClient(MapsActivity.this).getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                //mMap.setMyLocationEnabled(true);
                extraerlocation(location);
                if(location==null)
                {
                    Toast.makeText(MapsActivity.this,"Sin localizacion  "+encendido,Toast.LENGTH_SHORT).show();
                    if(encendido==null)
                    {
                        click1="no";
                    }
                    else
                    {
                        if(click1.equals("si") && encendido.equals("si"))
                        {
                            recreate();
                        }
                        else
                        {
                            click1="no";
                        }
                    }
                }
                else
                {
                    click1="bien";
                }
            }
        });
    }

    @Override
    public void onBackPressed() {

        enviardato();
        click1="";
        encendido="";
        super.onBackPressed();
    }

    public void extraerlocation(Location location)
    {
        if(location!=null) {
            LatLng sydney = new LatLng(location.getLatitude(), location.getLongitude());
            mMap.addMarker(new MarkerOptions().position(sydney).title("Mi Ubicación"));
            latitud = String.valueOf(location.getLatitude());
            longitud = String.valueOf(location.getLongitude());
            geocoder = new Geocoder(MapsActivity.this, Locale.getDefault());
            try {
                direccion = geocoder.getFromLocation(Double.parseDouble(latitud), Double.parseDouble(longitud), 1); // 1 representa la cantidad de resultados a obtener
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else
        {
            //Toast.makeText(MapsActivity.this,"Sin localizacion",Toast.LENGTH_SHORT).show();
        }
    }

    private boolean checkIfLocationOpened() {
        String provider = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
        System.out.println("Provider contains=> " + provider);
        if (provider.contains("gps") || provider.contains("network")){
            return true;
        }
        return false;
    }

    @Override
    public boolean onMyLocationButtonClick() {

        if(checkIfLocationOpened()==false)
        {
            activarGPS();
            Toast.makeText(MapsActivity.this,"Apagado",Toast.LENGTH_SHORT).show();
            restart=true;
            click1="si";
        }
        else
        {
            // Toast.makeText(MapsActivity.this,"Encendido",Toast.LENGTH_SHORT).show();
            //esto es necesario ver otra manera de hacerlo xD
            if(restart==false && click1.equals("no"))
            {
                click1="si";
                recreate();
                encendido="si";
            }
            else if (restart)
            {
                encendido="si";
                recreate();
            }
        }
        // Return false so that we don't consume the event and the default behavior still occurs
        // (the camera animates to the user's current position).
        return false;
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {
        extraerlocation(location);
    }

    public void enviardato()
    {
        if(direccion!=null)
        {
            Intent ventana= new Intent();
            ventana.putExtra("latitud",latitud);
            ventana.putExtra("longitud",longitud);
            ventana.putExtra("ciudad", direccion.get(0).getLocality());
            setResult(RESULT_OK,ventana);
            finish();
        }
        else
        {
            Intent ventana= new Intent();
            ventana.putExtra("latitud",latitud);
            ventana.putExtra("longitud",longitud);
            ventana.putExtra("ciudad","null");
            setResult(RESULT_OK,ventana);
            finish();
        }
    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        mMap.clear();
        Marker marker = mMap.addMarker(new MarkerOptions().position(latLng));
        if(marker!=null)
        {
            nueva=new Location("nueva");
            nueva.setLatitude(latLng.latitude);
            nueva.setLongitude(latLng.longitude);
            extraerlocation(nueva);
        }
    }

    private void activarGPS()
    {
        final AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setMessage("El GPS está desactivado, Desea activarlo?").setCancelable(false).setPositiveButton("Sí", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }
        })
        .setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        alert=builder.create();
        alert.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(alert!=null)
        {
            alert.dismiss();
        }
    }
}
