package com.example.android.politicalpreparedness

import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.core.content.ContextCompat.startActivity
import android.net.Uri
import android.content.Intent
import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import androidx.core.net.toUri
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

@BindingAdapter("externalUrl")
fun bindUrlToTextView(textView: TextView, url: String?) {
    if (TextUtils.isEmpty(url)) {
        textView.visibility = View.GONE
    } else {
        textView.visibility = View.VISIBLE
        textView.setOnClickListener {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(textView.context, browserIntent, null)
        }
    }
}

@BindingAdapter("dataSavedState")
fun bindSavedStateToTextView(textView: TextView, isSaved: Boolean) {
    when (isSaved) {
        true -> textView.setText(R.string.button_label_unfollow_election)
        false -> textView.setText(R.string.button_label_follow_election)
    }
}

/**
 * Use Glide library to load an image from url into an [ImageView]
 */
@BindingAdapter("imageUrl")
fun bindUrlToImage(imgView: ImageView, imgUrl: String?) {
    imgUrl?.let {
        val imgUri = imgUrl.toUri().buildUpon().scheme("https").build()
        Glide.with(imgView.context)
            .load(imgUri)
            .apply(
                RequestOptions()
                    .placeholder(R.drawable.loading_animation)
                    .error(R.drawable.ic_broken_image)
                    .circleCrop()
            )
            .into(imgView)
    }
}