package com.suman.weatherforecast;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.renderscript.Sampler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

import static com.suman.weatherforecast.R.color.colorGrey;
import static com.suman.weatherforecast.R.color.colorPrimary;
import static com.suman.weatherforecast.R.color.colorWhite;

public class DetailsActivity extends AppCompatActivity {
    protected JSONObject json_object;
    protected String wind_unit;
    protected String state;
    protected String city;
    protected String temp_unit;
    protected String dew_unit;
    protected String unit;
    protected String visibility_unit;
    private String getDateFromTimeStamp(Long timestamp) throws JSONException {
        Date date = new Date(timestamp*1000L); // *1000 is to convert seconds to milliseconds
        String Offset = json_object.getString("offset");
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE,MMM dd"); // the format of your date
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"+Offset)); // give a timezone reference for formating (see comment at the bottom
        String formattedDate = sdf.format(date);
        return formattedDate;
    }
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
    private Integer getIconResource(String summary){
        Integer resource = 0;
        switch(summary){
            case "clear-day":
                resource = R.drawable.clear;
                break;
            case "clear-night":
                resource = R.drawable.clear_night;
                break;
            case "rain":
                resource = R.drawable.rain;
                break;
            case "snow":
                resource = R.drawable.snow;
                break;
            case "sleet":
                resource = R.drawable.sleet;
                break;
            case "wind":
                // summary_label.setImageResource(R.drawable.wind);
                resource = R.drawable.wind;
                break;
            case "fog":
                resource = R.drawable.fog;
                break;
            case "cloudy":
                resource = R.drawable.cloudy;
                break;
            case "partly-cloudy-day":
                resource = R.drawable.cloud_day;
                break;
            case "partly-cloudy-night":
                resource = R.drawable.cloud_night;
                break;
        }
        return resource;
    }
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Intent intent = getIntent();
        Button button_weekly = (Button) findViewById(R.id.button2);
        Button button_hourly = (Button) findViewById(R.id.button);
        button_hourly.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        Integer index;
        String weather_json = intent.getStringExtra("JSON_OUTPUT");
        unit = intent.getStringExtra("UNITS");
        state = intent.getStringExtra("STATE");
        city = intent.getStringExtra("CITY");
        getUnits(unit);
        TextView summary_label1 = (TextView) findViewById(R.id.summary);
        summary_label1.setText("More Details for "+city+", "+state);
        try {
            json_object = new JSONObject(weather_json);
            // All the Hourly manipulations
            JSONObject hourly = json_object.getJSONObject("hourly");
            JSONArray hour_w = hourly.getJSONArray("data");
            TextView temp_heading = (TextView) findViewById(R.id.tempheading);
            temp_heading.setText("Temp("+temp_unit+")");
            for(index = 1 ; index < 25 ;index++)
            {
                Long time;
                String summary;
                Integer Temp;
                JSONObject hr = hour_w.getJSONObject(index);

                // CREATING NEW TABLE ROW
                TableRow new_tr = new TableRow(this);

                /* Time manipulations to be sent to another function*/
                time = hr.getLong("time");
                //Date date = new Date(time);
                String Offset = json_object.getString("offset");
                Date date = new Date(time*1000L); // *1000 is to convert seconds to milliseconds
                SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a"); // the format of your date
                sdf.setTimeZone(TimeZone.getTimeZone("GMT"+Offset)); // give a timezone reference for formating (see comment at the bottom
                String formattedDate = sdf.format(date);
                TextView time_label = new TextView(this);
                time_label.setText(sdf.format(date));
                //time_label.setLayoutParams(new TableRow.LayoutParams());

                // fetching Temperature
                Temp = hr.getInt("temperature");
                TextView temp_label = new TextView(this);
                temp_label.setText(Temp.toString());
                temp_label.setLayoutParams(new TableRow.LayoutParams(9));
                // fetching Image
                summary = hr.getString("icon");
                ImageView summary_label = new ImageView(this);
                Integer res_id = getIconResource(summary);
                summary_label.setImageResource(res_id);
                summary_label.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                summary_label.setAdjustViewBounds(true);
                summary_label.setMaxHeight(200);
                summary_label.setMaxWidth(200);

                // Setting the background for alternating table rows
                if( index%2 == 0) {
                    new_tr.setBackgroundColor(getResources().getColor(colorWhite));
                }
                else{
                    new_tr.setBackgroundColor(getResources().getColor(colorGrey));
                }
                //Adding all elements to the row
                new_tr.addView(time_label);
                new_tr.addView(summary_label);
                new_tr.addView(temp_label);
                new_tr.setPadding(0, 10, 0, 10);
                // Adding table row to the tableLayout
                TableLayout hour_layout = (TableLayout) findViewById(R.id.table_hourly);
                hour_layout.addView(new_tr);
            }
            // Adding the + Button
            final TableRow new_tr = new TableRow(this);
            final Button next_items = new Button(this);
            next_items.setText("+");
            next_items.setBackgroundColor(getResources().getColor(colorPrimary));
            new_tr.addView(next_items);
            new_tr.setBackgroundColor(getResources().getColor(R.color.colorGrey));
            new_tr.setGravity(Gravity.CENTER_HORIZONTAL);
            next_items.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    new_tr.removeView(next_items);
                    try {
                        show_rest_elements();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });

            TableLayout hour_layout = (TableLayout) findViewById(R.id.table_hourly);
            hour_layout.addView(new_tr);

            // All the weekly manipulations go here
            JSONObject weekly = json_object.getJSONObject("daily");
            JSONArray dayObject = weekly.getJSONArray("data");
            // Inserting data per day
            Integer[] Color = {R.color.colorPlum,R.color.colorRow1, R.color.colorRow2, R.color.colorRow3, R.color.colorRow4, R.color.colorRow5, R.color.colorRow6,R.color.colorRow7};
            for(index = 1;index <8;index++) {
                TableRow tr = new TableRow(this);
                tr.setBackgroundColor(getResources().getColor(Color[index]));
                JSONObject day = dayObject.getJSONObject(index);

                TextView day_date = new TextView(this);
                Long timestamp = day.getLong("time");
                String date = getDateFromTimeStamp(timestamp);
                day_date.setText(date);
                day_date.setTextSize(30);
                tr.addView(day_date);

                ImageView img = new ImageView(this);
                img.setMaxHeight(200);
                img.setMaxWidth(200);
                img.setScaleType(ImageView.ScaleType.FIT_END);
                img.setAdjustViewBounds(true);
                img.setImageResource(getIconResource(day.getString("icon")));
                tr.addView(img);

                TableRow tr1 = new TableRow(this);
                tr1.setBackgroundColor(getResources().getColor(Color[index]));
                Integer minTemp = day.getInt("temperatureMin");
                Integer maxTemp = day.getInt("temperatureMax");
                String temp_line = "Min : " + minTemp + temp_unit +" | Max: " + maxTemp + temp_unit;
                TextView temp_min = new TextView(this);
                temp_min.setText(temp_line);
                tr1.addView(temp_min);

                tr1.setPadding(10, 30, 10, 30);
                if(index == 1)
                {
                    tr.setPadding(10, 30, 10, 30);
                }
                else{
                    tr.setPadding(10, 10, 10, 30);
                }
                tr1.setDividerPadding(20);
                /* Dummy Row */
                TableRow tr_padding = new TableRow(this);
                tr_padding.setBackgroundColor(android.graphics.Color.LTGRAY);
                tr_padding.setMinimumHeight(30);
                TableLayout tbl_week = (TableLayout) findViewById(R.id.table_weekly);
                tbl_week.addView(tr);
                tbl_week.addView(tr1);
                tbl_week.addView(tr_padding);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    public void show_rest_elements() throws JSONException {
        Integer index = 0;
        // All the Hourly manipulations
        JSONObject hourly = json_object.getJSONObject("hourly");
        JSONArray hour_w = hourly.getJSONArray("data");
        TextView temp_heading = (TextView) findViewById(R.id.tempheading);
        temp_heading.setText("Temp("+temp_unit+")");
        for(index = 25 ; index < 49 ;index++)
        {
            Long time;
            String summary;
            Integer Temp;
            JSONObject hr = hour_w.getJSONObject(index);

            // CREATING NEW TABLE ROW
            TableRow new_tr = new TableRow(this);

                /* Time manipulations to be sent to another function*/
            time = hr.getLong("time");
            //Date date = new Date(time);
            Date date = new Date(time*1000L); // *1000 is to convert seconds to milliseconds
            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a"); // the format of your date
            sdf.setTimeZone(TimeZone.getTimeZone("GMT+8")); // give a timezone reference for formating (see comment at the bottom
            String formattedDate = sdf.format(date);
            TextView time_label = new TextView(this);
            time_label.setText(sdf.format(date));
            //time_label.setLayoutParams(new TableRow.LayoutParams());

            // fetching Temperature
            Temp = hr.getInt("temperature");
            TextView temp_label = new TextView(this);
            temp_label.setText(Temp.toString());
            temp_label.setLayoutParams(new TableRow.LayoutParams(9));
            // fetching Image
            summary = hr.getString("icon");
            ImageView summary_label = new ImageView(this);
            Integer res_id = getIconResource(summary);
            summary_label.setImageResource(res_id);
            summary_label.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            summary_label.setAdjustViewBounds(true);
            summary_label.setMaxHeight(200);
            summary_label.setMaxWidth(200);

            // Setting the background for alternating table rows
            if( index%2 == 0) {
                new_tr.setBackgroundColor(getResources().getColor(colorWhite));
            }
            else{
                new_tr.setBackgroundColor(getResources().getColor(colorGrey));
            }
            //Adding all elements to the row
            new_tr.addView(time_label);
            new_tr.addView(summary_label);
            new_tr.addView(temp_label);
            new_tr.setPadding(0,10,0,10);
            // Adding table row to the tableLayout
            TableLayout hour_layout = (TableLayout) findViewById(R.id.table_hourly);
            hour_layout.addView(new_tr);
        }
    }
    public void hideHours(View view){
        RelativeLayout hourview = (RelativeLayout) findViewById(R.id.layout_hours);
        RelativeLayout weekview = (RelativeLayout) findViewById(R.id.layout_weeks);
        ScrollView sc = (ScrollView) findViewById(R.id.scrolltop);
        sc.setBackgroundColor(Color.LTGRAY);
        weekview.setVisibility(View.VISIBLE);
        hourview.setVisibility(View.INVISIBLE);
        Button button_weekly = (Button) findViewById(R.id.button2);
        Button button_hourly = (Button) findViewById(R.id.button);
        button_weekly.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        button_hourly.setBackgroundColor(getResources().getColor(R.color.colorGrey));
    }
    public void hideWeeks(View view)
    {
        RelativeLayout weekview = (RelativeLayout) findViewById(R.id.layout_weeks);
        ScrollView sc = (ScrollView) findViewById(R.id.scrolltop);
        sc.setBackgroundColor(getResources().getColor(R.color.colorLightPink));
        weekview.setVisibility(View.INVISIBLE);
        RelativeLayout hourview = (RelativeLayout) findViewById(R.id.layout_hours);
        hourview.setVisibility(View.VISIBLE);
        Button button_weekly = (Button) findViewById(R.id.button2);
        Button button_hourly = (Button) findViewById(R.id.button);
        button_hourly.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        button_weekly.setBackgroundColor(getResources().getColor(R.color.colorGrey));

    }
}
