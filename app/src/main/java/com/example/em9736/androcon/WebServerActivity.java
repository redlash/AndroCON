package com.example.em9736.androcon;

import android.net.wifi.WifiManager;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.nanohttpd.protocols.http.NanoHTTPD;
import org.nanohttpd.util.ServerRunner;

import java.io.IOException;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.ServerSocket;

public class WebServerActivity extends AppCompatActivity {

    private static final int PORT = 8765;
    private AndroConHTTPD server;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_server);

        this.server = new AndroConHTTPD(PORT, this);
        startHttpd(null);
    }

    public void startHttpd(View v) {
        TextView tv = (TextView) findViewById(R.id.ipaddrView);
        TextView tv2 = (TextView) findViewById(R.id.textView);

        WifiManager wifi = (WifiManager)getSystemService(WIFI_SERVICE);
        int ipAddress = wifi.getConnectionInfo().getIpAddress();
        String ip = utils.OntologyHelper.ipToString(ipAddress);

        try {
            this.server.start();
        }catch (IOException e){

        }
        //tv.setText("Server started. To stop the server, press STOP button.");
        //tv2.setText("Please access to http://localhost:"+PORT+"\n or http://"+ip+":"+PORT);
        Button b = (Button)findViewById(R.id.button);
        b.setText("STOP");
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopHttpd(v);
            }
        });
    }

    public void stopHttpd(View v) {
        TextView tv = (TextView) findViewById(R.id.ipaddrView);
        if (server != null) {
            server.stop();
        }
        tv.setText("Server stopped.");
        Button b = (Button)findViewById(R.id.button);
        b.setText("START");
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startHttpd(v);
            }
        });
    }


}
