package com.lufstanza.app.viewModels;

import android.app.Application;
import android.widget.Spinner;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.lufstanza.app.models.Schedules;
import com.lufstanza.app.utils.Api;
import com.lufstanza.app.utils.AppController;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class SchedulesViewModel extends AndroidViewModel {

    //this is the data that we will fetch asynchronously
    private MutableLiveData<List<Object>> schedulesList_;
    private MutableLiveData<List<String>> airportsList_;
    ArrayList<Object> schedulesList = new ArrayList<>();
    ArrayList<String> airportsList = new ArrayList<>();
    public static String[] arrayAirpots;

    //we will call this method to get the data
    public SchedulesViewModel(Application application) {
        super(application);
        // Do rest of your stuff here ...
    }

    public LiveData<List<Object>> getScheduleList(String departure, String arrival) {
        //if the list is null
        if (schedulesList_ == null) {
            schedulesList_ = new MutableLiveData<List<Object>>();
            loadSchedules(departure, arrival);
        }
        //schedulesList_.postValue();
        return schedulesList_;
    }

    public LiveData<List<String>> getAirportsList() {
        //if the list is null
        if (airportsList_ == null) {
            airportsList_ = new MutableLiveData<List<String>>();
            loadAirports();
        }
        return airportsList_;
    }

    private void loadSchedules(String departure, String arrival) {
        //loadAirports();
        AppController.doToast("<<<--->>>");
        try{
            AppController.DEPARTURE =  AppController.storage.get("DEPARTURE", "");
            AppController.ARRIVAL =  AppController.storage.get("ARRIVAL", "");
            AppController.doToast(  AppController.DEPARTURE+"<-->"+  AppController.ARRIVAL);
          /*  AppController.DEPARTURE = AppController.DEPARTURE.split(" | ")[0];
            AppController.ARRIVAL = AppController.ARRIVAL.split(" | ")[0];*/
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
                        schedulesList.add(AppController.JSON_ERROR_STRING);
                        schedulesList.add(AppController.JSON_ERROR_STRING);
                        schedulesList_.setValue(schedulesList);
                    }
                    // todo: do something with the response string
                } else {
                    schedulesList.clear();
                    schedulesList.add(AppController.NETWORK_ERROR_STRING);
                    schedulesList.add(response.toString());
                    schedulesList_.setValue(schedulesList);
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                schedulesList.clear();
                schedulesList.add(AppController.NETWORK_ERROR_STRING);
                schedulesList.add(t.toString());
                schedulesList_.setValue(schedulesList);
            }
        });
    }

    private void updateSchedules(String departure, String arrival) {
        //loadAirports();
        schedulesList_.setValue(null);
        AppController.doToast("<<<--->>>");
        try{
            AppController.DEPARTURE =  departure;
            AppController.ARRIVAL =  arrival;
            AppController.doToast(  AppController.DEPARTURE+"<-->"+  AppController.ARRIVAL);
          /*  AppController.DEPARTURE = AppController.DEPARTURE.split(" | ")[0];
            AppController.ARRIVAL = AppController.ARRIVAL.split(" | ")[0];*/
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
                        schedulesList.add(AppController.JSON_ERROR_STRING);
                        schedulesList.add(AppController.JSON_ERROR_STRING);
                        schedulesList_.setValue(schedulesList);
                    }
                    // todo: do something with the response string
                } else {
                    schedulesList.clear();
                    schedulesList.add(AppController.NETWORK_ERROR_STRING);
                    schedulesList.add(response.toString());
                    schedulesList_.setValue(schedulesList);
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                schedulesList.clear();
                schedulesList.add(AppController.NETWORK_ERROR_STRING);
                schedulesList.add(t.toString());
                schedulesList_.setValue(schedulesList);
            }
        });
    }

    private void loadAirports() {
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
                            //AppController.appPreferences.put("localAirport", responseString);
                            //AppController.storage.store("localAirport", responseString);
                            JSONObject obj = new JSONObject(responseString);
                            JSONArray airport = obj.getJSONObject("AirportResource").getJSONObject("Airports").getJSONArray("Airport");
                            int len = airport.length();
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
                                    airportsList.add(AppController.JSON_ERROR_STRING);
                                    airportsList_.setValue(airportsList);
                                }
                            }
                        } catch (Throwable t) {
                            airportsList.clear();
                            airportsList.add(AppController.JSON_ERROR_STRING);
                            airportsList_.setValue(airportsList);
                        }
                        // todo: do something with the response string
                    } else {
                        airportsList.clear();
                        airportsList.add(AppController.NETWORK_ERROR_STRING);
                        airportsList_.setValue(airportsList);
                    }
                    //airportsList_.
                    airportsList_.setValue(airportsList);
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    airportsList.clear();
                    airportsList.add(AppController.NETWORK_ERROR_STRING);
                    airportsList_.setValue(airportsList);
                }
            });
        //}
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
                schedulesList_.setValue(schedulesList);
            }
        }
        schedulesList_.setValue(schedulesList);
    }
}
