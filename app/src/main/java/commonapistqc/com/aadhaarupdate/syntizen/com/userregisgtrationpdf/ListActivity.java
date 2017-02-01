package commonapistqc.com.aadhaarupdate.syntizen.com.userregisgtrationpdf;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
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

import com.itextpdf.text.Chapter;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Header;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class ListActivity extends AppCompatActivity {
    ArrayList<Data> al = null;
    MyDatabase mdb;
    File myFile;

    // convert from byte array to bitmap
    public static Bitmap getImage(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Dir";

        File dir = new File(path);
        if (!dir.exists())
            dir.mkdirs();

        myFile = new File(dir, "tmp.pdf");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ListActivity.this, RegistrationActivity.class);
                startActivity(i);
            }
        });
        setSupportActionBar(toolbar);
        mdb = new MyDatabase(this);
        SQLiteDatabase sqLiteDatabase = mdb.getWritableDatabase();
        Cursor c = mdb.getData();
        if (c.getCount() != 0) {
            al = getData(c);

            ListView listView = (ListView) findViewById(R.id.list);
            MyAdapter adapter = new MyAdapter(this, al);
            adapter.notifyDataSetChanged();
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Data data = al.get(position);
                    Toast.makeText(ListActivity.this, data.name + " " + data.mobile + " " + data.email + " " + position, Toast.LENGTH_SHORT).show();
                    pdfCreate(data);
                    pdfOpen();
                }
            });
        } else {
            Toast.makeText(this, "Register first to view data", Toast.LENGTH_SHORT).show();
        }
    }

    public void pdfCreate(Data data) {

        try {
            FileOutputStream output = new FileOutputStream(myFile);
            //Step 1
            Document document = new Document();
            //Step 2
            PdfWriter.getInstance(document, output);

            //Step 3
            document.open();
            Font chapterFont = FontFactory.getFont(FontFactory.HELVETICA, 20, Font.BOLDITALIC);
            chapterFont.setStyle(Font.UNDERLINE);
            Font paragraphFont = FontFactory.getFont(FontFactory.HELVETICA, 16, Font.NORMAL);
            Chunk chunk = new Chunk("User Details", chapterFont);
            Chapter chapter = new Chapter(new Paragraph(chunk), 1);
            chapter.setNumberDepth(0);
            //Step 4 Add content
            chapter.add(new Paragraph("Name    : "+data.name, paragraphFont));
            chapter.add(new Paragraph("User    : "+data.user, paragraphFont));
            chapter.add(new Paragraph("Address : "+data.address, paragraphFont));
            chapter.add(new Paragraph("Gender  : "+data.gender, paragraphFont));
            chapter.add(new Paragraph("Mobile  : "+data.mobile, paragraphFont));
            chapter.add(new Paragraph("DOB     : "+data.dob, paragraphFont));
            chapter.add(new Paragraph("City    : "+data.city, paragraphFont));
            chapter.add(new Paragraph("Email   : "+data.email, paragraphFont));
            Bitmap bmp = data.image;
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.PNG, 0, baos);
            byte[] b = baos.toByteArray();
            Image image = Image.getInstance(b);
            image.scaleToFit(150, 150);
            //image.setAbsolutePosition(0, 0);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                chapter.add(image);
            }
            document.add(chapter);
            //Step 5: Close the document
            document.close();
            output.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void pdfOpen() {

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(myFile), "application/pdf");
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    public ArrayList<Data> getData(Cursor c) {
        ArrayList<Data> al = new ArrayList<>();
        c.moveToFirst();
        do {
            al.add(new Data(c.getString(0), c.getString(1), c.getString(2), c.getString(3), c.getString(4), c.getString(5), c.getString(6), c.getString(7), getImage(c.getBlob(8))));
        } while (c.moveToNext());

        return al;
    }

    @Override
    protected void onPause() {
        super.onPause();
        mdb.close();
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
}
