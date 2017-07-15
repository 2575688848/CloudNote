package com.example.hasee.cloudnote;

import android.content.Intent;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hasee.cloudnote.com.qianfeng.cloudnote.util.HttpUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;



public class ContentActivity extends AppCompatActivity {

    public String mTime;
    private EditText title;
    private EditText content;
    private Button save;
   private int userid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);
        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null){
            actionBar.hide();
        }
        title=(EditText) findViewById(R.id.title_te);
        content=(EditText) findViewById(R.id.content_te);
        Intent intent=getIntent();
        ArrayList<String> lists=intent.getStringArrayListExtra("list");
        //Log.d("wqw",lists.get(0));
        title.setText(lists.get(0));
        content.setText(" "+lists.get(1));
        final int id=Integer.parseInt(lists.get(2));
        userid=Integer.parseInt(lists.get(3));
        save=(Button)findViewById(R.id.save);
        /**
         * huoqudangqianshijian
         */
        SimpleDateFormat sdf=new SimpleDateFormat("yyy/MM/dd HH:mm");
        mTime=sdf.format(new Date());


        Log.d("test",mTime);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              updatenote(id);

            }
        });
    }
    private void updatenote(int id){
        title=(EditText) findViewById(R.id.title_te);
        content=(EditText) findViewById(R.id.content_te);
        String titl=title.getText().toString();
        String conten=content.getText().toString();
        String url="http://10.0.2.2:8080/cloudnotes/UpdateNote.do?title="+titl+"&content="+conten+"&id="+id+"&time="+mTime;
        Log.d("ContentActivity",mTime);
        new updateTask().execute(url);
    }

    class updateTask extends AsyncTask<String,Void,String> {

        @Override
        protected String doInBackground(String... params) {
            //连接服务器，并返回数据
            return HttpUtil.get(params[0]);
            // return HttpUtil.post(params[0], params[1]);
        }

        @Override
        protected void onPostExecute(String result) {
            //是否修改成功
            if(TextUtils.isEmpty(result)){
                Toast.makeText(ContentActivity.this, "网络连接出错", Toast.LENGTH_LONG).show();
            }else if(result=="false"){
                    Toast.makeText(ContentActivity.this,"更新失败",Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(ContentActivity.this,"更新成功",Toast.LENGTH_SHORT).show();
                Intent intent=new Intent();

                intent.putExtra("data",userid);
                setResult(RESULT_OK,intent);

                finish();

            }
        }

    }
}
