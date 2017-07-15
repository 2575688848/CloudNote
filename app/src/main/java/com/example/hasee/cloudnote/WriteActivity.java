package com.example.hasee.cloudnote;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.hasee.cloudnote.com.qianfeng.cloudnote.util.HttpUtil;

import java.text.SimpleDateFormat;
import java.util.Date;

public class WriteActivity extends AppCompatActivity {

    private int id;
    private EditText title;
    private EditText content;
    private String time;
private Button add;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write);
        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null)
        {
            actionBar.hide();
        }
        title=(EditText)findViewById(R.id.title_w);
        content=(EditText)findViewById(R.id.content_w);
        add=(Button)findViewById(R.id.add);
        Intent intent =getIntent();
        id=intent.getIntExtra("userId",0);
        SimpleDateFormat sdf=new SimpleDateFormat("yyy/MM/dd HH:mm");
        time=sdf.format(new Date());

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              String title1=title.getText().toString();
                String content1=content.getText().toString();
                String url="http://10.0.2.2:8080/cloudnotes/AddNote.do?title="+title1+"&content="+content1+"&userId="+id+"&createTime="+time;
            new AddNoteTask().execute(url);
            }
        });
    }

    class AddNoteTask extends AsyncTask<String,Void,String> {

        @Override
        protected String doInBackground(String... params) {
            return HttpUtil.get(params[0]);
        }
        @Override
        protected void onPostExecute(String result) {
            if(TextUtils.isEmpty(result)){
                Toast.makeText(WriteActivity.this, "网络连接出错", Toast.LENGTH_LONG).show();
            }else if(result=="false"){
                Toast.makeText(WriteActivity.this,"添加失败",Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(WriteActivity.this,"添加成功",Toast.LENGTH_SHORT).show();
                Intent intent=new Intent();


                setResult(RESULT_OK,intent);

                finish();

            }
        }
    }

    }
