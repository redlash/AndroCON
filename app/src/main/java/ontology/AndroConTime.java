package ontology;

/**
 * Created by em9736 on 26/09/2016.
 */
public class AndroConTime extends AndroConThing {

    private int year;
    private int month;
    private int day;
    private int hour;
    private int minute;
    private int second;
    private String dayOfWeek;

    public AndroConTime(String name)
    {
        super(name);
    }

    public String toString()
    {
        return this.year+"-"+this.month+"-"+this.day+" "+this.hour+":"+this.minute;
    }
}
