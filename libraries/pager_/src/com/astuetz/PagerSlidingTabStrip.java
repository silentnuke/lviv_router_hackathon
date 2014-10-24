/*
 * Copyright (C) 2013 Andreas Stuetz <andreas.stuetz@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.astuetz;

import java.util.Locale;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.astuetz.pagerslidingtabstrip.R;

public class PagerSlidingTabStrip extends HorizontalScrollView {

    public interface TabBackgroundProvider {
        public View getPageTabBackground(int position);
    }

    public interface TabCustomViewProvider {
        public View getPageTabCustomView(int position);
    }

    // @formatter:off
    private static final int[] ATTRS = new int[]{
            android.R.attr.textSize,
            android.R.attr.textColor
    };
    // @formatter:on

    private LinearLayout.LayoutParams defaultTabLayoutParams;

    private final PageListener pageListener = new PageListener();
    public OnPageChangeListener delegatePageListener;

    private final PagerAdapterObserver adapterObserver = new PagerAdapterObserver();

    private LinearLayout tabsContainer;
    private ViewPager pager;

    private int tabCount;

    private int currentPosition = 0;
    private float currentPositionOffset = 0f;
    private int selectedPosition = 0;

    private Paint rectPaint;
    private Paint dividerPaint;

    private boolean checkedTabWidths = false;

    private int indicatorColor = 0xFF666666;
    private int underlineColor = 0x1A000000;
    private int dividerColor = 0x1A000000;

    private boolean shouldExpand = false;
    private boolean textAllCaps = true;

    private int scrollOffset = 52;
    private int indicatorHeight = 8;
    private int underlineHeight = 2;
    private int dividerPadding = 12;
    private int tabPadding = 24;
    private int dividerWidth = 1;

    private boolean backgroundDividerDrawn;

    private int tabTextSize = 12;
    private ColorStateList tabTextColor = ColorStateList.valueOf(0xFF666666);
    private Typeface tabTypeface = null;
    private int tabTypefaceStyle = Typeface.BOLD;

    private int lastScrollX = 0;

    private int tabBackgroundResId = R.drawable.background_tab;

    private Locale locale;

    public PagerSlidingTabStrip(Context context) {
        this(context, null);
    }

    public PagerSlidingTabStrip(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PagerSlidingTabStrip(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        setFillViewport(true);
        setWillNotDraw(false);

        tabsContainer = new LinearLayout(context);
        tabsContainer.setOrientation(LinearLayout.HORIZONTAL);
        tabsContainer.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        addView(tabsContainer);

        DisplayMetrics dm = getResources().getDisplayMetrics();

        scrollOffset = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, scrollOffset, dm);
        indicatorHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, indicatorHeight, dm);
        underlineHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, underlineHeight, dm);
        dividerPadding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dividerPadding, dm);
        tabPadding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, tabPadding, dm);
        dividerWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dividerWidth, dm);
        tabTextSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, tabTextSize, dm);

        // get system attrs (android:textSize and android:textColor)

        TypedArray a = context.obtainStyledAttributes(attrs, ATTRS);

        tabTextSize = a.getDimensionPixelSize(0, tabTextSize);
        tabTextColor = a.getColorStateList(1);
        if (tabTextColor == null) {
            tabTextColor = ColorStateList.valueOf(0xFF666666);
        }
        a.recycle();

        // get custom attrs

        a = context.obtainStyledAttributes(attrs, R.styleable.PagerSlidingTabStrip);

        indicatorColor = a.getColor(R.styleable.PagerSlidingTabStrip_pstsIndicatorColor, indicatorColor);
        underlineColor = a.getColor(R.styleable.PagerSlidingTabStrip_pstsUnderlineColor, underlineColor);
        dividerColor = a.getColor(R.styleable.PagerSlidingTabStrip_pstsDividerColor, dividerColor);
        indicatorHeight = a.getDimensionPixelSize(R.styleable.PagerSlidingTabStrip_pstsIndicatorHeight, indicatorHeight);
        underlineHeight = a.getDimensionPixelSize(R.styleable.PagerSlidingTabStrip_pstsUnderlineHeight, underlineHeight);
        dividerPadding = a.getDimensionPixelSize(R.styleable.PagerSlidingTabStrip_pstsDividerPadding, dividerPadding);
        tabPadding = a.getDimensionPixelSize(R.styleable.PagerSlidingTabStrip_pstsTabPaddingLeftRight, tabPadding);
        tabBackgroundResId = a.getResourceId(R.styleable.PagerSlidingTabStrip_pstsTabBackground, tabBackgroundResId);
        shouldExpand = a.getBoolean(R.styleable.PagerSlidingTabStrip_pstsShouldExpand, shouldExpand);
        scrollOffset = a.getDimensionPixelSize(R.styleable.PagerSlidingTabStrip_pstsScrollOffset, scrollOffset);
        textAllCaps = a.getBoolean(R.styleable.PagerSlidingTabStrip_pstsTextAllCaps, textAllCaps);

        a.recycle();

        rectPaint = new Paint();
        rectPaint.setAntiAlias(true);
        rectPaint.setStyle(Style.FILL);

        dividerPaint = new Paint();
        dividerPaint.setAntiAlias(true);
        dividerPaint.setStrokeWidth(dividerWidth);

        defaultTabLayoutParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);

        if (locale == null) {
            locale = getResources().getConfiguration().locale;
        }
    }

    public void setTabContainerGravity(int gravity) {
        tabsContainer.setGravity(gravity);
    }

    public void setViewPager(ViewPager pager) {
        this.pager = pager;

        if (pager.getAdapter() == null) {
            throw new IllegalStateException("ViewPager does not have adapter instance.");
        }

        pager.setOnPageChangeListener(pageListener);

        pager.getAdapter().registerDataSetObserver(adapterObserver);
        adapterObserver.setAttached(true);

        notifyDataSetChanged();
    }

    public void setOnPageChangeListener(OnPageChangeListener listener) {
        this.delegatePageListener = listener;
    }

    public void notifyDataSetChanged() {

        tabsContainer.removeAllViews();

        tabCount = pager.getAdapter().getCount();

        final TabBackgroundProvider tabBackgroundProvider;

        if (pager.getAdapter() instanceof TabBackgroundProvider) {
            tabBackgroundProvider = (TabBackgroundProvider) pager.getAdapter();
        } else {
            tabBackgroundProvider = null;
        }

        final TabCustomViewProvider tabCustomViewProvider;

        if (pager.getAdapter() instanceof TabCustomViewProvider) {
            tabCustomViewProvider = (TabCustomViewProvider) pager.getAdapter();
        } else {
            tabCustomViewProvider = null;
        }

        for (int i = 0; i < tabCount; i++) {
            final View customView = (tabCustomViewProvider != null) ? tabCustomViewProvider.getPageTabCustomView(i) : null;
            final View backgroundView;

            if (tabBackgroundProvider != null) {
                backgroundView = tabBackgroundProvider.getPageTabBackground(i);
            } else {
                backgroundView = null;
            }

            addTab(i, pager.getAdapter().getPageTitle(i), customView, backgroundView);
        }

        updateTabStyles();

        checkedTabWidths = false;

        getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {

            @SuppressWarnings("deprecation")
            @SuppressLint("NewApi")
            @Override
            public void onGlobalLayout() {

                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                    getViewTreeObserver().removeGlobalOnLayoutListener(this);
                } else {
                    getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }

                currentPosition = pager.getCurrentItem();
                scrollToChild(currentPosition, 0);
            }
        });

    }

    private void setSelectedPosition(final int position) {
        if (selectedPosition >= 0 && selectedPosition < tabsContainer.getChildCount()) {
            tabsContainer.getChildAt(selectedPosition).setSelected(false);
        }

        selectedPosition = position;

        if (position >= 0 && position < tabsContainer.getChildCount()) {
            tabsContainer.getChildAt(position).setSelected(true);
        }
    }

    private void addTab(final int position, final CharSequence title, final View customView, final View backgroundView) {

        if ((title == null || title.length() == 0) && customView == null) {
            throw new IllegalStateException("PagerSlidingTabStrip requires that tabs have either a (non-zero length) title, a custom view, or both.");
        }

        if (customView != null) {
            final ViewParent customViewParent = customView.getParent();

            if (customViewParent instanceof ViewGroup) {
                final ViewGroup viewGroup = (ViewGroup) customViewParent;
                viewGroup.removeView(customView);
            }
        }

        if (backgroundView != null) {
            final ViewParent backgroundViewParent = backgroundView.getParent();

            if (backgroundViewParent instanceof ViewGroup) {
                final ViewGroup viewGroup = (ViewGroup) backgroundViewParent;
                viewGroup.removeView(backgroundView);
            }
        }

        final TabLayout tabLayout = new TabLayout(getContext());
        tabLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));

        if (backgroundView != null) {
            tabLayout.setBackgroundView(backgroundView);
        }

        if (title != null && title.length() > 0) {
            final TextView textView = new TextView(getContext());
            textView.setText(title);
            textView.setGravity(Gravity.CENTER);
            textView.setSingleLine();
            tabLayout.setTextView(textView);
        }

        if (customView != null) {
            tabLayout.setCustomView(customView);
        }

        tabLayout.setFocusable(true);
        tabLayout.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                pager.setCurrentItem(position);
            }
        });

        tabLayout.setSelected(position == selectedPosition);

        tabsContainer.addView(tabLayout);
    }

    private void updateTabStyles() {

        for (int i = 0; i < tabCount; i++) {

            final TabLayout tabLayout = (TabLayout) tabsContainer.getChildAt(i);

            tabLayout.setLayoutParams(defaultTabLayoutParams);
            tabLayout.setBackgroundResource(tabBackgroundResId);

            if (shouldExpand) {
                tabLayout.setPadding(0, 0, 0, 0);
            } else {
                tabLayout.setPadding(tabPadding, 0, tabPadding, 0);
            }

            if (tabLayout.getTextView() != null) {

                TextView textView = tabLayout.getTextView();
                textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, tabTextSize);
                textView.setTypeface(tabTypeface, tabTypefaceStyle);
                textView.setTextColor(tabTextColor);

                // setAllCaps() is only available from API 14, so the upper case is made manually if we are on a
                // pre-ICS-build
                if (textAllCaps) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                        textView.setAllCaps(true);
                    } else {
                        textView.setText(textView.getText().toString().toUpperCase(locale));
                    }
                }
            }
        }

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        if (!shouldExpand || MeasureSpec.getMode(widthMeasureSpec) == MeasureSpec.UNSPECIFIED) {
            return;
        }

        int myWidth = getMeasuredWidth();
        int childWidth = 0;
        for (int i = 0; i < tabCount; i++) {
            childWidth += tabsContainer.getChildAt(i).getMeasuredWidth();
        }

        if (!checkedTabWidths && childWidth > 0 && myWidth > 0) {

            if (childWidth <= myWidth) {
                for (int i = 0; i < tabCount; i++) {
                    View tabView = tabsContainer.getChildAt(i);
                    tabView.setLayoutParams(new LinearLayout.LayoutParams(0, LayoutParams.MATCH_PARENT, tabView.getMeasuredWidth()));
                }
            }

            checkedTabWidths = true;
        }
    }

    private void scrollToChild(int position, int offset) {

        if (tabCount == 0) {
            return;
        }

        int newScrollX = tabsContainer.getChildAt(position).getLeft() + offset;

        if (position > 0 || offset > 0) {
            newScrollX -= scrollOffset;
        }

        if (newScrollX != lastScrollX) {
            lastScrollX = newScrollX;
            scrollTo(newScrollX, 0);
        }

    }

    @Override
    protected void dispatchDraw(final Canvas canvas) {

        super.dispatchDraw(canvas);

        if (isInEditMode() || tabCount == 0) {
            return;
        }

        final int height = getHeight();

        // draw divider

        final Rect clipBounds = canvas.getClipBounds();

        dividerPaint.setColor(dividerColor);

        final int tabDividerCount = (backgroundDividerDrawn) ? tabCount : tabCount - 1;

        for (int i = 0; i < tabDividerCount; i++) {
            View tab = tabsContainer.getChildAt(i);
            canvas.drawLine(tab.getRight(), dividerPadding, tab.getRight(), height - dividerPadding, dividerPaint);
        }

        // draw indicator line

        rectPaint.setColor(indicatorColor);

        // select the selected tab
        for (int i = 0; i < tabsContainer.getChildCount(); i++) {
            View tab = tabsContainer.getChildAt(i);
            tab.setSelected(i == selectedPosition);
        }

        // default: line below current tab
        View currentTab = tabsContainer.getChildAt(currentPosition);
        float lineLeft = currentTab.getLeft();
        float lineRight = currentTab.getRight();

        // if there is an offset, start interpolating left and right coordinates between current and next tab
        if (currentPositionOffset > 0f && currentPosition < tabCount - 1) {

            View nextTab = tabsContainer.getChildAt(currentPosition + 1);
            final float nextTabLeft = nextTab.getLeft();
            final float nextTabRight = nextTab.getRight();

            lineLeft = (currentPositionOffset * nextTabLeft + (1f - currentPositionOffset) * lineLeft);
            lineRight = (currentPositionOffset * nextTabRight + (1f - currentPositionOffset) * lineRight);
        }

        canvas.drawRect(lineLeft, height - indicatorHeight, lineRight, height, rectPaint);

        // draw underline

        rectPaint.setColor(underlineColor);
        canvas.drawRect(0, height - underlineHeight, tabsContainer.getWidth(), height, rectPaint);

    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (pager != null) {
            if (!adapterObserver.isAttached()) {
                pager.getAdapter().registerDataSetObserver(adapterObserver);
                adapterObserver.setAttached(true);
            }
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (pager != null) {
            if (adapterObserver.isAttached()) {
                pager.getAdapter().unregisterDataSetObserver(adapterObserver);
                adapterObserver.setAttached(false);
            }
        }
    }

    private class PageListener implements OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            currentPosition = position;
            currentPositionOffset = positionOffset;

            scrollToChild(position, (int) (positionOffset * tabsContainer.getChildAt(position).getWidth()));

            invalidate();

            if (delegatePageListener != null) {
                delegatePageListener.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            if (state == ViewPager.SCROLL_STATE_IDLE) {
                scrollToChild(pager.getCurrentItem(), 0);
            }

            if (delegatePageListener != null) {
                delegatePageListener.onPageScrollStateChanged(state);
            }
        }

        @Override
        public void onPageSelected(int position) {
            setSelectedPosition(position);

            if (delegatePageListener != null) {
                delegatePageListener.onPageSelected(position);
            }
        }

    }

    private static final class TabLayout extends ViewGroup {

        private View backgroundView;
        private View customView;
        private TextView textView;

        public TabLayout(final Context context) {
            super(context);

            setClipToPadding(false);
        }

        public View getBackgroundView() {
            return backgroundView;
        }

        public void setBackgroundView(final View backgroundView) {

            if (this.backgroundView != null) {
                removeView(this.backgroundView);
            }

            this.backgroundView = backgroundView;

            if (backgroundView != null && backgroundView.getParent() != this) {
                addView(backgroundView, 0);
            }

        }

        public View getCustomView() {
            return customView;
        }

        public void setCustomView(final View customView) {

            if (this.customView != null) {
                removeView(this.customView);
            }

            this.customView = customView;

            if (customView != null && customView.getParent() != this) {
                if (textView == null) {
                    addView(customView);
                } else {
                    addView(customView, indexOfChild(textView));
                }
            }

        }

        public TextView getTextView() {
            return textView;
        }

        public void setTextView(final TextView textView) {

            if (this.textView != null) {
                removeView(this.textView);
            }

            this.textView = textView;

            if (textView != null && textView.getParent() != this) {
                addView(textView);
            }

        }

        @Override
        public boolean shouldDelayChildPressedState() {
            return false;
        }

        @Override
        protected void onMeasure(final int widthMeasureSpec, final int heightMeasureSpec) {

            int maxHeight = 0;
            int maxWidth = 0;

            final int count = getChildCount();

            for (int i = 0; i < count; i++) {

                final View child = getChildAt(i);

                if (child.getVisibility() != GONE && child != backgroundView) {
                    measureChild(child, widthMeasureSpec, heightMeasureSpec);

                    maxWidth = Math.max(maxWidth, child.getMeasuredWidth());
                    maxHeight = Math.max(maxHeight, child.getMeasuredHeight());
                }
            }

            maxWidth = Math.max(maxWidth + getPaddingLeft() + getPaddingRight(), getSuggestedMinimumWidth());
            maxHeight = Math.max(maxHeight + getPaddingTop() + getPaddingBottom(), getSuggestedMinimumHeight());

            final int measuredWidth = resolveSize(maxWidth, widthMeasureSpec);
            final int measuredHeight = resolveSize(maxHeight, heightMeasureSpec);

            if (backgroundView != null) {
                backgroundView.measure(MeasureSpec.makeMeasureSpec(measuredWidth, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(measuredHeight, MeasureSpec.EXACTLY));
            }

            setMeasuredDimension(measuredWidth, measuredHeight);
        }

        @Override
        protected void onLayout(final boolean changed, final int left, final int top, final int right, final int bottom) {

            final int innerLeft = getPaddingLeft();
            final int innerRight = right - left - getPaddingRight();
            final int innerTop = getPaddingTop();
            final int innerBottom = bottom - top - getPaddingBottom();

            final int innerWidth = innerRight - innerLeft;
            final int innerHeight = innerBottom - innerTop;

            final int count = getChildCount();

            for (int i = 0; i < count; i++) {

                final View child = getChildAt(i);

                if (child.getVisibility() != GONE && child != backgroundView) {

                    final int width = child.getMeasuredWidth();
                    final int height = child.getMeasuredHeight();

                    final int childLeft = innerLeft + (innerWidth - width) / 2;
                    final int childRight = childLeft + width;
                    final int childTop = innerTop + (innerHeight - height) / 2;
                    final int childBottom = childTop + height;

                    child.layout(childLeft, childTop, childRight, childBottom);
                }
            }

            if (backgroundView != null) {
                backgroundView.layout(0, 0, right - left, bottom - top);
            }
        }
    }

    public void setIndicatorColor(int indicatorColor) {
        this.indicatorColor = indicatorColor;
        invalidate();
    }

    public void setIndicatorColorResource(int resId) {
        this.indicatorColor = getResources().getColor(resId);
        invalidate();
    }

    public int getIndicatorColor() {
        return this.indicatorColor;
    }

    public void setIndicatorHeight(int indicatorLineHeightPx) {
        this.indicatorHeight = indicatorLineHeightPx;
        invalidate();
    }

    public int getIndicatorHeight() {
        return indicatorHeight;
    }

    public void setUnderlineColor(int underlineColor) {
        this.underlineColor = underlineColor;
        invalidate();
    }

    public void setUnderlineColorResource(int resId) {
        this.underlineColor = getResources().getColor(resId);
        invalidate();
    }

    public int getUnderlineColor() {
        return underlineColor;
    }

    public void setDividerColor(int dividerColor) {
        this.dividerColor = dividerColor;
        invalidate();
    }

    public void setDividerColorResource(int resId) {
        this.dividerColor = getResources().getColor(resId);
        invalidate();
    }

    public int getDividerColor() {
        return dividerColor;
    }

    public void setBackgroundDividerDrawn(final boolean backgroundDividerDrawn) {
        this.backgroundDividerDrawn = backgroundDividerDrawn;
    }

    public boolean isBackgroundDividerDrawn() {
        return backgroundDividerDrawn;
    }

    public void setUnderlineHeight(int underlineHeightPx) {
        this.underlineHeight = underlineHeightPx;
        invalidate();
    }

    public int getUnderlineHeight() {
        return underlineHeight;
    }

    public void setDividerPadding(int dividerPaddingPx) {
        this.dividerPadding = dividerPaddingPx;
        invalidate();
    }

    public int getDividerPadding() {
        return dividerPadding;
    }

    public void setScrollOffset(int scrollOffsetPx) {
        this.scrollOffset = scrollOffsetPx;
        invalidate();
    }

    public int getScrollOffset() {
        return scrollOffset;
    }

    public void setShouldExpand(boolean shouldExpand) {
        this.shouldExpand = shouldExpand;
        if (pager != null) {
            requestLayout();
        }
    }

    public boolean getShouldExpand() {
        return shouldExpand;
    }

    public boolean isTextAllCaps() {
        return textAllCaps;
    }

    public void setAllCaps(boolean textAllCaps) {
        this.textAllCaps = textAllCaps;
    }

    public void setTextSize(int textSizePx) {
        this.tabTextSize = textSizePx;
        updateTabStyles();
    }

    public int getTextSize() {
        return tabTextSize;
    }

    public void setTextColor(ColorStateList textColor) {
        this.tabTextColor = textColor;
        updateTabStyles();
    }

    public void setTextColorResource(int resId) {
        this.tabTextColor = getResources().getColorStateList(resId);
        updateTabStyles();
    }

    public ColorStateList getTextColor() {
        return tabTextColor;
    }

    public void setTypeface(Typeface typeface, int style) {
        this.tabTypeface = typeface;
        this.tabTypefaceStyle = style;
        updateTabStyles();
    }

    public void setTabBackground(int resId) {
        this.tabBackgroundResId = resId;
    }

    public int getTabBackground() {
        return tabBackgroundResId;
    }

    public void setTabPaddingLeftRight(int paddingPx) {
        this.tabPadding = paddingPx;
        updateTabStyles();
    }

    public int getTabPaddingLeftRight() {
        return tabPadding;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        SavedState savedState = (SavedState) state;
        super.onRestoreInstanceState(savedState.getSuperState());
        currentPosition = savedState.currentPosition;
        setSelectedPosition(currentPosition);
        requestLayout();
    }

    @Override
    public Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        SavedState savedState = new SavedState(superState);
        savedState.currentPosition = currentPosition;
        return savedState;
    }

    static class SavedState extends BaseSavedState {
        int currentPosition;

        public SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in) {
            super(in);
            currentPosition = in.readInt();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeInt(currentPosition);
        }

        public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator<SavedState>() {
            @Override
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            @Override
            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
    }

    private class PagerAdapterObserver extends DataSetObserver {

        private boolean attached = false;

        @Override
        public void onChanged() {
            notifyDataSetChanged();
        }

        public void setAttached(boolean attached) {
            this.attached = attached;
        }

        public boolean isAttached() {
            return attached;
        }
    }
}