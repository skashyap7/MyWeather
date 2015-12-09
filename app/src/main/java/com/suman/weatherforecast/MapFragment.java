package com.suman.weatherforecast;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hamweather.aeris.communication.AerisCallback;
import com.hamweather.aeris.communication.AerisEngine;
import com.hamweather.aeris.communication.EndpointType;
import com.hamweather.aeris.maps.AerisMapView;
import com.hamweather.aeris.maps.MapViewFragment;
import com.hamweather.aeris.maps.interfaces.OnAerisMapLongClickListener;
import com.hamweather.aeris.model.AerisResponse;
import com.hamweather.aeris.tiles.AerisTile;

public class MapFragment extends MapViewFragment implements
        OnAerisMapLongClickListener, AerisCallback {
      private OnFragmentInteractionListener mListener;


    public static MapFragment newInstance(String param1, String param2) {
        MapFragment fragment = new MapFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public MapFragment() {
        // Required empty public constructor
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        AerisEngine.initWithKeys(getResources().getString(R.string.aeris_client_id), getResources().getString(R.string.aeris_client_secret), "WeatherForecast");
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        mapView = (AerisMapView)view.findViewById(R.id.aerisfragment_map);
        mapView.init(savedInstanceState, AerisMapView.AerisMapType.GOOGLE);

        Bundle bundle = getArguments();
        String lat = bundle.getString("latitude");
        String lon = bundle.getString("longitude");

        Location location = new Location("");
        location.setLatitude(Double.valueOf(lat));
        location.setLongitude(Double.valueOf(lon));
        mapView.moveToLocation(location,9);
        //mapView.setOnAerisMapLongClickListener(this);
        mapView.addLayer(AerisTile.RADSAT);
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    /*@Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }*/

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onResult(EndpointType endpointType, AerisResponse aerisResponse) {

    }

    @Override
    public void onMapLongClick(double v, double v1) {

    }

        public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
