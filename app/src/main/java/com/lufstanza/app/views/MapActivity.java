package com.lufstanza.app.views;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.lufstanza.app.R;
import com.lufstanza.app.utils.Api;
import com.lufstanza.app.utils.AppController;
import com.lufstanza.app.utils.FetchURL;
import com.lufstanza.app.utils.TaskLoadedCallback;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback, TaskLoadedCallback {

    private GoogleMap mMap;
    private MarkerOptions place1, place2;
    Button getDirection;
    private Polyline currentPolyline;
    private double lat1, long1, lat2, long2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarTransparent(this);
        setContentView(R.layout.activity_main_map);
        ((TextView)findViewById(R.id.departure_map)).setText(AppController.DEPARTURE);
        ((TextView)findViewById(R.id.arrival_map)).setText(AppController.ARRIVAL);
        // this.loadlongLat();
        loadLongLat();
        //place1 = new MarkerOptions().position(new LatLng(22.3039, 70.8022)).title("Location 1");//6.581744,3.31885
    }

    public void makeMap() {
        place1 = new MarkerOptions().position(new LatLng(lat1, long1)).title("Location 1");//6.581744,3.31885
        place2 = new MarkerOptions().position(new LatLng(lat2, long2)).title("Location 2");//51.50473,0.047286
        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.mapNearBy);
        mapFragment.getMapAsync(this);
    }

    private void loadLongLat() {
        AppController.doToast(AppController.DEPARTURE+" - "+AppController.ARRIVAL);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Api.BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())//.addConverterFactory(GsonConverterFactory.create())
                .build();
        Api api = retrofit.create(Api.class);
        Map<String, String> params = new HashMap<>();
        Call<String> stringCall = api.getLatLong(
                AppController.AUTORIZATION,
                AppController.DEPARTURE
        );
        stringCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    AppController.doToast("Done loading...");
                    String responseString = response.body();//JSONArray response
                    try {
                        JSONObject obj = new JSONObject(responseString);

                        JSONObject latlong = obj.getJSONObject("AirportResource").getJSONObject("Airports").getJSONObject("Airport").getJSONObject("Position").getJSONObject("Coordinate");
                        lat1 = latlong.getDouble("Latitude");
                        long1 = latlong.getDouble("Longitude");
                        AppController.doToast(latlong.toString());
                        Call<String> stringCall = api.getLatLong(
                                AppController.AUTORIZATION,
                                AppController.ARRIVAL
                        );
                        stringCall.enqueue(new Callback<String>() {
                            @Override
                            public void onResponse(Call<String> call, Response<String> response) {
                                if (response.isSuccessful()) {
                                    AppController.doToast("Done loading...");
                                    String responseString = response.body();//JSONArray response
                                    try {
                                        JSONObject obj = new JSONObject(responseString);

                                        JSONObject latlong = obj.getJSONObject("AirportResource").getJSONObject("Airports").getJSONObject("Airport").getJSONObject("Position").getJSONObject("Coordinate");
                                        lat2 = latlong.getDouble("Latitude");
                                        long2 = latlong.getDouble("Longitude");
                                        makeMap();
                                        AppController.doToast(latlong.toString());
                                        //AppController.doToast("map made - ");
                                    } catch (Throwable t) {
                                        AppController.doToast(t.toString());
                                    }
                                    // todo: do something with the response string
                                } else {
                                    AppController.doToast(response.toString());
                                }
                            }

                            @Override
                            public void onFailure(Call<String> call, Throwable t) {
                                AppController.doToast("failure");
                            }
                        });
                        //AppController.doToast(schedules.toString());
                    } catch (Throwable t) {
                        AppController.doToast(t.toString());
                    }
                    // todo: do something with the response string
                } else {
                    AppController.doToast(response.toString());
                }

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                AppController.doToast("failure");
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.addMarker(place1);
        mMap.addMarker(place2);

        CameraPosition googlePlex = CameraPosition.builder()
                .target(new LatLng(lat2, long2))
                .zoom(3)
                .bearing(0)
                .tilt(90)
                .build();

        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(googlePlex), 5000, null);
        new FetchURL(MapActivity.this).execute(getUrl(place1.getPosition(), place2.getPosition(), "driving"), "driving");
        findViewById(R.id.loading_icon).setVisibility(View.GONE);
    }

    public static void setStatusBarTransparent(Activity activity) {
        //activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            // activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE|WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
            // activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
    }

    private String getUrl(LatLng origin, LatLng dest, String directionMode) {
        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        // Mode
        String mode = "mode=" + directionMode;
        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + mode;
        // Output format
        String output = "json";
        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&key=" + getString(R.string.google_maps_key);
        return url;
    }

    @Override
    public void onTaskDone(Object... values) {
        if (currentPolyline != null)
            currentPolyline.remove();
        currentPolyline = mMap.addPolyline((PolylineOptions) values[0]);
    }
}