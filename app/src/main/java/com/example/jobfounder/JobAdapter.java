package com.example.jobfounder;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import br.com.simplepass.loadingbutton.customViews.CircularProgressButton;

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
    public View getView(final int position, View convertView, ViewGroup parent) {
        final FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
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
            holder.add_favorite = (CircularProgressButton) convertView.findViewById(R.id.add_favorite);
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
                intent.putExtra("job_name", holder.job_name.getText().toString());
                intent.putExtra("job_type", holder.job_type.getText().toString());
                intent.putExtra("job_land", holder.job_land.getText().toString());
                intent.putExtra("imgURL", jobModelArrayList.get(position).getImgURL());
                intent.putExtra("email", jobModelArrayList.get(position).getEmail());
                intent.putExtra("address", jobModelArrayList.get(position).getAddress());
                intent.putExtra("city", jobModelArrayList.get(position).getCity());
                intent.putExtra("PLZ", jobModelArrayList.get(position).getPlz());
                intent.putExtra("activeBy", jobModelArrayList.get(position).getActiveBy() != null ? jobModelArrayList.get(position).getActiveBy().toString() : "");
                intent.putExtra("subjectArea", jobModelArrayList.get(position).getSubjectArea());
                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) context, holder.card_view, "Hello");
                ActivityCompat.startActivity(context, intent, options.toBundle());
            }
        });

        holder.add_favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.add_favorite.startMorphAnimation();
                holder.add_favorite.setEnabled(false);
                Map<String, Object> favoriteData = new HashMap<>();
                favoriteData.put("jobName", holder.job_name.getText().toString());
                favoriteData.put("activeBy", jobModelArrayList.get(position).getActiveBy());
                favoriteData.put("jobType", holder.job_type.getText().toString());
                favoriteData.put("jobLand", holder.job_land.getText().toString());
                favoriteData.put("imgURL", jobModelArrayList.get(position).getImgURL());
                db.collection("users")
                        .document(currentUser.getUid())
                        .collection("favorites")
                        .add(favoriteData)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                holder.add_favorite.doneLoadingAnimation(R.color.success, BitmapFactory.decodeResource(context.getResources(), R.drawable.check_icon));
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("TAG", "Error adding document", e);
                        Toast.makeText(context, "Error adding to favorite",
                                Toast.LENGTH_SHORT).show();
                    }
                });

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
        protected CircularProgressButton add_favorite;
        MaterialCardView card_view;
    }

}