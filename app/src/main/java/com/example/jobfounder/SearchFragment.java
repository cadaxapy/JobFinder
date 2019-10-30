package com.example.jobfounder;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;



public class SearchFragment extends Fragment {
    String[] typeList = {"Stelle", "Nebenjob/+Ferienjob", "Ausbildungsstelle", "Studienplatz/+Werkstudent", "Praktikum/+Referendariat", "Abschlussarbeit"};
    String[] landList = {"Bayern", "Berlin", "Hamburg", "Rheinland-Pfalz", "Saarland", "Sachsen", "Thüringen", "Deutschland", "Ausland"};
    String[] professionList = {"Beratung+&+Consulting",
            "Elektronik,+Elektrotechnik",
            "Geisteswissenschaften,+Sprachen",
            "Informatik",
            "Ingenieurswissenschaften",
            "IT+&+Telekommunikation",
            "Kunst,+Kultur,+Gestaltung",
            "Landwirtschaft,+Natur,+Umwelt",
            "Medien+&+Unterhaltung",
            "Medizin+&+Gesundheit"};
    String selectedTypes = "";
    String selectedLands = "";
    String selectedProfessions = "";
    MainActivity mainActivity;
    LoadingScreen loadingScreen;
    private ListView listView;
    private boolean filterBoxVisible = false;
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
        loadingScreen = new LoadingScreen(this.getActivity());
         mainActivity = (MainActivity) getActivity();
        listView = (ListView) getView().findViewById(R.id.lv);
        fetchJSON(jsonURL);
        final LinearLayout filterBox = (LinearLayout) getView().findViewById(R.id.filter_box);
        ActionMenuItemView filterButton = (ActionMenuItemView) getView().findViewById(R.id.action_search);
        filterBox.setVisibility(View.GONE);
//        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
//                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//        layoutParams.setMargins(0, -filterBox.getHeight(), 0, 0);
//        filterBox.setLayoutParams(layoutParams);
        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(filterBox.getVisibility() == View.VISIBLE) {
                    filterBox.setVisibility(View.GONE);
                } else {
                    filterBox.setVisibility(View.VISIBLE);
                }

            }
        });
        final ChipGroup typeGroup = (ChipGroup) getView().findViewById(R.id.type_group);
        for(int i = 0; i < typeList.length; i++) {
            Chip chip = new Chip(getActivity(), null, R.attr.CustomChipChoiceStyle);
            chip.setText(typeList[i]);
            chip.setId(i);
            typeGroup.addView(chip);
        }
        final ChipGroup landGroup = (ChipGroup) getView().findViewById(R.id.land_group);
        for(int i = 0; i < landList.length; i++) {
            Chip chip = new Chip(getActivity(), null, R.attr.CustomChipChoiceStyle);
            chip.setText(landList[i]);
            chip.setId(i);
            landGroup.addView(chip);
        }
        final ChipGroup professionGroup = (ChipGroup) getView().findViewById(R.id.proffesion_group);
        for(int i = 0; i < professionList.length; i++) {
            Chip chip = new Chip(getActivity(), null, R.attr.CustomChipChoiceStyle);
            chip.setId(i);
            chip.setText(professionList[i]);
            professionGroup.addView(chip);
        }
        getView().findViewById(R.id.filter_search).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(typeGroup.getCheckedChipIds().size() > 0) {
                    List<Integer> selectedTypeIds = typeGroup.getCheckedChipIds();
                    for(int i = 0; i < selectedTypeIds.size(); i++) {
                        Chip chip = (Chip) typeGroup.getChildAt(selectedTypeIds.get(i));
                        selectedTypes += "&art=" + chip.getText().toString().trim();
                    }
                }
                if(professionGroup.getCheckedChipIds().size() >  0) {
                    List<Integer> selectedProfessionIds = professionGroup.getCheckedChipIds();
                    for(int i = 0; i < selectedProfessionIds.size(); i++) {
                        Chip chip = (Chip) professionGroup.getChildAt(selectedProfessionIds.get(i));
                        selectedProfessions += "&berufsfeld=" + chip.getText().toString().trim() ;
                    }
                }
                if(landGroup.getCheckedChipIds().size() > 0) {
                    List<Integer> selectedLandIds = landGroup.getCheckedChipIds();
                    for(int i = 0; i < selectedLandIds.size(); i++) {
                        Chip chip = (Chip) landGroup.getChildAt(selectedLandIds.get(i));
                        selectedLands += "&bundesland=" + chip.getText().toString().trim();
                    }
                }
                filterBox.setVisibility(View.GONE);
                String url = jsonURL + selectedTypes + selectedProfessions + selectedLands;
                fetchJSON(url);
            }
        });
        super.onViewCreated(view, savedInstanceState);
    }
    @SuppressLint("StaticFieldLeak")
    private void fetchJSON(final String url){
        mainActivity.showWhiteBackground();
        loadingScreen.showDialog();
        new AsyncTask<Void, Void, String>(){
            protected String doInBackground(Void[] params) {
                String response="";
                HashMap<String, String> map=new HashMap<>();
                try {
                    HttpRequest req = new HttpRequest(url);
                    response = req.prepare(HttpRequest.Method.GET).sendAndReadString();
                } catch (Exception e) {
                    response=e.getMessage();
                }
                return response;
            }
            protected void onPostExecute(String response) {
                jobModelArrayList = getInfo(response);
                tennisAdapter = new JobAdapter(getActivity(),jobModelArrayList);
                loadingScreen.hideDialog();
                mainActivity.showFragments();
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
                    playersModel.setAddress(dataobj.getString("Straße"));
                    playersModel.setCity(dataobj.getString("Ort"));
                    playersModel.setEmail(dataobj.getString("E-Mail"));
                    playersModel.setPlz(dataobj.getString("PLZ"));
                    playersModel.setJobName(dataobj.getString("Anschreiben zur Stelle"));
                    playersModel.setLand(dataobj.getString("Bundesland"));
                    playersModel.setSubjectArea(dataobj.getString("Fachrichtung"));
                    playersModel.setType(dataobj.getString("Art der Stelle"));
                    Date activeBy = null;
                    try {
                        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
                        activeBy = format.parse(dataobj.getString("Stelle aktiv bis (Publikationsende)"));
                    } catch (ParseException e) {
                        Log.d("DATE", e.getMessage());
                    }

                    playersModel.setActiveBy(activeBy);
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
