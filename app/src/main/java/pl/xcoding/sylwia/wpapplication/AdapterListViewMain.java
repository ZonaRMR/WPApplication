package pl.xcoding.sylwia.wpapplication;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;

public class AdapterListViewMain extends RecyclerView.Adapter<AdapterListViewMain.ViewHolder> {
    private ArrayList<RSSItem> data;
    private Context listContext;

    public AdapterListViewMain(Context context, ArrayList<RSSItem> data) {
        this.listContext = context;
        this.data = data;
    }


    @Override
    public AdapterListViewMain.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listview_activity, parent, false);
        return new AdapterListViewMain.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AdapterListViewMain.ViewHolder holder, int position) {
        holder.textView.setText(data.get(position).getTitle());

        holder.listview_image.setImageURI(data.get(position).getImage());

    }


    @Override
    public int getItemCount() {
        if (data != null) {
            return data.size();
        } else {
            return 0;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textView;
        private SimpleDraweeView listview_image;


        public ViewHolder(View v) {
            super(v);

            textView = (TextView) v.findViewById(R.id.listview_item_title);
            listview_image = (SimpleDraweeView) v.findViewById(R.id.listview_image);
        }
    }

}

