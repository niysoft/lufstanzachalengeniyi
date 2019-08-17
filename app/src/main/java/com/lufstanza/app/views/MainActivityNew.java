package com.lufstanza.app.views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.jaredrummler.materialspinner.MaterialSpinner;
import com.lufstanza.app.R;
import com.lufstanza.app.adapters.SchedulesAdapter;
import com.lufstanza.app.models.Schedules;
import com.lufstanza.app.utils.Api;
import com.lufstanza.app.utils.AppController;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class MainActivityNew extends AppCompatActivity implements View.OnClickListener {

    RecyclerView recyclerView;
    SchedulesAdapter adapter;
    private Activity activity;
    private Context context;
    ArrayList<Object> schedulesList = new ArrayList<>();
    ArrayList<String> airportsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_new);
        https://github.com/alphamu/android-MVVM-DataBinding-RecyclerViewExample.git
        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        activity = this;
        context = this;
        //Object this_ = this;
        List<String> emptyList = new ArrayList<>();

        List<String> saveAirport = AppController.storage.get("AIRPORT_LIST", emptyList);
        if(saveAirport.size() > 0){//saveAirport.size() > 0
            this.showSaveAirports(saveAirport);
        } else {
            this.loadAirportsLocal();
        }
    }

    private void showSaveAirports(List<String> airportList){

       /* model.getAirportsList().observe(this, new Observer<List<String>>() {
            @Override
            public void onChanged(@Nullable List<String> airportList) {*/
        AppController.storage.store("AIRPORT_LIST", airportList);
        String state = airportList.get(0);
        if (state.equals(AppController.NETWORK_ERROR_STRING)) {
            AppController.doToast("Error please retry action");
        } else if (state.equals(AppController.JSON_ERROR_STRING)) {
            AppController.doToast("Error please retry action");
        } else {
            airportList.add(0, "- [ Select Departure Airport ] -");
            String[] array1 = airportList.toArray(new String[airportList.size()]);
            airportList.remove(0);
            airportList.add(0, "- [ Select Arrival Airport ] -");// airportsList.add("- [ Select a PMB ] -");
            String[] array2 = airportList.toArray(new String[airportList.size()]);

            ((MaterialSpinner) findViewById(R.id.static_spinner)).setItems(array1);
            ((MaterialSpinner) findViewById(R.id.static_spinner1)).setItems(array2);
            ((MaterialSpinner) findViewById(R.id.static_spinner)).setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {
                @Override public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                    AppController.storage.store("DEPARTURE", item);
                }
            });
            ((MaterialSpinner) findViewById(R.id.static_spinner1)).setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {
                @Override public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                    AppController.storage.store("ARRIVAL", item);
                }
            });
            findViewById(R.id.date_picker).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    handleDateSelection();
                }
            });
        }
    }

    private void loadAirportsLocal(){
        try {
            JSONObject obj = new JSONObject(AppController.loadJSONFromAsset("airports.json"));
            JSONArray airport = obj.getJSONObject("AirportResource").getJSONObject("Airports").getJSONArray("Airport");
            int len = airport.length();
            if(airport.length() > 0){
                airportsList.add("LOS | NG");
                airportsList.add("ABV | NG");
                airportsList.add("MOW | RU");
                airportsList.add("BER | DE");
                for (int i = 0; i < len; i++) {
                    try {
                        String portString = airport.getJSONObject(i).getString("AirportCode") + " | " + airport.getJSONObject(i).getString("CountryCode");
                        airportsList.add(portString);
                    } catch (Exception e) {
                        airportsList.clear();
                    }
                }
                finalizeAriportLoading();
            }
        } catch (Throwable t) {
            findViewById(R.id.loading_icon).setVisibility(View.GONE);
            findViewById(R.id.placeholder).setVisibility(View.VISIBLE);
            ((TextView)findViewById(R.id.place_holder_text)).setText("Airports could be loaded at this time. Please retry later");
        }

    }

    private void loadAirports() {
        AppController.doToast("loading airport");
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Api.BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
        Api api = retrofit.create(Api.class);
        Map<String, String> params = new HashMap<>();
        Call<String> stringCall = api.getAirports(
                AppController.AUTORIZATION,
                100
        );
        stringCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    String responseString = response.body();//JSONArray response
                    try {
                        JSONObject obj = new JSONObject(responseString);
                        JSONArray airport = obj.getJSONObject("AirportResource").getJSONObject("Airports").getJSONArray("Airport");
                        int len = airport.length();
                        //airportsList.add(0, "- [ Select Departure Airport ] -");
                        airportsList.add("LOS | NG");
                        airportsList.add("ABV | NG");
                        airportsList.add("MOW | RU");
                        airportsList.add("BER | DE");
                        for (int i = 0; i < len; i++) {
                            try {
                                String portString = airport.getJSONObject(i).getString("AirportCode") + " | " + airport.getJSONObject(i).getString("CountryCode");
                                airportsList.add(portString);
                            } catch (Exception e) {
                                airportsList.clear();
                            }
                        }
                    } catch (Throwable t) {
                        airportsList.clear();
                    }
                    // todo: do something with the response string
                } else {
                    airportsList.clear();
                    findViewById(R.id.loading_icon).setVisibility(View.GONE);
                    findViewById(R.id.placeholder).setVisibility(View.VISIBLE);
                    ((TextView)findViewById(R.id.place_holder_text)).setText("Not schedlue found for selected departure/arrival. Kindly select another route");
                }
               finalizeAriportLoading();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                airportsList.clear();
                AppController.doToast(t.toString());
                findViewById(R.id.loading_icon).setVisibility(View.GONE);
                findViewById(R.id.placeholder).setVisibility(View.VISIBLE);
                ((TextView)findViewById(R.id.place_holder_text)).setText("Airports could be loaded at this time. Please retry later");
            }
        });
    }

    private void finalizeAriportLoading(){
        AppController.storage.store("AIRPORT_LIST", airportsList);

        airportsList.add(0, "- [ Select Departure Airport ] -");
        String[] array1 = airportsList.toArray(new String[airportsList.size()]);
        airportsList.remove(0);
        airportsList.add(0, "- [ Select Arrival Airport ] -");// airportsList.add("- [ Select a PMB ] -");
        String[] array2 = airportsList.toArray(new String[airportsList.size()]);
        AppController.doToast(array2.toString());

        ((MaterialSpinner) findViewById(R.id.static_spinner)).setItems(array1);
        ((MaterialSpinner) findViewById(R.id.static_spinner1)).setItems(array2);
        ((MaterialSpinner) findViewById(R.id.static_spinner)).setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {
            @Override public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                AppController.storage.store("DEPARTURE", item);
            }
        });
        ((MaterialSpinner) findViewById(R.id.static_spinner1)).setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {
            @Override public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                AppController.storage.store("ARRIVAL", item);
            }
        });
        findViewById(R.id.date_picker).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleDateSelection();
            }
        });
    }

    public void handleDateSelection(){
        try{
            schedulesList.clear();
            adapter.notifyDataSetChanged();
        }catch (Exception e){

        }
        AppController.DEPARTURE =  AppController.storage.get("DEPARTURE", "");
        AppController.ARRIVAL =  AppController.storage.get("ARRIVAL", "");
        if(AppController.DEPARTURE.equals("") || AppController.ARRIVAL.equals("")
                || AppController.DEPARTURE.equals("- [ Select Departure Airport ] -")
                || AppController.ARRIVAL.equals("- [ Select Arrival Airport ] -")){
            AppController.doToast("Please select departure and destination before choosing date");
        } else {
            AppController.DEPARTURE = AppController.DEPARTURE.split(" | ")[0];
            AppController.ARRIVAL = AppController.ARRIVAL.split(" | ")[0];
            AppController.doToast(AppController.DEPARTURE+" - "+AppController.ARRIVAL);
            Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog dialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    loadSchedules(AppController.DEPARTURE,  AppController.ARRIVAL);
                }
            }, year, month, day);
            dialog.show();
        }
    }

    private void loadSchedules(String departure, String arrival) {
        findViewById(R.id.placeholder).setVisibility(View.GONE);
        findViewById(R.id.loading_icon).setVisibility(View.VISIBLE);
        try{
            AppController.DEPARTURE =  AppController.storage.get("DEPARTURE", "");
            AppController.ARRIVAL =  AppController.storage.get("ARRIVAL", "");
        }catch (Exception e) {

        }
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Api.BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())//.addConverterFactory(GsonConverterFactory.create())
                .build();
        Api api = retrofit.create(Api.class);
        Map<String, String> params = new HashMap<>();
        Call<String> stringCall = api.getStringSchedules(
                AppController.AUTORIZATION,
                departure,
                arrival,
                AppController.DATE
        );
        stringCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    AppController.doToast("Done loading schedules...");
                    String responseString = response.body();//JSONArray response
                    try {
                        JSONObject obj = new JSONObject(responseString);
                        JSONArray schedules = obj.getJSONObject("ScheduleResource").getJSONArray("Schedule");
                        //AppController.doToast(schedules.toString());
                        parseResponse(schedules);
                    } catch (Throwable t) {
                        schedulesList.clear();
                    }
                    // todo: do something with the response string
                } else {
                    schedulesList.clear();
                    schedulesList.add(AppController.NETWORK_ERROR_STRING);
                    schedulesList.add(response.toString());
                    //schedulesList_.setValue(schedulesList);
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                schedulesList.clear();
                schedulesList.add(AppController.NETWORK_ERROR_STRING);
                schedulesList.add(t.toString());
                //schedulesList_.setValue(schedulesList);
            }
        });
    }

    public void parseResponse(JSONArray jsonArray) {
        for (int i = 0; i < jsonArray.length(); i++) {//new OuterClass().new InnerClass();
            schedulesList.add(new AppController().new Demacator(i + 1));
            try {
                JSONObject schedules = (JSONObject) jsonArray.get(i);
                JSONArray flights = schedules.getJSONArray("Flight");
                HashMap<String, String> applicationSettings = new HashMap<String, String>();
                for (int k = 0; k < flights.length(); k++) {
                    // AppController.doToast(flights.getJSONObject(k).getJSONObject("Departure").getString("AirportCode"));
                    Schedules schedules1 = new Schedules(
                            flights.getJSONObject(k).getJSONObject("Departure").getString("AirportCode"),
                            flights.getJSONObject(k).getJSONObject("Departure").getJSONObject("ScheduledTimeLocal").getString("DateTime"),
                            flights.getJSONObject(k).getJSONObject("Arrival").getString("AirportCode"),
                            flights.getJSONObject(k).getJSONObject("Arrival").getJSONObject("ScheduledTimeLocal").getString("DateTime"),
                            flights.getJSONObject(k).getJSONObject("MarketingCarrier").getString("AirlineID")
                    );
                    schedulesList.add(schedules1);
                }
            } catch (Exception e) {
                schedulesList.clear();
                schedulesList.add(AppController.JSON_ERROR_STRING);
                //schedulesList_.setValue(schedulesList);
            }
            findViewById(R.id.loading_icon).setVisibility(View.GONE);
            adapter = new SchedulesAdapter(MainActivityNew.this, schedulesList, activity);
            recyclerView.setAdapter(adapter);
        }
    }


    @Override
    public void onClick(View v) {


    }
}
