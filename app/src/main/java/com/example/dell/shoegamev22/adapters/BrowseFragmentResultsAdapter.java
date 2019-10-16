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

public class BrowseFragmentResultsAdapter extends AbstractItem<BrowseFragmentResultsAdapter, BrowseFragmentResultsAdapter.BrowseFragmentResultsAdapterViewHolder> {


    private String title;
    private String mainImage;
    private String price;
    private String itemId;


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

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }


    /*******************************************************************************************************************************************************
     * FAST ADAPTER CODE STARTS HERE
     *
     */


    @NonNull
    @Override
    public BrowseFragmentResultsAdapterViewHolder getViewHolder(View v) {
        return new BrowseFragmentResultsAdapterViewHolder(v);
    }

    @Override
    public int getType() {
        return R.id.browseFragmentResultsRecViewSingleObjectImgView;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.browse_fragment_results_rec_view_single_item;
    }


    @Override
    public void bindView(BrowseFragmentResultsAdapterViewHolder holder, List<Object> payloads) {
        super.bindView(holder, payloads);

        String priceText = "$" + price;
        holder.titleTextView.setText(getTitle());
        holder.priceTextView.setText(priceText);
        //test glide code
        Glide.with(holder.itemView).load(mainImage).apply(RequestOptions.bitmapTransform(new RoundedCornersTransformation(45, 0,
                RoundedCornersTransformation.CornerType.ALL))).apply(RequestOptions.placeholderOf(R.drawable.loading_default_img_square).fallback(R.drawable.fallback_product_img)
                .error(R.drawable.default_error_img)).into(holder.productImageView);


    }


    protected static class BrowseFragmentResultsAdapterViewHolder extends RecyclerView.ViewHolder {


        ImageView productImageView;
        ImageView cartImageView;
        TextView priceTextView, titleTextView;

        public BrowseFragmentResultsAdapterViewHolder(@NonNull View itemView) {
            super(itemView);


            productImageView = itemView.findViewById(R.id.browseFragmentResultsRecViewSingleObjectImgView);
            cartImageView = itemView.findViewById(R.id.browseFragmentResultsRecViewSingleObjectCartImgView);
            priceTextView = itemView.findViewById(R.id.browseFragmentResultsRecViewSingleObjectPriceTV);
            titleTextView = itemView.findViewById(R.id.browseFragmentResultsRecViewSingleObjectTitleTV);


        }
    }


}



