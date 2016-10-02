package jp.co.araiyusuke.googlemapsdemo;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

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

    final ArrayList<LatLng> latlngs = new ArrayList<>();
    LatLng sydney1 = new LatLng(-34, 151);
    LatLng sydney2 = new LatLng(-34, 161);
    LatLng sydney3 = new LatLng(-34, 171);
    LatLng sydney4 = new LatLng(-34, 181);
    LatLng sydney5 = new LatLng(-34, 191);


    @Override
    public void onMapReady(GoogleMap googleMap) {



        mMap = googleMap;
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney1));

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng point) {
                for(int i = 0; i < 100; i++) {
                    latlngs.add(new LatLng(-34, 150 + i));
                }

                Observable
                        .create(new Observable.OnSubscribe<LatLng>() {
                            @Override
                            public void call(Subscriber<? super LatLng> subscriber) {
                                for(LatLng latlng : latlngs) {
                                    sleep(1000);
                                    subscriber.onNext(latlng);
                                }
                                subscriber.onCompleted();
                            }
                        })
                        .onBackpressureBuffer()
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<LatLng>() {
                            @Override
                            public void onCompleted() {
                            }
                            @Override
                            public void onError(Throwable e) {

                            }
                            @Override
                            public void onNext(LatLng latlng) {
                                Log.d("デバッグ", "追加");
                                mMap.addMarker(new MarkerOptions().position(latlng).title("Marker in Sydney"));
                            }
                        });
            }
        });


    }

    private void sleep(int time) {
        try{
            Thread.sleep(time); //3000ミリ秒Sleepする
        }catch(InterruptedException e){}
    }
}
