package com.example.notes;

import static com.google.firebase.database.FirebaseDatabase.getInstance;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Random;

public class notesAdapter extends RecyclerView.Adapter<notesAdapter.viewHolder> {
    Context context;
    ArrayList<DataStoreModel> list;

    DataStoreModel model;
    FirebaseUser user;

    static int num=0;
    public notesAdapter(Context context, ArrayList<DataStoreModel> list) {
        this.context = context;
        this.list = list;

    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view  = LayoutInflater.from(context).inflate(R.layout.show_notes_design,parent,false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
         model=list.get(position);
        holder.titlid.setText(model.getTitle());
        holder.contid.setText(model.getMessage());
        holder.linrvw.setBackgroundColor(holder.itemView.getResources().getColor(randomColor(),null));


        holder.linrvw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(((MainActivity)context),editNoteActivity.class);
                intent.putExtra("id",list.get(position).getId());
                context.startActivity(intent);
                ((MainActivity) context).finish();
            }
        });
        holder.linrvw.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                AlertDialog dialog=new AlertDialog.Builder(context)
                        .setTitle("Delete")
                        .setMessage("Are you sure want to Delete ?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                user= FirebaseAuth.getInstance().getCurrentUser();
                                String qw=user.getUid();
                               FirebaseDatabase.getInstance().getReference().child("Users").child(qw).child("1")
                                       .child(list.get(position).getId()).removeValue();
                                Toast.makeText(context,"Deleted", Toast.LENGTH_SHORT).show();
                                ((MainActivity)context).showNotes();
                            }
                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        }).show();
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public int randomColor(){
        ArrayList<Integer> llist=new ArrayList<>();
        llist.add(R.color.offGreen);
        llist.add(R.color.offRed);
        llist.add(R.color.yellow);
        llist.add(R.color.orange);
        llist.add(R.color.blue);

        int randm= (int) System.currentTimeMillis();
        Random random=new Random(randm);
        int randmo=random.nextInt(llist.size());
        return llist.get(randmo);
    }
    public class viewHolder extends RecyclerView.ViewHolder{
        TextView titlid,contid;
        LinearLayout linrvw;
        CardView crdvw;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            titlid=itemView.findViewById(R.id.titlid);
            contid=itemView.findViewById(R.id.contid);
            linrvw=itemView.findViewById(R.id.linrvw);
            crdvw=itemView.findViewById(R.id.crdvw);
        }
    }

}
