package com.example.myimage;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ArrayList<String> UNCC = new ArrayList<>();
    ArrayList<String> ANDROID = new ArrayList<>();
    ArrayList<String> WINTER = new ArrayList<>();
    ArrayList<String> AURORA = new ArrayList<>();
    ArrayList<String> WONDERS = new ArrayList<>();
    EditText key;
    public static ImageView image;
    String pattern;
    int i=-1;
    public static final String photoListURL = "http://dev.theappsdr.com/apis/spring_2016/inclass5/URLs.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        key = (EditText) findViewById(R.id.editText);
        image = (ImageView) findViewById(R.id.imageView);

    }

    public void goClicked(View v)
    {

        if (isConnected())
        {
            pattern = key.getText().toString();
            new GetPhotosID().execute(photoListURL);
            
            if(pattern.equals("UNCC"))
            {
                getpics(UNCC);
            }
            else if(pattern.equals("WINTER"))
            {
                getpics(WINTER);
            }
            else if(pattern.equals("ANDROID"))
            {
                getpics(ANDROID);
            }
            else if(pattern.equals("WONDERS"))
            {
                getpics(WONDERS);
            }
            else if(pattern.equals("AURORA"))
            {
                getpics(AURORA);
            }
            else
            {
                Toast.makeText(MainActivity.this, "Wrong keyword", Toast.LENGTH_SHORT).show();
                image.setImageResource(0);

            }
        }
        else
        {
            Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }



    public void getpics(final ArrayList<String> a)
    {


        ImageButton next = (ImageButton) findViewById(R.id.imageButton2);
        ImageButton previous = (ImageButton) findViewById(R.id.imageButton);



        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (i == a.size() - 1) {
                    i = 0;
                    new GetPhotos().execute(a.get(i));
                } else
                    new GetPhotos().execute(a.get(++i));
                Log.d("I", i + "");
            }
        });
        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (i == 0) {
                    i = a.size() - 1;


                } else
                    new GetPhotos().execute(a.get(--i));
                Log.d("I", i + "");
            }
        });
    }



    public boolean isConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        }
        return false;
    }


    public class GetPhotosID extends AsyncTask<String, Void, String> {


        StringBuffer sb = new StringBuffer();


        HttpURLConnection con = null;
        int i = 0;

        @Override
        protected String doInBackground(String... params) {
            {
                try {
                    URL url = new URL(params[0]);
                    con = (HttpURLConnection) url.openConnection();
                    con.setRequestMethod("GET");
                    BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    String input = "";
                    while ((input = br.readLine()) != null)
                    {

                        sb.append(input);
                    }
                } catch (Exception e) {
                    Log.d("URL Exception", e.toString());
                } finally {
                    con.disconnect();
                }
                return sb.toString();
            }
        }


        protected void onPostExecute(String s) {

            final String[] photoID = s.split(";");
            for (String m : photoID) {
                int n = m.indexOf(",");
                String token1 = m.substring(0, n);
                if (token1.equals("UNCC")) {
                    UNCC.add(m.substring(n + 1, m.length()));
                } else if (token1.equals("Android")) {
                    ANDROID.add(m.substring(n + 1, m.length()));
                } else if (token1.equals("winter")) {
                    WINTER.add(m.substring(n + 1, m.length()));
                } else if (token1.equals("aurora")) {
                    AURORA.add(m.substring(n + 1, m.length()));
                } else if (token1.equals("wonders")) {
                    WONDERS.add(m.substring(n + 1, m.length()));
                }

            }



             /*   next.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (i == photoID.length - 1) {
                            i = 0;
                            new GetPhotos().execute(photoID[i]);
                        } else
                            new GetPhotos().execute(photoID[++i]);
                        Log.d("I", i + "");
                    }
                });
                previous.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (i == 0) {
                            i = photoID.length - 1;
                            new GetPhotos().execute(photoID[i]);
                        } else
                            new GetPhotos().execute(photoID[--i]);
                        Log.d("I", i + "");
                    }
                });*/
        }

    }
}
