package commonapistqc.com.aadhaarupdate.syntizen.com.userregisgtrationpdf;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

public class RegistrationActivity extends AppCompatActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener, RadioGroup.OnCheckedChangeListener, Spinner.OnItemSelectedListener {

    private final int PICK_IMG = 1;
    private EditText name, user, address, mobile, email;
    private Spinner city;
    private RadioGroup gender;
    private TextView datetxt;
    private Button datepick, capture, submit;
    private ImageView capturedimg;
    private DatePickerDialog datePickerDialog;
    private String mdate = "", mgender = "", mcity = "";
    private byte[] bitmapdata = null;
    private SQLiteDatabase sqLiteDatabase;
    private MyDatabase mdb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);


        city = (Spinner) findViewById(R.id.city);
        String[] citystr = {"Hyderabad", "Delhi", "Mumbai", "Banglore", "Chennai"};
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, citystr);
        city.setAdapter(arrayAdapter);
        city.setOnItemSelectedListener(this);

        gender = (RadioGroup) findViewById(R.id.gender);
        gender.setOnCheckedChangeListener(this);

        name = (EditText) findViewById(R.id.name);
        user = (EditText) findViewById(R.id.username);
        address = (EditText) findViewById(R.id.address);
        mobile = (EditText) findViewById(R.id.mobile);
        email = (EditText) findViewById(R.id.email);

        datetxt = (TextView) findViewById(R.id.datetxt);

        datepick = (Button) findViewById(R.id.datepick);
        capture = (Button) findViewById(R.id.capture);
        submit = (Button) findViewById(R.id.submit);

        datepick.setOnClickListener(this);
        capture.setOnClickListener(this);
        submit.setOnClickListener(this);

        capturedimg = (ImageView) findViewById(R.id.captureimg);

        Calendar calendar = Calendar.getInstance();
        datePickerDialog = new DatePickerDialog(this, this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.datepick:
                datePickerDialog.show();
                break;
            case R.id.capture:
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "select Picture"), PICK_IMG);
                break;
            case R.id.submit:
                if (mdate == "" || mcity == "" || mgender == "" || bitmapdata == null || TextUtils.isEmpty(name.getText()) || mobile.getText().toString().length() != 10 || TextUtils.isEmpty(user.getText()) || TextUtils.isEmpty(address.getText()) || TextUtils.isEmpty(mobile.getText()) || TextUtils.isEmpty(email.getText()) || !email.getText().toString().contains("@")) {
                    if (bitmapdata == null) {
                        Toast.makeText(this, "Please select an image", Toast.LENGTH_SHORT).show();
                    }
                    if (mcity == "") {
                        Toast.makeText(this, "Please select an city", Toast.LENGTH_SHORT).show();
                    }
                    if (mgender == "") {
                        Toast.makeText(this, "Please select an gender", Toast.LENGTH_SHORT).show();
                    }
                    if (mdate == "") {
                        Toast.makeText(this, "Please pick  date", Toast.LENGTH_SHORT).show();
                    }
                    if (TextUtils.isEmpty(name.getText())) {
                        name.setError("Name Should not be empty");
                    }
                    if (TextUtils.isEmpty(user.getText())) {
                        user.setError("Name Should not be empty");
                    }
                    if (TextUtils.isEmpty(address.getText())) {
                        address.setError("Name Should not be empty");
                    }
                    if (TextUtils.isEmpty(mobile.getText())) {
                        mobile.setError("Name Should not be empty");
                    }
                    if (TextUtils.isEmpty(email.getText())) {
                        email.setError("Name Should not be empty");
                    }
                    if (!email.getText().toString().contains("@")) {
                        email.setError("Email Should be a valid one");
                    }
                    if (mobile.getText().toString().length() != 10) {
                        mobile.setError("Mobile Number Should Be a Valid one");
                    }
                } else {

                    mdb = new MyDatabase(this, name.getText().toString(), user.getText().toString(), address.getText().toString(), mgender, mobile.getText().toString(), mdate, mcity, email.getText().toString(), bitmapdata);
                    sqLiteDatabase = mdb.getWritableDatabase();
                    try {
                        Long l = mdb.insertData(sqLiteDatabase);
                        Log.d("code", l.toString());
                        if (l == -1) {
                            Toast.makeText(this, "Data not Inserted", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(this, "Data sucessfully Inserted", Toast.LENGTH_SHORT).show();
                        }

                    } catch (SecurityException e) {
                        e.getMessage();
                    }finally {
                        //mdb.close();
                    }
                   // sqLiteDatabase = mdb.getWritableDatabase();
                    Cursor c = mdb.getData();
                   /* c.moveToFirst();
                    do{
                      Log.d("code",c.getString(5)+" ------ "+c.getBlob(8));
                        byte[] byteArray=c.getBlob(8);
                        Bitmap bm = BitmapFactory.decodeByteArray(byteArray, 0 ,byteArray.length);
                        capturedimg.setImageBitmap(bm);
                    }while (c.moveToNext());*/
                    ArrayList<Data> arraylist=getData(c);
                    ListActivity list=new ListActivity(arraylist);
                    Intent listActivity=new Intent(this,ListActivity.class);
                    startActivity(listActivity);

                    mdb.close();
                }
                break;
        }

    }

    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
        mdate = i2 + "/" + (i1 + 1) + "/" + i;
        datetxt.setText(mdate);
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        switch (radioGroup.getCheckedRadioButtonId()) {
            case R.id.male:
                mgender = "Male";
                break;
            case R.id.female:
                mgender = "Female";
                break;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        mcity = adapterView.getItemAtPosition(i).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        mcity = "NULL";
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMG && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {
                //Getting the Bitmap from Gallery
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                bitmapdata = bos.toByteArray();
                bitmap.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, bos);
                capturedimg.setImageBitmap(bitmap);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public ArrayList<Data> getData(Cursor c) {
        ArrayList<Data> al = new ArrayList<>();
        Data data = new Data();
        c.moveToFirst();
        do {
            Data.name = c.getString(0);
            Data.mobile = c.getString(4);
            Data.email=c.getString(7);
            al.add(data);
        } while (c.moveToNext());

        return al;
    }
}
