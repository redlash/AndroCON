package com.example.em9736.androcon;

import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.text.format.DateUtils;
import android.net.Uri;

//import com.aware.providers.Locations_Provider;
//import com.aware.utils.DatabaseHelper;

import java.io.IOException;
import java.util.List;

/**
 * Created by em9736 on 16/09/2016.
 */
public class Aware2AndroConAdapter {



    public static void getLocation()
    {
 /*       Cursor last_location = context.getContentResolver().query(Locations_Provider.Locations_Data.CONTENT_URI, null, null, null, Locations_Provider.Locations_Data.TIMESTAMP + " DESC LIMIT 1");
        if (last_location != null && last_location.moveToFirst()) {
            double lat = last_location.getDouble(last_location.getColumnIndex(Locations_Provider.Locations_Data.LATITUDE));
            double lon = last_location.getDouble(last_location.getColumnIndex(Locations_Provider.Locations_Data.LONGITUDE));
            long timestamp = last_location.getLong(last_location.getColumnIndex(Locations_Provider.Locations_Data.TIMESTAMP));

            Location user_location = new Location("Current Location");
            user_location.setLatitude(lat);
            user_location.setLongitude(lon);
            user_location.setAccuracy(last_location.getFloat(last_location.getColumnIndex(Locations_Provider.Locations_Data.ACCURACY)));

            last_update.setText(String.format("%s", DateUtils.getRelativeTimeSpanString(timestamp, System.currentTimeMillis(), DateUtils.MINUTE_IN_MILLIS).toString()));

            try {
                Geocoder geo = new Geocoder(context);
                String geo_text = "";
                List<Address> addressList = geo.getFromLocation(lat, lon, 1);
                for (int i = 0; i < addressList.size(); i++) {
                    Address address1 = addressList.get(i);
                    for (int j = 0; j < address1.getMaxAddressLineIndex(); j++) {
                        if (address1.getAddressLine(j).length() > 0) {
                            geo_text += address1.getAddressLine(j) + "\n";
                        }
                    }
                    geo_text += address1.getCountryName();
                }

                geo_text += " (" + last_location.getFloat(last_location.getColumnIndex(Locations_Provider.Locations_Data.ACCURACY)) + " meters)";

                //address.setText(geo_text);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }*/
    }

    public static void getActivity()
    {

        /*Uri activity_uri = Uri.parse("content://com.aware.plugin.google.activity_recognition.provider.gar/plugin_google_activity_recognition");
        Cursor activity_data = getContentResolver().query(activity_uri,
                null, null, null, " timestamp DESC LIMIT 2");
        if( activity_data != null && activity_data.getCount() > 0 ) {
            //tv.setText(DatabaseHelper.cursorToString(activity_data));
        }else {
            //tv.setText("Could not get Activity data.");
        }
        if( activity_data != null && ! activity_data.isClosed() ) activity_data.close();*/
    }


}
