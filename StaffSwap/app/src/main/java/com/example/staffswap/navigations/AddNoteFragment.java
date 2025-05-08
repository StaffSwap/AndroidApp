package com.example.staffswap.navigations;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.staffswap.CreateNoteActivity;
import com.example.staffswap.R;
import com.example.staffswap.model.SQLiteHelper;


public class AddNoteFragment extends Fragment {



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View view =  inflater.inflate(R.layout.fragment_add_note, container, false);


        CardView addNotePageButton = view.findViewById(R.id.goToCreateNotePageCardView);
        addNotePageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent;
                intent = new Intent(requireActivity(), CreateNoteActivity.class);
                startActivity(intent);
            }
        });


        return view;




    }

    @Override
    public void onResume() {
        super.onResume();

        RecyclerView recyclerView = requireView().findViewById(R.id.recyclerViewNotelist);

        X x = new X();
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(x);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);


        SQLiteHelper sqLiteHelper = new SQLiteHelper(
                requireContext(),
                "mynotebook.db",
                null,
                1
        );

        new Thread(new Runnable() {
            @Override
            public void run() {
                SQLiteDatabase sqLiteDatabase = sqLiteHelper.getReadableDatabase();

                Cursor cursor = sqLiteDatabase.query(
                        "notes",
                        null,
                        null,
                        null,
                        null,
                        null,
                        "`id` DESC"
                );

//                while (cursor.moveToNext()){
//                    String title = cursor.getString(2);
//                    Log.i("MyNoteBook", title);
//
//                }

                requireActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        NotelistAdapter noteListAdapter = new NotelistAdapter(cursor);
                        recyclerView.setAdapter(noteListAdapter);
                    }
                });


            }
        }).start();
    }
}

 class NotelistAdapter extends RecyclerView.Adapter<NotelistAdapter.NoteViewHolder> {

     Cursor cursor;

     public NotelistAdapter(Cursor cursor){
         this.cursor = cursor;
     }

     static class NoteViewHolder extends  RecyclerView.ViewHolder {

         TextView titleView;
         TextView contentView;
         TextView date_createdView;

         View containerView;

         String id;

         public NoteViewHolder(@NonNull View itemView) {
             super(itemView);
             titleView = itemView.findViewById(R.id.textViewNoteTitleView);
             contentView = itemView.findViewById(R.id.textViewNoteContentView);
             date_createdView = itemView.findViewById(R.id.textViewNoteSaveDate);
             containerView = itemView;
         }
     }

     @NonNull
     @Override
     public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
         LayoutInflater inflater = LayoutInflater.from(parent.getContext());
         View view  = inflater.inflate(R.layout.note_item,parent,false);
         NoteViewHolder noteViewHolder = new NoteViewHolder(view);

         return noteViewHolder;
     }

     @Override
     public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {

         cursor.moveToPosition(position);

         holder.id = cursor.getString(0);
         String title = cursor.getString(1);
         String content = cursor.getString(2);
         String date = cursor.getString(3);

         holder.titleView.setText(title);
         holder.contentView.setText(content);
         holder.date_createdView.setText(date);

         holder.containerView.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {

                 Intent i = new Intent(view.getContext(),CreateNoteActivity.class);
                 i.putExtra("id", holder.id);
                 i.putExtra("title", title);
                 i.putExtra("content", content);
                 view.getContext().startActivity(i);
             }
         });

     }

     @Override
     public int getItemCount() {
         return cursor.getCount();
     }

     public void m(int position){
         notifyItemRemoved(position);
     }



 }


class X extends ItemTouchHelper.Callback{
    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        return makeMovementFlags(0,ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT);
//         return 0;
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        Log.i("MyNoteBook","onSwiped");

        NotelistAdapter.NoteViewHolder holder = (NotelistAdapter.NoteViewHolder) viewHolder;


        SQLiteHelper sqLiteHelper = new SQLiteHelper(
                viewHolder.itemView.getContext(),
                "mynotebook.db",
                null,
                1
        );

        new Thread(new Runnable() {
            @Override
            public void run() {

                SQLiteDatabase sqLiteDatabase = sqLiteHelper.getWritableDatabase();
                int row = sqLiteDatabase.delete(
                        "notes",
                        "`id` =?",
                        new String[]{holder.id}
                );

                Log.i("MyNoteBook",row+" Row deleted");
            }
        }).start();


    }
}