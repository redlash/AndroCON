package com.example.em9736.androcon;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.os.Environment;
import android.view.View;
import android.widget.TextView;

import com.hp.hpl.jena.query.Dataset;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.ResultSetFormatter;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.sparql.util.StrUtils;
import com.hp.hpl.jena.tdb.TDBFactory;
import com.hp.hpl.jena.update.GraphStore;
import com.hp.hpl.jena.update.GraphStoreFactory;
import com.hp.hpl.jena.update.UpdateFactory;
import com.hp.hpl.jena.update.UpdateProcessor;
import com.hp.hpl.jena.update.UpdateRequest;
import com.hp.hpl.jena.update.UpdateAction;
import com.hp.hpl.jena.util.FileManager;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import utils.UIHelper;

/**
 * Created by em9736 on 16/09/2016.
 */
public class AndroConDB {

    static public final String NL = System.getProperty("line.separator");
    static final String PREFIX = "PREFIX androcon: <http://localhost/toplevel/androcon.owl#>"+NL+
            "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"+NL+
            "PREFIX owl: <http://www.w3.org/2002/07/owl#>";
    static final String SCHEMAFILE = "AndroCon.owl";
    static final String DBDIR = "AndroCon";


    private Dataset dataSet = null;
    private Activity activity = null;
    private int textViewId = 0;
    private String tdbDir = DBDIR;

    public AndroConDB (Activity a, int id, String tdbdir)
    {
        this.activity = a;
        this.textViewId = id;
        this.tdbDir = tdbdir;
    }

    public String createDB(Activity activity, int textViewId)
    {
        String retString = "";
        if (this.activity != null) UIHelper.appendText(this.activity, this.textViewId, "");

        File rootDir = null;
        if (this.activity != null) rootDir = this.activity.getApplicationContext().getFilesDir();
        else rootDir = Environment.getExternalStorageDirectory();

        File storeDir = new File(rootDir.getAbsolutePath()+"/"+this.tdbDir);
        if (!storeDir.exists()) {
            storeDir.mkdir();
        }
        this.dataSet = TDBFactory.createDataset(storeDir.getAbsolutePath());

//                Model b = dataSet.getDefaultModel();
//        AssetManager am = activity.getApplicationContext().getAssets();
//
//        InputStream in;
//        try
//        {
//            in = am.open(SCHEMAFILE);
//
//        } catch (IOException e) {
//            //if (in == null){
//            throw new IllegalArgumentException("File: "+SCHEMAFILE+" not found.");
//            //}
//        }
//        b.read(in,"");
//        FileManager.get().readModel( b, SCHEMAFILE);


        if (this.activity != null)
        {
            UIHelper.appendText(this.activity, this.textViewId, "TDB initializing finished.");
            UIHelper.appendText(this.activity, this.textViewId, "TDB schema:");
        }
        retString += "TDB initializing finished.\nTDB schema:\n";

        //display the schema:
        String sparqlQueryString;
        sparqlQueryString = PREFIX + NL + "SELECT ?s ?o { ?s ?p ?o }";

        Query query = QueryFactory.create(sparqlQueryString);
        QueryExecution qexec = QueryExecutionFactory.create(query, dataSet);
        ResultSet results = qexec.execSelect();
        QuerySolution qs;
        String ss = "";
        while (results.hasNext()) {
            qs = results.nextSolution();
            ss = qs.toString();
            if (activity != null) UIHelper.appendText(activity, textViewId, ss);
            retString += ss+"\n";
        }
        qexec.close();
        return retString;
    }

    public void addNamedIndividual(String individual, String parent)
    {
        //<http://localhost/toplevel/androcon.owl#WT> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://www.w3.org/2002/07/owl#NamedIndividual> .
        //<http://localhost/toplevel/androcon.owl#WT> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://localhost/toplevel/androcon.owl#OfficeBuilding> .
        updateContext(individual, "rdf:type", "owl:NamedIndividual");
        updateContext(individual, "rdf:type", parent);
    }

//    public void insertLocation(String user, String location, String country, String city, int postal)
//    {
//        //insert an individual of location first: name the location, assign address to the location.
//        addNamedIndividual("androcon:"+location, "androcon:Address");
//        updateContext("androcon:"+location, "androcon:addressCity", "\""+city+"\"");
//        updateContext("androcon:"+location, "androcon:addressCountry", "\""+country+"\"");
//        updateContext("androcon:"+location, "androcon:addressPostalCode", "\""+postal+"\"");
//
//        //insert user hasLocatoin Location.
//        //updateContext("androcon:"+user, "androcon:hasLocation", "androcon:"+location);
//    }

    public void insertLocation(String location, String country, String city, String area, String street, String number, int postal)
    {
        //insert an individual of location first: name the location, assign address to the location.
        String l;
        if (location == null) l = number+street+utils.OntologyHelper.getCurrentTimestamp();
        else l = location;
        addNamedIndividual("androcon:"+l, "androcon:Address");
        if (country != null) updateContext("androcon:"+location, "androcon:addressCountry", "\""+country+"\"");
        if (country != null) updateContext("androcon:"+location, "androcon:addressCountry", "\""+country+"\"");
        if (city != null) updateContext("androcon:"+l, "androcon:addressCity", "\""+city+"\"");
        if (area != null) updateContext("androcon:"+l, "androcon:addressArea", "\""+area+"\"");
        updateContext("androcon:"+l, "androcon:addressStreet", "\""+street+"\"");
        updateContext("androcon:"+l, "androcon:addressNumber", "\""+number+"\"");
        if (postal != 0) updateContext("androcon:"+location, "androcon:addressPostalCode", "\""+postal+"\"");

        //insert user hasLocatoin Location.
        //updateContext("androcon:"+user, "androcon:hasLocation", "androcon:"+location);
    }

    public void insertPosture(String user, String posture)
    {
        //insert an individual of activity first: name the Activity with timestamp.
        addNamedIndividual("androcon:"+posture, "androcon:Activity");

        //insert user hasPosture activity
        updateContext("androcon:"+user, "androcon:hasPosture", "androcon:"+posture);
    }

    public void insertUser(String user, String firstname, String lastname)
    {
        //Create named individual;
        addNamedIndividual("androcon:"+user, "androcon:User");
        //Add data property;
        //<http://localhost/toplevel/androcon.owl#User1> <http://xmlns.com/foaf/0.1/family_name> "Wang" .
        //<http://localhost/toplevel/androcon.owl#User1> <http://xmlns.com/foaf/0.1/firstName> "Chris" .
        updateContext("androcon:"+user, "foaf:family_name", "\""+lastname+"\"");
        updateContext("androcon:"+user, "foaf:firstName", "\""+firstname+"\"");

    }

    public void updateContext(String subject, String predicate, String object)
    {

        if (this.activity != null)
        {
            //UIHelper.displayText(this.activity, this.textViewId,"Inserting context triple...");
            //UIHelper.appendText(this.activity, this.textViewId,"Inserting context triple...");
        }

        if (this.dataSet == null)
        {
            //if (this.activity != null) UIHelper.appendText(this.activity, this.textViewId, "No available dataset!");
            return;
        }

        String insertStatement = String.format("INSERT { %s %s %s } where {?s ?p ?o}", subject,
                    predicate, object);

        String sparqlUpdateString = StrUtils.strjoinNL(
                PREFIX,
                //"INSERT { androcon:User1 androcon:hasLocation androcon:WT } where {?s ?p ?o}"
                insertStatement
        );

        //if (this.activity != null) UIHelper.appendText(this.activity, this.textViewId, sparqlUpdateString);

        try
        {

            UpdateRequest request = UpdateFactory.create(sparqlUpdateString) ;
            UpdateAction.execute(request, dataSet);

        } finally
        {
            String queryStatement =  String.format("SELECt ?s ?p ?o WHERE {%s %s ?o}", subject, predicate);
            String sparqlQueryString;
            sparqlQueryString = PREFIX + NL + queryStatement;
            //if (this.activity != null) UIHelper.appendText(this.activity, this.textViewId, sparqlQueryString);

            Query query = QueryFactory.create(sparqlQueryString);
            QueryExecution qexec = QueryExecutionFactory.create(query, dataSet);
            ResultSet results = qexec.execSelect();
            QuerySolution qs;
            String ss = "";
            while (results.hasNext()) {
                qs = results.nextSolution();
                ss = qs.toString();
                //if (this.activity != null) UIHelper.appendText(this.activity, this.textViewId, ss);
            }
            qexec.close();
        }
    }

    public void deleteContext(String subject, String predicate, String object)
    {

        if (this.activity != null)
        {
            UIHelper.appendText(this.activity, this.textViewId,"Deleting context triple...");
        }

        if (this.dataSet == null)
        {
            if (this.activity != null) UIHelper.appendText(this.activity, this.textViewId, "No available dataset!");
            return;
        }

        String deleteStatement = "DELETE WHERE {";
        if (subject == null) deleteStatement += "?s";
        else deleteStatement += subject;
        if (predicate == null) deleteStatement += " ?p";
        else deleteStatement += " "+predicate;
        if (object == null) deleteStatement += " ?o}";
        else deleteStatement += " "+object+"}";

        String sparqlUpdateString = StrUtils.strjoinNL(
                PREFIX,
                deleteStatement
        );

        if (this.activity != null) UIHelper.appendText(this.activity, this.textViewId, sparqlUpdateString);
        //String deleteString = "DELETE DATA"+
        //"{"+
        //    "<http://example/book2> dc:title \"David Copperfield\" ;"+
        //"dc:creator \"Edmund Wells\" ."+
        //"}";
        //PREFIX foaf:  <http://xmlns.com/foaf/0.1/>
        // DELETE WHERE { ?person foaf:givenName 'Fred';
        // ?property      ?value }

        try
        {

            UpdateRequest request = UpdateFactory.create(sparqlUpdateString) ;
            UpdateAction.execute(request, dataSet);

        } finally
        {
            String queryStatement =  String.format("SELECt ?s ?p ?o WHERE {%s %s ?o}", subject, predicate);
            String sparqlQueryString;
            sparqlQueryString = PREFIX + NL + queryStatement;
            if (this.activity != null) UIHelper.appendText(this.activity, this.textViewId, sparqlQueryString);

            Query query = QueryFactory.create(sparqlQueryString);
            QueryExecution qexec = QueryExecutionFactory.create(query, dataSet);
            ResultSet results = qexec.execSelect();
            QuerySolution qs;
            String ss = "";
            while (results.hasNext()) {
                qs = results.nextSolution();
                ss = qs.toString();
                if (this.activity != null) UIHelper.appendText(this.activity, this.textViewId, ss);
            }
            qexec.close();
        }

    }



    //Return: SPARQL query string.
    //Return: Json string.
    public String queryContext(String subject, String predicate, String object)
    {
        if (dataSet == null)
        {
            //if (this.activity != null) UIHelper.appendText(this.activity, this.textViewId, "Query failed, no available dataset.");
            return null;
        }
        String sparqlQueryString;
        sparqlQueryString = PREFIX + NL + "SELECT ?s ?p ?o WHERE { ";
        if (subject == null) sparqlQueryString += "?s";
        else sparqlQueryString += subject;
        if (predicate == null) sparqlQueryString += " ?p";
        else sparqlQueryString += " "+predicate;
        if (object == null) sparqlQueryString += " ?o }";
        else sparqlQueryString += " "+object+" }";

        //if (this.activity != null) UIHelper.appendText(this.activity, this.textViewId, sparqlQueryString);

        Query query = QueryFactory.create(sparqlQueryString);
        QueryExecution qexec = QueryExecutionFactory.create(query, dataSet);
        ResultSet results = qexec.execSelect();

        QuerySolution qs;
        String ss = "";
        while (results.hasNext()) {
            qs = results.nextSolution();
            ss += qs.toString();
            //if (this.activity != null) UIHelper.appendText(this.activity, this.textViewId, ss);
        }
        //ResultSetFormatter.out(results);
        //ResultSetFormatter.outputAsJSON(results);
        qexec.close();

        //Transfer the query result to json:
//        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//        ResultSetFormatter.outputAsJSON(outputStream, results);
//        String json = new String(outputStream.toByteArray());
        //if (this.activity != null) UIHelper.appendText(this.activity, this.textViewId, json);
        return ss;
    }


    public void loadDB()
    {
        UIHelper.displayText(null,0,"placeholder");
    }

    public void openDB()
    {

    }

    public void closeDB()
    {
        if (dataSet != null) {
            dataSet.close();
        }
    }
}
