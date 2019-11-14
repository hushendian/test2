/*
 * Tencent is pleased to support the open source community by making QMUI_Android available.
 *
 * Copyright (C) 2017-2018 THL A29 Limited, a Tencent company. All rights reserved.
 *
 * Licensed under the MIT License (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 *
 * http://opensource.org/licenses/MIT
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.domuiteam.domui.widget.Button;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.domuiteam.domui.R;

/**
 * 可以方便地生成圆角矩形/圆形 {@link android.graphics.drawable.Drawable}。
 * <p>
 * <ul>
 * <li>使用 {@link #setBgData(ColorStateList)} 设置背景色。</li>
 * <li>使用 {@link #setStrokeData(int, ColorStateList)} 设置描边大小、描边颜色。</li>
 * </ul>
 */
public class RoundButtonDrawable extends GradientDrawable {

    /**
     * 圆角大小是否自适应为 View 的高度的一般
     */
    private ColorStateList mFillColors;
    private int mStrokeWidth = 0;
    private ColorStateList mStrokeColors;
    private static final int ORIGITION_LEFT_RIGHT = 0;
    private static final int DEFAULT_COLOR = 0;

    /**
     * 设置按钮的背景色(只支持纯色,不支持 Bitmap 或 Drawable)
     */
    private void setBgData(@Nullable ColorStateList colors) {
        if (hasNativeStateListAPI()) {
            super.setColor(colors);
        } else {
            mFillColors = colors;
            final int currentColor;
            if (colors == null) {
                currentColor = Color.TRANSPARENT;
            } else {
                currentColor = colors.getColorForState(getState(), 0);
            }
            setColor(currentColor);
        }
    }

    /**
     * 设置按钮的描边粗细和颜色
     */
    private void setStrokeData(int width, @Nullable ColorStateList colors) {
        if (hasNativeStateListAPI()) {
            super.setStroke(width, colors);
        } else {
            mStrokeWidth = width;
            mStrokeColors = colors;
            final int currentColor;
            if (colors == null) {
                currentColor = Color.TRANSPARENT;
            } else {
                currentColor = colors.getColorForState(getState(), 0);
            }
            setStroke(width, currentColor);
        }
    }

    private boolean hasNativeStateListAPI() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }


    @Override
    protected boolean onStateChange(int[] stateSet) {
        boolean superRet = super.onStateChange(stateSet);
        if (mFillColors != null) {
            int color = mFillColors.getColorForState(stateSet, 0);
            setColor(color);
            superRet = true;
        }
        if (mStrokeColors != null) {
            int color = mStrokeColors.getColorForState(stateSet, 0);
            setStroke(mStrokeWidth, color);
            superRet = true;
        }
        return superRet;
    }

    @Override
    public boolean isStateful() {
        return (mFillColors != null && mFillColors.isStateful())
                || (mStrokeColors != null && mStrokeColors.isStateful())
                || super.isStateful();
    }

    static GradientDrawable fromAttributeSet(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.DOMUIRoundButton, defStyleAttr, 0);
        ColorStateList colorBg = typedArray.getColorStateList(R.styleable.DOMUIRoundButton_domui_backgroundColor);
        ColorStateList colorBorder = typedArray.getColorStateList(R.styleable.DOMUIRoundButton_domui_borderColor);
        int borderWidth = typedArray.getDimensionPixelSize(R.styleable.DOMUIRoundButton_domui_borderWidth, 0);
        int mRadius = typedArray.getDimensionPixelSize(R.styleable.DOMUIRoundButton_domui_radius, 0);
        int mRadiusTopLeft = typedArray.getDimensionPixelSize(R.styleable.DOMUIRoundButton_domui_radiusTopLeft, 0);
        int mRadiusTopRight = typedArray.getDimensionPixelSize(R.styleable.DOMUIRoundButton_domui_radiusTopRight, 0);
        int mRadiusBottomLeft = typedArray.getDimensionPixelSize(R.styleable.DOMUIRoundButton_domui_radiusBottomLeft, 0);
        int mRadiusBottomRight = typedArray.getDimensionPixelSize(R.styleable.DOMUIRoundButton_domui_radiusBottomRight, 0);
        int mGradientStartColor = typedArray.getColor(R.styleable.DOMUIRoundButton_domui_gradientStart, 0);
        int mGradientEndColor = typedArray.getColor(R.styleable.DOMUIRoundButton_domui_gradientEnd, 0);
        int mGradientOrientation = typedArray.getDimensionPixelSize(R.styleable.DOMUIRoundButton_domui_radiusBottomRight, 0);
        typedArray.recycle();
        GradientDrawable bg;
        if (mGradientStartColor != DEFAULT_COLOR && mGradientEndColor != DEFAULT_COLOR) {
            int[] colors = {mGradientStartColor, mGradientEndColor};
            Orientation orientation;
            if (mGradientOrientation == ORIGITION_LEFT_RIGHT) {
                orientation = Orientation.LEFT_RIGHT;
            } else {
                orientation = Orientation.TOP_BOTTOM;
            }
            bg = new GradientDrawable(orientation, colors);
            bg.setStroke(borderWidth, colorBorder);
        } else {
            bg = new RoundButtonDrawable();
            ((RoundButtonDrawable) bg).setBgData(colorBg);
            ((RoundButtonDrawable) bg).setStrokeData(borderWidth, colorBorder);
        }


        if (mRadiusTopLeft > 0 || mRadiusTopRight > 0 || mRadiusBottomLeft > 0 || mRadiusBottomRight > 0) {
            float[] radii = new float[]{
                    mRadiusTopLeft, mRadiusTopLeft,
                    mRadiusTopRight, mRadiusTopRight,
                    mRadiusBottomRight, mRadiusBottomRight,
                    mRadiusBottomLeft, mRadiusBottomLeft
            };
            bg.setCornerRadii(radii);
        } else {
            bg.setCornerRadius(mRadius);
            if (mRadius > 0) {
            }
        }
        return bg;
    }

}
