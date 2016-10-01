package pl.xcoding.sylwia.wpapplication;


import android.os.AsyncTask;

import java.net.URL;
import java.util.ArrayList;

public class RSSDownloader extends AsyncTask<String, Void,ArrayList<RSSItem>> {
    @Override
    protected ArrayList<RSSItem> doInBackground(String... params) {
        try {
            URL url = new URL(params[0]);
            ArrayList<RSSItem> rssItems = RSSReader.startReader(url);
            return rssItems;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }
    }

