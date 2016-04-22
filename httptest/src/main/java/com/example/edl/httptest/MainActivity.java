package com.example.edl.httptest;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "downloadWebpage";
    private static final String URL = "http://www.baidu.com";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button = (Button) findViewById(R.id.button);
        if (button != null) {
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new downloadWebpage().execute(URL);
                }
            });
        }

    }


    /** AsyncTask<Params, Progress, Result>
     * AsyncTask可以使UI线程的使用变得恰当而又容易。这个类可以执行后台操作，
     * 并把结果发布到UI线程，而不用处理thread/handler
     * Params 启动任务执行的输入参数，比如HTTP请求的URL,传给execute()和doInBackground()
     * Progress (可选)更新任务进度，传给onProgressUpdate()
     * Result doInBackground()的返回值
     *
     * 注意：该方法不适用于重复且长时间运行的任务
     * */
    class downloadWebpage extends AsyncTask<String, Void, String> {

        /**
         * 任务开始前的准备工作，比如在UI中显示一个ProgressBar
         */
        @Override
        protected void onPreExecute() {
            Log.i(TAG, "[AsyncTask]->>>>>onPreExecute()");
            super.onPreExecute();
        }

        //必须重写该方法，该方法执行完后把结果传给onPostExecute
        @Override
        protected String doInBackground(String... params) {
            System.out.println("[downloadWebpage->]doInBackground " + params[0]);
            try {
                return HttpUtils.getURL(params[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return "No Webpage";
        }

        @Override
        protected void onPostExecute(String string) {
            System.out.println(string);
            System.out.println("onPostExecute ---->");
        }
    }


}

