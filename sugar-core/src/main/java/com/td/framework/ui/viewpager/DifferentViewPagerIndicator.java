package com.td.framework.ui.viewpager;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.td.framework.R;
import com.td.framework.ui.viewpager.listener.OnTabClickListener;
import com.td.framework.ui.viewpager.listener.OnTabSelectedListener;
import com.td.framework.utils.DensityUtils;
import com.td.framework.utils.L;

import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * 三角形Tab指示器
 * TODO 6.0挤在一起了
 */
public class DifferentViewPagerIndicator extends LinearLayout {

    /**
     * tab上的内容
     */
    private List<String> mTabTitles;
    /**
     * 与之绑定的ViewPager
     */
    public ViewPager mViewPager;
    /**
     * 标题最大字体
     */
    private float mMaxTextSize = 32L;
    /**
     * 标题最小字体
     */
    private float mMixTextSize = 14L;
    /**
     * 消息红点最小宽度
     */
    private float mMixBadgeWidth = 0;
    /**
     * 消息红点最大宽度
     */
    private float mMaxBadgeWidth = 0;

    /**
     * 最后一次偏移
     */
    float lastPositionOffset = 0L;
    /**
     * 最大字体与最小字体的差值
     */
    private float mTitleTextSizeCult;
    /**
     * 红点的右间距
     */
    private float mBadgeRightMargin = 10.5f;

    /**
     * 最大标题的底步
     */
    private float mTitleTextBottomMargin = 0.0f;


    /**
     * 最大红点与最小红点的差值
     */
    private float mBadgeSizeCult;
    /**
     * 最大字体与最小字体的差值比例 mTitleTextSizeCult/偏移量
     */
    private float mTitleTextSizeProportion;
    /**
     * 最大红点与最小红点的差值比例  计算方式为 mBadgeCult/偏移量
     */
    private float mBadgeSizeProportion;
    /**
     * 绘制三角形的画笔
     */
    private Paint mPaint;
    /**
     * path构成一个三角形
     */
    private Path mPath;
    /**
     * 三角形的宽度
     */
    private int mTriangleWidth;
    /**
     * 三角形的高度
     */
    private int mTriangleHeight;
    private Map<Integer, Integer> mBadgeCounts = new WeakHashMap<>();

    /**
     * 回调监听
     */
    private OnTabClickListener mOnTabClickListener;
    private OnTabSelectedListener onTabSelectedListener;


    public void setOnTabSelectedListener(OnTabSelectedListener onTabSelectedListener) {
        this.onTabSelectedListener = onTabSelectedListener;
    }

    public void setmOnTabClickListener(OnTabClickListener mOnTabClickListener) {
        this.mOnTabClickListener = mOnTabClickListener;
    }

    public DifferentViewPagerIndicator(Context context) {
        this(context, null);
    }

    public DifferentViewPagerIndicator(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DifferentViewPagerIndicator(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
//        setVerticalGravity(VERTICAL);

        init(context);

    }




    private void init(Context context) {

        mMixBadgeWidth = DensityUtils.dp2px(context, 6f);
        mMaxBadgeWidth = DensityUtils.dp2px(context, 20f);
        setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM);
        // 标题字体之间的差值
        mTitleTextSizeCult = (mMaxTextSize - mMixTextSize);

        // 红点相差值
        mBadgeSizeCult = (mMaxBadgeWidth - mMixBadgeWidth);

        mTitleTextBottomMargin = DensityUtils.dp2px(context, 2f);

    }


    /**
     * 设置相应下标的文字
     *
     * @param position 下标
     * @param text     文字
     */
    public void setTabTitle(int position, String text) {
        try {
            View viewGroup = ((ViewGroup) getChildAt(position)).getChildAt(0);
            View view = ((ViewGroup) viewGroup).getChildAt(0);
            if (view instanceof TextView) {
                TextView tvText = (TextView) view;
                tvText.setText(text);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置消息红点
     *
     * @param position   下标
     * @param badgeCount 消息数量
     */
    public void setBadge(int position, int badgeCount) {
        try {
            mBadgeCounts.put(position, badgeCount);

            TextView mtvBadge = (TextView) ((ViewGroup) getChildAt(position)).getChildAt(1);
            //当前显示

            if (badgeCount == 0) {
                mtvBadge.setVisibility(GONE);
            } else {
                mtvBadge.setVisibility(VISIBLE);
                if (position == mViewPager.getCurrentItem()) {
                    mtvBadge.setText("" + badgeCount);
                    if (badgeCount >= 99) {
                        mtvBadge.setText("99");
                    }
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        }


    }




    /**
     * 设置tab的标题内容 可选，可以自己在布局文件中写死
     *
     * @param datas
     */
    public void setTabItemTitles(List<String> datas) {
        // 如果传入的list有值，则移除布局文件中设置的view
        if (datas != null && datas.size() > 0) {
            this.removeAllViews();
            this.mTabTitles = datas;

            for (int i = 0; i < mTabTitles.size(); i++) {
                mBadgeCounts.put(0, 0);
                // 添加view
                addView(generateTextView(mTabTitles.get(i), i));
            }


            // 设置item的click事件
            //setItemClickEvent();
        }
    }

    private View generateTextView(String title, final int position) {
        //加到 相对布局中
        FrameLayout mWarpLayout = new FrameLayout(getContext());
        //设置布局参数
        FrameLayout.LayoutParams mWarpLayoutParams =
                new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        mWarpLayout.setLayoutParams(mWarpLayoutParams);
//        mWarpLayout.setMinimumWidth(DensityUtils.dp2px(getContext(),40));
//        mWarpLayout.setBackgroundColor(Color.GRAY);
        mWarpLayoutParams.gravity = Gravity.BOTTOM;
//        mWarpLayoutParams.gravity = Gravity.TOP |  Gravity.LEFT;


        //创建------------------------------TextView

        LinearLayout mTitleWarp = new LinearLayout(getContext());
        LayoutParams mWarpTitleParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        mTitleWarp.setLayoutParams(mWarpTitleParams);
        mWarpTitleParams.gravity = Gravity.BOTTOM;
        mTitleWarp.setGravity(Gravity.BOTTOM);

        TextView tvTitle = new TextView(getContext());
        LinearLayout.LayoutParams tvTitleParams = new LinearLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        tvTitle.setGravity(Gravity.BOTTOM);
//        tvTitle.setGravity(Gravity.TOP | Gravity.LEFT);
        tvTitle.setTextColor(Color.WHITE);
//        tvTitle.setBackgroundColor(0xff03a4f9);


        tvTitle.setText(title);
        tvTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, mMixTextSize);
        if (position == 0) {
            tvTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, mMaxTextSize);
//            tvTitle.setTypeface(Typeface.DEFAULT_BOLD);
            tvTitleParams.bottomMargin = (int) -mTitleTextBottomMargin;
        }
        //给右边设置多一点距离
        tvTitleParams.rightMargin = DensityUtils.dp2px(getContext(), mBadgeRightMargin);
//        tvTitleParams.rightMargin = DensityUtils.dp2px(getContext(), 60);
        // 顶部给多一点距离，让红点往上一点
        tvTitleParams.topMargin = DensityUtils.dp2px(getContext(), 3f);

        tvTitle.setLayoutParams(tvTitleParams);
        //创建------------------------------TextView

        //-----------------创建红点---------------
        //background_badge_process

        TextView tvBadge = new TextView(getContext());
        tvBadge.setBackgroundResource(R.drawable.background_badge_process);
        tvBadge.setGravity(Gravity.CENTER);
        tvBadge.setTextColor(Color.WHITE);
        //相对布

        FrameLayout.LayoutParams badgeLayoutParam =
                new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT,
                        FrameLayout.LayoutParams.WRAP_CONTENT);

        badgeLayoutParam.gravity = Gravity.RIGHT;
        badgeLayoutParam.width = (int) mMixBadgeWidth;
        badgeLayoutParam.height = (int) mMixBadgeWidth;
        //为0的时候，放大第一个
        if (position == 0) {
            badgeLayoutParam.width = (int) mMaxBadgeWidth;
            badgeLayoutParam.height = (int) mMaxBadgeWidth;
            tvBadge.setTextSize(TypedValue.COMPLEX_UNIT_SP, 11f);
        } else {
            // 剩下的两个点 左右边距
            badgeLayoutParam.rightMargin = DensityUtils.dp2px(getContext(), 10);
            badgeLayoutParam.topMargin = DensityUtils.dp2px(getContext(), 2);
        }

        tvBadge.setVisibility(GONE);
        tvBadge.setLayoutParams(badgeLayoutParam);


        //-----------------创建红点---------------


        //外面包裹一层
        mTitleWarp.addView(tvTitle);

        //增加文本
        mWarpLayout.addView(mTitleWarp);
        //增加红点
        mWarpLayout.addView(tvBadge);


        //-----------------待办三角形-------------
//        if (position == 0) {
//            ImageView ivTrangle = new ImageView(getContext());
//            ivTrangle.setImageResource(R.drawable.triangle_down);
//            FrameLayout.LayoutParams mTrangleParms =
//                    new FrameLayout.LayoutParams(50, 50);
//            mTrangleParms.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
//            ivTrangle.setLayoutParams(mTrangleParms);
//            mWarpLayout.addView(ivTrangle);
//        }

        //-----------------待办三角形-------------


        //点击事件
        mWarpLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnTabClickListener != null) {
                    mOnTabClickListener.onTabClickListener(position, v);
                }
                //当前不等于
                if (position != mViewPager.getCurrentItem()) {
                    mViewPager.setCurrentItem(position, true);


                }


            }
        });


        return mWarpLayout;
    }

    public void setViewPager(final ViewPager mViewPager, final int pos) {
        this.mViewPager = mViewPager;

        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                if (onTabSelectedListener != null) {
                    onTabSelectedListener.onTabSelectedListener(position);
                }
            }

            @Override
            public void onPageScrolled(int position, float positionOffset,
                                       int positionOffsetPixels) {

                reset(-1);

                //计算 最大文字和最小文字的比例值
                mTitleTextSizeProportion = mTitleTextSizeCult * positionOffset;
                //计算 最大红点和最小红点的比例值
                mBadgeSizeProportion = mBadgeSizeCult * positionOffset;
//                L.e("mTitleTextSizeProportion:" + mTitleTextSizeProportion + "       mBadgeSizeProportion:" + mBadgeSizeProportion);

                //右边   左边的(x,height)缩放
                if (lastPositionOffset > positionOffset && positionOffset != 0) {
//                    L.e("右边 positionOffset:" + positionOffset + "       position:" + position);
                    //右滑

                    if (position >= 0 && positionOffset <= 1.0f) {
//                      //上一个
                        changeTitleSize(position + 1,
                                mMixTextSize + mTitleTextSizeProportion,
                                positionOffset, false, mBadgeSizeProportion
                                , false);
                        //当前
                        changeTitleSize(position,
                                mMaxTextSize - mTitleTextSizeProportion,
                                positionOffset,
                                true, mBadgeSizeProportion
                                , false);
                    }


                } else if (lastPositionOffset < positionOffset && positionOffset != 0) {
//                    L.e("左边 positionOffset:" + positionOffset + "       position:" + position);

                    //左滑
                    //没有大于边界
                    if (position < mViewPager.getChildCount() && positionOffset <= 1.0f) {
                        //下一个
                        changeTitleSize(position + 1,
                                mMixTextSize + mTitleTextSizeProportion,
                                positionOffset,
                                false, mBadgeSizeProportion
                                , false);
                        //当前
                        changeTitleSize(position,
                                mMaxTextSize - mTitleTextSizeProportion,
                                positionOffset,
                                true, mBadgeSizeProportion
                                , false);
                    }
                } else if (positionOffset > 1 || positionOffset < -1) {
                    //  reset(position);
                    L.e("reset");
                } else {
//                    L.e("选中：" + position);

                    reset(position);

                }
                lastPositionOffset = positionOffset;
            }

            @Override
            public void onPageScrollStateChanged(int state) {


            }
        });
        // 设置当前页
        mViewPager.setCurrentItem(pos);
//        reset(pos, true);

    }

    private void reset(int pos) {
        //直接遍历所有的
//        L.e("更改状态:" + pos);
        for (int i = 0; i < getChildCount(); i++) {

            changeTitleSize(i,
                    i == pos ? mMaxTextSize : mMixTextSize,
                    i == pos ? 1 : 0,
                    i == pos,
                    0
                    , true);


//            if (i == pos) {
//                changeTitleSize(i,
//                        mMaxTextSize,
//                        0, true, 0);
//            } else {
//                changeTitleSize(i,
//                        mMixTextSize,
//                        0, isCurrent, 0);
//            }

//            postInvalidate();

        }
    }

    /**
     * 更改标题字体大小
     *
     * @param position       目标
     * @param textSize       字体大小
     * @param positionOffset 偏移量
     * @param isCurrent      是否是当前
     */
    protected void changeTitleSize(int position,
                                   float textSize,
                                   float positionOffset,
                                   boolean isCurrent,
                                   float badgeSizeProportion,
                                   boolean isReset) {
        View view = ((ViewGroup) getChildAt(position))
                .getChildAt(0);

        View targetView = ((ViewGroup) view).getChildAt(0);

        //获取标题View
        if (targetView instanceof TextView) {
            TextView tvText = (TextView) targetView;

            tvText.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);

            LinearLayout.LayoutParams tvTitleParams = (LinearLayout.LayoutParams) tvText.getLayoutParams();
            //大文字的高度降低
            if (isCurrent) {
                tvTitleParams.bottomMargin = (int) (-mTitleTextBottomMargin + mTitleTextBottomMargin * positionOffset);
//                L.e("tvTitleParams isCurrent :" + tvTitleParams.bottomMargin + "   position:" + position);
            } else {
                tvTitleParams.bottomMargin = (int) -(mTitleTextBottomMargin * positionOffset);
//                L.e("tvTitleParams notCurrent :" + tvTitleParams.bottomMargin + "   position:" + position);

            }
            //思路没转变过来
            if (isCurrent && isReset) {
                tvTitleParams.bottomMargin = (int) (-mTitleTextBottomMargin);
                L.e("重置的情况");
            }
        }


        //当前---------------红点  缩小
        TextView mtvBadge = (TextView) ((ViewGroup) getChildAt(position)).getChildAt(1);
        //1. 布局参数
        //2. 字体大小
        FrameLayout.LayoutParams mBadgeParam = (FrameLayout.LayoutParams) mtvBadge.getLayoutParams();
        //当前
        if (isCurrent) {
            mBadgeParam.width = (int) (mMaxBadgeWidth - badgeSizeProportion);
            mBadgeParam.height = (int) (mMaxBadgeWidth - badgeSizeProportion);
            //右边的边距  防止 变大了 遮挡住
//            mBadgeParam.rightMargin = (int) (mBadgeRightMargin / 2 + (mBadgeRightMargin / 2 * positionOffset));

        } else {
            //下一个
            mBadgeParam.width = (int) (mMixBadgeWidth + badgeSizeProportion);
            mBadgeParam.height = (int) (mMixBadgeWidth + badgeSizeProportion);
            //更改margin的位置
//            mBadgeParam.rightMargin = (int) (mBadgeRightMargin - (mBadgeRightMargin / 2 * positionOffset));
        }

        mtvBadge.setLayoutParams(mBadgeParam);

        Integer mBadgeCount = mBadgeCounts.get(position);
        //消息红点
        String badgeCount = mBadgeCount.toString();
        //显示红点
        if (mBadgeCount > 0) {
            mtvBadge.setVisibility(VISIBLE);
        } else {
            mtvBadge.setVisibility(GONE);
        }

        if (mBadgeCount >= 99) {
            badgeCount = "99";
        }

        //设置当前文字和字体
        if (isCurrent) {
            if (positionOffset < 0.5f || isReset) {
                mtvBadge.setText(badgeCount);
                mtvBadge.setTypeface(Typeface.DEFAULT_BOLD);
                mtvBadge.setTextSize(TypedValue.COMPLEX_UNIT_SP, isReset ? 11f : 11f - (5f * positionOffset));
            } else {
                mtvBadge.setText("");
                mtvBadge.setTextSize(TypedValue.COMPLEX_UNIT_SP, 11f);
                mtvBadge.setTypeface(Typeface.MONOSPACE);
            }
        } else {
            //设置下一个文字和大小
            if (positionOffset > 0.4f) {
                mtvBadge.setText(badgeCount);
                mtvBadge.setTypeface(Typeface.DEFAULT_BOLD);
                mtvBadge.setTextSize(TypedValue.COMPLEX_UNIT_SP, 6f + (5f * positionOffset));
            } else {
                mtvBadge.setText("");
                mtvBadge.setTextSize(TypedValue.COMPLEX_UNIT_SP, 11f);
                mtvBadge.setTypeface(Typeface.MONOSPACE);
            }
        }

    }
}
