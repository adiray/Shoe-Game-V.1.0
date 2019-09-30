package com.example.dell.shoegamev22.adapters;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.dell.shoegamev22.R;
import com.mikepenz.fastadapter.items.AbstractItem;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class HomeFragmentRecommendationsAdapter extends AbstractItem<HomeFragmentRecommendationsAdapter, HomeFragmentRecommendationsAdapter.HomeFragRecommendationsViewHolder> {

    private String title;
    private String mainImage;
    private String price;






    //GETTERS AND SETTERS

    public String getMainImage() {
        return mainImage;
    }

    public void setMainImage(String mainImage) {
        this.mainImage = mainImage;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }


    /*******************************************************************************************************************************************************
     * FAST ADAPTER CODE STARTS HERE
     *
     */


    @NonNull
    @Override
    public HomeFragRecommendationsViewHolder getViewHolder(View v) {
        return new HomeFragRecommendationsViewHolder(v);
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
    public void bindView(HomeFragRecommendationsViewHolder holder, List<Object> payloads) {
        super.bindView(holder, payloads);

        String priceText = "$" + price;
        holder.titleTextView.setText(getTitle());
        holder.priceTextView.setText(priceText);
        //test glide code
        Glide.with(holder.itemView).load(mainImage).apply(RequestOptions.bitmapTransform(new RoundedCornersTransformation(45, 0,
                RoundedCornersTransformation.CornerType.ALL))).apply(RequestOptions.placeholderOf(R.drawable.loading_default_img_square).fallback(R.drawable.fallback_product_img)
                .error(R.drawable.default_error_img)).into(holder.productImageView);


    }


    protected static class HomeFragRecommendationsViewHolder extends RecyclerView.ViewHolder {

        ImageView productImageView;
        ImageView cartImageView;
        TextView priceTextView, titleTextView;


        public HomeFragRecommendationsViewHolder(@NonNull View itemView) {
            super(itemView);

            productImageView = itemView.findViewById(R.id.homeFragmentBestDealsImgView);
            cartImageView = itemView.findViewById(R.id.homeFragmentBestDealsCartImgView);
            priceTextView = itemView.findViewById(R.id.homeFragmentBestDealsPriceTV);
            titleTextView = itemView.findViewById(R.id.homeFragmentBestDealsTitleTV);
        }
    }


}
