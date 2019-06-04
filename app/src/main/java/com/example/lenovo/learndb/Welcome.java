package com.example.lenovo.learndb;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Welcome extends AppCompatActivity {

    @BindView(R.id.imageView) ImageView Im_profile;
    @BindView(R.id.name) TextView Tv_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        ButterKnife.bind(this);

        Tv_name.setText(getIntent().getStringExtra("name"));
        String filepath= getIntent().getStringExtra("image");

        //Bitmap mBitmap = getImageFileFromSDCard(filepath);
        Im_profile.setImageBitmap(BitmapFactory.decodeFile(new File(filepath).getAbsolutePath()));
        //Toast.makeText(this, ""+filepath, Toast.LENGTH_SHORT).show();
        //Log.e("flepath",""+filepath);

    }

    private Bitmap getImageFileFromSDCard(String filename){
        File imagefolder=new File(Environment.getExternalStorageDirectory(),"/Test");
        imagefolder.mkdirs();
        File file=new File(imagefolder,filename);
        Bitmap bitmap = null;
        File imageFile = new File(Environment.getExternalStorageDirectory() + filename);
        try {
            FileInputStream fis = new FileInputStream(file);
            bitmap = BitmapFactory.decodeStream(fis);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

}
