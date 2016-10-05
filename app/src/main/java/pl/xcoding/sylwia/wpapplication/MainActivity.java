package pl.xcoding.sylwia.wpapplication;

import android.content.Context;
import android.content.res.Configuration;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Surface;
import android.view.WindowManager;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    private ArrayList<RSSItem> data;
    private RecyclerView mRecyclerView;
    private AdapterListViewMain adapterListViewMain;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        setContentView(R.layout.activity_main);

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.activity_main);
        swipeRefreshLayout.setOnRefreshListener(this);

        if (swipeRefreshLayout.isRefreshing()) {

            swipeRefreshLayout.post(new Runnable() {
                @Override
                public void run() {
                    swipeRefreshLayout.setRefreshing(true);
                    myRefresh();
                }
            });
        }

        data = new ArrayList<RSSItem>();
        adapterListViewMain = new AdapterListViewMain(this, data);

        mRecyclerView = (RecyclerView) findViewById(R.id.list_view);
        mRecyclerView.setHasFixedSize(true);

        if (isTablet(this)) {

            if (getRotation(this) == 90 || getRotation(this) == 270) {
                RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
                mRecyclerView.setLayoutManager(mLayoutManager);
            } else {
                RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getApplicationContext(), 3);
                mRecyclerView.setLayoutManager(mLayoutManager);
            }
        } else {
            if (getRotation(this) == 0 || getRotation(this) == 180) {
                RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getApplicationContext(), 1);
                mRecyclerView.setLayoutManager(mLayoutManager);
            } else {
                RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
                mRecyclerView.setLayoutManager(mLayoutManager);

            }
        }

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


    public static boolean isTablet(Context context) {
        if ((context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE) {
            System.out.println("Tablet--------------------------------------------------------");
            return true;
        } else {
            System.out.println("Telefon---------------------------------------------------------------");
            return false;
        }
    }

    public int getRotation(Context context) {
        final int rotation = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getRotation();
        switch (rotation) {
            case Surface.ROTATION_0:
                return 0;
            case Surface.ROTATION_90:
                return 90;
            case Surface.ROTATION_180:
                return 180;
            default:
                return 270;
        }
    }


    @Override
    public void onRefresh() {
        myRefresh();
    }

    private void myRefresh() {
        swipeRefreshLayout.setRefreshing(true);
        data.clear();
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
        swipeRefreshLayout.setRefreshing(false);
    }

}