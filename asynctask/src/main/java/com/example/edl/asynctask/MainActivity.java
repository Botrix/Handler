package com.example.edl.asynctask;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ProgressBar;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "AsyncTask";
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBar = (ProgressBar) findViewById(R.id.bar);

        //new CustomAsyncTask().execute("First parameter", "Second parameter", "Etc.");
        new CustomAsyncTask().execute(1, 5, 10, 15, 20, 25, 50, 75, 100);
    }


    class CustomAsyncTask extends AsyncTask<Integer, Integer, Void> {

        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Integer... params) {
            /*
            for (String parameter : params) {
                Log.i(TAG, "Received parameter: " + parameter);
            }
            */

            for (Integer progress : params) {
                publishProgress(progress);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            int progress = values[0];
            progressBar.setProgress(progress);
        }
    }
}

