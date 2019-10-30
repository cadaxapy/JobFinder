package com.example.jobfounder;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class FavoriteFragment extends Fragment {
    MainActivity mainActivity;
    LoadingScreen loadingScreen;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_favorite, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        loadingScreen = new LoadingScreen(this.getActivity());
        mainActivity = (MainActivity) getActivity();
        mainActivity.showWhiteBackground();
        loadingScreen.showDialog();
        super.onViewCreated(view, savedInstanceState);
        final ListView listView = (ListView) getView().findViewById(R.id.lv);
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference favRef = db.collection("users")
                .document(currentUser.getUid())
                .collection("favorites");
        favRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    ArrayList<JobModel> jobModelArrayList = new ArrayList<>();

                    for (QueryDocumentSnapshot document : task.getResult()) {
                        JobModel jobModel = new JobModel();
                        jobModel.setJobName(document.getString("jobName"));
                        jobModel.setLand(document.getString("jobLand"));
                        jobModel.setType(document.getString("jobType"));
                        jobModel.setImgURL(document.getString("imgURL"));
                        jobModel.setIsImportant(document.getBoolean("isImportant") != null ? document.getBoolean("isImportant") : false);
                        jobModel.setIsOptional(document.getBoolean("isOptional") != null ? document.getBoolean("isOptional"): false);
                        jobModel.setFavoriteId(document.getId());
                        jobModelArrayList.add(jobModel);
                    }
                    Log.d("USR", jobModelArrayList.get(0).getJobName());
                    Log.d("USR", jobModelArrayList.get(1).getJobName());
                    FavoriteAdapter favoriteAdapter = new FavoriteAdapter(getActivity(),jobModelArrayList);
                    listView.setAdapter(favoriteAdapter);
                    mainActivity.showFragments();
                    loadingScreen.hideDialog();
                } else {
                    Log.d("TAG", "Error getting documents: ", task.getException());
                }
            }
        });
    }
}
