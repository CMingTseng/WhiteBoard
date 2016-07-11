package com.shutup.whiteboard;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.ScaleAnimation;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {

    private static final String TAG = "Main";


    private float screenW = 0;
    private float screenH = 0;
    private WhiteBoardViewCurrentState mState = null;

    @BindView(R.id.whiteboard)
    WhiteBoardView mWhiteBoardView;
    @BindView(R.id.penColor)
    ImageButton mPenColor;
    @BindView(R.id.penSize)
    ImageButton mPenSize;
    @BindView(R.id.topToolBar)
    LinearLayout mTopToolBar;
    @BindView(R.id.showHide)
    ImageButton mShowHide;
    @BindView(R.id.showHideAlone)
    ImageButton mShowHideAlone;
    @BindView(R.id.bottomToolBar)
    LinearLayout mBottomToolBar;
    @BindView(R.id.undo)
    ImageButton mUndo;
    @BindView(R.id.redo)
    ImageButton mRedo;
    @BindView(R.id.reset)
    ImageButton mReset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        screenH = displayMetrics.heightPixels;
        screenW = displayMetrics.widthPixels;

        mState  = WhiteBoardViewCurrentState.getInstance();

        ButterKnife.bind(this);
    }

    @OnClick({R.id.penColor, R.id.penSize, R.id.showHide, R.id.showHideAlone,R.id.undo, R.id.redo, R.id.reset})
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.penColor:
                break;
            case R.id.penSize:
                break;
            case R.id.showHide:
                handleToolBarShowHide();
                break;
            case R.id.showHideAlone:
                handleToolBarShowHide();
                break;
            case R.id.undo:
                mWhiteBoardView.undo();
                checkPathStackSize(mState);
                break;
            case R.id.redo:
                mWhiteBoardView.redo();
                checkPathStackSize(mState);
                break;
            case R.id.reset:
                mWhiteBoardView.resetBoard();
                checkPathStackSize(mState);
                break;
        }
    }

    private void handleToolBarShowHide() {
        if (mState.isShow()) {
            hideTopToolBar();
            hideBottomToolBar();
            mState.setShow(false);
        } else {
            showTopToolBar();
            showBottomToolBar();
            mState.setShow(true);
        }
    }

    private void hideTopToolBar() {
        ScaleAnimation scale = new ScaleAnimation(1, 1, 1, 0, 0, 0);
        scale.setDuration(2000);
        scale.setFillAfter(true);
        mTopToolBar.startAnimation(scale);
        changeViewState(mTopToolBar, false);
    }

    private void showTopToolBar() {
        ScaleAnimation scale = new ScaleAnimation(1, 1, 0, 1, 0, 0);
        scale.setDuration(2000);
        scale.setFillAfter(true);
        mTopToolBar.startAnimation(scale);
        changeViewState(mTopToolBar, true);
    }

    private void hideBottomToolBar() {
        float height = mBottomToolBar.getHeight();
        ScaleAnimation scale = new ScaleAnimation(1, 1, 1, 0, 0, height);
        scale.setDuration(2000);
        scale.setFillAfter(true);
        mBottomToolBar.startAnimation(scale);
        changeViewState(mBottomToolBar, false);

        mShowHideAlone.setVisibility(View.VISIBLE);
    }

    private void showBottomToolBar() {
        float height = mBottomToolBar.getHeight();
        ScaleAnimation scale = new ScaleAnimation(1, 1, 0, 1, 0, height);
        scale.setDuration(2000);
        scale.setFillAfter(true);
        mBottomToolBar.startAnimation(scale);
        changeViewState(mBottomToolBar, true);
        mShowHideAlone.setVisibility(View.GONE);
    }

    private void changeViewState(ViewGroup viewGroup, boolean state){
        for ( int i = 0; i < viewGroup.getChildCount();  i++ ){
            View view = viewGroup.getChildAt(i);
            view.setClickable(state); // Or whatever you want to do with the view.
        }
    }

    public void checkPathStackSize(WhiteBoardViewCurrentState state){
        if (state.getSavePaths().size() > 0){
            mUndo.setImageDrawable(getResources().getDrawable(R.drawable.undo));
        }else{
            mUndo.setImageDrawable(getResources().getDrawable(R.drawable.undo_normal));
        }
        if (state.getDeletePaths().size() > 0){
            mRedo.setImageDrawable(getResources().getDrawable(R.drawable.redo));

        }else{
            mRedo.setImageDrawable(getResources().getDrawable(R.drawable.redo_normal));
        }
    }
}
