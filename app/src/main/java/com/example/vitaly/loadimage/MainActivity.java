package com.example.vitaly.loadimage;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    private ImageView ivOut;

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
    }

    private void initView() {
        ivOut = (ImageView) findViewById(R.id.ivOutActivityMain);
    }

    public void onClickLoad(View view) {
        Toast.makeText(this, "Hello", Toast.LENGTH_SHORT).show();
        /*new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        loadImage();
                    }
                }
        ).start();*/

        MyAsyncTask mat = new MyAsyncTask();
        mat.execute();

        Picasso.with(this).load("https://upload.wikimedia.org/wikipedia/commons/4/4f/Three_Sisters_Sunset.jpg").into(ivOut);
    }

    private Bitmap loadImage(){

        Bitmap image = null;
        try {
            URL imageURL = new URL("https://upload.wikimedia.org/wikipedia/commons/4/4f/Three_Sisters_Sunset.jpg");
            URLConnection connection = imageURL.openConnection();
            connection.connect();
            InputStream inputStream = connection.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(inputStream);

            image = BitmapFactory.decodeStream(bis);

            bis.close();
            inputStream.close();

            //ivOut.setImageBitmap(image);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }

    private class MyAsyncTask extends AsyncTask<Void,Integer,Integer>{

        private Bitmap image;
        private ProgressDialog progress;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress = ProgressDialog.show(MainActivity.this,"Loading","Waiting for load...");
        }

        @Override
        protected Integer doInBackground(Void... params) {

            for (int i=0; i<5; i++){
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                publishProgress(i);
            }

            image = loadImage();

            return 1;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            progress.setMessage("loaded - "+values[0].intValue());
        }

        @Override
        protected void onPostExecute(Integer aVoid) {
            super.onPostExecute(aVoid);
            progress.dismiss();
            ivOut.setImageBitmap(image);
        }
    }

}