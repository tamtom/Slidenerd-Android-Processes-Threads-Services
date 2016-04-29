package github.tamtom.downloadimage;

import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private EditText mEditText;
    private ListView mListView;
    private Button mButton;
    private ProgressBar mProgressBar;
    private String[] listOfImages;
    private LinearLayout loadingSection;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mEditText = (EditText) findViewById(R.id.downloadURL);
        mButton = (Button) findViewById(R.id.downloadImage);
        mListView = (ListView) findViewById(R.id.urlList);

        listOfImages = getResources().getStringArray(R.array.imageUrls);
        mProgressBar = (ProgressBar) findViewById(R.id.downloadProgress);
        mListView.setOnItemClickListener(this);
        loadingSection = (LinearLayout) findViewById(R.id.linear);
        handler = new Handler();
    }

    public void downloadImage(View view) {
     String url =   mEditText.getText().toString();
        Thread thread = new Thread(new MyThread(url));
        thread.start();
    }
public void downloadImageUsingThread(String url) throws IOException {
    URL downloadUrl = new URL(url);
    HttpsURLConnection connection = (HttpsURLConnection) downloadUrl.openConnection();
    InputStream inputStream = connection.getInputStream();
    int read = -1;
    File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath()+"/"+ Uri.parse(url).getLastPathSegment());
    FileOutputStream outputStream = new FileOutputStream(file);
Log.d("the path",file.getAbsolutePath());
    byte[] buffer = new byte[1024];
    while ((read=inputStream.read(buffer))!=-1){
        outputStream.write(buffer,0,read);

    }
connection.disconnect();
    inputStream.close();
    outputStream.close();
    this.runOnUiThread(new Runnable() {
        @Override
        public  void run() {
            Log.d("finish","dd");
            loadingSection.setVisibility(View.GONE);
        }
    });
    /*
     this code for video

               #13 Android Handler Example: Post Runnable Messages
     */

   /* handler.post(new Runnable() {
        @Override
        public void run() {
            loadingSection.setVisibility(View.GONE);
        }
    });*/
}
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        mEditText.setText(listOfImages[position]);
    }
    class MyThread implements Runnable{
       private String url;
        public MyThread(String url) {
        this.url = url;
        }

        @Override
        public  void run() {
            try {
                /*
                 this code for video

               #13 Android Handler Example: Post Runnable Messages

               */

                /*

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                       loadingSection.setVisibility(View.VISIBLE);
      }
              });
*/
                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public  void run() {
                        Log.d("downloading","dd");
                        loadingSection.setVisibility(View.VISIBLE);
                    }
                });
                downloadImageUsingThread(url);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

