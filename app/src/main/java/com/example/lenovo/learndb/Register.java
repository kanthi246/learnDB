package com.example.lenovo.learndb;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.RuntimeExceptionDao;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Register extends AppCompatActivity {

    @BindView(R.id.name) EditText eName;

    @BindView(R.id.email) EditText eEmail;

    @BindView(R.id.password) EditText ePassword;

    @BindView(R.id.c_password) EditText eCPassword;

    @BindView(R.id.rb_gender) RadioGroup rGroup;

    @BindView(R.id.captureimage) Button bCaptureImage;

    @BindView(R.id.register) Button mSave;

    private DbHelper mdbHelper;
    public static final int REQUEST_IMAGE_CAPTURE=1;
    private String imagename=null;

    private Uri picUri;
    private File picFile;
    public static String imagePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        mdbHelper = OpenHelperManager.getHelper(this, DbHelper.class);
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        imagePath = CompressImageHandler.createFile();
        bCaptureImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(Register.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(Register.this,
                            new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE},
                            1);
                } else {
                    //Intent in = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    //startActivityForResult(in,REQUEST_IMAGE_CAPTURE);
                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    picFile = new File(imagePath);
                    picUri = Uri.fromFile(picFile);
                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, picUri);
                    startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE);
                }
            }
        });



        mSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(eName.getText().length()==0){
                    Toast.makeText(Register.this, "Enter Name", Toast.LENGTH_SHORT).show();
                }else if(eEmail.getText().length()==0){
                    Toast.makeText(Register.this, "Enter Email", Toast.LENGTH_SHORT).show();
                }else if(ePassword.getText().length()==0){
                    Toast.makeText(Register.this, "Enter Password", Toast.LENGTH_SHORT).show();
                }else if(eCPassword.getText().length()==0){
                    Toast.makeText(Register.this, "Enter Confirm Password", Toast.LENGTH_SHORT).show();
                }else if(rGroup.getCheckedRadioButtonId()==-1){
                    Toast.makeText(Register.this, "Please Select Gender", Toast.LENGTH_SHORT).show();
                }else if(!ePassword.getText().toString().trim().equalsIgnoreCase(eCPassword.getText().toString().trim())){
                    Toast.makeText(Register.this, "Password Mismatch", Toast.LENGTH_SHORT).show();
                }else if(imagePath==null){
                    Toast.makeText(Register.this, "please capture image", Toast.LENGTH_SHORT).show();
                }else{
                    UserDetails details=new UserDetails();
                    details.setName(eName.getText().toString());
                    details.setEmail(eEmail.getText().toString());
                    details.setPassword(ePassword.getText().toString());
                    details.setC_password(eCPassword.getText().toString());
                    details.setGender(String.valueOf(rGroup.getCheckedRadioButtonId()));
                    details.setImagepath(imagePath);
                    try {
                        RuntimeExceptionDao<UserDetails,Integer> userDetailsDao = mdbHelper.getUserDetails();
                        userDetailsDao.create(details);
                        Toast.makeText(Register.this, "Done", Toast.LENGTH_SHORT).show();
                        finish();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==REQUEST_IMAGE_CAPTURE && resultCode==RESULT_OK){
            //Bitmap bitmap= (Bitmap) data.getExtras().get("data");
            //Uri selectedimage= data.getData();
            imagename = "photo"+getDateTime()+".jpg";
            //storeCameraPhotoInSDCard(bitmap,imagename);
            //compressImage(selectedimage);
            new ImageCompression().execute(imagePath);
        }

    }

    private void storeCameraPhotoInSDCard(Bitmap bitmap,String imagename){
        File outputFile = new File(Environment.getExternalStorageDirectory(), "Test");
        outputFile.mkdirs();
        File file=new File(outputFile,imagename);
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.sss'Z'", Locale.US);
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        java.util.Date date = new java.util.Date();
        return dateFormat.format(date);
    }

    public class ImageCompression extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            if (strings.length == 0 || strings[0] == null)
                return null;
            return CompressImageHandler.compressImage(strings[0]);
        }
        protected void onPostExecute(String imagePath) {
            //captureImage.setImageBitmap(BitmapFactory.decodeFile(new File(imagePath).getAbsolutePath()));
            //Toast.makeText(Register.this,"Compress image is saved in your sd card(/ImagecompressDemo/Images)",Toast.LENGTH_LONG).show();
        }
    }
}
