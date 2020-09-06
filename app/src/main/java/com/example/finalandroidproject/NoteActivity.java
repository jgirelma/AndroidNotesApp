package com.example.finalandroidproject;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class NoteActivity extends AppCompatActivity {
    private EditText titleView;
    private EditText bodyView;
    private static final String TAG = "NoteActivity";
    FirebaseFirestore db;
    private Note note;
    private String userId;
    Boolean alreadyInDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);



        db = FirebaseFirestore.getInstance();

        Bundle data = getIntent().getExtras();
        userId = data.getString("userId");
        note = (Note) data.getParcelable("note");
        System.out.println(note.isIdNull());
        alreadyInDatabase = !note.isIdNull();

        titleView = findViewById(R.id.Title);
        bodyView = findViewById(R.id.Body);

        titleView.setText(note.getTitle());
        bodyView.setText(note.getBody());

        Button backButton = findViewById(R.id.Back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Button saveButton = findViewById(R.id.Save);
        saveButton.setOnClickListener( new View.OnClickListener() {
            public void onClick(View view){
                saveNote();
            }
        });

        Button deleteButton = findViewById(R.id.Delete);
        deleteButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                deleteNote();
            }
        });
        
    }

    public void saveNote() {


        if(alreadyInDatabase) {

            db.collection(userId)
                    .document(note.getId())
                    .update("title", titleView.getText().toString(), "body", bodyView.getText().toString(), "time", System.currentTimeMillis() / 1000l);

        } else {
                db.collection(userId)
                    .add(new Note(titleView.getText().toString(), bodyView.getText().toString()))
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            note.setId(documentReference.getId());
                            alreadyInDatabase = true;
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w(TAG, "Error adding document", e);
                        }
                    });
        }


    }

    public void deleteNote() {
        if(alreadyInDatabase) {
            db.collection(userId).document(note.getId())
                .delete()
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d(TAG, "DocumentSnapshot successfully deleted!");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w(TAG, "Error deleting document", e);
                        }
                    });
        }
        finish();

    }

}
