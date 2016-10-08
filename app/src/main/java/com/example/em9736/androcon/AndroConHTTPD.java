package com.example.em9736.androcon;

import android.app.Activity;
import android.os.Handler;

import org.nanohttpd.protocols.http.IHTTPSession;
import org.nanohttpd.protocols.http.NanoHTTPD;
import org.nanohttpd.protocols.http.request.Method;
import org.nanohttpd.protocols.http.response.Response;
import org.nanohttpd.protocols.http.response.Status;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringBufferInputStream;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;
import org.json.*;

/**
 * Created by em9736 on 10/09/2016.
 */
public class AndroConHTTPD extends NanoHTTPD {

    private Handler handler = new Handler();
    private AndroConDB dataBase;
    private Activity activity = null;
    private String debugString = "";

    public AndroConHTTPD(int port, Activity a)
    {
        super(port);
        this.activity = a;
        //create DB instance.
        this.dataBase = new AndroConDB(this.activity, R.id.textView, AndroConDB.DBDIR);
        this.dataBase.createDB(this.activity, R.id.textView);
    }

    @Override
    public Response serve(IHTTPSession session) {
        Method method = session.getMethod();
        String uri = session.getUri();
        //HelloServer.LOG.info(method + " '" + uri + "' ");
        String[] resLayers = uri.split("/");
        String msg = "<html><body><h1>AndroCon Context Management Service</h1>\n";

        if (Method.PUT.equals(method) || Method.POST.equals(method)) {
            Map<String, String> files = new HashMap<String, String>();
            try {
                session.parseBody(files);
                files = session.getParms();
                msg = files.toString();
                if (files.size() == 7)//Insert location;
                {
                    String location = files.get("locationName");
                    String country = files.get("addressCountry");
                    String city = files.get("addressCity");
                    String street = files.get("addressStreet");
                    String number = files.get("addressNumber");
                    String area = files.get("addressArea");
                    String postal = files.get("addressPostalCode");
                    msg += "\n"+location+": "+country+" "+city+" "+area+" "+street+" "+number+" ";
                    //Need to create the individual of location first with locationName;
                    if (!location.equals(""))
                    {
                        this.dataBase.addNamedIndividual("androcon:"+location, "androcon:Address");
                    }
                    if (!city.equals(""))
                    {
                        this.dataBase.updateContext("androcon:"+location,"androcon:addressCity", "\""+city+"\"");
                    }
                    if (!country.equals(""))
                    {
                        this.dataBase.updateContext("androcon:"+location,"androcon:addressCountry", "\""+country+"\"");
                    }
                    if (!street.equals(""))
                    {
                        this.dataBase.updateContext("androcon:"+location,"androcon:addressStreet", "\""+street+"\"");
                    }
                    if (!number.equals(""))
                    {
                        this.dataBase.updateContext("androcon:"+location,"androcon:addressNumber", "\""+number+"\"");
                    }
                    if (!postal.equals(""))
                    {
                        this.dataBase.updateContext("androcon:"+location,"androcon:addressPostalCode", "\""+postal+"\"");
                    }
                    if (!area.equals(""))
                    {
                        this.dataBase.updateContext("androcon:"+location,"androcon:addressArea", "\""+area+"\"");
                    }
                    msg += "\nInsert location finished.";
                }else if (files.size() == 3)//Insert location context;
                {
                    String location = files.get("locationName");
                    String user = files.get("userName");
                    String time = files.get("time");

                    if (!user.equals(""))
                    {
                        this.dataBase.updateContext("androcon:"+user,"androcon:hasLocation", "androcon:"+location);
                    }
                    if (!time.equals(""))
                    {
                        //this.dataBase.updateContext("androcon:"+location,"androcon:addressArea", "\""+time+"\"");
                    }
                    msg += "\nInsert location context finished.";
                }

            } catch (IOException ioe) {
                return Response.newFixedLengthResponse(Status.INTERNAL_ERROR, NanoHTTPD.MIME_PLAINTEXT, "SERVER INTERNAL ERROR: IOException: " + ioe.getMessage());
            } catch (ResponseException re) {
                return Response.newFixedLengthResponse(re.getStatus(), NanoHTTPD.MIME_PLAINTEXT, re.getMessage());
            } finally
            {
                return Response.newFixedLengthResponse(Status.ACCEPTED, NanoHTTPD.MIME_PLAINTEXT, msg);
            }
        }else //GET
//        <form action="blah.php" method="post">
//        <input type="text" name="data" value="mydata" />
//        <input type="submit" />
//        </form>

        {
            msg += "<p>\n";
            if (resLayers.length > 1)//Get resources;
            {
                if (resLayers[1].equals("location"))
                {
                    String queryResult = createLocationList();
                    queryResult = queryResult.replaceAll("<","&lt");
                    queryResult = queryResult.replaceAll(">","&gt");
                    msg += queryResult;
                }else if (resLayers[1].equals("haslocation"))
                {
                    String queryResult = createLocationContextList();
                    queryResult = queryResult.replaceAll("<","&lt");
                    queryResult = queryResult.replaceAll(">","&gt");
                    msg += queryResult;
                }else if (resLayers[1].equals("all"))
                {
                    String queryResult = createAllList();
                    queryResult = queryResult.replaceAll("<","&lt");
                    queryResult = queryResult.replaceAll(">","&gt");
                    msg += queryResult;
                }else if (resLayers[1].equals("insertlocation"))//Get address form;
                {
                    msg += createAddressForm();
                }else if (resLayers[1].equals("insertlocationcontext"))//Get location context form;
                {
                    msg += createLocationContextForm();
                }
            }else//Get index page - RESTful documentation;
            {
                msg += createDocumentation();
            }
            msg += "</p>\n";

            msg += "</body></html>\n";


            return Response.newFixedLengthResponse(msg);
        }
    }

    private String createLocationList()
    {
        return this.dataBase.queryContext(null, "rdf:type", "androcon:Address");
    }

    private String createLocationContextList()
    {
        return this.dataBase.queryContext(null, "androcon:hasLocation", null);
    }

    private String createAllList()
    {
        return this.dataBase.queryContext(null, null, null);
    }

    private String createAddressForm()
    {
        String msg = "<form action='#' method='post'>\n" +
                "  Location name: <input type='text' name='locationName'><br/>\n" +
                "  Postal code: <input type='text' name='addressPostalCode'><br/>" +
                "  Street number: <input type='text' name='addressNumber'><br/>\n" +
                "  Street name: <input type='text' name='addressStreet'><br/>\n" +
                "  Area: <input type='text' name='addressArea'><br/>\n" +
                "  City: <input type='text' name='addressCity'></p>\n" +
                "  Country: <input type='text' name='addressCountry'><br/>\n" +
                "  <input type=\"submit\" value=\"Submit\"><br/>\n" +
                "</form>\n";
        return msg;
    }

    private String createLocationContextForm()
    {
        String msg = "<form action='#' method='post'>\n" +
                "  User name: <input type='text' name='userName'><br/>\n" +
                "  Location name: <input type='text' name='locationName'><br/>\n" +
                "  Time: <input type='text' name='time'><br/>\n" +
                "  <input type=\"submit\" value=\"Submit\"><br/>\n" +
                "</form>\n";
        return msg;
    }

    private String createDocumentation()
    {
        return "RESTful documentation:";
    }

    public String getContext()
    {
        //if action = /location:
        this.dataBase.queryContext(null, "androcon:addressStreet", null);
        this.dataBase.queryContext(null, "androcon:addressNumber", null);
        //if action = /haslocation:
        return this.dataBase.queryContext(null, "androcon:hasLocation", null);

        //parse the sparql result to json:
    }

    public void postContext()
    {
        //if action = /location: get location properties from posted json;
        this.dataBase.insertLocation("AUT SOUTH", null, null, "Manukau", "Great South Rd", "640", 2025);
        //if action = /haslocation: get location context from posted json;
        this.dataBase.updateContext("androcon:User1","androcon:hasLocation", "androcon:109SymondStreet");

    }

    public String restToSparql(String[] url)
    {
        return "";
    }

    public Object[] sparqlResultToObject(String sparqlResult)
    {
        return null;
    }

}
