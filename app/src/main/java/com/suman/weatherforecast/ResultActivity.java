package com.suman.weatherforecast;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

public class ResultActivity extends FragmentActivity {
    public JSONObject weather_json;
    // Facebook related variables
    CallbackManager callbackManager;
    ShareDialog shareDialog;

    String city;
    String state;
    String unit;
    String weatherSummary_g;
    Integer Temprature;
    String img_url;
    String wind_unit;
    String temp_unit;
    String dew_unit;
    String visibility_unit;
    Long latitude;
    Long longitude;
    String icon_val;
    protected void getUnits(String unit)
    {
        switch(unit)
        {
            case "si":
                wind_unit = "mps";
                dew_unit = "°C";
                temp_unit = "°C";
                visibility_unit = "km";
                break;
            case "us":
                wind_unit = "mph";
                dew_unit = "°F";
                temp_unit = "°F";
                visibility_unit ="mi";
                break;
        }
    }
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        shareDialog = new ShareDialog(this);
        shareDialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {
            @Override
            public void onSuccess(Sharer.Result result) {
                Log.d("LOG_TAG", "success");
                Context context = getApplicationContext();
                CharSequence text = "Facebook Post Successful";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }

            @Override
            public void onError(FacebookException error) {
                Log.d("LOG_TAG", "error");
                Log.d("LOG_TAG", String.valueOf(error));
            }

            @Override
            public void onCancel() {
                Log.d("LOG_TAG", "cancel");
                Context context = getApplicationContext();
                CharSequence text = "Facebook Post Cancelled";
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }
        });

        Intent intent = getIntent();
        String json_input = intent.getStringExtra("JSON_OUTPUT");
        city = intent.getStringExtra("CITY");
        state = intent.getStringExtra("STATE");
        unit = intent.getStringExtra("UNITS");
        getUnits(unit);
        JSONObject json_object = null;
        Integer chanceRain;
        Integer precipitation;
        String precip_value;
        String windSpeed;
        String dewPoint;
        Integer Humidity;
        String Visibility;
        Long Sunrise;
        Long Sunset;
        Integer lowTemp;
        Integer highTemp;
        Integer temp;
        String weatherSummary;
        String icon_val;
        try {
            json_object = new JSONObject(json_input);
            weather_json = new JSONObject(json_input);
            latitude = json_object.getLong("latitude");
            longitude = json_object.getLong("longitude");
            setContentView(R.layout.activity_result);
            JSONObject currently = json_object.getJSONObject("currently");
            JSONObject daily = json_object.getJSONObject("daily");
            JSONArray daily_array = daily.getJSONArray("data");
            JSONObject daily_first = daily_array.getJSONObject(0);

            weatherSummary = currently.getString("summary");
            weatherSummary_g = currently.getString("summary");
            weatherSummary = weatherSummary+" in "+city+","+state;
            TextView weather_label = (TextView) findViewById(R.id.weathersummary);
            weather_label.setText(weatherSummary);

            chanceRain = currently.getInt("precipProbability");
            chanceRain = chanceRain*100;
            TextView rain_label = (TextView)findViewById(R.id.chancerain);
            rain_label.setText(chanceRain.toString()+"%");

            dewPoint = currently.getString("dewPoint")+dew_unit;
            TextView dew_label = (TextView) findViewById(R.id.dewpoint);
            Log.d("output",dewPoint);
            dew_label.setText(dewPoint);

            precipitation = currently.getInt("precipIntensity");
            precip_value ="None"; // Setting default to avoid error
            if(precipitation < 0.002){
                    precip_value = "None";
            }
            else if (precipitation >= 0.002 && precipitation < 0.017){
                    precip_value = "Very Light";
            }
            else if(precipitation >= 0.017 && precipitation < 0.1){
                    precip_value = "Light";
            }
            else if(precipitation >= 0.1 && precipitation < 0.4){
                    precip_value = "Moderate";
            }
            else if(precipitation >= 0.4){
                    precip_value = "High";
            }
            TextView precipitation_label = (TextView)findViewById(R.id.precipitation);
            precipitation_label.setText(precip_value);

            // Setting Wind Speed
            windSpeed = currently.getString("windSpeed")+wind_unit;
            TextView windspeed_label = (TextView) findViewById(R.id.windspeed);
            windspeed_label.setText(windSpeed);

            // Setting Humidity
            Humidity= currently.getInt("humidity");
            TextView humidity_label = (TextView)findViewById(R.id.humidity);
            Humidity = Humidity *100;
            humidity_label.setText(Humidity +"%");

            // Setting Visibility
            Visibility = currently.getString("visibility")+visibility_unit;
            TextView visibility_label = (TextView)findViewById(R.id.visibility);
            visibility_label.setText(Visibility);

            String Offset = json_object.getString("offset");
            Sunrise = daily_first.getLong("sunriseTime");
            Date date = new Date(Sunrise*1000L); // *1000 is to convert seconds to milliseconds
            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a"); // the format of your date
            sdf.setTimeZone(TimeZone.getTimeZone("GMT" + Offset)); // give a timezone reference for formating (see comment at the bottom
            TextView sunrise_label = (TextView)findViewById(R.id.sunrise);
            sunrise_label.setText(sdf.format(date));


            Sunset = daily_first.getLong("sunsetTime");
            Date date1 = new Date(Sunset*1000L); // *1000 is to convert seconds to milliseconds
            SimpleDateFormat sdf1 = new SimpleDateFormat("hh:mm a"); // the format of your date
            sdf1.setTimeZone(TimeZone.getTimeZone("GMT+8")); // give a timezone reference for formating (see comment at the bottom
            TextView sunset_label = (TextView)findViewById(R.id.sunset);
            sunset_label.setText(sdf.format(date1));

            lowTemp = daily_first.getInt("temperatureMin");
            highTemp = daily_first.getInt("temperatureMax");
            TextView hightemp_label = (TextView) findViewById(R.id.maxtemp);
            hightemp_label.setText(lowTemp+"° | "+highTemp+"°");

            temp = currently.getInt("temperature");
            Temprature = temp;
            TextView temp_label = (TextView) findViewById(R.id.temperature);
            temp_label.setText(temp+temp_unit);
            String img_src = null;
            icon_val = currently.getString("icon");
            ImageView img_icon = (ImageView)findViewById(R.id.icon);
            Log.d("Icon", icon_val);
            switch(icon_val){
                case "clear-day":
                    img_icon.setImageResource(R.drawable.clear);
                    img_url="http://cs-server.usc.edu:45678/hw/hw8/images/clear.png";
                    break;
                case "clear-night":
                    img_icon.setImageResource(R.drawable.clear_night);
                    img_url="http://cs-server.usc.edu:45678/hw/hw8/images/clear_night.png";
                    break;
                case "rain":
                    img_icon.setImageResource(R.drawable.rain);
                    img_url="http://cs-server.usc.edu:45678/hw/hw8/images/rain.png";
                    break;
                case "snow":
                    img_icon.setImageResource(R.drawable.snow);
                    img_url="http://cs-server.usc.edu:45678/hw/hw8/images/smow.png";
                    break;
                case "sleet":
                    img_icon.setImageResource(R.drawable.sleet);
                    img_url="http://cs-server.usc.edu:45678/hw/hw8/images/sleet.png";
                    break;
                case "wind":
                    img_icon.setImageResource(R.drawable.wind);
                    img_url="http://cs-server.usc.edu:45678/hw/hw8/images/wind.png";
                    break;
                case "fog":
                    img_icon.setImageResource(R.drawable.fog);
                    img_url="http://cs-server.usc.edu:45678/hw/hw8/images/fog.png";
                    break;
                case "cloudy":
                    img_icon.setImageResource(R.drawable.cloudy);
                    img_url="http://cs-server.usc.edu:45678/hw/hw8/images/cloudy.png";
                    break;
                case "partly-cloudy-day":
                    img_icon.setImageResource(R.drawable.cloud_day);
                    img_url="http://cs-server.usc.edu:45678/hw/hw8/images/cloud_day.png";
                    break;
                case "partly-cloudy-night":
                    img_url="http://cs-server.usc.edu:45678/hw/hw8/images/cloud_night.png";
                    img_icon.setImageResource(R.drawable.cloud_night);
                    break;
            }
        } catch (JSONException e1) {
            e1.printStackTrace();
        }
    }
    public void viewDetails(View view) throws JSONException {
        // view Hourly
        Intent intent = new Intent(this, DetailsActivity.class);
        intent.putExtra("JSON_OUTPUT",weather_json.toString());
        intent.putExtra("UNITS",unit);
        intent.putExtra("STATE",state);
        intent.putExtra("CITY",city);
        startActivity(intent);
    }
    public void viewMap(View view){
        // Start activity for Map
        Intent intent = new Intent(this, MapsActivity.class);
        intent.putExtra("Longitude",longitude.toString());
        intent.putExtra("Latitude", latitude.toString());
        startActivity(intent);
    }
    public void facebookShare(View view){
        // Again Facebook related stuff
        String content_title = "Current Weather in "+city+","+state;
        String summary = weatherSummary_g+" , "+Temprature+ " ℉";
        if (shareDialog.canShow(ShareLinkContent.class)) {

            ShareLinkContent linkContent = new ShareLinkContent.Builder()
                    .setContentTitle(content_title)
                    .setContentDescription(summary)
                    .setContentUrl(Uri.parse("http://forecast.io/"))
                    .setImageUrl(Uri.parse(img_url))
                    .build();
            shareDialog.show(linkContent);
        }
    }
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
