package ontology;

/**
 * Created by em9736 on 28/09/2016.
 */
public class AndroConLocationContext {
    private String userName;
    private String locationName;
    private String time;

    public AndroConLocationContext(String user, String location, String t)
    {
        if (user == null) this.userName = "";
        else this.userName = user;
        this.locationName = location;
        if (t == null) this.time = utils.OntologyHelper.getCurrentTimestamp();
        else this.time = t;
    }

    public String getUserName()
    {
        return this.userName;
    }
    public void setUserName(String user)
    {
        this.userName = user;
    }

    public String getLocationName()
    {
        return this.locationName;
    }
    public void setLocationName(String location)
    {
        this.locationName = location;
    }

    public String getTime()
    {
        return this.time;
    }
    public void setTime(String t)
    {
        this.time = t;
    }
}
