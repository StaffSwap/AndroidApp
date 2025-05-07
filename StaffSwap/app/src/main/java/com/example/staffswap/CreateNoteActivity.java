package com.example.staffswap;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.staffswap.model.SQLiteHelper;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CreateNoteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_create_note);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Intent intent = getIntent();

        String id = intent.getStringExtra("id");
        String title = intent.getStringExtra("title");
        String content = intent.getStringExtra("content");

        EditText editTextTitle = findViewById(R.id.editTextTextNoteTitle);
        EditText editTextContent = findViewById(R.id.editTextTextNoteContent);

        if(title!=null){
            editTextTitle.setText(title);
        }

        if(content!=null){
            editTextContent.setText(content);
        }

        Button saveNote = findViewById(R.id.saveNoteButton);
        saveNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                EditText editTextTitle = findViewById(R.id.editTextTextNoteTitle);
                EditText editTextContent = findViewById(R.id.editTextTextNoteContent);

                if (editTextTitle.getText().toString().isEmpty()){
                    Toast.makeText(CreateNoteActivity.this, "Please Fill title", Toast.LENGTH_SHORT).show();

                }else if (editTextContent.getText().toString().isEmpty()){
                    Toast.makeText(CreateNoteActivity.this, "Please Fill Content", Toast.LENGTH_SHORT).show();
                }else{
                    //save note

                    SQLiteHelper sqLiteHelper = new SQLiteHelper(
                            CreateNoteActivity.this,
                            "mynotebook.db",
                            null,
                            1
                    );

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            SQLiteDatabase sqLiteDatabase = sqLiteHelper.getWritableDatabase();
                            ContentValues contentValues = new ContentValues();
                            contentValues.put("title",editTextTitle.getText().toString());
                            contentValues.put("content",editTextContent.getText().toString());

                            SimpleDateFormat format  = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");
                            contentValues.put("date_created",format.format(new Date()));


                            if (id != null){
                                int count = sqLiteDatabase.update(
                                        "notes",
                                        contentValues,
                                        "`id` =?",
                                        new String[]{id}
                                );
                                Log.i("MyNoteBook",count+"Row updated");
                            }else{
                                long inserted = sqLiteDatabase.insert(
                                        "notes",
                                        "null",
                                        contentValues
                                );

                                Log.i("MyNoteBook",String.valueOf(inserted));

                            }



                            sqLiteDatabase.close();

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    editTextTitle.setText("");
                                    editTextContent.setText("");

                                    editTextTitle.requestFocus();
                                }
                            });


                        }
                    }).start();



                }

            }
        });


    }
}