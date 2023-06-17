package com.tiger.firebasecrud_in_android;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Point;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;


public class MainAdapter extends FirebaseRecyclerAdapter<MainModel,MainAdapter.myviewholder>
{


    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public MainAdapter(@NonNull FirebaseRecyclerOptions<MainModel> options)
    {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myviewholder holder, @SuppressLint("RecyclerView") int position, @NonNull MainModel model)
    {
        holder.stname.setText(model.getName());
        holder.tcourse.setText(model.getCourse());
        holder.tmail.setText(model.getEmail());

        // Load the image URL into the ImageView using Glide
        Glide.with(holder.c.getContext())
                .load(model.getTurl())
                .circleCrop()
                .error(R.drawable.ic_launcher_background)
                .into(holder.c);
        holder.btedit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                final DialogPlus dp=DialogPlus.newDialog(holder.c.getContext())
                        .setContentHolder(new ViewHolder(R.layout.update_item))
                        .setPadding(16, 16, 16, 16)  // Optional: Add padding if desired
                        .setGravity(Gravity.CENTER)
                        .setContentWidth(getDynamicWidth(holder.c.getContext()))
                        .create();

                View vw=dp.getHolderView();
                EditText name=vw.findViewById(R.id.etName);
                EditText course=vw.findViewById(R.id.etCourse);
                EditText mail=vw.findViewById(R.id.etEmail);
                EditText url=vw.findViewById(R.id.etUrl);
                Button update=vw.findViewById(R.id.btnUpdate);

                name.setText(model.getName());
                course.setText(model.getCourse());
                mail.setText(model.getEmail());
                url.setText(model.getTurl());
                dp.show();

                update.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        Map<String,Object> details=new HashMap<>();
                        details.put("name",name.getText().toString());
                        details.put("course",course.getText().toString());
                        details.put("email",mail.getText().toString());
                        details.put("turl",url.getText().toString());

                        FirebaseDatabase.getInstance().getReference().child("teacher")
                                .child(getRef(position).getKey()).updateChildren(details)
                                .addOnSuccessListener(new OnSuccessListener<Void>()
                                {
                                    @Override
                                    public void onSuccess(Void unused)
                                    {
                                        Toast.makeText(holder.stname.getContext(), "Data Update Successful ", Toast.LENGTH_SHORT).show();
                                        dp.dismiss();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() 
                                {
                                    @Override
                                    public void onFailure(@NonNull Exception e) 
                                    {
                                        Toast.makeText(holder.stname.getContext(), "Data Update Failed ", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                });





            }
        });

        holder.btdelete.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(holder.stname.getContext());
                builder.setTitle("Confirmation")
                        .setMessage("Are you sure you want to delete this record?")
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Delete the record from the Firebase Realtime Database
                                DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("teacher");
                                databaseRef.child(getRef(position).getKey()).removeValue()
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid)
                                            {
                                                // Deletion successful
                                                Toast.makeText(holder.stname.getContext(), "Record deleted", Toast.LENGTH_SHORT).show();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                // Deletion failed
                                                //dp.dismiss();
                                                Toast.makeText(holder.stname.getContext(), "Failed to delete record", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {
                                // Cancel button clicked, do nothing
                                //dp.dismiss();
                                Toast.makeText(holder.stname.getContext(), "Cancelled", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .show();
            }
        });


    }
    // Method to calculate the dynamic width based on screen size
    private int getDynamicWidth(Context context)
    {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int screenWidth = size.x;

        // Calculate the desired width based on the screen size
        int dialogWidth = (int) (screenWidth * 0.8); // Adjust the factor as per your preference

        return dialogWidth;
    }
    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.rc_item,parent,false);
        return new myviewholder(view);
    }

    public class myviewholder extends RecyclerView.ViewHolder
    {
        CircleImageView c;
        TextView stname,tmail,tcourse;
        Button btedit;
        Button btdelete;
        public myviewholder(@NonNull View vitems)
        {
            super(vitems);
            c=(CircleImageView)vitems.findViewById(R.id.pic);
            stname=(TextView)vitems.findViewById(R.id.tname);
            tmail=(TextView)vitems.findViewById(R.id.email);
            tcourse=(TextView)vitems.findViewById(R.id.course);
            btedit=(Button) vitems.findViewById(R.id.btnEdit);
            btdelete=(Button) vitems.findViewById(R.id.btnDelete);
        }


    }
}
