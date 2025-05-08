package com.example.staffswap;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.example.staffswap.model.CustomAlert;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class UserLoginActivity extends AppCompatActivity {

    EditText NIC,Password;
    String nic,password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        NIC = findViewById(R.id.UserLoginText01);
         Password = findViewById(R.id.UserLoginText02);

        Button loginButton = findViewById(R.id.UserLoginBtn);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               nic = NIC.getText().toString();
               password = Password.getText().toString();
                if(nic.isEmpty()){
                    CustomAlert.showCustomAlert(UserLoginActivity.this, "Error !", "Please Fill NIC", R.drawable.cancel);
                } else if (password.isEmpty()) {
                    CustomAlert.showCustomAlert(UserLoginActivity.this, "Error !", "Please Fill Password", R.drawable.cancel);
                }else {
                    searchFirebase();
                }
            }
        });
    }
    private  void searchFirebase(){

        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        firestore.collection("User")
                .whereEqualTo("NIC", nic).whereEqualTo("Password", password).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (queryDocumentSnapshots.isEmpty()) {
                            customAlert.showCustomAlert(UserLoginActivity.this,"Error ","Invalid UserName or Password",R.drawable.cancel);
                        } else {
                            for (QueryDocumentSnapshot document : queryDocumentSnapshots) {

                                String documentId = document.getId();
                                String name = document.getString("Name");
                                String email = document.getString("Email");
                                String subject = document.getString("Subject");
                                String number = document.getString("Number");
                                String nic = document.getString("NIC");

                                SharedPreferences userSharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
                                SharedPreferences.Editor editor = userSharedPreferences.edit();

                                editor.putString("UserID", documentId);
                                editor.putString("UserName", name);
                                editor.putString("UserEmail", email);
                                editor.putString("UserMobile", number);
                                editor.putString("UserNIC", nic);
                                editor.putString("UserSubject", subject);
                                editor.apply();

                                Intent intent01 = new Intent(UserLoginActivity.this, UserHomeActivity.class);
                                startActivity(intent01);
                                finish();
                            }
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        customAlert.showCustomAlert(UserLoginActivity.this,"Error ","Fail to load Data ! ",R.drawable.cancel);
                    }
                });
    }
}

