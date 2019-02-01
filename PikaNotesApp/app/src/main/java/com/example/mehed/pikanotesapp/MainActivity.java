package com.example.mehed.pikanotesapp;

import android.content.Intent;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth fAuth;
    private RecyclerView mNotesList;
    private GridLayoutManager gridLayoutManager;
    private DatabaseReference fNotesDatabase;
    private List<object>blagList = new ArrayList<>();
    private List<String>blagKey = new ArrayList<>();
    adapter mAdapter;
    private DatabaseReference mData;
    private EditText Search;
    private SearchView searchView;
    //private String Search_note_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        Search=findViewById(R.id.search_id);

        searchView = findViewById(R.id.idSearchView);
        search(searchView);

        mNotesList = (RecyclerView) findViewById(R.id.main_notes_list);
        mAdapter = new adapter(blagList,blagKey,MainActivity.this);
        gridLayoutManager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);

//        mNotesList.setHasFixedSize(true);
        mNotesList.setLayoutManager(gridLayoutManager);
//
//      blagList.add(new object("aaaa","aaaa","aaaaa"));

        mNotesList.setItemAnimator(new DefaultItemAnimator());
        mNotesList.setAdapter(mAdapter);



        fAuth = FirebaseAuth.getInstance();
        if (fAuth.getCurrentUser() != null) {
            fNotesDatabase = FirebaseDatabase.getInstance().getReference().child("Notes").child(fAuth.getCurrentUser().getUid());
        }

        updateUI();

        fNotesDatabase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                object o = dataSnapshot.getValue(object.class);
                blagList.add(o);
                blagKey.add(dataSnapshot.getKey().toString());
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }



    private void updateUI(){
        if(fAuth.getCurrentUser()!=null) {
            Log.i("MainActivity","fAuth!=null");
        } else
        {
            Intent startIntent= new Intent(MainActivity.this, StartActivity.class);
            startActivity(startIntent);
            Log.i("MainActivity","fAuth==null");


        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case R.id.main_new_note_btn:
                Intent newIntent = new Intent(MainActivity.this, NewNoteActivity.class);
                startActivity(newIntent);
                break;
            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(this,StartActivity.class);
                startActivity(intent);
        }
        return true;
    }
    private void search(SearchView searchView) {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                ArrayList<object> newList=new ArrayList<>();
                ArrayList<String > newKey = new ArrayList<>();
                for (object userInfo : blagList){
                    String content=userInfo.getContent();
                    String title=userInfo.getTitle();
                    String time=userInfo.getTime();
                    if (content.contains(newText)||title.contains(newText)||time.contains(newText)){
                        newList.add(userInfo);
                        newKey.add(blagKey.get(blagList.indexOf(userInfo)));
                    }
                }
                mAdapter.setFilter(newList,newKey);
                return true;
            }
        });
    }

}
