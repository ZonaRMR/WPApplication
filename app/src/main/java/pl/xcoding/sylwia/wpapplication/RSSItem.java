package pl.xcoding.sylwia.wpapplication;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;


public class RSSItem implements Parcelable {

    private String title;
    private String link;
    private String description;
    private String image;

    public RSSItem() {
    }

    public RSSItem(Parcel parcel) {
        final Bundle data = parcel.readBundle();

        title = data.getString("title");
        link = data.getString("link");
        description = data.getString("description");
        image = data.getString("image");

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        final Bundle data = new Bundle();

        data.putString("title", title);
        data.putString("link", link);
        data.putString("description", description);
        data.putString("image", image);

        dest.writeBundle(data);
    }

    public static final Parcelable.Creator<RSSItem> CREATOR = new Parcelable.Creator<RSSItem>() {
        public RSSItem createFromParcel(Parcel data) {
            return new RSSItem(data);
        }

        public RSSItem[] newArray(int size) {
            return new RSSItem[size];
        }
    };

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
