package com.example.jobfounder;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Text;


public class SettingFragment extends Fragment {
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    LoadingScreen loadingScreen;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_setting, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        final FirebaseUser currentUser = mAuth.getInstance().getCurrentUser();
        db = FirebaseFirestore.getInstance();
        loadingScreen = new LoadingScreen(this.getActivity());
        final MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.showWhiteBackground();

        loadingScreen.showDialog();
        final TextView username = (TextView) getView().findViewById(R.id.username);
        final TextView email = (TextView) getView().findViewById(R.id.email);
        final TextView phone = (TextView) getView().findViewById(R.id.phone);
        DocumentReference docRef = db.collection("users").document(currentUser.getUid());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(!task.isSuccessful()) {
                    Log.d("TAG", "get failed with ", task.getException());
                    return;
                }
                DocumentSnapshot document = task.getResult();
                if(!document.exists()) {
                    Log.d("TAG", "No such document");
                    return;
                }
                username.setText(document.getString("username"));
                email.setText(document.getString("email"));
                phone.setText(currentUser.getPhoneNumber());
                loadingScreen.hideDialog();
                mainActivity.showFragments();
            }
        });
        super.onViewCreated(view, savedInstanceState);
    }
}
