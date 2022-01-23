package com.example.android.politicalpreparedness

import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.core.content.ContextCompat.startActivity
import android.net.Uri
import android.content.Intent
import android.text.TextUtils
import android.view.View

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