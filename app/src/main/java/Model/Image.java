package Model;

public class Image
{
    private String mimageuri;

    public Image()
    {

    }

    public Image (String getimageuri)
    {
        mimageuri = getimageuri;
    }

    public String getMimageuri()
    {
        return mimageuri;
    }

    public void setMimageuri(String mimageuri) {
        this.mimageuri = mimageuri;
    }
}
