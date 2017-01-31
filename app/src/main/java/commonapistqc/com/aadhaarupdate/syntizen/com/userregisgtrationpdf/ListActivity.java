package commonapistqc.com.aadhaarupdate.syntizen.com.userregisgtrationpdf;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ListActivity extends AppCompatActivity {
    ArrayList<Data> al = null;
    MyDatabase mdb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        FloatingActionButton fab=(FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(ListActivity.this,RegistrationActivity.class);
                startActivity(i);
            }
        });
        setSupportActionBar(toolbar);
       mdb = new MyDatabase(this);
        SQLiteDatabase sqLiteDatabase = mdb.getWritableDatabase();
        Cursor c=mdb.getData();
        if (c.getCount()!=0) {
            al = getData(c);

            ListView listView = (ListView) findViewById(R.id.list);
            MyAdapter adapter = new MyAdapter(this, al);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Data data = al.get(position);
                    Toast.makeText(ListActivity.this, data.name + " " + data.mobile + " " + data.email + " " + position, Toast.LENGTH_SHORT).show();
                }
            });
        }else{
            Toast.makeText(this, "Register first to view data", Toast.LENGTH_SHORT).show();
        }
    }

    public ArrayList<Data> getData(Cursor c) {
        ArrayList<Data> al = new ArrayList<>();
        c.moveToFirst();
        do {
            al.add(new Data(c.getString(0),c.getString(4),c.getString(7),getImage(c.getBlob(8))));
        } while (c.moveToNext());

        return al;
    }
    // convert from byte array to bitmap
    public static Bitmap getImage(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }
    class MyAdapter extends BaseAdapter {
        ArrayList<Data> listdata = null;
        Context context;

        public MyAdapter(Context context, ArrayList<Data> al) {
            this.listdata = al;
            this.context = context;

        }

        @Override
        public int getCount() {
            return listdata.size();
        }

        @Override
        public Object getItem(int position) {
            return listdata.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View row = convertView;
            if (row == null) {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
                row = inflater.inflate(R.layout.singleview, parent, false);
            }
            Data data = listdata.get(position);
            TextView tv = (TextView) row.findViewById(R.id.textView);
            TextView tv1 = (TextView) row.findViewById(R.id.textView2);
            TextView tv2 = (TextView) row.findViewById(R.id.textView3);
            ImageView dbcapturedimg = (ImageView) row.findViewById(R.id.dbcaptureimg);
            tv.setText(data.name);
            tv1.setText(data.mobile);
            tv2.setText(data.email);
            dbcapturedimg.setImageBitmap(data.image);
            return row;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mdb.close();
    }
}
