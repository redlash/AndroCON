package ontology;

/**
 * Created by em9736 on 28/09/2016.
 */
public class AndroConActivityContext {
    private String userName;
    private String activityName;
    private String time;

    public AndroConActivityContext(String user, String activity, String t)
    {
        if (user == null) this.userName = "";
        else this.userName = user;
        this.activityName = activity;
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

    public String getActivityName()
    {
        return this.activityName;
    }
    public void setActivityName(String activity)
    {
        this.activityName = activity;
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
