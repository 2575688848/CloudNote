package com.cloudnote.model;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hasee.cloudnote.ContentActivity;
import com.example.hasee.cloudnote.LoginActivity;
import com.example.hasee.cloudnote.MainActivity;
import com.example.hasee.cloudnote.R;

import org.w3c.dom.Text;

import java.util.List;




/**
 * Created by Hasee on 2017/7/10.
 */

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.ViewHolder> implements View.OnClickListener,View.OnLongClickListener{

    private List<Note> mNoteList;
    private RecyclerViewOnItemClickListener ClickListener;
    private RecyclerViewOnItemLongClickListener LongClickListener;
    @Override
    public void onClick(View v) {

        if (ClickListener != null) {
            //注意这里使用getTag方法获取数据
            ClickListener.onItemClickListener(v, (Integer) v.getTag());
        }
    }

    @Override
    public boolean onLongClick(View v) {
        return LongClickListener != null && LongClickListener.onItemLongClickListener(v, (Integer) v.getTag());

    }

    /*设置点击事件*/
    public void setRecyclerViewOnItemClickListener(RecyclerViewOnItemClickListener onItemClickListener) {
        this.ClickListener = onItemClickListener;
    }

    /*设置长按事件*/
    public void setOnItemLongClickListener(RecyclerViewOnItemLongClickListener onItemLongClickListener) {
        this.LongClickListener = onItemLongClickListener;
    }

    public interface RecyclerViewOnItemClickListener {

        void onItemClickListener(View view, int position);

    }

    public interface RecyclerViewOnItemLongClickListener {

        boolean onItemLongClickListener(View view, int position);

    }


    public   class ViewHolder extends RecyclerView.ViewHolder {
         View Noteview;
           TextView title;
            TextView content;
TextView creattime;

            public ViewHolder(View view) {
                super(view);
                Noteview=view;
                content=(TextView) view.findViewById(R.id.content_tv);
                title = (TextView) view.findViewById(R.id.title_tv);
                creattime=(TextView)view.findViewById(R.id.creattime_tv);

                }
            }


        public NoteAdapter(List<Note> fruitList) {
            mNoteList = fruitList;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notes, parent, false);
             ViewHolder holder = new ViewHolder(view);
            view.setOnClickListener(this);
            view.setOnLongClickListener(this);

            return holder;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            Note note = mNoteList.get(position);
            holder.title.setText(note.getTitle());
            holder.content.setText(note.getContent());
            holder.creattime.setText(note.getCreateTime());
            holder.Noteview.setTag(position);
        }



        @Override
        public int getItemCount() {
            return mNoteList.size();
        }
}
