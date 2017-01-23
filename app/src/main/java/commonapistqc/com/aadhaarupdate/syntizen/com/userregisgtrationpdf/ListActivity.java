package commonapistqc.com.aadhaarupdate.syntizen.com.userregisgtrationpdf;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class ListActivity extends AppCompatActivity {
    ArrayList<Data> al=null;

    public ListActivity(ArrayList<Data> arraylist) {
        this.al=arraylist;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ListView listView=(ListView) findViewById(R.id.list);
    }


class MyAdapter extends ArrayAdapter<String>{

    public MyAdapter(Context context, int resource) {
        super(context, resource);
    }
}
}
