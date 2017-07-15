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

import static android.R.attr.onClick;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText mUsernameEt;
    private EditText mPasswordEt;
    private Button mLoginBt;
    private Button mRegisterBt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null){
            actionBar.hide();
        }
        initViews();
    }
    private void initViews() {
        //在布局文件中找到对应ID的控件对象
        mUsernameEt = (EditText) findViewById(R.id.username_et);
        mPasswordEt = (EditText) findViewById(R.id.password_et);
        mLoginBt = (Button) findViewById(R.id.login_bt);
        mRegisterBt = (Button) findViewById(R.id.register_bt);
        //创建监听器对象

        //给按钮对象设置监听器
        mLoginBt.setOnClickListener(this);
        mRegisterBt.setOnClickListener (this);
    }
    @Override
    public void onClick(View view){
        switch(view.getId()){
            case R.id.login_bt:
                //登录
                login();
                break;
            case R.id.register_bt:
                //跳转注册界面
                register();
                break;
        }
    }

    /**
     * 登录方法
     */
    private void login() {
        //获得用户输入的内容
        String username = mUsernameEt.getText().toString();
        String password = mPasswordEt.getText().toString();

        if(TextUtils.isEmpty(username)){
            Toast.makeText(LoginActivity.this, "用户名不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(password)){
            Toast.makeText(LoginActivity.this, "密码不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        //创建登录服务器的URL
        String url = "http://10.0.2.2:8080/cloudnotes/Login.do?username="+username+"&password="+password;
       // String url = "http://10.80.105.223/cloudnotes/Login.do";
       // String args = "username="+username+"&password="+password;
        //执行异步任务
        new LoginTask().execute(url);
        //new LoginTask().execute(url,args);
        //Toast.makeText(LoginActivity.this, "进行登录,用户名："+username+",密码："+password, Toast.LENGTH_SHORT).show();
    }
    private void register(){
        //创建Intent对象
        Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
        //启动Activity
        startActivity(intent);
    }

    //实现登陆服务器的异步任务

    class LoginTask extends AsyncTask<String,Void,String> {

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
                Toast.makeText(LoginActivity.this, "网络连接出错", Toast.LENGTH_LONG).show();
            }else {
                int userId = Integer.parseInt(result);
                if(userId == 0){
                    Toast.makeText(LoginActivity.this, "账号或密码错误", Toast.LENGTH_LONG).show();
                }else{
                    //跳转到主界面
                    int Id = Integer.parseInt(result);
                    Intent intent = new Intent(LoginActivity.this,MainActivity.class);

                    intent.putExtra("userId",Id);
                    startActivity(intent);
                    finish();
                }
            }
        }

    }
}























