package com.example.weatherappfcc;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class WeatherDataService {

    public static final String QUERY_FOR_CITY_ID = "https://www.metaweather.com/api/location/search/?query=";
    public static final String QUERY_FOR_CITY_WEATHER_BY_ID = "https://www.metaweather.com/api/location/";

    Context context;
    String cityId;

    public WeatherDataService(Context context) {
        this.context = context;
    }

    public interface VolleyResponseListener{
        void onError(String message);
        void onResponse(String cityId);
    }

    public void getCityId(String cityName, VolleyResponseListener volleyResponseListener){


        String url = QUERY_FOR_CITY_ID +cityName;

        JsonArrayRequest request = new JsonArrayRequest( Request.Method.GET,url,null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                 cityId = " ";
                try {
                    JSONObject cityInfo = response.getJSONObject( 0 );
                    cityId = cityInfo.getString( "woeid" );
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                // this worked but didnt return city Id
              //  Toast.makeText( context,"city Id =" + cityId,Toast.LENGTH_SHORT ).show();
                volleyResponseListener.onResponse( cityId );
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
               // Toast.makeText( context,"Something Wrong",Toast.LENGTH_SHORT ).show();
                volleyResponseListener.onError( "Something wrong" );
            }
        } );

        MySingleton.getInstance( context ).addToRequestQueue( request );

        // Id was not returned!


    }

    public interface ForecastByIDResponse{
        void onError(String message);
        void onResponse(WeatherReportModel weatherReportModel);
    }
    public void getCityForecastById(String cityID,ForecastByIDResponse forecastByIDResponse){

        String url = QUERY_FOR_CITY_WEATHER_BY_ID + cityID;

        List<WeatherReportModel> report = new ArrayList<>();

        //get json object
        JsonObjectRequest request = new JsonObjectRequest( Request.Method.GET,url,null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
               // Toast.makeText( context,response.toString(),Toast.LENGTH_SHORT ).show();

                try {
                    JSONArray consolodated_weather_list = response.getJSONArray( "consolidated_weather" );


                    WeatherReportModel first_day = new WeatherReportModel();

                    JSONObject first_day_date_from_api = (JSONObject) consolodated_weather_list.get( 0 );
                    first_day.setId(first_day_date_from_api.getInt( "id" ));
                    first_day.setWeather_state_name( first_day_date_from_api.getString( "weather_state_name" ) );
                    first_day.setWeather_state_abbr( first_day_date_from_api.getString( "weather_state_abbr" ) );
                    first_day.setWind_direction_compass(first_day_date_from_api.getString( "wind_direction_compass" ));
                    first_day.setCreated( first_day_date_from_api.getString( "created" ) );
                    first_day.setApplicable_date( first_day_date_from_api.getString( "applicable_date" ) );
                    first_day.setMin_temp( first_day_date_from_api.getLong( "min_temp" ) );
                    first_day.setMax_temp( first_day_date_from_api.getLong( "max_temp" ) );
                    first_day.setThe_temp( first_day_date_from_api.getLong( "the_temp" ) );
                    first_day.setWind_speed( first_day_date_from_api.getLong( "wind_speed" ) );
                    first_day.setWind_direction( first_day_date_from_api.getLong( "wind_direction" ) );
                    first_day.setVisibility( first_day_date_from_api.getLong( "visibility" ) );
                    first_day.setHumidity( first_day_date_from_api.getInt( "humidity" ) );
                    first_day.setPredictability( first_day_date_from_api.getInt( "predictability" ) );

                    forecastByIDResponse.onResponse( first_day );


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        } ) ;


        MySingleton.getInstance( context ).addToRequestQueue( request );

        // get property called "consolodated_weather" which is an array


        //get each item in array and assaign it to a new weatherReportModel object

    }
//
//    public List<WeatherReportModel> getCityForecastByName(String cityName){
//
//    }

}
