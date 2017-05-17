package com.example.NDKDemo;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.nio.IntBuffer;

public class MyActivity extends Activity {
    /**
     * Called when the activity is first created.
     */
    Button buttonC;
    Button buttonJava;
    ImageView imageView;
    TextView textView;

    final String ByC = "C";
    final String ByJava = "Java";

    Bitmap image = null;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        buttonC = (Button)findViewById(R.id.buttonC);
        buttonJava = (Button)findViewById(R.id.buttonJava);
        imageView = (ImageView)findViewById(R.id.imageView);
        textView = (TextView)findViewById(R.id.textView);
        loadImage();
    }

    public void onClick(View v){

        String process = "";
        switch (v.getId()){
            case R.id.buttonC:
                process = "C";
                break;

            case R.id.buttonJava:
                process = "Java";
               break;

            case R.id.buttonReset:
                loadImage();
                return;
        }

        // 复制图像到int数组
        int height = image.getHeight();
        int width = image.getWidth();
        int[] buffer = new int[width*height];
        image.copyPixelsToBuffer(IntBuffer.wrap(buffer));

        long begin = System.currentTimeMillis();
        if(process.equals(ByC)){
            InvertColor.executeByC(buffer);
        }else {
            InvertColor.executeByJava(buffer);
        }
        long end = System.currentTimeMillis();

        // 显示测试结果
        String interval = String.format("测试结果: %s %.3fs", process, (end - begin) / 1000.0);
        textView.setText(interval);

        // 显示图片
        Bitmap newBmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        newBmp.setPixels(buffer, 0, width, 0, 0, width, height);
        imageView.setImageBitmap(newBmp);
    }

    private void loadImage(){
        Bitmap b = null;

        try {
            InputStream is = getResources().getAssets().open("pic.jpg");

            // 为便于计算，设置成ARGB模式
            BitmapFactory.Options op = new BitmapFactory.Options();
            op.inPreferredConfig = Bitmap.Config.ARGB_8888;
            b = BitmapFactory.decodeStream(is, null, op);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        image = b;
        imageView.setImageBitmap(image);
    }
}
