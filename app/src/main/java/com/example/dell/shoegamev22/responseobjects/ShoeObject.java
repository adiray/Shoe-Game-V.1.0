package com.example.dell.shoegamev22.responseobjects;


import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.example.dell.shoegamev22.R;
import com.google.gson.annotations.SerializedName;
import com.mikepenz.fastadapter.items.AbstractItem;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;


public class ShoeObject extends AbstractItem<ShoeObject, ShoeObject.ShoeObjectViewHolder> {

    @SerializedName("image5")
    private String image5;

    @SerializedName("image3")
    private String image3;

    @SerializedName("image4")
    private String image4;

    @SerializedName("largestSize")
    private String largestSize;

    @SerializedName("created")
    private long created;

    @SerializedName("discount")
    private Object discount;

    @SerializedName("description")
    private Object description;

    @SerializedName("title")
    private String title;

    @SerializedName("ownerId")
    private Object ownerId;

    @SerializedName("image2")
    private String image2;

    @SerializedName("color3")
    private String color3;

    @SerializedName("color1")
    private String color1;

    @SerializedName("color2")
    private String color2;

    @SerializedName("mainImage")
    private String mainImage;

    @SerializedName("price")
    private String price;

    @SerializedName("___class")
    private String ___class;

    @SerializedName("firstSize")
    private String firstSize;

    @SerializedName("sku")
    private String sku;

    @SerializedName("updated")
    private long updated;

    @SerializedName("objectId")
    private String objectId;

    public void setImage5(String image5) {
        this.image5 = image5;
    }

    public String getImage5() {
        return image5;
    }

    public void setImage3(String image3) {
        this.image3 = image3;
    }

    public String getImage3() {
        return image3;
    }

    public void setImage4(String image4) {
        this.image4 = image4;
    }

    public String getImage4() {
        return image4;
    }

    public void setLargestSize(String largestSize) {
        this.largestSize = largestSize;
    }

    public String getLargestSize() {
        return largestSize;
    }

    public void setCreated(long created) {
        this.created = created;
    }

    public long getCreated() {
        return created;
    }

    public void setDiscount(Object discount) {
        this.discount = discount;
    }

    public Object getDiscount() {
        return discount;
    }

    public void setDescription(Object description) {
        this.description = description;
    }

    public Object getDescription() {
        return description;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setOwnerId(Object ownerId) {
        this.ownerId = ownerId;
    }

    public Object getOwnerId() {
        return ownerId;
    }

    public void setImage2(String image2) {
        this.image2 = image2;
    }

    public String getImage2() {
        return image2;
    }

    public void setColor3(String color3) {
        this.color3 = color3;
    }

    public String getColor3() {
        return color3;
    }

    public void setColor1(String color1) {
        this.color1 = color1;
    }

    public String getColor1() {
        return color1;
    }

    public void setColor2(String color2) {
        this.color2 = color2;
    }

    public String getColor2() {
        return color2;
    }

    public void setMainImage(String mainImage) {
        this.mainImage = mainImage;
    }

    public String getMainImage() {
        return mainImage;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPrice() {
        return price;
    }

    public void setClass(String ___class) {
        this.___class = ___class;
    }

    public String get___class() {
        return ___class;
    }

    public void setFirstSize(String firstSize) {
        this.firstSize = firstSize;
    }

    public String getFirstSize() {
        return firstSize;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getSku() {
        return sku;
    }

    public void setUpdated(long updated) {
        this.updated = updated;
    }

    public long getUpdated() {
        return updated;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getObjectId() {
        return objectId;
    }

    @Override
    public String toString() {
        return
                "ShoeObject{" +
                        "image5 = '" + image5 + '\'' +
                        ",image3 = '" + image3 + '\'' +
                        ",image4 = '" + image4 + '\'' +
                        ",largestSize = '" + largestSize + '\'' +
                        ",created = '" + created + '\'' +
                        ",discount = '" + discount + '\'' +
                        ",description = '" + description + '\'' +
                        ",title = '" + title + '\'' +
                        ",ownerId = '" + ownerId + '\'' +
                        ",image2 = '" + image2 + '\'' +
                        ",color3 = '" + color3 + '\'' +
                        ",color1 = '" + color1 + '\'' +
                        ",color2 = '" + color2 + '\'' +
                        ",mainImage = '" + mainImage + '\'' +
                        ",price = '" + price + '\'' +
                        ",___class = '" + ___class + '\'' +
                        ",firstSize = '" + firstSize + '\'' +
                        ",sku = '" + sku + '\'' +
                        ",updated = '" + updated + '\'' +
                        ",objectId = '" + objectId + '\'' +
                        "}";
    }


    /*******************************************************************************************************************************************************
     * FAST ADAPTER CODE STARTS HERE
     *
     */


    @NonNull
    @Override
    public ShoeObjectViewHolder getViewHolder(View v) {
        return new ShoeObjectViewHolder(v);
    }

    @Override
    public int getType() {
        return R.id.homeFragmentBestDealsImgView;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.home_fragment_best_deals_rec_view_single_object;
    }


    @Override
    public void bindView(ShoeObjectViewHolder holder, List<Object> payloads) {
        super.bindView(holder, payloads);

        String priceText = "$" + price;
        holder.titleTextView.setText(getTitle());
        holder.priceTextView.setText(priceText);
        //test glide code
        Glide.with(holder.itemView).load(mainImage).apply(RequestOptions.bitmapTransform(new RoundedCornersTransformation(45, 0,
                RoundedCornersTransformation.CornerType.ALL))).apply(RequestOptions.placeholderOf(R.drawable.loading_default_img_square).fallback(R.drawable.fallback_product_img)
                .error(R.drawable.default_error_img)).into(holder.productImageView);


    }


    protected static class ShoeObjectViewHolder extends RecyclerView.ViewHolder {

        ImageView productImageView;
        ImageView cartImageView;
        TextView priceTextView, titleTextView;


        public ShoeObjectViewHolder(@NonNull View itemView) {
            super(itemView);

            productImageView = itemView.findViewById(R.id.homeFragmentBestDealsImgView);
            cartImageView = itemView.findViewById(R.id.homeFragmentBestDealsCartImgView);
            priceTextView = itemView.findViewById(R.id.homeFragmentBestDealsPriceTV);
            titleTextView = itemView.findViewById(R.id.homeFragmentBestDealsTitleTV);
        }
    }


}














/*
        RequestListener<Drawable> mRequestListener = new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {



                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {



                return false;
            }
        };


        Glide.with(holder.itemView).load(mainImage).addListener(mRequestListener).into(holder.productImageView);

*/






  /*
        Glide.with(context).load(...)
                .listener(object : RequestListener<Drawable> {
            override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                //TODO handle error images while loading photo
                return true
            }

            override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                //TODO use "resource" as the photo for your ImageView
                return true
            }

        }).submit()



         Glide.with(holder.itemView).load(mainImage).apply(RequestOptions.bitmapTransform(new RoundedCornersTransformation(45, 0,
                RoundedCornersTransformation.CornerType.BOTTOM))).into(holder.productImageView);

*/










        /*private BaseTarget target2 = new BaseTarget<BitmapDrawable>() {
  @Override
  public void onResourceReady(BitmapDrawable bitmap, Transition<? super BitmapDrawable> transition) {
    // do something with the bitmap
    // for demonstration purposes, let's set it to an imageview
    imageView2.setImageDrawable(bitmap);
  }

  @Override
  public void getSize(SizeReadyCallback cb) {
    cb.onSizeReady(250, 250);
  }

  @Override
  public void removeCallback(SizeReadyCallback cb) {}
};

private void loadImageSimpleTargetApplicationContext() {
  GlideApp
    .with(context.getApplicationContext()) // safer!
    .load(eatFoodyImages[1])
    .into(target2);
}*/




         /*RequestOptions options = new RequestOptions()
                .placeholder(R.drawable.artist_img_default).fallback(R.drawable.default_image_fallback_169)
                .error(R.drawable.default_error_img).bitmapTransform(new RoundedCornersTransformation(45, 0,
                        RoundedCornersTransformation.CornerType.BOTTOM));
         Glide.with(this).load(R.drawable.venues_saved_categories_img).apply(RequestOptions.bitmapTransform(new RoundedCornersTransformation(45, 0,
                RoundedCornersTransformation.CornerType.BOTTOM))).into(venuesButton);*/
/*

        Glide.with(holder.itemView).load(getProfilePicture()).apply(RequestOptions.bitmapTransform(new RoundedCornersTransformation(45, 0,
                RoundedCornersTransformation.CornerType.BOTTOM))).into(holder.artist_image_vh);


*/













/*<?xml version="1.0" encoding="utf-8"?>
<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:layout_width="wrap_content"
    android:layout_height="match_parent"
    android:orientation="vertical">


    <ImageView
        android:id="@+id/homeFragmentBestDealsImgView"
        android:layout_width="110dp"
        android:layout_height="110dp"
        android:layout_marginStart="32dp"
        android:layout_marginTop="28dp"
        android:background="@drawable/rounded_rect_8dp"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent" />


    <TextView
        android:id="@+id/homeFragmentBestDealsPriceTV"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_marginStart="12dp"
        android:layout_marginTop="10dp"
        android:fontFamily="@font/quicksand_bold"
        android:text="$86"
        android:textColor="#7E7A7A"
        android:textSize="12sp"
        app:layout_constraintStart_toStartOf="@+id/homeFragmentBestDealsImgView"
        app:layout_constraintTop_toBottomOf="@+id/homeFragmentBestDealsImgView" />


    <ImageView
        android:id="@+id/homeFragmentBestDealsCartImgView"
        android:layout_width="13dp"
        android:layout_height="13dp"
        android:layout_marginTop="10dp"
        android:layout_marginEnd="13dp"
        android:src="@drawable/shopping_bag_win10_gray"
        app:layout_constraintEnd_toEndOf="@+id/homeFragmentBestDealsImgView"
        app:layout_constraintTop_toBottomOf="@+id/homeFragmentBestDealsImgView" />


</androidx.constraintlayout.widget.ConstraintLayout>*/