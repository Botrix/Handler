package com.example.edl.progressbar;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    public final static String IMAGEURL = "http://image.coolapk.com/discovery/2016/0411/257251_1460354674_0053.png.m.jpg";
    private ProgressBar bar;
    private TextView text;
    private Bitmap mDownloadImage;
    private ImageView image;
    private Button btnStart;
    private Button btnStop;
    private Button btn;
    private Button pic_btn;
    private ProgressDialog dialog;
    public final static int PROGRESS = 1;
    public final static int DOWNLOAD = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initWidgets();
    }

    private void initWidgets() {
        bar = (ProgressBar) findViewById(R.id.bar);
        text = (TextView) findViewById(R.id.text);
        image = (ImageView) findViewById(R.id.img);

        btnStart = (Button) findViewById(R.id.btnStart);
        btnStop = (Button) findViewById(R.id.btnStop);
        btn = (Button) findViewById(R.id.download_btn);
        pic_btn = (Button) findViewById(R.id.pic_btn);

        btnStart.setOnClickListener(this);
        btnStop.setOnClickListener(this);
        btn.setOnClickListener(this);
        pic_btn.setOnClickListener(this);

        //进度对话框
        dialog = new ProgressDialog(this);
        dialog.setTitle("提示信息");
        dialog.setMessage("正在下载，请稍后...");
        dialog.setCancelable(false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnStart: //Start按钮
                bar.setVisibility(View.VISIBLE);
                //将线程加入到handler的线程队列中，如果该Handler被触发就会执行其消息队列中的线程
                updateBarHandler.post(updateThread);
                break;
            case R.id.btnStop:  //Stop按钮
                //将接口从线程队列中移除
                updateBarHandler.removeCallbacks(updateThread);
                break;
            case R.id.download_btn: //下载图片按钮
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        //下载图片
                        mDownloadImage = HttpUtils.getNetWorkBitmap(IMAGEURL);
                        Log.i("显示图片", "下载图片>>>>>>");
                        Message msg = updateBarHandler.obtainMessage();
                        msg.what = DOWNLOAD;
                        msg.obj =mDownloadImage;
                       /* try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }*/
                        updateBarHandler.sendMessage(msg);
                    }
                }).start();
                dialog.show();
                break;
            case R.id.pic_btn:  //Picasso下载
                String imageUri = "http://static-jkxy.qiniudn.com/eoeandroid%2Fferweima.jpg";
                ImageView ivBasicImage = (ImageView) findViewById(R.id.img);
                dialog.show();
                Picasso.with(getApplicationContext()).load(imageUri).into(ivBasicImage);
                dialog.dismiss();
                System.out.println("[Picasso下载正在执行-->>>>>>>]");
                break;
            default:
                break;
        }
    }

    //创建一个handler，内部完成处理消息方法
    private Handler updateBarHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case PROGRESS:
                    //在TextView中显示进度条的百分比
                    //String result = Integer.toString(msg.arg1);
                    text.setText(String.format("%s%%", msg.arg1));

                    bar.setProgress(msg.arg1);
                    Bundle bundle = msg.getData();
                    //把Runnable对象发送到消息队列
                    updateBarHandler.post(updateThread);
                    System.out.println("[Bundle test---->" + bundle.getString("test"));
                    break;
                case DOWNLOAD:
                    //dialog.show();
                    image.setImageBitmap((Bitmap) msg.obj); //显示图片
                    dialog.dismiss();
                    Log.i("下载显示图片", "显示图片>>>>>>");
                    break;
                default:
                    break;
            }

        }
    };

    Runnable updateThread = new Runnable() {
        int i = 0;
        //将要执行的操作写在线程对象的run方法当中
        //Runnable的run()方法运行在UI线程
        @Override
        public void run() {

            System.out.println("Begin Thread " + i);
            i += 10;
            //首先获得一个消息结构
            Message msg = updateBarHandler.obtainMessage();
            msg.what = PROGRESS;
            //给消息结构的arg1参数赋值
            msg.arg1 = i;
            Bundle bundle = new Bundle();
            bundle.putString("test", "test bundle");
            msg.setData(bundle);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (i > 100) {
                //把线程从线程队列中移除
                updateBarHandler.removeCallbacks(updateThread);
            } else {
                //把消息发送到消息队列中
                updateBarHandler.sendMessage(msg);
            }
        }
    };
}



