package com.example.vtr.googlemapisapi_nougat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CheckInAPI extends AppCompatActivity implements
        GoogleMap.OnMyLocationButtonClickListener,
        GoogleMap.OnMyLocationClickListener,
        OnMapReadyCallback,
        ActivityCompat.OnRequestPermissionsResultCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private MeuDB db;

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private static float ZOOM_LEVEL = 17.0f;
    private boolean mPermissionDenied = false;
    private GoogleMap mMap;
    protected GoogleApiClient mGoogleApiClient;
    TextView txtEndereco;

    Geocoder geocoder;
    double lat, lon = 0;
    Location location;
    private Enderecos enderecos;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_localizacao_atual);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        txtEndereco = findViewById(R.id.txtEndereco);
        db = new MeuDB(this);
        txtEndereco.setText(db.findLastEnderecoDescription());
    }

    private void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public boolean onMyLocationButtonClick() {
        return false;
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {
        double lat = location.getLatitude();
        double lon = location.getLongitude();
        getLocationRequest();
        try {
            // Converte coordenadas para conseguir endereco do usuario
            converterCoordenadas(lat, lon);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
//        mMap.setMyLocationEnabled(true);
        mMap.setOnMyLocationButtonClickListener(this);
        mMap.setOnMyLocationClickListener(this);
        buildGoogleApiClient();
        enableMyLocation();
    }

    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission to access the location is missing.
            PermissionUtils.requestPermission(this, LOCATION_PERMISSION_REQUEST_CODE,
                    Manifest.permission.ACCESS_FINE_LOCATION, true);
        } else if (mMap != null) {
            // Access to the location has been granted to the app.
            mMap.setMyLocationEnabled(true);
        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        if (mPermissionDenied) {
            // Permission was not granted, display error dialog.
            showMissingPermissionError();
            mPermissionDenied = false;
        }
    }

    private void showMissingPermissionError() {
        PermissionUtils.PermissionDeniedDialog
                .newInstance(true).show(getSupportFragmentManager(), "dialog");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
            return;
        }

        if (PermissionUtils.isPermissionGranted(permissions, grantResults,
                Manifest.permission.ACCESS_FINE_LOCATION)) {
            // Enable the my location layer if the permission has been granted.
            enableMyLocation();
        } else {
            // Display the missing permission error dialog when the fragments resume.
            mPermissionDenied = true;
        }
    }

    private LocationRequest getLocationRequest() {
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        return locationRequest;
    }

    public void fazerCheckIn(View view) {
        Endereco endereco = new Endereco();
        this.location = mMap.getMyLocation();
        onMyLocationClick(this.location);
        salvar();
    }

    public void salvar() {
        String descricao = txtEndereco.getText().toString();

        Endereco endereco = new Endereco();
        endereco.setDescricao(descricao);

        db.insertEndereco(endereco);

        Toast.makeText(this, "Endereco salvo com sucesso", Toast.LENGTH_SHORT).show();
    }

    // Este metodo recebe por parametro as coordenadas ao clicar no botao de localizacao
    // obtendo essas coordenadas, podemos conseguir diversas informacoes em String da localizacao do usuario.
    public void converterCoordenadas(double lat, double lon) throws IOException {
        geocoder = new Geocoder(this, Locale.getDefault());

        // Criando lista para os valores de endereco
        List<Address> addresses;

        // Adicionando o resultado encontrado com as coordenadas para uma lista
        addresses = geocoder.getFromLocation(lat, lon, 1);

        String endereco = addresses.get(0).getAddressLine((0));

        // Definindo o texto na tela com o endereco do resultado obtido da lista de enderecos
        txtEndereco.setText(addresses.get(0).getAddressLine(0));
        // TODO: ARRUMAR A ADICAO DE ENDERECOS
//        enderecos = new Enderecos();
//
//        if (endereco != null) {
//            enderecos.adicionarEndereco(endereco);
//        }

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            location = LocationServices.FusedLocationApi.getLastLocation(
                    mGoogleApiClient);
            if (location != null) {
                lat = location.getLatitude();
                lon = location.getLongitude();

                LatLng loc = new LatLng(lat, lon);
//                mMap.addMarker(new MarkerOptions().position(loc).title("New Marker"));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(loc));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), ZOOM_LEVEL));
            }
    }
    // Metodos nao utilizados
    @Override public void onConnectionSuspended(int i) { } @Override public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {}

    public void abrirListaEnderecos(View view) {
        Intent intent = new Intent(this, Enderecos.class);
        startActivity(intent);
    }
}