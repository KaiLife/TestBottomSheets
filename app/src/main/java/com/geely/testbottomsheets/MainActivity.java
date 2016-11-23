package com.geely.testbottomsheets;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RelativeLayout;

import static com.geely.testbottomsheets.R.id.bottom_sheet;

public class MainActivity extends AppCompatActivity {

    private CoordinatorLayout coordinatorLayout;
    private RelativeLayout rel_title_back;
    private CustomBottomSheetBehavior behavior;
    private NestedScrollView bottomSheet;
    private RelativeLayout rel_title;

    /**
     * 标识初始化时是否修改了底栏高度
     */
    private boolean isSetBottomSheetHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorlayout);
        rel_title_back = (RelativeLayout) findViewById(R.id.rel_title_back);
        rel_title = (RelativeLayout) findViewById(R.id.rel_title);

        bottomSheet = (NestedScrollView) findViewById(bottom_sheet);
        behavior = CustomBottomSheetBehavior.from(bottomSheet);
        behavior.addBottomSheetCallback(new CustomBottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, @CustomBottomSheetBehavior.State int newState) {

            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                if (bottomSheet.getTop() < rel_title_back.getHeight() + behavior.getPeekHeight()) {
                    //设置底栏完全展开时，出现的顶部工具栏的动画
                    rel_title.setVisibility(View.GONE);
                    rel_title_back.setVisibility(View.VISIBLE);
                    rel_title_back.setTranslationY(rel_title_back.getTop() + rel_title_back.getHeight() - bottomSheet.getTop());
                } else {
                    rel_title.setVisibility(View.VISIBLE);
                    rel_title_back.setVisibility(View.GONE);
                }
            }
        });

        findViewById(R.id.lin_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                behavior.setState(CustomBottomSheetBehavior.STATE_COLLAPSED);
            }
        });
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        //修改SetBottomSheet的高度 留出顶部工具栏的位置
        if (!isSetBottomSheetHeight) {
            CoordinatorLayout.LayoutParams linearParams = (CoordinatorLayout.LayoutParams) bottomSheet.getLayoutParams();
            linearParams.height = coordinatorLayout.getHeight() - rel_title_back.getHeight();
            bottomSheet.setLayoutParams(linearParams);
            isSetBottomSheetHeight = true;
        }
    }
}