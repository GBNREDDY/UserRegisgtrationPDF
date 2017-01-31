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
    Data(String name, String mobile, String email, Bitmap image){
        this.name=name;
        this.mobile=mobile;
        this.email=email;
        this.image=image;
    }
}
