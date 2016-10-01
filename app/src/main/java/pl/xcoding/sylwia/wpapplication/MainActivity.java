package pl.xcoding.sylwia.wpapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ArrayList<RSSItem> data;
    private RecyclerView mRecyclerView;
    private AdapterListViewMain adapterListViewMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        data = new ArrayList<RSSItem>();


        adapterListViewMain = new AdapterListViewMain(this, data);

        mRecyclerView = (RecyclerView) findViewById(R.id.list_view);
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mRecyclerView.setAdapter(adapterListViewMain);

        new RSSDownloader() {


            @Override
            protected void onPostExecute(ArrayList<RSSItem> result) {

                data.addAll(result);
                adapterListViewMain.notifyDataSetChanged();
                Toast.makeText(MainActivity.this,
                        "Pobrano " + result.size() + " wiadomości",
                        Toast.LENGTH_SHORT).show();

            }

        }.execute("http://wiadomosci.wp.pl/kat,1329,ver,rss,rss.xml?ticaid=117d3d&_ticrsn=3");

    }
}