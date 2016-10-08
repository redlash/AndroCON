package com.example.em9736.androcon;

import android.content.res.AssetManager;
import android.renderscript.ScriptGroup;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.hp.hpl.jena.query.Dataset;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.ResourceFactory;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.sparql.util.StrUtils;
import com.hp.hpl.jena.tdb.TDBFactory;
import com.hp.hpl.jena.update.UpdateAction;
import com.hp.hpl.jena.update.UpdateFactory;
import com.hp.hpl.jena.update.UpdateRequest;
import com.hp.hpl.jena.util.FileManager;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import utils.OntologyHelper;
import utils.UIHelper;

public class MainActivity extends AppCompatActivity {

    static public final String NL = System.getProperty("line.separator");
    static final String PREFIX = "PREFIX androcon: <http://localhost/toplevel/androcon.owl#>"+NL+
            "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>";
    static final String SCHEMAFILE = "AndroCon.owl";
    static final String DBDIR = "AndroCon";

    static final String inputFileName = "university.owl";
    //static final String PREFIX = "http://www.semanticweb.org/em9736/ontologies/2016/8/university.owl#";
    //static final String PREFIX = "http://www.semanticweb.org/em9736/ontologies/2016/8/university.owl#";
    //static final String PREFIX = "http://localhost/toplevel/androcon.owl#";

    private AndroConDB dataBase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.dataBase = new AndroConDB(this, R.id.textView, DBDIR);
        UIHelper.displayText(this, R.id.textView, OntologyHelper.getCurrentTimestamp());
    }

    public void loadRDF(View v) {
        UIHelper.displayText(this, R.id.textView, "");
        AssetManager am = getApplicationContext().getAssets();

        Model model = ModelFactory.createDefaultModel();
        //InputStream in = FileManager.get().open(inputFileName);
        InputStream in;
        try
        {
            in = am.open(inputFileName);

        } catch (IOException e) {
            //if (in == null){
                throw new IllegalArgumentException("File: "+inputFileName+" not found.");
            //}
        }
        model.read(in,"");

        //Property p = ResourceFactory.createProperty(PREFIX+"teaches");
        Property p = ResourceFactory.createProperty("http://www.w3.org/1999/02/22-rdf-syntax-ns#type");

		/*Resource s = ResourceFactory.createResource(PREFIX+"Lecture3");
		Property p = ResourceFactory.createProperty(PREFIX+"teaches");
		Resource o = ResourceFactory.createResource(PREFIX+"CS101");

		Statement st = ResourceFactory.createStatement(s, p, o);
		model.add(st);*/

        StmtIterator i = model.listStatements(null, p, (RDFNode) null);
        while (i.hasNext()){
            Statement ss = i.next();
            Resource lecturer = ss.getSubject();
            UIHelper.appendText(this,R.id.textView, lecturer.toString());
            RDFNode module = ss.getObject();
            UIHelper.appendText(this,R.id.textView, module.toString());
        }
        //model.write(System.out);
    }

    public void createDB(View v)
    {
        UIHelper.displayText(this, R.id.textView, "");
        if (this.dataBase != null) this.dataBase.createDB(this, R.id.textView);
    }

    //Should insert timestamp automatically.
    public void updateContext(View v)
    {
        if (this.dataBase == null) return;
        UIHelper.displayText(this, R.id.textView, "");
        //this.dataBase.insertLocation("User1","109SymondStreet", "NZ", "Auckland", 1010);
        //this.dataBase.updateContext("androcon:109SymondStreet","androcon:addressCity", "\"Auckland\"");
        //this.dataBase.updateContext("androcon:109SymondStreet","androcon:addressPostalCode", "\"1010\"");
    }

    public void queryContext(View v)
    {
        if (this.dataBase == null) return;
        UIHelper.displayText(this, R.id.textView, "");
        this.dataBase.queryContext(null, "androcon:hasLocation", null);
    }

    public void deleteContext(View v)
    {
        if (this.dataBase == null) return;
        UIHelper.displayText(this, R.id.textView, "");
        this.dataBase.deleteContext("androcon:User1", "androcon:hasLocation", null);
    }
}
