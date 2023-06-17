package com.tiger.firebasecrud_in_android;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class Add_Activity extends AppCompatActivity {
    EditText sname;
    EditText smail;
    EditText scourse;
    EditText surl;
    Button sav,hom;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        getSupportActionBar().hide();

        sname=(EditText) findViewById(R.id.saveName);
        smail=(EditText) findViewById(R.id.saveEmail);
        scourse=(EditText) findViewById(R.id.saveCourse);
        surl=(EditText) findViewById(R.id.saveUrl);

        sav=(Button)findViewById(R.id.save);
        hom=(Button) findViewById(R.id.home);

        sav.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Map<String,Object> details=new HashMap<>();
                details.put("name",sname.getText().toString());
                details.put("course",scourse.getText().toString());
                details.put("email",smail.getText().toString());
                details.put("turl",surl.getText().toString());

                FirebaseDatabase.getInstance().getReference().child("teacher").push()
                        .setValue(details)
                        .addOnSuccessListener(new OnSuccessListener<Void>()
                        {
                            @Override
                            public void onSuccess(Void unused)
                            {
                                Toast.makeText(Add_Activity.this, "Data Saved Successfully ", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener()
                        {
                            @Override
                            public void onFailure(@NonNull Exception e)
                            {
                                Toast.makeText(Add_Activity.this, "Data Saving Failed ", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        });
            }
        });

        hom.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                finish();
                //startActivity(new Intent(getApplicationContext(),MainActivity.class));
            }
        });

    }
}