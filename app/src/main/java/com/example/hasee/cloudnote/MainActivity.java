package com.example.hasee.cloudnote;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;

import android.preference.DialogPreference;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.cloudnote.model.Note;
import com.cloudnote.model.NoteAdapter;
import com.example.hasee.cloudnote.com.qianfeng.cloudnote.util.HttpUtil;

import java.util.ArrayList;
import java.util.List;


/**
 *获得登陆界面传来的Id;
 */
public class MainActivity extends AppCompatActivity {

    private Button write;
    private SearchView search;

    private int mUserId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null){
            actionBar.hide();
        }
        //获得登陆界面的ID;
        Intent intent =getIntent();
        mUserId=intent.getIntExtra("userId",0);
        Log.d("test","userId--->"+mUserId);
        String url="http://10.0.2.2:8080/cloudnotes/FindNotes.do?id="+mUserId;
        new FindNotetask().execute(url);

        search=(SearchView)findViewById(R.id.search) ;
        write=(Button)findViewById(R.id.write);
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                String url="http://10.0.2.2:8080/cloudnotes/Search.do?id="+mUserId+"&keywords="+query;
                new FindNotetask().execute(url);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(TextUtils.isEmpty(newText)){
                    String url="http://10.0.2.2:8080/cloudnotes/FindNotes.do?id="+mUserId;
                    new FindNotetask().execute(url);
                }
                return false;
            }
        });
       write.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent intent=new Intent(MainActivity.this,WriteActivity.class);
               intent.putExtra("userId",mUserId);
               startActivityForResult(intent,1);
           }
       });
    }

    class FindNotetask extends AsyncTask<String,Void,String>{

        @Override
        protected String doInBackground(String... params) {
            return HttpUtil.get(params[0]);

        }
        @Override
        protected void onPostExecute(String result) {
            if(!TextUtils.isEmpty(result)){
                final List<Note> notes = JSON.parseArray(result, Note.class);


                RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycle);
                StaggeredGridLayoutManager layoutManager = new
                        StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                final NoteAdapter adapter = new NoteAdapter(notes);
                recyclerView.setAdapter(adapter);
                adapter.setRecyclerViewOnItemClickListener(new NoteAdapter.RecyclerViewOnItemClickListener() {
                    @Override
                    public void onItemClickListener(View view, int position) {
                      Note note=notes.get(position);

                        ArrayList<String> lists=new ArrayList<>();
                        lists.add(note.getTitle());
                        lists.add(note.getContent());
                        lists.add(Integer.toString(note.getId()));
                        lists.add(Integer.toString(mUserId));
                        Intent intent = new Intent(MainActivity.this,ContentActivity.class);
                        intent.putExtra("list",lists);
                        startActivityForResult(intent,1);
                    }
                });
                adapter.setOnItemLongClickListener(new NoteAdapter.RecyclerViewOnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClickListener(View view, final int position) {
                        AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
                        builder.setTitle("删除笔记").setNegativeButton("NO",null).setPositiveButton("YES", new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                adapter.notifyItemRemoved(position);

                                Toast.makeText(MainActivity.this,"删除成功",Toast.LENGTH_SHORT).show();

                                 int id=notes.get(position).getId();
                                notes.remove(position);
                                adapter.notifyDataSetChanged();
                                String url = "http://10.0.2.2:8080/cloudnotes/DeleteNote.do?id="+id;
                                new deletenoteTask().execute(url);

                            }
                            }).show();
                        return true;
                    }
                });

            }
        }
    }
    class deletenoteTask extends AsyncTask<String,Void,String> {

        @Override
        protected String doInBackground(String... params) {
            //连接服务器，并返回数据
            return HttpUtil.get(params[0]);
            // return HttpUtil.post(params[0], params[1]);
        }

        @Override
        protected void onPostExecute(String result) {
            //是否登录成功
            if(TextUtils.isEmpty(result)){
                Toast.makeText(MainActivity.this, "网络连接出错", Toast.LENGTH_LONG).show();
            }
                if(result=="false"){
                    Toast.makeText(MainActivity.this,"删除不成功",Toast.LENGTH_SHORT).show();
            }
            if(result=="true"){
                Toast.makeText(MainActivity.this,"删除成功",Toast.LENGTH_SHORT).show();

            }
        }

    }
    @Override
    protected  void onActivityResult(int requesCode,int resultCode,Intent data){
      switch (requesCode){
          case 1:if(resultCode==RESULT_OK){
              //String id=data.getStringExtra("data");
              String url1="http://10.0.2.2:8080/cloudnotes/FindNotes.do?id="+mUserId;
              new FindNotetask().execute(url1);
          }
      }
    }
}
