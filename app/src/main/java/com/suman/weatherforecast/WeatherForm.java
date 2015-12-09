package com.suman.weatherforecast;

import android.content.Intent;
import android.net.Uri;
import android.net.http.HttpResponseCache;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.JsonReader;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.facebook.FacebookSdk;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class WeatherForm extends AppCompatActivity {
    public final static String EXTRA_MESSAGE = "com.suman.weatherforecast.MESSAGE";
    public final static String JSON_OUTPUT = "com.suman.weatherforecast.MESSAGE";
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_form);
        if (android.os.Build.VERSION.SDK_INT > 9)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_weather_form, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public void submitForm(View view) throws MalformedURLException {
        int error = 0;
        String city_text="", state_text="", street_text="", degree_text="us";
        EditText street = (EditText) findViewById(R.id.editText);
        TextView errortext = (TextView) findViewById(R.id.error);
        errortext.setText("");
        if(street.getText().toString().isEmpty()){
            errortext.setText("Please enter the Street");
            error = 1;
            return;
        }
        else {
            street_text = street.getText().toString();
        }
        EditText city = (EditText) findViewById(R.id.editText1);
        if(city.getText().toString().isEmpty()){
            errortext.setText("Please enter the City");
            error = 1;
            return;
        }
        else{
            city_text = city.getText().toString();
        }
        Spinner state = (Spinner) findViewById(R.id.spinner);
        state_text = state.getSelectedItem().toString();
        if(state_text.equals("Select State")){
            errortext.setText("Please select the State");
            error = 1;
            return;
        }
        RadioButton degree_us = (RadioButton) findViewById(R.id.radioButton);
        RadioButton degree_si = (RadioButton) findViewById(R.id.radioButton2);
        if(degree_us.isChecked())
        {
            degree_text = "us";
        }
        else{
            degree_text ="si";
        }
        if(error == 0) {
            StringBuilder json_output = new StringBuilder();
            Uri uri = Uri.parse("http://sumancsci571-env.elasticbeanstalk.com/index.php");
            Uri.Builder uri_builder = uri.buildUpon();
            //uri_builder.path("http://sumancsci571-env.elasticbeanstalk.com/index.php");
            uri_builder.appendQueryParameter("street", street_text)
                    .appendQueryParameter("state", state_text)
                    .appendQueryParameter("degree", degree_text)
                    .appendQueryParameter("city",city_text)
                    .build();
            try {
                URL url = new URL(uri_builder.toString());
                HttpURLConnection aws_connection = (HttpURLConnection)url.openConnection();
                Log.d("Street", street_text);
                Log.d("City", city_text);
                Log.d("State", state_text);
                Log.d("Degrees", degree_text);
                try {
                    InputStream in = new BufferedInputStream(aws_connection.getInputStream());
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        Log.d("Output", line.toString());
                        json_output.append(line);
                    }
                    aws_connection.disconnect();
                    Log.d("Output", json_output.toString());
                    Intent intent = new Intent(this, ResultActivity.class);
                    intent.putExtra("JSON_OUTPUT",json_output.toString());
                    intent.putExtra("CITY",city_text);
                    intent.putExtra("STATE",state_text);
                    intent.putExtra("UNITS",degree_text);
                    startActivity(intent);
                }
                catch(IOException e){
                    Log.d("Exception","IO exception");
                    e.printStackTrace();
                }
            } catch (IOException e) {
                Log.d("Exception","IO exception");
                e.printStackTrace();
            }

        }
    }
    public void clearForm(View view){
        EditText inputtext = (EditText) findViewById(R.id.editText);
        inputtext.getText().clear();
        EditText inputtext1 = (EditText) findViewById(R.id.editText1);
        inputtext1.getText().clear();
        TextView errortext = (TextView) findViewById(R.id.error);
        errortext.setText("");
    }
     public void showAbout(View view){
         Intent intent = new Intent(this, DisplayAboutActivity.class);
         String about_me = "Name: Suman Kashyap Student ID: 1533-745-841";
         intent.putExtra(JSON_OUTPUT,about_me);
         startActivity(intent);
     }
    public void redirectForecast(View view)
    {
        Intent viewIntent = new Intent("android.intent.action.VIEW", Uri.parse("http://forecast.io/"));
        startActivity(viewIntent);
    }
}
