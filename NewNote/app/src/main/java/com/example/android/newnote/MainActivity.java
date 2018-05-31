package com.example.android.newnote;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.android.newnote.database.NoteUtil;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnDeleteListener{

    private Toolbar mToolbar;
    private RecyclerView mRecyclerView;
    private MaterialSearchView mSearchView;
    private Spinner mSpinner;
    private FloatingActionButton mFloatingActionButton;
    private List<Note> mNoteList;
    private MyAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initData();
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateUI();
    }

    private void initData(){
        mNoteList = NoteUtil.getsNoteUtil(this).getNotes();
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mFloatingActionButton = (FloatingActionButton) findViewById(R.id.add_button);
        mSpinner = (Spinner) findViewById(R.id.spinner);
        Log.i("test","current size "+ mNoteList.size());
        mRecyclerView.setLayoutManager(new GridLayoutManager(this,3));
        mRecyclerView.addItemDecoration(new SpaceItemDecoration(20));
        mAdapter = new MyAdapter(this,mNoteList,this);
        mAdapter.setOnDeleteListener(this);
        mRecyclerView.setAdapter(mAdapter);

    }

    private void initView(){
        setSupportActionBar(mToolbar);
        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,NoteDetailActivity.class);
                startActivity(intent);
            }
        });

        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.i("test","current "+ position);
                switch (position){
                    case 0:
                        updateUI("modifytime");
                        break;
                    case 1:
                        updateUI("color");
                        break;
                    case 2:
                        updateUI("title");
                        break;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        mSpinner.setAdapter(new MySpinnerAdapter(this,getResources().getTextArray(R.array.order)));
        initSearchView();

    }

    private void updateUI(){
        List<Note> noteList = NoteUtil.getsNoteUtil(this).getNotes();
        mNoteList = noteList;
        if(mAdapter == null){
            mAdapter = new MyAdapter(this,noteList,this);
            mRecyclerView.setAdapter(mAdapter);
        }else {
            if(mAdapter.getItemCount() != noteList.size()){
                mAdapter = new MyAdapter(this,noteList,this);
                mRecyclerView.setAdapter(mAdapter);
            }else {
                mAdapter.setNoteList(noteList);
                mAdapter.notifyDataSetChanged();
            }
        }
        mAdapter.notifyDataSetChanged();
    }

    private void updateUI(String order){
        List<Note> noteList = NoteUtil.getsNoteUtil(this).orderBy(order);
        mNoteList = noteList;
        if(mAdapter == null){
            mAdapter = new MyAdapter(this,noteList,this);
            mRecyclerView.setAdapter(mAdapter);
        }else {
            if(mAdapter.getItemCount() != noteList.size()){
                mAdapter = new MyAdapter(this,noteList,this);
                mRecyclerView.setAdapter(mAdapter);
            }else {
                mAdapter.setNoteList(noteList);
                mAdapter.notifyDataSetChanged();
            }
        }
        mAdapter.notifyDataSetChanged();
    }
    /**
     * 初始化搜索框
     */
    private void initSearchView(){
        mSearchView = (MaterialSearchView) findViewById(R.id.search_view);
        mSearchView.setVoiceSearch(false);
        mSearchView.setCursorDrawable(R.drawable.custom_cursor);
        mSearchView.setEllipsize(true);

        mSearchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                List<Note> queryList = NoteUtil.getsNoteUtil(MainActivity.this).search(newText);
                mAdapter.setNoteList(queryList);
                mAdapter.notifyDataSetChanged();
                return true;
            }
        });

        mSearchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {

            }

            @Override
            public void onSearchViewClosed() {
                updateUI();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_action, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        mSearchView.setMenuItem(item);
        return true;
    }

    @Override
    public void onDelete() {
        updateUI();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_backup:
                //数据备份
                try {
                    File dbFile = new File("/data/data/com.example.android.newnote/databases/"+"noteDB.db");
                    File file = new File(Environment.getExternalStorageDirectory(), "noteDB.db");
                    if (!file.exists()) {
                        try {
                            if(ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!=
                                    PackageManager.PERMISSION_GRANTED){
                                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
                            }
                            if(ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)!=
                                    PackageManager.PERMISSION_GRANTED){
                                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
                            }
                            file.createNewFile();

                        } catch (IOException e) {

                            e.printStackTrace();
                        }
                    }else {

                    }
                    FileInputStream is = new FileInputStream(dbFile);
                    FileOutputStream out = new FileOutputStream(file);
                    byte[] buff = new byte[1024];
                    int n = 0;
                    while ((n = is.read(buff)) > 0) {
                        Log.e("tag", "len=" + n);
                        out.write(buff, 0, n);
                    }
                    is.close();
                    out.close();
                }catch (Exception e){
                    Log.i("test","catch " + e.toString());
                }

                return true;
            case R.id.action_backdata:
                //数据恢复
                NoteUtil.getsNoteUtil(this).returnData();
                updateUI();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
