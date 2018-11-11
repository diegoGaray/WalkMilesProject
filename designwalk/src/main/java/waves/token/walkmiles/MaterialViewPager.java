package waves.token.walkmiles;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.astuetz.PagerSlidingTabStrip;
import waves.token.walkmiles.header.HeaderDesign;
import waves.token.walkmiles.header.MaterialViewPagerImageHelper;

/**
 * Created by florentchampigny on 28/04/15.
 * <p/>
 * The main class of MaterialViewPager
 * To use in an xml layout with attributes viewpager_*
 * <p/>
 * Display a preview with header, actual logo and fake cells
 */
public class MaterialViewPager extends FrameLayout implements ViewPager.OnPageChangeListener {

    /**
     * Contains all references to MatervialViewPager's header views
     */
    protected MaterialViewPagerHeader materialViewPagerHeader;
    //the child toolbar
    protected Toolbar mToolbar;
    //the child viewpager
    protected ViewPager mViewPager;
    //a view used to add placeholder color below the header
    protected View headerBackground;
    //a view used to add fading color over the headerBackgroundContainer
    protected View toolbarLayoutBackground;
    //Class containing the configuration of the MaterialViewPager
    protected MaterialViewPagerSettings settings = new MaterialViewPagerSettings();
    protected Listener listener;
    int lastPosition = -1;
    int currentPagerState = Integer.MIN_VALUE;
    /**
     * the layout containing the header
     * default : add @layout/material_view_pager_default_header
     * with viewpager_header you can set your own layout
     */
    private ViewGroup headerBackgroundContainer;
    /**
     * the layout containing tabs
     * default : add @layout/material_view_pager_pagertitlestrip_standard
     * with viewpager_pagerTitleStrip you can set your own layout
     */
    private ViewGroup pagerTitleStripContainer;
    /**
     * the layout containing the viewpager, can be replaced to add your own implementation of viewpager
     */
    private ViewGroup viewpagerContainer;

    //region construct
    /**
     * the layout containing logo
     * default : empty
     * with viewpager_logo you can set your own layout
     */
    private ViewGroup logoContainer;

    public MaterialViewPager(Context context) {
        super(context);
    }

    public MaterialViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        settings.handleAttributes(context, attrs);
    }

    public MaterialViewPager(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        settings.handleAttributes(context, attrs);
    }

    //endregion

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public MaterialViewPager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        settings.handleAttributes(context, attrs);
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        SavedState ss = (SavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());

        this.settings = ss.settings;
        if (headerBackground != null) {
            headerBackground.setBackgroundColor(this.settings.color);
        }

        waves.token.walkmiles.MaterialViewPagerAnimator animator = waves.token.walkmiles.MaterialViewPagerHelper.getAnimator(this.getContext());

        //-1*ss.yOffset restore to 0
        animator.restoreScroll(-1 * ss.yOffset, ss.settings);
        waves.token.walkmiles.MaterialViewPagerHelper.register(getContext(), animator);
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();

        SavedState ss = new SavedState(superState);
        //end
        ss.settings = this.settings;
        ss.yOffset = waves.token.walkmiles.MaterialViewPagerHelper.getAnimator(getContext()).lastYOffset;

        return ss;
    }

    /**
     * Retrieve the displayed viewpager, don't forget to use
     * getPagerTitleStrip().setAdapter(materialviewpager.getViewPager())
     * after set an adapter
     *
     * @return the displayed viewpager
     */
    public ViewPager getViewPager() {
        return mViewPager;
    }

    /**
     * Retrieve the displayed tabs
     *
     * @return the displayed tabs
     */
    public PagerSlidingTabStrip getPagerTitleStrip() {
        return (PagerSlidingTabStrip) pagerTitleStripContainer.findViewById(waves.token.walkmiles.R.id.materialviewpager_pagerTitleStrip);
    }

    /**
     * Retrieve the displayed toolbar
     *
     * @return the displayed toolbar
     */
    public Toolbar getToolbar() {
        return mToolbar;
    }

    /**
     * Retrieve the displayed toolbar
     */
    public void setToolbar(Toolbar toolbar) {
        mToolbar = toolbar;
    }

    /**
     * change the header displayed image with a fade
     * may remove Picasso
     */
    public void setImageUrl(String imageUrl, int fadeDuration) {
        if (imageUrl != null) {
            final ImageView headerBackgroundImage = (ImageView) findViewById(waves.token.walkmiles.R.id.materialviewpager_imageHeader);
            //if using MaterialViewPagerImageHeader
            if (headerBackgroundImage != null) {
                ViewCompat.setAlpha(headerBackgroundImage, settings.headerAlpha);
                MaterialViewPagerImageHelper.setImageUrl(headerBackgroundImage, imageUrl, fadeDuration);
                setImageHeaderDarkLayerAlpha();
            }
        }
    }

    /**
     * change the header displayed image with a fade and an OnLoadListener
     * may remove Picasso
     */
    public void setImageUrl(String imageUrl, int fadeDuration, OnImageLoadListener imageLoadListener) {
        if (imageLoadListener != null) {
            MaterialViewPagerImageHelper.setImageLoadListener(imageLoadListener);
        }
        setImageUrl(imageUrl, fadeDuration);
    }

    /**
     * change the header displayed image with a fade
     * may remove Picasso
     */
    public void setImageDrawable(Drawable drawable, int fadeDuration) {
        if (drawable != null) {
            final ImageView headerBackgroundImage = (ImageView) findViewById(waves.token.walkmiles.R.id.materialviewpager_imageHeader);
            //if using MaterialViewPagerImageHeader
            if (headerBackgroundImage != null) {
                ViewCompat.setAlpha(headerBackgroundImage, settings.headerAlpha);
                MaterialViewPagerImageHelper.setImageDrawable(headerBackgroundImage, drawable, fadeDuration);
                setImageHeaderDarkLayerAlpha();
            }
        }
    }

    /**
     * Change alpha of the header image dark layer to reveal text.
     */
    public void setImageHeaderDarkLayerAlpha() {
        final View headerImageDarkLayerView = findViewById(waves.token.walkmiles.R.id.materialviewpager_headerImageDarkLayer);
        //if using MaterialViewPagerImageHeader
        if (headerImageDarkLayerView != null) {
            headerImageDarkLayerView.setBackgroundColor(getResources().getColor(android.R.color.black));
            ViewCompat.setAlpha(headerImageDarkLayerView, settings.imageHeaderDarkLayerAlpha);
        }
    }

    /**
     * Change the header color
     */
    public void setColor(int color, int fadeDuration) {
        if (waves.token.walkmiles.MaterialViewPagerHelper.getAnimator(getContext()) != null) {
            waves.token.walkmiles.MaterialViewPagerHelper.getAnimator(getContext()).setColor(color, fadeDuration * 2);
        }
    }

    public ViewGroup getHeaderBackgroundContainer() {
        return headerBackgroundContainer;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (currentPagerState != ViewPager.SCROLL_STATE_SETTLING) {
            if (positionOffset >= 0.5) {
                onPageSelected(position + 1);
            } else if (positionOffset <= -0.5) {
                onPageSelected(position - 1);
            } else {
                onPageSelected(position);
            }
        }
    }

    public void notifyHeaderChanged() {
        int position = lastPosition;
        lastPosition = -1;
        onPageSelected(position);
    }

    //region ViewPagerOnPageListener

    @Override
    public void onPageSelected(int position) {
        if (position == lastPosition || listener == null) {
            return;
        }

        HeaderDesign headerDesign = listener.getHeaderDesign(position);
        if (headerDesign == null) {
            return;
        }

        int fadeDuration = 400;
        int color = headerDesign.getColor();
        if (headerDesign.getColorRes() != 0) {
            color = getContext().getResources().getColor(headerDesign.getColorRes());
        }

        if (headerDesign.getDrawable() != null) {
            setImageDrawable(headerDesign.getDrawable(), fadeDuration);
        } else {
            setImageUrl(headerDesign.getImageUrl(), fadeDuration);
        }

        setColor(color, fadeDuration);

        lastPosition = position;
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        currentPagerState = state;
        if (settings.displayToolbarWhenSwipe) {
            waves.token.walkmiles.MaterialViewPagerHelper.getAnimator(getContext()).onViewPagerPageChanged();
        }
    }

    public void setMaterialViewPagerListener(Listener listener) {
        this.listener = listener;
    }

    @Override
    protected void onDetachedFromWindow() {
        waves.token.walkmiles.MaterialViewPagerHelper.unregister(getContext());
        listener = null;
        super.onDetachedFromWindow();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        //add @layout/material_view_pager_layout as child, containing all the MaterialViewPager views
        addView(LayoutInflater.from(getContext()).inflate(waves.token.walkmiles.R.layout.material_view_pager_layout, this, false));

        headerBackgroundContainer = (ViewGroup) findViewById(waves.token.walkmiles.R.id.headerBackgroundContainer);
        pagerTitleStripContainer = (ViewGroup) findViewById(waves.token.walkmiles.R.id.pagerTitleStripContainer);
        viewpagerContainer = (ViewGroup) findViewById(waves.token.walkmiles.R.id.viewpager_layout);
        logoContainer = (ViewGroup) findViewById(waves.token.walkmiles.R.id.logoContainer);

        mToolbar = (Toolbar) findViewById(waves.token.walkmiles.R.id.toolbar);
        if (settings.disableToolbar) {
            mToolbar.setVisibility(INVISIBLE);
        }

        { //replace the viewpager ?
            int viewPagerLayoutId = settings.viewpagerId;
            if (viewPagerLayoutId != -1) {
                viewpagerContainer.removeAllViews();
                viewpagerContainer.addView(LayoutInflater.from(getContext()).inflate(viewPagerLayoutId, viewpagerContainer, false));
            }
        }

        mViewPager = (ViewPager) findViewById(waves.token.walkmiles.R.id.materialviewpager_viewpager);

        mViewPager.addOnPageChangeListener(this);

        //inflate subviews defined in attributes

        {
            int headerId = settings.headerLayoutId;
            if (headerId == -1) {
                if (settings.animatedHeaderImage) {
                    headerId = waves.token.walkmiles.R.layout.material_view_pager_moving_header;
                } else {
                    headerId = waves.token.walkmiles.R.layout.material_view_pager_imageview_header;
                }
            }
            headerBackgroundContainer.addView(LayoutInflater.from(getContext()).inflate(headerId, headerBackgroundContainer, false));
        }

        if (isInEditMode()) { //preview titlestrip
            //add fake tabs on edit mode
            settings.pagerTitleStripId = waves.token.walkmiles.R.layout.tools_material_view_pager_pagertitlestrip;
        }
        if (settings.pagerTitleStripId != -1) {
            pagerTitleStripContainer.addView(LayoutInflater.from(getContext()).inflate(settings.pagerTitleStripId, pagerTitleStripContainer, false));
        }

        if (settings.logoLayoutId != -1) {
            logoContainer.addView(LayoutInflater.from(getContext()).inflate(settings.logoLayoutId, logoContainer, false));
            if (settings.logoMarginTop != 0) {
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) logoContainer.getLayoutParams();
                layoutParams.setMargins(0, settings.logoMarginTop, 0, 0);
                logoContainer.setLayoutParams(layoutParams);
            }
        }

        headerBackground = findViewById(waves.token.walkmiles.R.id.headerBackground);
        toolbarLayoutBackground = findViewById(waves.token.walkmiles.R.id.toolbar_layout_background);

        initialiseHeights();

        //construct the materialViewPagerHeader with subviews
        if (!isInEditMode()) {
            materialViewPagerHeader = MaterialViewPagerHeader
                    .withToolbar(mToolbar)
                    .withToolbarLayoutBackground(toolbarLayoutBackground)
                    .withPagerSlidingTabStrip(pagerTitleStripContainer)
                    .withHeaderBackground(headerBackground)
                    .withStatusBackground(findViewById(waves.token.walkmiles.R.id.statusBackground))
                    .withLogo(logoContainer);

            //and construct the MaterialViewPagerAnimator
            //attach it to the activity to enable MaterialViewPagerHeaderView.setMaterialHeight();
            MaterialViewPagerHelper.register(getContext(), new MaterialViewPagerAnimator(this));
        } else {

            //if in edit mode, add fake cardsviews
            View sample = LayoutInflater.from(getContext()).inflate(waves.token.walkmiles.R.layout.tools_list_items, pagerTitleStripContainer, false);

            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) sample.getLayoutParams();
            int marginTop = Math.round(waves.token.walkmiles.Utils.dpToPx(settings.headerHeight + 10, getContext()));
            params.setMargins(0, marginTop, 0, 0);
            super.setLayoutParams(params);

            addView(sample);
        }
    }

    //endregion

    private void initialiseHeights() {
        if (headerBackground != null) {
            headerBackground.setBackgroundColor(this.settings.color);

            ViewGroup.LayoutParams layoutParams = headerBackground.getLayoutParams();
            layoutParams.height = (int) waves.token.walkmiles.Utils.dpToPx(this.settings.headerHeight + settings.headerAdditionalHeight, getContext());
            headerBackground.setLayoutParams(layoutParams);
        }
        if (pagerTitleStripContainer != null) {
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) pagerTitleStripContainer.getLayoutParams();
            int marginTop = (int) waves.token.walkmiles.Utils.dpToPx(this.settings.headerHeight - 40, getContext());
            layoutParams.setMargins(0, marginTop, 0, 0);
            pagerTitleStripContainer.setLayoutParams(layoutParams);
        }
        if (toolbarLayoutBackground != null) {
            ViewGroup.LayoutParams layoutParams = toolbarLayoutBackground.getLayoutParams();
            layoutParams.height = (int) Utils.dpToPx(this.settings.headerHeight, getContext());
            toolbarLayoutBackground.setLayoutParams(layoutParams);
        }
    }

    public interface Listener {
        HeaderDesign getHeaderDesign(int page);
    }

    public interface OnImageLoadListener {
        void OnImageLoad(ImageView imageView, Bitmap bitmap);
    }

    static class SavedState extends BaseSavedState {
        //required field that makes Parcelables from a Parcel
        public static final Parcelable.Creator<SavedState> CREATOR =
                new Parcelable.Creator<SavedState>() {
                    public SavedState createFromParcel(Parcel in) {
                        return new SavedState(in);
                    }

                    public SavedState[] newArray(int size) {
                        return new SavedState[size];
                    }
                };
        public waves.token.walkmiles.MaterialViewPagerSettings settings;
        public float yOffset;

        SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in) {
            super(in);
            this.settings = in.readParcelable(MaterialViewPagerSettings.class.getClassLoader());
            this.yOffset = in.readFloat();
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeParcelable(this.settings, flags);
            out.writeFloat(this.yOffset);
        }
    }

}