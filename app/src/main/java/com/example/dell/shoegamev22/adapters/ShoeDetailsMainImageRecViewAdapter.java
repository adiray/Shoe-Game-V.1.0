package com.example.dell.shoegamev22.adapters;

import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.dell.shoegamev22.R;
import com.mikepenz.fastadapter.items.AbstractItem;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ShoeDetailsMainImageRecViewAdapter extends AbstractItem<ShoeDetailsMainImageRecViewAdapter, ShoeDetailsMainImageRecViewAdapter.ShoeImageVh> {


    private String imageReference;



    public String getImageReference() {
        return imageReference;
    }

    public void setImageReference(String imageReference) {
        this.imageReference = imageReference;
    }

    /*******************************************************************************************************************************************************
     * FAST ADAPTER CODE STARTS HERE
     *
     */



    @NonNull
    @Override
    public ShoeImageVh getViewHolder(View v) {
        return new ShoeImageVh(v);
    }

    @Override
    public int getType() {
        return R.id.shoeDetailsActivityMainImageRecViewImage;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.view_shoe_details_main_image_rec_view_single_object;
    }


    @Override
    public void bindView(ShoeImageVh holder, List<Object> payloads) {
        super.bindView(holder, payloads);


        //Glide.with(holder.itemView).load(imageReference).into(holder.productImageView);



        //test glide code
     /*   Glide.with(holder.itemView).load(imageReference).apply(RequestOptions.placeholderOf(R.drawable.loading_default_img_square).fallback(R.drawable.fallback_product_img)
                .error(R.drawable.default_error_img)).into(holder.productImageView);
*/

        Glide.with(holder.itemView).load(imageReference).apply(RequestOptions.placeholderOf(R.drawable.loading_default_img_square).error(R.drawable.default_error_img)).into(holder.productImageView);



    }





    static class ShoeImageVh extends RecyclerView.ViewHolder {

        ImageView productImageView;


        public ShoeImageVh(@NonNull View itemView) {
            super(itemView);

            productImageView = itemView.findViewById(R.id.shoeDetailsActivityMainImageRecViewImage);

        }
    }







}
