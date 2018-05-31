package com.example.android.newnote;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.android.newnote.database.NoteUtil;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Derrick on 2018/5/30.
 */

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.NoteViewHolder>{

    private List<Note> mNoteList;
    private static Context mContext;
    private OnDeleteListener mOnDeleteListener;

    public void setOnDeleteListener(OnDeleteListener onDeleteListener) {
        mOnDeleteListener = onDeleteListener;
    }

    public void setNoteList(List<Note> noteList) {
        mNoteList = noteList;
    }

    public MyAdapter(Context context,List<Note> noteList,OnDeleteListener deleteListener){
        this.mContext = context.getApplicationContext();
        this.mNoteList = noteList;
        this.mOnDeleteListener = deleteListener;
    }

    @Override
    public NoteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_item, parent, false);
        return new NoteViewHolder(view,this);
    }

    @Override
    public void onBindViewHolder(NoteViewHolder holder, int position) {
        holder.bind(mNoteList.get(position),mOnDeleteListener);
    }

    @Override
    public int getItemCount() {
        return mNoteList.size();
    }

    public static class NoteViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView mTitleTextView;
        private TextView mTimeTextView;
        private TextView mContentTextView;
        private ImageButton mImageButton;
        private MyAdapter mMyAdapter;
        private View mView;
        private Note mNote;
        private OnDeleteListener mOnDeleteListener;

        public NoteViewHolder(View itemView,MyAdapter adapter) {
            super(itemView);
            mMyAdapter = adapter;
            mTitleTextView = itemView.findViewById(R.id.title_text_view);
            mTimeTextView = itemView.findViewById(R.id.time_text_view);
            mContentTextView = itemView.findViewById(R.id.content_text_view);
            mImageButton = itemView.findViewById(R.id.delete_button);
            mView = itemView;
            itemView.setOnClickListener(this);
        }

        public void bind(Note note,OnDeleteListener deleteListener){
            mNote = note;
            mOnDeleteListener = deleteListener;
            mImageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    NoteUtil.getsNoteUtil(mContext).deleteNote(mNote.getId());
                    if(mOnDeleteListener != null){
                        mOnDeleteListener.onDelete();
                    }
                }
            });
            if(mTitleTextView != null && mTimeTextView != null && mContentTextView != null){
                mTitleTextView.setText(note.getTitle());
                Date date = new Date(note.getModifyTime());
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String dateString = formatter.format(date);
                mTimeTextView.setText(dateString);
                mContentTextView.setText(note.getContent());
                mView.setBackgroundColor(mContext.getResources().getColor(mNote.getColor()));
            }
        }

        @Override
        public void onClick(View v) {

            Intent intent = new Intent(mContext,NoteDetailActivity.class);
            Bundle bundle = new Bundle();
            bundle.putInt("id",mNote.getId());
            bundle.putInt("color",mNote.getColor());
            bundle.putString("title",mNote.getTitle());
            bundle.putString("content",mNote.getContent());
            bundle.putLong("time",mNote.getModifyTime());
            intent.putExtra("bundle",bundle);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(intent);

        }
    }
}
