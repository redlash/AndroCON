import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.example.em9736.androcon.R;
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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import utils.OntologyHelper;

/**
 * Created by em9736 on 21/09/2016.
 */

public class TestTDBActivity extends AppCompatActivity {

    static public final String NL = System.getProperty("line.separator");
    static final String PREFIX = "PREFIX androcon: <http://localhost/toplevel/androcon.owl#>"+NL+
            "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>";
    static final String SCHEMAFILE = "AndroCon.owl";
    static final String DBDIR = "AndroCon";

    static final String inputFileName = "university.owl";
    //static final String PREFIX = "http://www.semanticweb.org/em9736/ontologies/2016/8/university.owl#";
    //static final String PREFIX = "http://www.semanticweb.org/em9736/ontologies/2016/8/university.owl#";
    //static final String PREFIX = "http://localhost/toplevel/androcon.owl#";

    private Dataset dataSet;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.dataSet = null;
        TextView tv = (TextView) findViewById(R.id.textView);
        tv.setText(OntologyHelper.getCurrentTimestamp());
    }

    public void loadRDF(View v) {
        TextView tv = (TextView) findViewById(R.id.textView);
        tv.setText("");
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
            tv.append(lecturer.toString()+"\n");
            RDFNode module = ss.getObject();
            tv.append(module.toString()+"\n");
        }
        //model.write(System.out);
    }

    public void createDB(View v)
    {
        TextView tv = (TextView) findViewById(R.id.textView);
        tv.setText("");

        File internalDir = getApplicationContext().getFilesDir();
        File storeDir = new File(internalDir.getAbsolutePath()+"/"+DBDIR);
        if (!storeDir.exists()) {
            storeDir.mkdir();
        }
        this.dataSet = TDBFactory.createDataset(storeDir.getAbsolutePath());

//        Model b = dataSet.getDefaultModel();
//        AssetManager am = getApplicationContext().getAssets();
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
        //FileManager.get().readModel( b, SCHEMAFILE);

        String sparqlQueryString;
        //if (contextType == null) {
        sparqlQueryString = PREFIX + NL + "SELECT ?s ?o { ?s ?p ?o }";
        /*    return sparqlQueryString;
        }
        if (contextType.equals("Location")) {
            sparqlQueryString = PREFIX + NL + "SELECT ?s ?o { ?s university:hasLocation ?o }";
        }else if (contextType.equals("Activity")){
            sparqlQueryString = PREFIX + NL + "SELECT ?s ?o { ?s university:activity ?o }";
        }else {
            sparqlQueryString = PREFIX + NL + "SELECT ?s ?o { ?s ?p ?o }";
        }*/

        Query query = QueryFactory.create(sparqlQueryString);
        QueryExecution qexec = QueryExecutionFactory.create(query, dataSet);
        ResultSet results = qexec.execSelect();
        QuerySolution qs;
        String ss = "";
        while (results.hasNext()) {
            qs = results.nextSolution();
            ss = qs.toString();
            tv.append(ss + "\n");
        }
        qexec.close();
    }

    //Should insert timestamp automatically.
    public void updateContext(View v)
    {
        final String INSERT = "INSERT";

        TextView tv = (TextView) findViewById(R.id.textView);
        tv.setText("");

        if (this.dataSet == null)
        {
            tv.setText("No available dataset!");
            return;
        }

        //dataSet.begin(ReadWrite.WRITE) ;
        try
        {
            //GraphStore graphStore = GraphStoreFactory.create(dataSet) ;
            // Do a SPARQL Update.
            String s="androcon:User1", p="androcon:hasLocation", o="androcon:WT";
            String insertStatement = String.format("INSERT { %s %s %s } where {?s ?p ?o}", s, p, o);
            String sparqlUpdateString = StrUtils.strjoinNL(
                    PREFIX,
                    "INSERT { androcon:User1 androcon:hasLocation androcon:WT } where {?s ?p ?o}"
            );

            UpdateRequest request = UpdateFactory.create(sparqlUpdateString) ;
            //UpdateProcessor proc = UpdateExecutionFactory.create(request, graphStore);
            UpdateAction.execute(request, dataSet);
            //proc.execute();
            //dataSet.commit() ;

        } finally
        {
            //dataSet.end() ;
            String sparqlQueryString;
            //if (contextType == null) {
            sparqlQueryString = PREFIX + NL + "SELECT ?s ?o " +
                    "WHERE { ?s androcon:hasLocation ?o }";
            Query query = QueryFactory.create(sparqlQueryString);
            QueryExecution qexec = QueryExecutionFactory.create(query, dataSet);
            ResultSet results = qexec.execSelect();
            QuerySolution qs;
            String ss = "";
            while (results.hasNext()) {
                qs = results.nextSolution();
                ss = qs.toString();
                tv.append(ss + "\n");
            }
            qexec.close();
        }
    }
}
