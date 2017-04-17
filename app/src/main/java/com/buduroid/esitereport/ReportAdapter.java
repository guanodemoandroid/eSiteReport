package com.buduroid.esitereport;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.List;

public class ReportAdapter extends BaseAdapter {

    private Activity activity;
    private LayoutInflater inflater;
    private List<ReportData> reportItem;
    private ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    public ReportAdapter(Activity activity, List<ReportData> reportItem) {
        this.activity = activity;
        this.reportItem = reportItem;
    }

    @Override
    public int getCount() {
        return reportItem.size();
    }

    @Override
    public Object getItem(int location) {
        return reportItem.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.list_row, null);

        if (imageLoader == null)
            imageLoader = AppController.getInstance().getImageLoader();
        NetworkImageView thumbNail = (NetworkImageView) convertView.findViewById(R.id.thumbnail);
        TextView tvRepDate = (TextView) convertView.findViewById(R.id.tvRepDate);
        TextView tvRepTitle = (TextView) convertView.findViewById(R.id.tvRepTitle);
        TextView tvRepDesc = (TextView) convertView.findViewById(R.id.tvRepDesc);
        TextView tvRepLocation = (TextView) convertView.findViewById(R.id.tvRepLocation);
        TextView tvRepID = (TextView) convertView.findViewById(R.id.tvRepID);


        // getting billionaires data for the row
        ReportData m = reportItem.get(position);

        // thumbnail image
        thumbNail.setImageUrl(m.getThumbnailUrl(), imageLoader);

        // name
        tvRepDate.setText(m.getRepDate());
        tvRepTitle.setText(m.getRepTitle());
        tvRepDesc.setText(m.getRepDesc());
        tvRepLocation.setText(m.getRepLocation());
        tvRepID.setText(m.getRepID());

        //tvRepTitle.setText("Wealth Source: " + String.valueOf(m.getRepTitle()));



        return convertView;
    }

}
