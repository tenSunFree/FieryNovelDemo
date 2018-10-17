package com.home.fierynoveldemo;

import android.util.Log;
import android.view.View;

import com.ocnyang.pagetransformerhelp.transformer.ABaseTransformer;

public class StackTransformer extends ABaseTransformer {

    private static final String TAG = "StackTransformer";

    public StackTransformer() {
    }

    protected void onTransform(View view, float position) {
        view.setTranslationX(position < 0.0F ? 0.0F : (float) (-view.getWidth()) * position);
    }

    /** 為翻頁時, 添加淡淡的陰影 */
    @Override
    public void transformPage(View view, float position) {
        Log.d(TAG, "position: " + position);
        int pageWidth = view.getWidth();
        if (position < -1) {
            view.setAlpha(0);
        } else if (position <= 0) {
            view.setAlpha(1);
            view.setTranslationX(0);
        } else if (position <= 1) {
            view.setAlpha(1 - position / 3);
            view.setTranslationX(pageWidth * -position);
        } else {
            view.setAlpha(0);
        }
    }
}
