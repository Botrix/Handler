package com.example.edl.downloadimage;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import com.example.edl.downloadimage.utils.HttpUtils;

public class MainActivity extends AppCompatActivity {
    public final static String IMAGEURL = "http://image.coolapk.com/discovery/2016/0411/257251_1460354674_0053.png.m.jpg";
    public final static String TAG = "debug";
    private ImageView image = null;
    private downloadImageTask task;
    private boolean _isExe = false;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initWidgets();
        task = new downloadImageTask(); //要在主线程中创建AsyncTask的实例
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (_isExe) {
            task.cancel(true); //取消操作
        }
    }

    private void initWidgets() {

        //进度对话框
        dialog = new ProgressDialog(this);
        dialog.setTitle("提示信息");
        dialog.setMessage("正在下载，请稍后...");
        dialog.setCancelable(false);

        image = (ImageView) findViewById(R.id.img);

        Button btn = (Button) findViewById(R.id.download_btn);
        if (btn != null) {
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!_isExe) {
                        task.execute(IMAGEURL); //启动AsyncTask，只执行一次
                        System.out.println("[AsyncTask.execute() is running>>>>>>>]");
                        _isExe = true;
                    }

                }
            });
        }
    }

   /* public void onClick(View v) {
        switch (v.getId()) {
            case R.id.download_btn:
                if (!_isExe) {
                    task.execute("http://f.hiphotos.baidu.com/imaged2cd3.jpg"); // 执行异步操作
                    _isExe = true;
                }
                break;

            default:
                break;
        }
    }
*/
    /** AsyncTask<Params, Progress, Result>
     * AsyncTask可以使UI线程的使用变得恰当而又容易。这个类可以执行后台操作，
     * 并把结果发布到UI线程，而不用处理thread/handler
     * Params 启动任务执行的输入参数，比如HTTP请求的URL,传给execute()和doInBackground()
     * Progress (可选)更新任务进度，传给onProgressUpdate()
     * Result doInBackground()的返回值
     *
     * 注意：该方法不适用于重复且长时间运行的任务
     * */
   class downloadImageTask extends AsyncTask<String, Void, Bitmap> {

        /**
         * 任务开始前的准备工作，比如在UI中显示一个ProgressBar
         */
        @Override
       protected void onPreExecute() {
            Log.i(TAG, "[AsyncTask]->>>>>onPreExecute()");
            dialog.show();
            super.onPreExecute();
       }

        //必须重写该方法，该方法执行完后把结果传给onPostExecute
       @Override
       protected Bitmap doInBackground(String... params) {
           System.out.println("[downloadImageTask->]doInBackground " + params[0]);
           return HttpUtils.getNetWorkBitmap(params[0]);
       }

       @Override
       protected void onPostExecute(Bitmap bitmap) {
           image.setImageBitmap(bitmap);
           dialog.dismiss();
       }
   }

/*
    class downloadImageTask extends AsyncTask<String, Integer, Boolean> {

       @Override
       protected void onPreExecute() {
           dialog.show(); //显示进度对话框
       }

       @Override
        protected Boolean doInBackground(String... params) {
            System.out.println("[downloadImageTask->]doInBackground " + params[0]);
            mDownloadImage = HttpUtils.getNetWorkBitmap(params[0]);
            return true;
        }

        //更新UI
        @Override
        protected void onPostExecute(Boolean result) {
            image.setImageBitmap(mDownloadImage);
            dialog.dismiss();
            System.out.println("result = " + result);
            super.onPostExecute(result);
        }

        // 更新进度回调
        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

    }
    */
}

