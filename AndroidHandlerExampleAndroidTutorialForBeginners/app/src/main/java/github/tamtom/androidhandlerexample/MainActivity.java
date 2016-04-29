package github.tamtom.androidhandlerexample;


import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;



public class MainActivity extends AppCompatActivity {
    Thread thread;
    Handler handler;
    ProgressBar mProgressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        thread = new Thread(new MyThread());

        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
          mProgressBar.setProgress(msg.arg1);
                
            }
        };
        thread.start();

    }
    class MyThread implements Runnable{

        @Override
        public void run() {


            for (int i = 0; i < 100; i++) {
                Message message = Message.obtain();
            message.arg1 = i;
                handler.sendMessage(message);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
