package commonapistqc.com.aadhaarupdate.syntizen.com.userregisgtrationpdf;

import android.graphics.Bitmap;

/**
 * Created by user on 20-Jan-17.
 */

public class Data {
    public  String name;
    public  String mobile;
    public  String email;
    public  Bitmap image;
    public  String user;
    public  String address;
    public  String dob;
    public  String gender;
    public  String city;
    Data(String name, String user,String address,String gender, String mobile, String dob,String city, String email, Bitmap image){
        this.name=name;
        this.mobile=mobile;
        this.email=email;
        this.image=image;
        this.address=address;
        this.user=user;
        this.city=city;
        this.dob=dob;
        this.gender=gender;
    }
}
