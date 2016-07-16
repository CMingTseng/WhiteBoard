package com.shutup.whiteboard;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.ScaleAnimation;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {

    private static final String TAG = "Main";


    private float screenW = 0;
    private float screenH = 0;
    private WhiteBoardViewCurrentState mState = null;
    private SeekBar mPenSizeSelect = null;

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

        mState = WhiteBoardViewCurrentState.getInstance();

        ButterKnife.bind(this);
    }

    @OnClick({R.id.penColor, R.id.penSize, R.id.showHide, R.id.showHideAlone, R.id.undo, R.id.redo, R.id.reset})
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.penColor:
                handlePenColor();
                break;
            case R.id.penSize:
                handlePenSize();
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

    private void handlePenSize() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
        View view = View.inflate(MainActivity.this, R.layout.pen_size_select_dialog, null);

        mPenSizeSelect = (SeekBar) view.findViewById(R.id.penSizeSelect);
        mPenSizeSelect.setProgress(mState.getPenSize());
        mPenSizeSelect.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser){

                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mState.setPenSize(seekBar.getProgress());
            }
        });
        alertDialog.setView(view);
        alertDialog.setTitle(R.string.select_pen_size_title);
        alertDialog.setPositiveButton(getString(R.string.ok),new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mWhiteBoardView.loadPaint();
            }
        });
        alertDialog.show();
    }

    private void handlePenColor() {
        int[] colors = new int[19];
        int[] colors_id = new int[]{
                R.color.red, R.color.pink, R.color.purple, R.color.deep_purple, R.color.indigo,
                R.color.blue, R.color.light_blue, R.color.cyan, R.color.teal, R.color.green,
                R.color.light_green, R.color.lime, R.color.yellow, R.color.amber, R.color.orange,
                R.color.deep_orange, R.color.brown, R.color.grey, R.color.blue_grey,
        };

        for (int i = 0; i < colors_id.length; i++) {
            colors[i] = getResources().getColor(colors_id[i]);
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

    private void changeViewState(ViewGroup viewGroup, boolean state) {
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            View view = viewGroup.getChildAt(i);
            view.setClickable(state); // Or whatever you want to do with the view.
        }
    }

    public void checkPathStackSize(WhiteBoardViewCurrentState state) {
        if (state.getSavePaths().size() > 0) {
            mUndo.setImageDrawable(getResources().getDrawable(R.drawable.undo));
        } else {
            mUndo.setImageDrawable(getResources().getDrawable(R.drawable.undo_normal));
        }
        if (state.getDeletePaths().size() > 0) {
            mRedo.setImageDrawable(getResources().getDrawable(R.drawable.redo));

        } else {
            mRedo.setImageDrawable(getResources().getDrawable(R.drawable.redo_normal));
        }
    }
}
