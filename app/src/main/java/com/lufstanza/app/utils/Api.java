package com.lufstanza.app.utils;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface Api {

    String BASE_URL = "https://api.lufthansa.com/v1/";//https://api.lufthansa.com/v1/mds-references/airports/
    //String BASE_URL_LAT_LONG = "https://api.lufthansa.com/v1/mds-references/airports/";
    //String BASE_URL_AIRPORT = "https://api.lufthansa.com/v1/mds-references//";

    @Headers({
            "Accept: application/json",
            "Cache-Control: max-age=640000"
    })
    @GET("operations/schedules/{departure}/{arrival}/{date}")//@GET("Home.php/{email}") ?directFlights=1
    Call<String> getStringSchedules(
            @Header("Authorization") String header,
            @Path("departure") String departure,
            @Path("arrival") String arrival,
            @Path("date") String date
    );

    @Headers({
            "Accept: application/json",
            "Cache-Control: max-age=640000"
    })
    @GET("mds-references/airports/{airport}")//@GET("Home.php/{email}") ?directFlights=1
    Call<String> getLatLong(
            @Header("Authorization") String header,
            @Path("airport") String airport
    );

    @Headers({
            "Accept: application/json",
            "Cache-Control: max-age=640000"
    })
    @GET("mds-references/airports/")//@GET("Home.php/{email}") ?directFlights=1
    Call<String> getAirports(
            @Header("Authorization") String header,
            @Query("limit") int limit
    );
}
