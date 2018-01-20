package com.example.js.framelayoutanimate;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.menu.ShowableListMenu;
import android.support.v7.widget.ForwardingListener;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.R.attr.y;

public class MainActivity extends AppCompatActivity {

    private final static int MAXMOVESIZE_Y = 800;
    private final static int MAXMOVESIZE_X = 800;
    @BindView(R.id.ballview)
    BallView ballview;
    @BindView(R.id.main)
    LinearLayout main;

    private int downY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initView();
        initData();
    }

    private void initView() {
        main.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_DOWN:
                        downY = (int) event.getY();
                        return true;
                    case MotionEvent.ACTION_MOVE:
                        float y1 = event.getY();
                        if (y1 > downY&&y1<600) {
                            float offset = y1 - downY;
                            float progress = offset > MAXMOVESIZE_Y ? 1 : offset / MAXMOVESIZE_Y;
                            float progress_x = offset > MAXMOVESIZE_X ? 1 : offset / MAXMOVESIZE_X;
                            ballview.setProgres(progress_x,progress);
                            return true;
                        }
                       break;
                    case MotionEvent.ACTION_UP:
                        ballview.release();
                        break;
                    default:
                        break;
                }
                return false;
            }
        });
    }

    private void initData() {


    }


}
