package com.igorbelogubov.weather.view;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.igorbelogubov.weather.tools.InfoWindowRefresher;
import com.igorbelogubov.weather.R;
import com.igorbelogubov.weather.database.DataBase;
import com.squareup.picasso.Picasso;

public class CustomInfoWindow implements GoogleMap.InfoWindowAdapter {

    private View myContentsView;
    private LayoutInflater inflater;
    private Context ctxt = null;
    private DataBase dataBase;

    public CustomInfoWindow(Context ctxt, LayoutInflater inflater) {
        this.ctxt = ctxt;
        this.inflater = inflater;
        dataBase = new DataBase(ctxt);
        dataBase.open();
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        myContentsView = inflater.inflate(R.layout.custom_info_window, null);
        Cursor c = dataBase.getData();
        if (c.moveToFirst()) {
            int nameIndex = c.getColumnIndex(DataBase.COLUMN_NAME);
            int descriptionIndex = c.getColumnIndex(DataBase.COLUMN_DESCRIPTION);
            int tempIndex = c.getColumnIndex(DataBase.COLUMN_TEMP);
            int pressureIndex = c.getColumnIndex(DataBase.COLUMN_PRESSURE);
            int humidityIndex = c.getColumnIndex(DataBase.COLUMN_HUMIDITY);
            int tempMinIndex = c.getColumnIndex(DataBase.COLUMN_TEMP_MIN);
            int tempMaxIndex = c.getColumnIndex(DataBase.COLUMN_TEMP_MAX);
            int windSpeedIndex = c.getColumnIndex(DataBase.COLUMN_WIND_SPEED);
            int windDegIndex = c.getColumnIndex(DataBase.COLUMN_WIND_DEG);
            int iconIndex = c.getColumnIndex(DataBase.COLUMN_ICON);

            TextView city = ((TextView) myContentsView.findViewById(R.id.city));
            String upCity = c.getString(nameIndex);
            upCity = upCity.substring(0, 1).toUpperCase() + upCity.substring(1);
            city.setText(upCity);
            TextView description = ((TextView) myContentsView.findViewById(R.id.description));
            description.setText(c.getString(descriptionIndex));
            TextView temp = ((TextView) myContentsView.findViewById(R.id.temp));
            temp.setText(c.getString(tempIndex));
            TextView pressure = ((TextView) myContentsView.findViewById(R.id.pressure));
            pressure.setText(c.getString(pressureIndex));
            TextView humidity = ((TextView) myContentsView.findViewById(R.id.humidity));
            humidity.setText(c.getString(humidityIndex));
            TextView tempMin = ((TextView) myContentsView.findViewById(R.id.tempMin));
            tempMin.setText(c.getString(tempMinIndex));
            TextView tempMax = ((TextView) myContentsView.findViewById(R.id.tempMax));
            tempMax.setText(c.getString(tempMaxIndex));
            TextView windSpeed = ((TextView) myContentsView.findViewById(R.id.windSpeed));
            windSpeed.setText(c.getString(windSpeedIndex));
            TextView windDeg = ((TextView) myContentsView.findViewById(R.id.windDeg));
            windDeg.setText(c.getString(windDegIndex));
            ImageView icon = ((ImageView) myContentsView.findViewById(R.id.icon));
            Picasso.with(icon.getContext())
                    .load("http://api.openweathermap.org/img/w/" + c.getString(iconIndex))
                    .into(icon, new InfoWindowRefresher(marker));
        }
        return myContentsView;
    }
}
