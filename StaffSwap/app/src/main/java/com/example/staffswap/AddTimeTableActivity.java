package com.example.staffswap;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.dynamicanimation.animation.SpringAnimation;

import com.example.staffswap.model.CustomAlert;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class AddTimeTableActivity extends AppCompatActivity {
    Spinner spinner;
    String selectedDay;
    TextInputLayout lesson01, lesson02, lesson03, lesson04, lesson05, lesson06, lesson07, lesson08;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_time_table);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        spinner = findViewById(R.id.AddTimeTableSpinner);

        ArrayList<String> leaveTypes = new ArrayList<>();
        leaveTypes.add("Select Day ---");
        leaveTypes.add("Monday");
        leaveTypes.add("Tuesday");
        leaveTypes.add("Wednesday");
        leaveTypes.add("Thursday");
        leaveTypes.add("Friday");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                leaveTypes
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedDay = parent.getItemAtPosition(position).toString();
                Log.e("Selected Day ", selectedDay);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Handle no selection
            }
        });

         lesson01 = findViewById(R.id.textInputLayoutClass01);
         lesson02 = findViewById(R.id.textInputLayout02);
         lesson03 = findViewById(R.id.textInputLayout03);
         lesson04 = findViewById(R.id.textInputLayout04);
         lesson05 = findViewById(R.id.textInputLayout05);
         lesson06 = findViewById(R.id.textInputLayout06);
         lesson07 = findViewById(R.id.textInputLayout07);
         lesson08 = findViewById(R.id.textInputLayout08);



        Button timeTableSubmitButton = findViewById(R.id.timeTableSubmitButton);
        timeTableSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String lesson01class = lesson01.getEditText().getText().toString().trim();
                String lesson02class = lesson02.getEditText().getText().toString().trim();
                String lesson03class = lesson03.getEditText().getText().toString().trim();
                String lesson04class = lesson04.getEditText().getText().toString().trim();
                String lesson05class = lesson05.getEditText().getText().toString().trim();
                String lesson06class = lesson06.getEditText().getText().toString().trim();
                String lesson07class = lesson07.getEditText().getText().toString().trim();
                String lesson08class = lesson08.getEditText().getText().toString().trim();




                if (selectedDay.equals("Select Day ---")) {
                    CustomAlert.showCustomAlert(AddTimeTableActivity.this,"Error ","Please select a Date",R.drawable.cancel);
                }else{
                    Log.e("time table class ", selectedDay);

                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
                    String UserName = sharedPreferences.getString("UserName", "");


                    Map<String, Object> Sessions = new HashMap<>();
                    Sessions.put("8.00 AM - 8.40 AM", lesson01class);
                    Sessions.put("8.40 AM - 9.20 AM", lesson02class);
                    Sessions.put("9.20 AM - 10.00 AM", lesson03class);
                    Sessions.put("10.00 AM - 10.40 AM", lesson04class);
                    Sessions.put("10.50 AM - 11.30 AM", lesson05class);
                    Sessions.put("11.30 AM - 12.10 PM", lesson06class);
                    Sessions.put("12.10 PM - 12.50 PM", lesson07class);
                    Sessions.put("12.50 PM - 13.30 PM", lesson08class);

                    db.collection("Schedule")
                            .document(UserName)
                            .collection("TimeTable")
                            .document(selectedDay)
                            .set(Collections.singletonMap("sessions", Sessions))
                            .addOnSuccessListener(aVoid -> {
                                CustomAlert.showCustomAlert(AddTimeTableActivity.this,"Success ",selectedDay+" timetable added successfully",R.drawable.checked);
                                lesson01.getEditText().setText("");
                                lesson02.getEditText().setText("");
                                lesson03.getEditText().setText("");
                                lesson04.getEditText().setText("");
                                lesson05.getEditText().setText("");
                                lesson06.getEditText().setText("");
                                lesson07.getEditText().setText("");
                                lesson08.getEditText().setText("");

                                spinner.setSelection(0);


                            })
                            .addOnFailureListener(e -> {
                                Log.e("Firestore", "Error adding timetable", e);
                            });

                }
            }
        });



    }

}
