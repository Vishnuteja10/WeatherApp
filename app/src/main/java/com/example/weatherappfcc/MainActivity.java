package com.example.weatherappfcc;

import android.os.Bundle;
import android.view.View;
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
    WeatherDataService weatherDataService = new WeatherDataService(MainActivity.this);


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

        btn_getWeatherById.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                weatherDataService.getCityForecastById( et_dataInput.getText().toString(), new WeatherDataService.ForecastByIDResponse() {
                    @Override
                    public void onError(String message) {
                        Toast.makeText( MainActivity.this,"Error",Toast.LENGTH_SHORT ).show();
                    }

                    @Override
                    public void onResponse(WeatherReportModel weatherReportModel) {
                     Toast.makeText( MainActivity.this,weatherReportModel.toString(),Toast.LENGTH_SHORT ).show();
                    }
                } );

            }
        } );

        btn_getWeatherByName.setOnClickListener( v -> Toast.makeText( MainActivity.this,"its get weather by Name",Toast.LENGTH_SHORT ).show() );

        btn_cityId.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // this didnt work

                 weatherDataService.getCityId( et_dataInput.getText().toString(), new WeatherDataService.VolleyResponseListener() {
                    @Override
                    public void onError(String message) {
                        Toast.makeText( MainActivity.this,message,Toast.LENGTH_SHORT ).show();

                    }

                    @Override
                    public void onResponse(String cityId) {
                        Toast.makeText( MainActivity.this,"Returned Id of"+cityId,Toast.LENGTH_SHORT ).show();

                    }
                } );

            }
        } );

    }
}