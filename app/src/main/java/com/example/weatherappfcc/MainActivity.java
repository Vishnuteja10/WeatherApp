package com.example.weatherappfcc;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.CollationElementIterator;

public class MainActivity extends AppCompatActivity {

    Button btn_cityId,btn_getWeatherById,btn_getWeatherByName;
    EditText et_dataInput;
    ListView lv_weatherReport;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );

        // here we are assigning the control to layout

        btn_cityId = findViewById( R.id.btn_getCityId );
        btn_getWeatherById = findViewById( R.id.btn_getWeatherByCityId );
        btn_getWeatherByName = findViewById( R.id.btn_getWeatherByCityName );

        et_dataInput = findViewById( R.id.et_datainput );
        lv_weatherReport = findViewById( R.id.lv_WeatherReports );

        // click listeners for each button

        btn_getWeatherById.setOnClickListener( v -> {
            Toast.makeText( MainActivity.this, "its get weather by id", Toast.LENGTH_SHORT ).show();
        } );

        btn_getWeatherByName.setOnClickListener( v -> Toast.makeText( MainActivity.this,"its get weather by Name",Toast.LENGTH_SHORT ).show() );

        btn_cityId.setOnClickListener( v -> {

//            // Instantiate the RequestQueue.
            RequestQueue queue = Volley.newRequestQueue(this);
            String url ="https://www.metaweather.com/api/location/search/?query=london";

            JsonArrayRequest request = new JsonArrayRequest( Request.Method.GET,url,null, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {

                    String cityId = " ";
                    try {
                        JSONObject cityInfo = response.getJSONObject( 0 );
                         cityId = cityInfo.getString( "woeid" );
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Toast.makeText( MainActivity.this,"city Id =" + cityId,Toast.LENGTH_SHORT ).show();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText( MainActivity.this,"Something Wrong",Toast.LENGTH_SHORT ).show();
                }
            } );
//
            queue.add(request);
//// Request a string response from the provided URL.
//            StringRequest stringRequest = new StringRequest( Request.Method.GET, url,
//                    new Response.Listener<String>() {
//                        @Override
//                        public void onResponse(String response) {
//
//                            Toast.makeText( MainActivity.this, response, Toast.LENGTH_SHORT ).show();
//                        }
//                    }, new Response.ErrorListener() {
//                @Override
//                public void onErrorResponse(VolleyError error) {
//
//                    Toast.makeText( MainActivity.this,"error",Toast.LENGTH_SHORT ).show();
//                }
            });

// Add the request to the RequestQueue.



    }
}