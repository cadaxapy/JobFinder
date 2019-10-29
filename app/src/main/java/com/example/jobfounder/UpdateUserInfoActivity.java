package com.example.jobfounder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class UpdateUserInfoActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    LoadingScreen loadingScreen;
    EditText name, username, email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_user_info);
        init();
    }

    private void init() {
        loadingScreen = new LoadingScreen(UpdateUserInfoActivity.this);
        final FirebaseUser currentUser = mAuth.getInstance().getCurrentUser();
        db = FirebaseFirestore.getInstance();
        name = (EditText) findViewById(R.id.name);
        username = (EditText) findViewById(R.id.username);
        email = (EditText) findViewById(R.id.email);
        loadingScreen.showDialog();

        findViewById(R.id.buttonContinue).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Object> userData = new HashMap<>();
                userData.put("id", currentUser.getUid());
                userData.put("email", email.getText().toString());
                userData.put("name", name.getText().toString());
                userData.put("username", username.getText().toString());
                db.collection("users").document(currentUser.getUid())
                        .set(userData)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                startActivity(new Intent(UpdateUserInfoActivity.this, MainActivity.class));
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w("TAG", "Error adding document", e);
                                Toast.makeText(UpdateUserInfoActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
        DocumentReference docRef = db.collection("users").document(currentUser.getUid());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                loadingScreen.hideDialog();
                if(!task.isSuccessful()) {
                    Log.d("TAG", "get failed with ", task.getException());
                    return;
                }
                DocumentSnapshot document = task.getResult();
                if(document.exists()) {
                    username.setText(document.getString("username"));
                    name.setText(document.getString("name"));
                    email.setText(document.getString("email"));
                }
            }
        });
    }
}
