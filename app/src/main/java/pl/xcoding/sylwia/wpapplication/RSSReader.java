package pl.xcoding.sylwia.wpapplication;


import org.apache.commons.lang3.StringEscapeUtils;
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

            int event;
            String text = null;

            event = myparser.getEventType();
            boolean parsingComplete = true;
            while (event != XmlPullParser.END_DOCUMENT) {
                String name = myparser.getName();
                switch (event) {
                    case XmlPullParser.START_TAG:
                        if (name.equals("title")) {
                        System.out.println("<tytul>");
                    } else if (name.equals("link")) {
                        System.out.println("<link>");
                    } else if (name.equals("description")) {
                        System.out.println("<description>");
                    } else {
                            System.out.println(name);
                    }
                        break;

                    case XmlPullParser.TEXT:
                        text = myparser.getText();
                        System.out.println(text);
                        break;

                    case XmlPullParser.END_TAG:

                        if (name.equals("title")) {
                            System.out.println("</tytul>");
                        } else if (name.equals("link")) {
                            System.out.println("</link>");
                        } else if (name.equals("description")) {
                            System.out.println("</description>");
                        } else {
                        }

                        break;
                }

                event = myparser.next();
            }

            parsingComplete = false;


            stream.close();


//            SAXParserFactory factory = SAXParserFactory.newInstance();
//            SAXParser parser = factory.newSAXParser();
//            XMLReader reader = parser.getXMLReader();
//            RSSParser rssParser = new RSSParser();
//            InputSource inputSource = new InputSource(url.openStream());
//
//
//            reader.setContentHandler(rssParser);
//            reader.parse(inputSource);


            return new ArrayList<>();
        } catch (Exception e) {

            e.printStackTrace();
            throw new SAXException();
        }

    }

}

