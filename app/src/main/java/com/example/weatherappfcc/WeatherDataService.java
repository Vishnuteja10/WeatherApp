package com.example.weatherappfcc;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class WeatherDataService {

    public static final String QUERY_FOR_CITY_ID = "https://www.metaweather.com/api/location/search/?query=";

    Context context;
    String cityId;

    public WeatherDataService(Context context) {
        this.context = context;
    }

    public String getCityId(String cityName){


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
                Toast.makeText( context,"city Id =" + cityId,Toast.LENGTH_SHORT ).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText( context,"Something Wrong",Toast.LENGTH_SHORT ).show();
            }
        } );

        MySingleton.getInstance( context ).addToRequestQueue( request );

        // Id was not returned!
        return cityId;

    }

//    public List<WeatherReportModel> getCityForecastById(String cityID){
//
//    }
//
//    public List<WeatherReportModel> getCityForecastByName(String cityName){
//
//    }

}
