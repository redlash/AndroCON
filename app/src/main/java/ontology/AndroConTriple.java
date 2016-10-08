package ontology;

/**
 * Created by em9736 on 26/09/2016.
 */
public class AndroConTriple {
    private Object subject;
    private String predicate;
    private Object object;

    public AndroConTriple(Object s, String p, Object o)
    {
        this.subject = s;
        this.predicate = p;
        this.object = o;
    }

    public String toString()
    {
        return this.subject.toString()+" "+this.predicate+" "+this.object.toString();
    }
}
