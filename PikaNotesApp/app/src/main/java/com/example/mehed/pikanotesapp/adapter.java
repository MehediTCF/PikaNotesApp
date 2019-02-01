package com.example.mehed.pikanotesapp;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mehed.pikanotesapp.R;
import com.example.mehed.pikanotesapp.object;

import java.util.ArrayList;
import java.util.List;

public class adapter extends RecyclerView.Adapter<adapter.MyViewHolder> {

    private List<object> blogList;
    private List<String> blogKey;
    private ArrayList<object> arrayList;
Context cn;


    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView note_title_,note_time_;
        public MyViewHolder(View view){
            super(view);
            note_time_ = view.findViewById(R.id.note_time);
            note_title_ = view.findViewById(R.id.note_title);

            note_title_.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(cn.getApplicationContext(),NewNoteActivity.class);
                    intent.putExtra("noteId",blogKey.get(getAdapterPosition()));
                    cn.startActivity(intent);
                }
            });
        }
    }

    public adapter(List<object> blogList, List<String> blogKey, Context cn){
        this.blogList = blogList;
        this.blogKey = blogKey;
        this.cn = cn;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.single_note_layout,viewGroup,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        object p = blogList.get(i);
        myViewHolder.note_time_.setText(String.valueOf(p.getTime()));
        myViewHolder.note_title_.setText(p.getTitle());
    }

    @Override
    public int getItemCount() {
        return blogList.size();
    }

    public void setFilter(List<object> newList,List<String > newKey){
        blogList=new ArrayList<>();
        blogList.addAll(newList);
        blogKey = new ArrayList<>();
        blogKey.addAll(newKey);
        notifyDataSetChanged();
    }
}
