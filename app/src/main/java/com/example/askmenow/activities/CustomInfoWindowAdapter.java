package com.example.askmenow.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.askmenow.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

public class CustomInfoWindowAdapter extends AppCompatActivity {

    /*private final View mWindow;
    private final Context mContext;*/
    private String markerName;
    private String markerAddr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.marker_info_window);
        Intent intent = getIntent();
        setUpView(intent);
    }

    private void setUpView(Intent intent) {
        markerName = (String) intent.getStringExtra("MARKERNAME");
        markerAddr = (String) intent.getStringExtra("MARKERADDR");
        TextView markerNameText = findViewById(R.id.marker_name);
        TextView markerAddrText = findViewById(R.id.marker_address);
        markerNameText.setText(markerName);
        markerAddrText.setText(markerAddr);

    }

    /*public CustomInfoWindowAdapter(Context context) {
        mContext = context;
        mWindow = LayoutInflater.from(context).inflate(R.layout.marker_info_window, null);
    }
    private void renderWindowText(Marker marker, View view) {
        String title = marker.getTitle();
        TextView titleTv = view.findViewById(R.id.marker_name);
        if (!TextUtils.isEmpty(title)) {
            titleTv.setText(title);
        }
        String snippet = marker.getSnippet();
        TextView snippetTv = view.findViewById(R.id.marker_address);
        if (!TextUtils.isEmpty(snippet)) {
            snippetTv.setText(snippet);
        }
    }
    @Override
    public View getInfoWindow(Marker marker) {
        if (marker != null) {
            renderWindowText(marker, mWindow);
            mMarker = marker;
            renderWindowText(marker, mWindow);
        }
        return null;
    }
    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }*/

}