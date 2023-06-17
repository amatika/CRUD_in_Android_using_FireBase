package com.tiger.firebasecrud_in_android;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.SearchView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity
{
  FloatingActionButton fb;
  RecyclerView rcv;
  MainAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       // getSupportActionBar().hide(); //hide the title bar
        getSupportActionBar().setTitle("FireBase Tutorial");
        fb=(FloatingActionButton)findViewById(R.id.fab);
        rcv=findViewById(R.id.rv);
        rcv.setLayoutManager(new LinearLayoutManager(this));
        FirebaseRecyclerOptions<MainModel> options=new FirebaseRecyclerOptions.Builder<MainModel>()
                .setQuery(FirebaseDatabase.getInstance().getReference().child("teacher"),MainModel.class)
                .build();
        adapter=new MainAdapter(options);
        rcv.setAdapter(adapter);
        fb.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(getApplicationContext(),Add_Activity.class));
            }
        });

    }

    @Override
    protected void onStart()
    {
        super.onStart();
        adapter.startListening();
        // Add any onStart() operations or event tracking
    }

    @Override
    protected void onStop()
    {
        super.onStop();
        adapter.stopListening();
        // Add any onStop() operations or event tracking
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search, menu);

        // Get the SearchView and configure it
       // SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.sbar).getActionView();
       // searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setQueryHint("Search...");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener()
        {
            @Override
            public boolean onQueryTextSubmit(String query)
            {
                txtSearch(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText)
            {
                return false;
            }
        });
        return true;
    }

   public void txtSearch(String s)
   {
       FirebaseRecyclerOptions<MainModel> options=new FirebaseRecyclerOptions.Builder<MainModel>()
               .setQuery(FirebaseDatabase.getInstance().getReference().child("teacher").orderByChild("name").startAt(s).endAt(s+'~'),MainModel.class)
               .build();
       adapter=new MainAdapter(options);
       adapter.startListening();
       rcv.setAdapter(adapter);
   }

}