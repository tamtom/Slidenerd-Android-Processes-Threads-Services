package github.tamtom.threading;

import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;

import java.util.ArrayList;

public class MainActivity extends ActionBarActivity {
    private ListView mListView;
    private ProgressBar mProgressBar;
 private String[] texts = new String[]{
"String 1", "String 2", "String 3", "String 4"
         , "String 5"
         , "String 6"
         , "String 7"
         , "String 8", "String 9"
         , "String 10"
         , "String 11", "String 12", "String 13"
         , "String 14"
         , "String 15"
         , "String 16", "String 17"
         , "String 18"
         , "String 19"





 };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
       mProgressBar = (ProgressBar) findViewById(R.id.progressBar1);
        mListView = (ListView) findViewById(R.id.listv);
        mListView.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,new ArrayList<String>()));
new MyTask().execute();
    }
    class MyTask extends AsyncTask<Void,String,Void>{
        private ArrayAdapter<String> mAdapter;
        int count = 0;
        @Override
        protected void onPreExecute() {
           mAdapter = (ArrayAdapter<String>) mListView.getAdapter();
         mProgressBar.setMax(texts.length);




        }

        @Override
        protected Void doInBackground(Void... params) {
            for (String item : texts){
                publishProgress(item);
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {

                }
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            mAdapter.add(values[0]);
            count++;
           mProgressBar.setProgress(count);

        }

        @Override
        protected void onPostExecute(Void aVoid) {
mProgressBar.setVisibility(View.GONE);
        }

    }
}
