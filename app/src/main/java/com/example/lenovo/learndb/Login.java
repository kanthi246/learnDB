package com.example.lenovo.learndb;

import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.RuntimeExceptionDao;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Login extends AppCompatActivity {

    @BindView(R.id.email) EditText mEmail;

    @BindView(R.id.password) EditText mPassword;

    @BindView(R.id.login) Button mLogin;

    @BindView(R.id.register) Button mRegister;

    private DbHelper mdbHelper;
    private List<UserDetails> muserDetailsList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        mdbHelper= OpenHelperManager.getHelper(this,DbHelper.class);
        //getdata();
        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(muserDetailsList.size()==0){
                    Toast.makeText(Login.this, "Register User To Login", Toast.LENGTH_SHORT).show();
                }else{
                    if(mEmail.getText().length()==0){
                        Toast.makeText(Login.this, "Enter Email", Toast.LENGTH_SHORT).show();
                    }else if(mPassword.getText().length()==0){
                        Toast.makeText(Login.this, "Enter Password", Toast.LENGTH_SHORT).show();
                    }else{
                        for(int i=0;i<muserDetailsList.size();i++){
                            if(mEmail.getText().toString().equalsIgnoreCase(muserDetailsList.get(i).getEmail())
                                    || mPassword.getText().toString().equalsIgnoreCase(muserDetailsList.get(i).getPassword())){
                                startActivity(new Intent(Login.this,Welcome.class).
                                        putExtra("name",muserDetailsList.get(i).getName()).
                                        putExtra("image",muserDetailsList.get(i).getImagepath()));
                            }else{
                                Toast.makeText(Login.this, "No Data Found", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }
            }
        });

        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this,Register.class));
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        try {
            RuntimeExceptionDao<UserDetails,Integer> userDetailsDao = mdbHelper.getUserDetails();
            muserDetailsList=userDetailsDao.queryForAll();

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void getdata(){
        String path = Environment.getExternalStorageDirectory().toString()+"/Test";
        Log.d("File_path", "Path: "+path);
        File directory = new File(path);
        File[] files = directory.listFiles();
        Log.d("Files_length", "Size: "+ files.length);
        for (int i = 0; i < files.length; i++)
        {
            Log.d("Files_name", "FileName:" + files[i].getName());
            Log.d("Files_absolutepath", "FileAbsolutePath:" + files[i].getAbsolutePath());
        }

    }
}
