package com.example.jobfounder;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;

public class JobAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<JobModel> jobModelArrayList;

    public JobAdapter(Context context, ArrayList<JobModel> jobModelArrayList) {

        this.context = context;
        this.jobModelArrayList = jobModelArrayList;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }
    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getCount() {
        return jobModelArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return jobModelArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.job_item, null, true);
            holder.card_view = (MaterialCardView) convertView.findViewById(R.id.card_view);
            holder.iv = (ImageView) convertView.findViewById(R.id.image_url);
            holder.job_name = (TextView) convertView.findViewById(R.id.job_name);
            holder.job_type = (TextView) convertView.findViewById(R.id.job_type);
            holder.job_land = (TextView) convertView.findViewById(R.id.job_land);
            holder.share = (FloatingActionButton) convertView.findViewById(R.id.share);
            convertView.setTag(holder);
        }else {
            // the getTag returns the viewHolder object set as a tag to the view
            holder = (ViewHolder)convertView.getTag();
        }

        Picasso.get().load(jobModelArrayList.get(position).getImgURL()).into(holder.iv);
        holder.job_name.setText(jobModelArrayList.get(position).getJobName());
        holder.job_type.setText(jobModelArrayList.get(position).getType());
        holder.job_land.setText(jobModelArrayList.get(position).getLand());
        holder.card_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailsActivity.class);
                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) context, holder.card_view, "Hello");
                ActivityCompat.startActivity(context, intent, options.toBundle());
            }
        });
        holder.share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "This is my text to send.");
                sendIntent.setType("text/plain");

                Intent shareIntent = Intent.createChooser(sendIntent, null);
                context.startActivity(shareIntent);
            }
        });
        return convertView;
    }
    private class ViewHolder {
        protected FloatingActionButton share;
        protected TextView job_name, job_type, job_land;
        protected ImageView iv;
        MaterialCardView card_view;
    }

}