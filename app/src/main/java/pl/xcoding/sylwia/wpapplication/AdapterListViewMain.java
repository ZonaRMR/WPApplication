package pl.xcoding.sylwia.wpapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class AdapterListViewMain extends BaseAdapter{
    private ArrayList<RSSItem> data;
    private Context listContext;
    private LayoutInflater layoutInflater;

    public AdapterListViewMain(Context context, ArrayList<RSSItem> data) {
        this.listContext = context;
        this.data = data;
        layoutInflater = LayoutInflater.from(listContext);
    }
    private class CustomHolder {
        TextView tvTitle;
        ImageView ivImage;
    }
    @Override
    public int getCount() {
        if (data != null) {
            return data.size();
        } else {
            return 0;
        }
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CustomHolder viewCache;
        RSSItem actualItem = data.get(position);


        if (convertView == null) {

            convertView = layoutInflater.inflate(
                    R.layout.listview_activity, null);

            viewCache = new CustomHolder();


            viewCache.tvTitle = (TextView) convertView
                    .findViewById(R.id.listview_item_title);

            viewCache.ivImage = (ImageView) convertView
                    .findViewById(R.id.listview_image);

            convertView.setTag(viewCache);
        } else {
            viewCache = (CustomHolder) convertView.getTag();
        }


        viewCache.tvTitle.setText(actualItem.getTitle());

        return convertView;
    }
    }

