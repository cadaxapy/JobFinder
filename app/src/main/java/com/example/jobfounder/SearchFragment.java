package com.example.jobfounder;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.ActionMenuView;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.view.menu.ActionMenuItemView;
import androidx.fragment.app.Fragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SearchFragment extends Fragment {
    private ListView listView;
    private String jsonURL = "https://www.wikway.de/companies/offers-json?password=ain1018";
    private final int jsoncode = 1;
    ArrayList<JobModel> jobModelArrayList;
    private JobAdapter tennisAdapter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        listView = (ListView) getView().findViewById(R.id.lv);
        final LinearLayout filterBox = (LinearLayout) getView().findViewById(R.id.filter_box);
        ActionMenuItemView searchButton = (ActionMenuItemView) getView().findViewById(R.id.action_search);
        filterBox.setVisibility(View.GONE);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterBox.setVisibility(View.VISIBLE);
                TranslateAnimation animate = new TranslateAnimation(
                        0,
                        0,
                        filterBox.getHeight(),
                        0);
                animate.setDuration(500);
                animate.setFillAfter(true);
                filterBox.startAnimation(animate);
            }
        });
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Intent intent = new Intent(MainActivity.this, DetailsActivity.class);
//                intent.putExtra(name:"example", )
//            }
//                Toast.makeText(getActivity(), String.valueOf(position), Toast.LENGTH_SHORT).show();
//                if (listener != null) {
//                    int position = getAdapterPosition();
//                    if (position != RecyclerView.NO_POSITION) {
//                        listener.onItemClick(position);
//                //new intent
//
//
//            }
//        });
        fetchJSON();
        super.onViewCreated(view, savedInstanceState);
    }

    @SuppressLint("StaticFieldLeak")
    private void fetchJSON(){

        new AsyncTask<Void, Void, String>(){
            protected String doInBackground(Void[] params) {
                String response="";
                HashMap<String, String> map=new HashMap<>();
                try {
                    HttpRequest req = new HttpRequest(jsonURL);
                    response = req.prepare(HttpRequest.Method.GET).sendAndReadString();
                } catch (Exception e) {
                    response=e.getMessage();
                }
                return response;
            }
            protected void onPostExecute(String response) {
                jobModelArrayList = getInfo(response);
                tennisAdapter = new JobAdapter(getActivity(),jobModelArrayList);
                listView.setAdapter(tennisAdapter);
            }
        }.execute();
    }


    public ArrayList<JobModel> getInfo(String response) {
        ArrayList<JobModel> jobModelArrayList = new ArrayList<>();
        try {
            JSONArray data = new JSONArray(response);


                for (int i = 0; i < data.length(); i++) {

                    JobModel playersModel = new JobModel();
                    JSONObject dataobj = data.getJSONObject(i);
                    playersModel.setImgURL(dataobj.getString("Logo"));
                    playersModel.setJobName(dataobj.getString("Anschreiben zur Stelle"));
                    playersModel.setLand(dataobj.getString("Bundesland"));
                    playersModel.setType(dataobj.getString("Art der Stelle"));

                    JSONArray jsonArrayProfessions = dataobj.getJSONArray("Berufsfeld");
                    List<String> professions = new ArrayList<String>();
                    for (int j = 0; j < jsonArrayProfessions.length(); j++) {
                        professions.add(jsonArrayProfessions.getString(j));
                    }
                    playersModel.setProfessions(professions);
                    jobModelArrayList.add(playersModel);

                }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jobModelArrayList;
    }


}
