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

    private Context context;
    private byte[] img;

    public MyDatabase(Context context) {
        super(context, DBName, null, version);
        sqLiteDatabase =this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table registration (name TEXT,username TEXT,address TEXT,gender TEXT,mobile TEXT,dob TEXT,city TEXT,email TEXT,image BLOB); ");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public Long insertData(SQLiteDatabase sqLiteDatabase, String name, String user, String address, String gender, String mobile, String dob, String city, String email, byte[] img){
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
