package Model;

import android.net.Uri;

public class customer {

    public String userid,shopname,emailid,phoneno,Permission,image;

    public customer(String s)
    {

    }

    public customer(String username, String fullname, String emailid, String phoneno, String Permission,String image) {
        this.userid = username;
        this.shopname = fullname;
        this.emailid = emailid;
        this.phoneno = phoneno;
        this.Permission = Permission;
        this.image = image;
    }

    public customer(String s, String fullname, String email, String phone, String userid) {
    }

    public customer(Uri uploadSessionUri) {
    }
}
