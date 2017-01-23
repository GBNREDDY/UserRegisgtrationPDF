package commonapistqc.com.aadhaarupdate.syntizen.com.userregisgtrationpdf;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by user on 20-Jan-17.
 */

public class MyDatabase extends SQLiteOpenHelper{

    private static final String DBName="mydb";
    private static final int version=1;
    private SQLiteDatabase sqLiteDatabase=null;

    private String name,user,address,gender,mobile,dob,city,email;
    private Context context;
    private byte[] img;

    public MyDatabase(Context context, String name, String user, String address, String gender, String mobile, String dob, String city, String email, byte[] img) {
        super(context, DBName, null, version);
        sqLiteDatabase =this.getWritableDatabase();
        this.name=name;
        this.user=user;
        this.address=address;
        this.gender=gender;
        this.mobile=mobile;
        this.dob=dob;
        this.city=city;
        this.email=email;
        this.context=context;
        this.img=img;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table registration (name varchar(50),username varchar2(20),address varchar2(100),gender varchar2(10),mobile varchar(20),dob varcahr2(15),city varcahr2(20),email varchar2(100),image BLOB) ");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public Long insertData(SQLiteDatabase sqLiteDatabase){
        ContentValues contentValues=new ContentValues();
        contentValues.put("name",name);
        contentValues.put("username",user);
        contentValues.put("address",address);
        contentValues.put("gender",gender);
        contentValues.put("mobile",mobile);
        contentValues.put("dob",dob);
        contentValues.put("city",city);
        contentValues.put("email",email);
        contentValues.put("image",img);
        return sqLiteDatabase.insertOrThrow("registration","image",contentValues);
    }

    public Cursor getData(){
        Cursor cursor= this.sqLiteDatabase.rawQuery("select * from registration;",null);
        return cursor;
    }

}
