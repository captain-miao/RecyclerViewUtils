package com.github.learn.databinding;

import android.databinding.BindingAdapter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.github.learn.refreshandload.R;
import com.github.learn.refreshandload.databinding.ActivityDataBindingBinding;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.security.SecureRandom;


public class DataBindingRecyclerActivity extends AppCompatActivity implements ImageLoaderCallback {
    private static final String TAG = "DataBindingRecyclerActivity";

    private ActivityDataBindingBinding mBinding;
    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityDataBindingBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        //setContentView(R.layout.activity_data_binding);
        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }

        //mBinding.setTxtColor(getResources().getColor(R.color.blue));
        mBinding.setBgColor(getResources().getColor(R.color.green));
        //mBinding.setVariable(BR.txtColor, getResources().getColor(R.color.blue));
        mBinding.setImageUrl(images[0]);
        mBinding.setClickHandler(this);
        mBinding.setCallback(this);

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


//    @BindingConversion
//    public static ColorDrawable convertColorToDrawable(int color) {
//       return new ColorDrawable(color);
//    }


    @BindingAdapter({"imageUrl", "callback"})
    public static void loadImage(ImageView imageView, String imageUrl, final ImageLoaderCallback callback) {
        callback.onImageLoading();
        Picasso.with(imageView.getContext())
                .load(imageUrl)
                .into(imageView, new Callback() {
                    @Override
                    public void onSuccess() {
                        callback.onImageReady();
                    }

                    @Override
                    public void onError() {
                        callback.onImageLoadError();
                    }
                });
    }

    public void ChangeColor(View view){
        mBinding.setTxtColor(getRandomColor());
    }

    public void ChangeBackground(View view){
        mBinding.setBgColor(getRandomBgColor());
    }

    public void ChangeImage(View view){
        mBinding.setImageUrl(getRandomImage());
    }


    private int getRandomColor() {
      SecureRandom rgb = new SecureRandom();
      return Color.HSVToColor(150, new float[]{
              rgb.nextInt(359), 1, 1
      });
    }

    private int getRandomBgColor() {
      SecureRandom rgb = new SecureRandom();
      return Color.HSVToColor(50, new float[]{
              rgb.nextInt(359), 1, 1
      });
    }

    private String getRandomImage() {
      SecureRandom rgb = new SecureRandom();
        return images[rgb.nextInt(9)];
    }

    private String[] images = new String[]{
            "http://ww1.sinaimg.cn/small/7a8aed7bjw1f2sm0ns82hj20f00l8tb9.jpg",
            "http://ww4.sinaimg.cn/small/7a8aed7bjw1f2tpr3im0mj20f00l6q4o.jpg",
            "http://ww4.sinaimg.cn/small/610dc034jw1f2uyg3nvq7j20gy0p6myx.jpg",
            "http://ww2.sinaimg.cn/small/7a8aed7bjw1f2w0qujoecj20f00kzjtt.jpg",
            "http://ww3.sinaimg.cn/small/7a8aed7bjw1f2x7vxkj0uj20d10mi42r.jpg",
            "http://ww1.sinaimg.cn/small/7a8aed7bjw1f2zwrqkmwoj20f00lg0v7.jpg",
            "http://ww2.sinaimg.cn/small/7a8aed7bjw1f30sgi3jf0j20iz0sg40a.jpg",
            "http://ww4.sinaimg.cn/small/7a8aed7bjw1f32d0cumhkj20ey0mitbx.jpg",
            "http://ww2.sinaimg.cn/small/610dc034gw1f35cxyferej20dw0i2789.jpg",
            "http://ww2.sinaimg.cn/small/7a8aed7bjw1f340c8jrk4j20j60srgpf.jpg"
    };

    @Override
    public void onImageLoading() {
        mBinding.setIsLoading(true);
    }

    @Override
    public void onImageReady() {
        mBinding.setIsLoading(false);
    }

    @Override
    public void onImageLoadError() {
        mBinding.setIsLoading(false);
    }
}
