package com.github.learn.viewpage;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.github.learn.refreshandload.databinding.ViewPageHeaderItemViewBinding;

/**
 * @author YanLu
 * @since 16/5/20
 */
public class HeaderViewPageAdapter extends BasePagerAdapter<GirlViewModel> {

        public HeaderViewPageAdapter(Context c) {
            super(c);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            GirlViewModel model = getItem(position);

            ViewPageHeaderItemViewBinding frgGuideBinding
                    = ViewPageHeaderItemViewBinding.inflate(mInflater, container, false);
            frgGuideBinding.setViewModel(model);
            container.addView(frgGuideBinding.getRoot());
            return frgGuideBinding.getRoot();

        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            if(object instanceof View) {
                container.removeView((View) object);
            }
        }


        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }