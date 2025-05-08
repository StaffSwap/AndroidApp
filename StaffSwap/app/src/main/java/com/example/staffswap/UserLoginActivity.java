package com.example.staffswap;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.example.staffswap.model.CustomAlert;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class UserLoginActivity extends AppCompatActivity {

    EditText NIC,Password;

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

               String nic = NIC.getText().toString();
                String  password = Password.getText().toString();
                if(nic.isEmpty()){
                    CustomAlert.showCustomAlert(UserLoginActivity.this, "Error !", "Please Fill NIC", R.drawable.cancel);
                } else if (password.length() > 5) {
                    Intent intent = new Intent(UserLoginActivity.this, UserHomeActivity.class);
                    startActivity(intent);

                }
            }
        });


    }
}