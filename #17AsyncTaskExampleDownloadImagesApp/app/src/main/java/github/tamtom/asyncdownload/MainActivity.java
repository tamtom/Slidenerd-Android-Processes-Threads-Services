package github.tamtom.asyncdownload;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity  implements AdapterView.OnItemClickListener{
    private EditText mEditText;
    private ListView mListView;

    private ProgressBar mProgressBar;
    private String[] listOfImages;



    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mEditText = (EditText) findViewById(R.id.downloadURL);

        mListView = (ListView) findViewById(R.id.urlList);

        listOfImages = getResources().getStringArray(R.array.imageUrls);
        mProgressBar = (ProgressBar) findViewById(R.id.downloadProgress);
        mListView.setOnItemClickListener(this);



    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        mEditText.setText(listOfImages[position]);
    }

    public void downloadImage(View view) {
        new MyTask().execute(mEditText.getText().toString());
    }
    class MyTask extends AsyncTask<String,Integer,Boolean>{
        int contentLength=-1;
        @Override
        protected void onPreExecute() {
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Boolean doInBackground(String... params) {
            URL downloadUrl = null;

            int counter =0;
            boolean success = false;
            try {
                downloadUrl = new URL(params[0]);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            HttpsURLConnection connection = null;
            try {
                connection = (HttpsURLConnection) downloadUrl.openConnection();
                contentLength = connection.getContentLength();
            } catch (IOException e) {
                e.printStackTrace();
            }
            InputStream inputStream = null;
            try {
                inputStream = connection.getInputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
            int read = -1;
            File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath()+"/"+ Uri.parse(params[0]).getLastPathSegment());
            FileOutputStream outputStream = null;
            try {
                outputStream = new FileOutputStream(file);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            Log.d("the path",file.getAbsolutePath());
            byte[] buffer = new byte[1024];
            try {
                while ((read=inputStream.read(buffer))!=-1){
                    outputStream.write(buffer,0,read);
                    counter+=read;
                    publishProgress(counter);

                }
                success = true;
            } catch (IOException e) {
                e.printStackTrace();
            }
            connection.disconnect();
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return success;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            mProgressBar.setProgress((int)((double)values[0]/contentLength*100));
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            mProgressBar.setVisibility(View.GONE);
            if(aBoolean)
                Toast.makeText(MainActivity.this,"Success",Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(MainActivity.this,"Fail",Toast.LENGTH_SHORT).show();

        }
    }
}

