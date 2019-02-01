package com.example.mehed.pikanotesapp;

import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class NewNoteActivity extends AppCompatActivity {
    private Button btnCreate;
    private EditText etTitle, etcontent;
    private Toolbar mToolbar;
    private FirebaseAuth fAuth;
    private DatabaseReference fNotesDatabase;
    String noteId;
    boolean isExist;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_note);
        btnCreate=(Button) findViewById(R.id.new_note_btn);
        etTitle=(EditText) findViewById(R.id.new_note_title);
        etcontent=(EditText) findViewById(R.id.new_note_content);
        mToolbar=(Toolbar)findViewById(R.id.new_note_toolbar);

        setSupportActionBar(mToolbar);

//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);
        try{
            noteId = getIntent().getStringExtra("noteId");
            if(!noteId.trim().equals("")){
                isExist = true;
            }
            else{
                isExist = false;
            }

        }catch (Exception e){
            //Toast.makeText(NewNoteActivity.this,"Error",Toast.LENGTH_LONG).show();
        }



        fAuth=FirebaseAuth.getInstance();
        fNotesDatabase=FirebaseDatabase.getInstance().getReference().child("Notes").child(fAuth.getCurrentUser().getUid());


        if(isExist){
            fNotesDatabase.child(noteId).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String t = dataSnapshot.child("title").getValue().toString();
                    String tx = dataSnapshot.child("content").getValue().toString();
                    etTitle.setText(t);
                    etcontent.setText(tx);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title=etTitle.getText().toString().trim();
                String content = etcontent.getText().toString().trim();


                if(!TextUtils.isEmpty(title) && !TextUtils.isEmpty(content))
                {
                        if(isExist){
                            fNotesDatabase.child(noteId).child("title").setValue(title);
                            fNotesDatabase.child(noteId).child("content").setValue(content);
                            fNotesDatabase.child(noteId).child("time").setValue(Calendar.getInstance().getTime().toString());
                            Toast.makeText(NewNoteActivity.this,"Note Updated",Toast.LENGTH_LONG).show();
                        }
                        else {
                            createNote(title, content);
                        }
                        onBackPressed();
                }
                else
                {
                    Snackbar.make(view, "Fill Empty Fields", Snackbar.LENGTH_SHORT).show();

                }

            }
        });

    }
    private void createNote(String title, String content)
    {
        if(fAuth.getCurrentUser()!=null)
        {
            final DatabaseReference newNoteRef = fNotesDatabase.push();
            final Map noteMap = new HashMap();
            noteMap.put("title", title);
            noteMap.put("content", content);
            noteMap.put("time",Calendar.getInstance().getTime().toString());
            Thread mainThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    newNoteRef.setValue(noteMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if(task.isSuccessful())
                            {
                                Toast.makeText(NewNoteActivity.this, "Note Added to Database", Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                Toast.makeText(NewNoteActivity.this, "ERROR" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                            }
                        }
                    });

                }
            });
            mainThread.start();


        }
        else
        {
            Toast.makeText(this, "USERS IS NOT SIGNED IN", Toast.LENGTH_SHORT).show();
        }
        onBackPressed();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.delete,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        super.onOptionsItemSelected(item);

        switch (item.getItemId()){
            case R.id.new_note :
                if(isExist){
                    fNotesDatabase.child(noteId).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(NewNoteActivity.this,"Note Deleted",Toast.LENGTH_LONG).show();
                                    noteId = "";
                                    finish();
                                }
                        }
                    });
                }
                break;
        }
        return true;
    }

}
