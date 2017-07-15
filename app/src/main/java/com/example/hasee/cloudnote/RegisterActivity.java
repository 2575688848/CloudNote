package com.example.hasee.cloudnote;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.hasee.cloudnote.com.qianfeng.cloudnote.util.HttpUtil;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {


    private EditText mUsernameEt;
    private EditText mPasswordEt;
    private EditText mRepasswordEt;
    private Button mRegisterBt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initViews();
    }

    public void initViews() {
        mUsernameEt = (EditText) findViewById(R.id.username_et);
        mPasswordEt = (EditText) findViewById(R.id.password_et);
        mRepasswordEt = (EditText) findViewById(R.id.repassword_et);
        mRegisterBt = (Button) findViewById(R.id.register_bt);
        mRegisterBt.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.register_bt:
                register();
                break;
        }
    }

    public void register() {
        String username = mUsernameEt.getText().toString();
        String password = mPasswordEt.getText().toString();
        String repassword = mRepasswordEt.getText().toString();

        if (repassword.equals(password)&&(!TextUtils.isEmpty(repassword))&&(!TextUtils.isEmpty(password))) {
            String url = "http://10.0.2.2:8080/cloudnotes/register.do?username=" + username + "&password=" + password;

            //执行异步任务
            new RegisterTask().execute(url);
        } if (!repassword.equals(password)){
            Toast.makeText(RegisterActivity.this, "密码输入不匹配，请重新输入", Toast.LENGTH_SHORT).show();
        }if(TextUtils.isEmpty(username)||TextUtils.isEmpty(password)||TextUtils.isEmpty(repassword)) {
            Toast.makeText(RegisterActivity.this, "输入不能为空", Toast.LENGTH_SHORT).show();
        }
    }

    class RegisterTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            return HttpUtil.get(params[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            if(result=="false"){
                Toast.makeText(RegisterActivity.this,"注册失败，请重新注册",Toast.LENGTH_SHORT).show();
            }
            else if(TextUtils.isEmpty(result)){
                Toast.makeText(RegisterActivity.this, "网络连接出错", Toast.LENGTH_LONG).show();
            }
            else {
                Toast.makeText(RegisterActivity.this,"恭喜你，注册成功",Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(RegisterActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();
            }
        }
    }
}