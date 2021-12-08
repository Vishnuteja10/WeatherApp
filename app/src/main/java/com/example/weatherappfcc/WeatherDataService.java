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
        void onResponse(List<WeatherReportModel> weatherReportModels);
    }
    public void getCityForecastById(String cityID,ForecastByIDResponse forecastByIDResponse){

        String url = QUERY_FOR_CITY_WEATHER_BY_ID + cityID;

        List<WeatherReportModel> weatherReportModels = new ArrayList<>();

        //get json object
        JsonObjectRequest request = new JsonObjectRequest( Request.Method.GET,url,null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
               // Toast.makeText( context,response.toString(),Toast.LENGTH_SHORT ).show();

                try {
                    JSONArray consolidated_weather_list = response.getJSONArray( "consolidated_weather" );




                    for(int i = 0; i<consolidated_weather_list.length(); i++) {
                        WeatherReportModel one_day_weather = new WeatherReportModel();
                        JSONObject first_day_date_from_api = (JSONObject) consolidated_weather_list.get( i );
                        one_day_weather.setId( first_day_date_from_api.getInt( "id" ) );
                        one_day_weather.setWeather_state_name( first_day_date_from_api.getString( "weather_state_name" ) );
                        one_day_weather.setWeather_state_abbr( first_day_date_from_api.getString( "weather_state_abbr" ) );
                        one_day_weather.setWind_direction_compass( first_day_date_from_api.getString( "wind_direction_compass" ) );
                        one_day_weather.setCreated( first_day_date_from_api.getString( "created" ) );
                        one_day_weather.setApplicable_date( first_day_date_from_api.getString( "applicable_date" ) );
                        one_day_weather.setMin_temp( first_day_date_from_api.getLong( "min_temp" ) );
                        one_day_weather.setMax_temp( first_day_date_from_api.getLong( "max_temp" ) );
                        one_day_weather.setThe_temp( first_day_date_from_api.getLong( "the_temp" ) );
                        one_day_weather.setWind_speed( first_day_date_from_api.getLong( "wind_speed" ) );
                        one_day_weather.setWind_direction( first_day_date_from_api.getLong( "wind_direction" ) );
                        one_day_weather.setVisibility( first_day_date_from_api.getLong( "visibility" ) );
                        one_day_weather.setHumidity( first_day_date_from_api.getInt( "humidity" ) );
                        one_day_weather.setPredictability( first_day_date_from_api.getInt( "predictability" ) );

                        weatherReportModels.add(one_day_weather  );
                    }
                    forecastByIDResponse.onResponse( weatherReportModels );


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

    public interface GetCityForecastByNameCallback{
        void onError(String message);
        void onResponse(List<WeatherReportModel> weatherReportModels);
    }

    public void getCityForecastByName(String cityName,GetCityForecastByNameCallback getCityForecastByNameCallback){
         //fetch the city  id given city name
        getCityId( cityName, new VolleyResponseListener() {
            @Override
            public void onError(String message) {

            }

            @Override
            public void onResponse(String cityId) {
                  // now we have the city id
                getCityForecastById( cityId, new ForecastByIDResponse() {
                    @Override
                    public void onError(String message) {

                    }

                    @Override
                    public void onResponse(List<WeatherReportModel> weatherReportModels) {
                        // we the weather report
                        getCityForecastByNameCallback.onResponse( weatherReportModels );
                    }
                } );
            }
        } );

        //fetch the city forecast given city id
    }

}
