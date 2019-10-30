package com.example.jobfounder;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.chip.Chip;
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

public class FavoriteAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<JobModel> jobModelArrayList;

    public FavoriteAdapter(Context context, ArrayList<JobModel> jobModelArrayList) {

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
        Log.d("POSI", String.valueOf(position));
        Log.d("ARR", jobModelArrayList.toString());
        Log.d("POS", jobModelArrayList.get(position).getJobName());
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        final AlertDialog dialogBuilder = builder.create();
        LayoutInflater dialogInflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        final View dialogView = dialogInflater.inflate(R.layout.favorite_tags, null);
        CheckBox important = (CheckBox) dialogView.findViewById(R.id.is_important);
        CheckBox optional = (CheckBox) dialogView.findViewById(R.id.is_optional);

        final FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.favorite_item, null, true);
            holder.card_view = (MaterialCardView) convertView.findViewById(R.id.card_view);
            holder.iv = (ImageView) convertView.findViewById(R.id.image_url);
            holder.job_name = (TextView) convertView.findViewById(R.id.job_name);
            holder.job_type = (TextView) convertView.findViewById(R.id.job_type);
            holder.job_land = (TextView) convertView.findViewById(R.id.job_land);
            holder.is_important = (Chip) convertView.findViewById(R.id.important_tag);
            holder.is_optional = (Chip) convertView.findViewById(R.id.optional_tag);
            convertView.setTag(holder);
        }else {
            // the getTag returns the viewHolder object set as a tag to the view
            holder = (ViewHolder)convertView.getTag();
        }
        convertView.findViewById(R.id.add_tag).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogBuilder.show();
            }
        });
        dialogView.findViewById(R.id.cancel_tag).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogBuilder.dismiss();
            }
        });
        dialogView.findViewById(R.id.save_tags).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogBuilder.dismiss();
                Map<String, Object> updates = new HashMap<>();
                final boolean isImportant = ((CheckBox) dialogView.findViewById(R.id.is_important)).isChecked();
                final boolean isOptional = ((CheckBox) dialogView.findViewById(R.id.is_optional)).isChecked();
                if(isImportant) {
                    holder.is_important.setVisibility(View.VISIBLE);
                } else {
                    holder.is_important.setVisibility(View.GONE);
                }
                if(isOptional) {
                    holder.is_optional.setVisibility(View.VISIBLE);
                } else {
                    holder.is_optional.setVisibility(View.GONE);
                }
                updates.put("isImportant", isImportant);
                updates.put("isOptional", isOptional);
                DocumentReference favoriteRef = db.collection("users")
                        .document(currentUser.getUid())
                        .collection("favorites")
                        .document(jobModelArrayList.get(position).getFavoriteId());
                favoriteRef.update(updates)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d("tag", "DocumentSnapshot successfully updated!");
                            }
                        });

            }
        });
        dialogBuilder.setView(dialogView);
        Picasso.get().load(jobModelArrayList.get(position).getImgURL()).into(holder.iv);
        holder.job_name.setText(jobModelArrayList.get(position).getJobName());
        holder.job_type.setText(jobModelArrayList.get(position).getType());
        holder.job_land.setText(jobModelArrayList.get(position).getLand());
        if(jobModelArrayList.get(position).getIsImportant()) {
            important.setChecked(true);
            holder.is_important.setVisibility(View.VISIBLE);
        }
        if(jobModelArrayList.get(position).getIsOptional()) {
            optional.setChecked(true);
            holder.is_optional.setVisibility(View.VISIBLE);
        }
        return convertView;
    }
    private class ViewHolder {
        protected FloatingActionButton share;
        protected TextView job_name, job_type, job_land;
        protected Chip is_important, is_optional;
        protected ImageView iv;
        protected CircularProgressButton add_favorite;
        MaterialCardView card_view;
    }

}