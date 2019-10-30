package com.example.jobfounder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class DetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Intent intent = getIntent();
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((TextView) findViewById(R.id.job_name)).setText(intent.getStringExtra("job_name"));
        ((TextView) findViewById(R.id.job_land)).setText(intent.getStringExtra("job_land"));
        ((TextView) findViewById(R.id.job_type)).setText(intent.getStringExtra("job_type"));
        ((TextView) findViewById(R.id.email)).setText(intent.getStringExtra("email"));
        ((TextView) findViewById(R.id.address)).setText(intent.getStringExtra("address"));
        ((TextView) findViewById(R.id.city)).setText(intent.getStringExtra("city"));
        ((TextView) findViewById(R.id.plz)).setText(intent.getStringExtra("PLZ"));
        ((TextView) findViewById(R.id.subject_area)).setText(intent.getStringExtra("subjectArea"));
        ((TextView) findViewById(R.id.active_by)).setText(intent.getStringExtra("activeBy"));
        Picasso.get().load(intent.getStringExtra("imgURL")).into((ImageView) findViewById(R.id.image_url));
    }
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
