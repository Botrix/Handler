package com.example.edl.handler;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {
    private ProgressBar bar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bar = (ProgressBar)findViewById(R.id.bar);

        //当点击startButton按钮时
        Button startButton = (Button) findViewById(R.id.startButton);
        if (startButton != null) {
            startButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    bar.setVisibility(View.VISIBLE);
                    //将线程加入到handler的线程队列中
                    updateBarHandler.post(updateThread);
                }
            });
        }

        Button endButton = (Button) findViewById(R.id.endButton);
        if (endButton != null) {
            endButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    //将接口从线程队列中移除
                    updateBarHandler.removeCallbacks(updateThread);
                }
            });
        }
    }


    //使用匿名内部类来复写Handler当中的handleMessage方法
    Handler updateBarHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            bar.setProgress(msg.arg1);
            Bundle bundle = msg.getData();
            //Causes the Runnable r to be added to the message queue. The runnable will
            // be run on the thread to which this handler is attached.
            updateBarHandler.post(updateThread);
            System.out.println("test---->" + bundle.getString("test"));
        }

    };
    //线程类，该类使用匿名内部类的方式进行声明
    Runnable updateThread = new Runnable() {
        int i = 0;

        @Override
        public void run() {
            System.out.println("Begin Thread " + i);
            i = i + 10;
            //得到一个消息对象
            Message msg = updateBarHandler.obtainMessage();

            //将msg对象的arg1参数的值设置为i,用arg1和arg2这两个成员变量传递消息
            msg.arg1 = i;
            Bundle bundle = new Bundle();
            bundle.putString("test", "test bundle");
            msg.setData(bundle);
            try {
                //设置当前显示睡眠1秒
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            //将msg对象加入到消息队列当中
            if (i > 100) {
                //将线程对象从handler当中移除
                updateBarHandler.removeCallbacks(updateThread);
                System.out.println(">>>>>>");
            } else {
                updateBarHandler.sendMessage(msg);
                System.out.println("<<<<<<");
            }
        }
    };
}

