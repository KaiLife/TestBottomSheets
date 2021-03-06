package com.geely.testbottomsheets;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import static com.geely.testbottomsheets.R.id.bottom_sheet;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

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
                switch (newState) {
                    case CustomBottomSheetBehavior.STATE_COLLAPSED:
                        Log.d("bottomsheet-", "STATE_COLLAPSED");
                        break;
                    case CustomBottomSheetBehavior.STATE_DRAGGING:
                        Log.d("bottomsheet-", "STATE_DRAGGING");
                        break;
                    case CustomBottomSheetBehavior.STATE_EXPANDED:
                        Log.d("bottomsheet-", "STATE_EXPANDED");
                        break;
                    case CustomBottomSheetBehavior.STATE_ANCHOR_POINT:
                        Log.d("bottomsheet-", "STATE_ANCHOR_POINT");
                        break;
                    case CustomBottomSheetBehavior.STATE_HIDDEN:
                        Log.d("bottomsheet-", "STATE_HIDDEN");
                        break;
                    default:
                        Log.d("bottomsheet-", "STATE_SETTLING");
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                if (bottomSheet.getTop() < rel_title_back.getHeight() + behavior.getPeekHeight()) {
                    //设置底栏完全展开时，出现的顶部工具栏的动画
//                    Log.d("onSlide---", "slideOffset: " + slideOffset);
                    Log.d("onSlide---", "bottomSheet.getTop(): " + bottomSheet.getTop()
                            + "  Y:" + (bottomSheet.getTop() - 2 * rel_title.getHeight()));
                    rel_title.setVisibility(View.VISIBLE);
                    rel_title.setAlpha(1 - setSlideAlpha(slideOffset));
                    rel_title.setTranslationY(bottomSheet.getTop() - 2 * rel_title.getHeight());
                    rel_title_back.setVisibility(View.VISIBLE);
                    rel_title_back.setAlpha(setSlideAlpha(slideOffset));
                    rel_title_back.setTranslationY(rel_title_back.getHeight() - bottomSheet.getTop());
                } else {
                    rel_title.setVisibility(View.VISIBLE);
                    rel_title_back.setVisibility(View.GONE);
                }
            }
        });

        behavior.setState(CustomBottomSheetBehavior.STATE_ANCHOR_POINT);
        findViewById(R.id.lin_back).setOnClickListener(this);
        findViewById(R.id.btn_high).setOnClickListener(this);
        findViewById(R.id.btn_low).setOnClickListener(this);
    }

    private float setSlideAlpha(float slideOffset) {
        if (slideOffset < 0.9) {
            slideOffset = (float) 0.9;
        }

        if (slideOffset > 1) {
            slideOffset = 1;
        }

        return (float) ((slideOffset - 0.9) * 10);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lin_back:
                bottomSheet.fullScroll(NestedScrollView.FOCUS_UP);
                behavior.setState(CustomBottomSheetBehavior.STATE_COLLAPSED);
                break;

            case R.id.btn_high:
                bottomSheet.fullScroll(NestedScrollView.FOCUS_UP);
                behavior.setAnchorPoint(dip2px(150));
                behavior.refreshAnchor();
                break;

            case R.id.btn_low:
                bottomSheet.fullScroll(NestedScrollView.FOCUS_UP);
                behavior.setAnchorPoint(dip2px(500));
                behavior.refreshAnchor();
                break;

            default:
                break;
        }
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public int dip2px(float dpValue) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
