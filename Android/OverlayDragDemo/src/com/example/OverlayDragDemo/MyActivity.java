package com.example.OverlayDragDemo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.os.Bundle;
import android.view.MotionEvent;
import com.amap.mapapi.core.GeoPoint;
import com.amap.mapapi.map.MapActivity;
import com.amap.mapapi.map.MapView;
import com.amap.mapapi.map.Overlay;

public class MyActivity extends MapActivity {

    private MapView mapView;
    private GeoPoint location;// 覆盖物的地理坐标
    private Point screenLocation;// 覆盖物的屏幕坐标

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        mapView = (MapView) findViewById(R.id.main_mapView);
        mapView.setBuiltInZoomControls(true);

        //用给定的经纬度构造一个GeoPoint，单位是微度 (度 * 1E6)
        location = new GeoPoint((int) (39.982378 * 1E6),
                (int) (116.304923 * 1E6));
        mapView.getController().setCenter(location); //设置地图中心点
        mapView.getController().setZoom(15);//设置地图zoom级别
        mapView.getOverlays().add(new MyOverlay());
    }

    // 自定义Overlay
    public class MyOverlay extends Overlay {

        // 手指按下位置与覆盖物左上角的相对位置
        private int dx;
        private int dy;

        // 覆盖物的大小
        private int width;
        private int height;

        // 是否拖拽中
        private boolean isDragging = false;

        // 覆盖物的显示图像
        private Bitmap bmp;

        {
            bmp = BitmapFactory.decodeResource(
                    getResources(), R.drawable.da_marker_red);
            width = bmp.getWidth();
            height = bmp.getHeight();
        }

        @Override
        public void draw(Canvas canvas, MapView mapView, boolean shadow) {
            super.draw(canvas, mapView, shadow);

            // 如果不在拖拽状态，则重新计算覆盖物的屏幕坐标
            if(!isDragging){
                screenLocation = new Point();
                mapView.getProjection().toPixels(location, screenLocation);
            }
            // 添加标记
            canvas.drawBitmap(bmp, screenLocation.x, screenLocation.y, null);
        }

        @Override
        public boolean onTouchEvent(MotionEvent motionEvent, MapView mapView){
            int x = Math.round(motionEvent.getX());
            int y = Math.round(motionEvent.getY());

            switch(motionEvent.getAction()){
                case MotionEvent.ACTION_DOWN:
                    dx = x - screenLocation.x;
                    dy = y - screenLocation.y;

                    // 如果点在覆盖物的位置，则开始拖拽
                    if(dx>0 && dx < width &&
                       dy>0 && dy < height){
                        isDragging = true;
                        // 必须返回true，表示这个事件已处理，下同
                        return true;
                    }
                    break;

                case MotionEvent.ACTION_MOVE:
                    if(isDragging){
                        // 更新位置
                        screenLocation = new Point(x-dx, y-dy);
                        // 强制重绘
                        mapView.invalidate();
                        return true;
                    }
                    break;

                case MotionEvent.ACTION_UP:
                    if(isDragging){
                        // 更新地理坐标位置，保证地图移动时位置不变
                        location = mapView.getProjection().fromPixels(x-dx, y-dy);
                        // 强制重绘
                        mapView.invalidate();
                        isDragging = false;
                        return true;
                    }
                    break;
            }
            // 其他情况，则不处理
            return false;
        }
    }
}