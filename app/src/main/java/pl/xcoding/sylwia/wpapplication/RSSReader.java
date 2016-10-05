package pl.xcoding.sylwia.wpapplication;

import android.support.annotation.NonNull;

import org.apache.commons.lang3.StringUtils;
import org.xml.sax.SAXException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RSSReader {

    public static ArrayList<RSSItem> startReader(URL url) throws SAXException, IOException {

        try {
            HttpURLConnection conn = getHttpURLConnection(url);
            InputStream stream = conn.getInputStream();
            XmlPullParserFactory xmlFactoryObject = XmlPullParserFactory.newInstance();
            XmlPullParser myparser = xmlFactoryObject.newPullParser();
            myparser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            myparser.setInput(stream, null);
            ArrayList<RSSItem> myItems = new ArrayList<>();
            RSSItem rssItem = null;
            int event;
            String myTag = null;
            event = myparser.getEventType();
            boolean processingItem = false;
            String regularExpression = "src=\"(.*)\" width";
            String regularExpressionDialog = "left\"/>((.|\\W)*)<a";
            String regularExpressionDialog2 = "^((.|\\W)*)<a";
            Pattern pattern = Pattern.compile(regularExpression);
            Pattern patternDialog = Pattern.compile(regularExpressionDialog);
            Pattern patternDialog2 = Pattern.compile(regularExpressionDialog2);

            readEvent(myparser, myItems, rssItem, event, myTag, processingItem, pattern, patternDialog, patternDialog2);

            stream.close();

            return myItems;
        } catch (Exception e) {
            e.printStackTrace();
            throw new SAXException();
        }

    }

    private static void readEvent(XmlPullParser myparser, ArrayList<RSSItem> myItems, RSSItem rssItem, int event, String myTag, boolean processingItem, Pattern pattern, Pattern patternDialog, Pattern patternDialog2) throws XmlPullParserException, IOException {
        String text;
        while (event != XmlPullParser.END_DOCUMENT) {
            String name = myparser.getName();
            switch (event) {

                case XmlPullParser.START_TAG:
                    if (null != name && name.equals("item")) {
                        processingItem = true;
                        rssItem = new RSSItem();
                    }
                    myTag = checkName(name);
                    break;

                case XmlPullParser.TEXT:
                    text = myparser.getText();
                    if (StringUtils.isNotBlank(myTag) && processingItem && StringUtils.isNotBlank(text)) {
                        checkTag(rssItem, text, myTag, pattern, patternDialog, patternDialog2);
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
    }

    private static void checkTag(RSSItem rssItem, String text, String myTag, Pattern pattern, Pattern patternDialog, Pattern patternDialog2) {
        if (myTag.equals("title")) {
            rssItem.setTitle(text);
        } else if (myTag.equals("link")) {
            rssItem.setLink(text);
        } else if (myTag.equals("description")) {
            checkRegularExpression(rssItem, text, pattern);
            chceckRegularExpressionDialog(rssItem, text, patternDialog, patternDialog2);
        } else {
        }
    }

    private static void chceckRegularExpressionDialog(RSSItem rssItem, String text, Pattern patternDialog, Pattern patternDialog2) {
        Matcher matcher1 = patternDialog.matcher(text);
        Matcher matcher2 = patternDialog2.matcher(text);
        if (matcher1.find()) {
            replace(rssItem, matcher1);
        } else if (matcher2.find()) {
            replace(rssItem, matcher2);
        }
    }

    private static void checkRegularExpression(RSSItem rssItem, String text, Pattern pattern) {
        Matcher matcher = pattern.matcher(text);
        if (matcher.find()) {
            rssItem.setImage(matcher.group(1));
        }
    }

    @NonNull
    private static String checkName(String name) {
        String myTag;
        if (name.equals("title")) {
            myTag = "title";
        } else if (name.equals("link")) {
            myTag = "link";
        } else if (name.equals("description")) {
            myTag = "description";
        } else {
            myTag = "";
        }
        return myTag;
    }

    private static void replace(RSSItem rssItem, Matcher matcher1) {
        rssItem.setDescription(matcher1.group(1).replaceAll("&#8211;|\\n", "").replaceAll("\\s*&#8226;\\s+", ". ").replaceAll("^\\.\\s*", "").replaceAll("^\\-\\s", ""));
    }

    @NonNull
    private static HttpURLConnection getHttpURLConnection(URL url) throws IOException {
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(10000);
        conn.setConnectTimeout(15000);
        conn.setRequestMethod("GET");
        conn.setDoInput(true);
        conn.connect();
        return conn;
    }

}

