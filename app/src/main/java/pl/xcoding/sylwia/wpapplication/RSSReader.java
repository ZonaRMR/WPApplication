package pl.xcoding.sylwia.wpapplication;


import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class RSSReader {

    public static ArrayList<RSSItem> startReader(URL url) throws SAXException, IOException {

        try {
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.connect();
            InputStream stream = conn.getInputStream();
            XmlPullParserFactory xmlFactoryObject = XmlPullParserFactory.newInstance();
            XmlPullParser myparser = xmlFactoryObject.newPullParser();

            myparser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            myparser.setInput(stream, null);

            ArrayList<RSSItem> myItems = new ArrayList<>();
            RSSItem rssItem = null;

            int event;
            String text = null;
            String myTag = null;
            event = myparser.getEventType();
            boolean processingItem = false;
            while (event != XmlPullParser.END_DOCUMENT) {
                String name = myparser.getName();
                switch (event) {
                    case XmlPullParser.START_TAG:
                        if (null != name && name.equals("item")) {
                            processingItem = true;
                            rssItem = new RSSItem();
                        }
                        if (name.equals("title")) {
                            myTag = "title";
                        } else if (name.equals("link")) {
                            myTag = "link";
                        } else if (name.equals("description")) {
                            myTag = "description";
                        } else {
                            myTag = "";
                        }

                        break;

                    case XmlPullParser.TEXT:
                        text = myparser.getText();
                        if (processingItem && StringUtils.isNotBlank(text)) {
                            System.out.println("<" + myTag + ">" + text + "</" + myTag + ">");
                            if (myTag.equals("title")) {
                                rssItem.setTitle(text);
                            } else if (myTag.equals("link")) {
                                rssItem.setLink(text);
                            } else if (myTag.equals("description")) {
                                rssItem.setDescription(text);
                            } else {
                            }
                        }
                        break;

                    case XmlPullParser.END_TAG:
                        if (null != name && name.equals("item")) {
                            processingItem = false;
                            myItems.add(rssItem);
                        }
                        break;
                }

                event = myparser.next();
            }


            stream.close();


            return myItems;
        } catch (Exception e) {

            e.printStackTrace();
            throw new SAXException();
        }

    }

}

