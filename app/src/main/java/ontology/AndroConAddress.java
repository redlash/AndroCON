package ontology;

/**
 * Created by em9736 on 26/09/2016.
 */
public class AndroConAddress extends AndroConSpace {

    private String area;
    private String city;
    private String country;
    private String street;
    private String number;
    private int postal;

    public AndroConAddress(String name)
    {
        super(name);
    }

    public AndroConAddress(String name, String country, String city, String area, String street, String number, int postal)
    {
        super(name);
        this.country = country;
        this.city = city;
        this.area = area;
        this.street = street;
        this.number = number;
        this.postal = postal;
    }

    public String getAddressStreet()
    {
        return this.street;
    }

    public void setAddressStreet(String s)
    {
        this.street = s;
    }

    public String getAddressNumber()
    {
        return this.number;
    }

    public void setAddressNumber(String s)
    {
        this.number = s;
    }

    public int getPostalCode()
    {
        return this.postal;
    }

    public void setPostalCode(int p)
    {
        this.postal = p;
    }

    public String getAddressCountry()
    {
        return this.country;
    }

    public void setAddressCountry(String s)
    {
        this.country = s;
    }

    public String getAddressCity()
    {
        return this.city;
    }

    public void setAddressCity(String s)
    {
        this.city = s;
    }

    public String getAddressArea()
    {
        return this.area;
    }

    public void setAddressArea(String s)
    {
        this.area = s;
    }

    public String toString()
    {
        return this.number+" "+this.street;
    }
}
