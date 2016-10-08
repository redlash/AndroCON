package utils;

import android.os.Environment;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import ontology.AndroConAddress;

/**
 * Created by em9736 on 21/09/2016.
 */
public class OntologyHelper {
    /**
     * This method get the current datetime, and turn into a string.
     * @return
     */
    public static String getCurrentTimestamp()
    {
        long ts = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
        Date resultdate = new Date(ts);
        return sdf.format(resultdate);
    }

    public static String ipToString(int ip) {
        String ipStr =
                String.format("%d.%d.%d.%d",
                        (ip & 0xff),
                        (ip >> 8 & 0xff),
                        (ip >> 16 & 0xff),
                        (ip >> 24 & 0xff));
        return ipStr;
    }

    public static JSONArray createJSONArray(Object[] objectArray) throws JSONException {
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject = null;
        Gson gson = new Gson();
        String jsonString = null;

        for (int i=0; i<objectArray.length; i++)
        {
            jsonString = gson.toJson(objectArray[i]);
            jsonObject = new JSONObject(jsonString);
            jsonArray.put(jsonObject);

        }
        return jsonArray;
    }

    public static AndroConAddress[] jsonToAddressArray(String json) throws JSONException
    {
        JSONArray data = new JSONArray(json);
        AndroConAddress[] a = new AndroConAddress[data.length()];
        String s = null;
        for (int i=0;i<data.length();i++) {
            a[i]= new AndroConAddress(data.getJSONObject(i).getString("locationName"));
            a[i].setAddressStreet(data.getJSONObject(i).getString("addressStreet"));
            a[i].setAddressNumber(data.getJSONObject(i).getString("addressNumber"));
            a[i].setPostalCode(data.getJSONObject(i).getInt("addressPostal"));
            s = data.getJSONObject(i).getString("addressCountry");
            if (!s.isEmpty()) a[i].setAddressCountry(s);
            s = data.getJSONObject(i).getString("addressCity");
            if (!s.isEmpty()) a[i].setAddressCity(s);
            s = data.getJSONObject(i).getString("addressArea");
            if (!s.isEmpty()) a[i].setAddressArea(s);
        }
        return a;
    }


    public static void createJSONFile(JSONArray data, String filename) throws IOException,JSONException {
        if (!checkExternalStorage()){
            return;
        }
        String text = data.toString();
        FileOutputStream fos = new FileOutputStream(filename);
        fos.write(text.getBytes());
        fos.close();
    }

    public static JSONArray readJSONFile(String filename) throws IOException, JSONException {
        FileInputStream fis = new FileInputStream(filename);
        BufferedInputStream bis = new BufferedInputStream(fis);
        StringBuffer b = new StringBuffer();
        while (bis.available() != 0) {
            char c = (char)bis.read();
            b.append(c);
        }
        bis.close();
        fis.close();

        JSONArray data = new JSONArray(b.toString());
        return data;

//        StringBuffer jsonBuffer = new StringBuffer();
//        for (int i=0;i<data.length();i++) {
//            String tour = data.getJSONObject(i).getString("tour");
//            jsonBuffer.append(tour+"\n");
//        }
    }

    public static boolean checkExternalStorage() {
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            return true;
        }
        return false;
    }


}
