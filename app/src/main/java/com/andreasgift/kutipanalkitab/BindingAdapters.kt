package com.andreasgift.kutipanalkitab

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.squareup.picasso.Picasso

object BindingAdapters {

    /**
     * Bind image to layout by providing imageUrl
     */
    @BindingAdapter("app:imageUrl")
    @JvmStatic
    fun setImage(view: ImageView, imageUrl: String?) {
        Picasso.get()
            .load(imageUrl)
            .fit()
            .centerCrop()
            .error(R.drawable.defaultbg)
            .into(view)
    }
}