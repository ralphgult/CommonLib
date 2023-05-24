package com.xlg.commonlibs.ext

import android.graphics.drawable.Drawable
import android.widget.ImageView
import coil.load
import coil.size.ViewSizeResolver
import coil.transform.CircleCropTransformation
import coil.transform.RoundedCornersTransformation

fun ImageView.intoCorners(url: Any, radius: Float, placeHolder: Int = 0) {
    into(
        url = url,
        topLeft = radius,
        topRight = radius,
        bottomLeft = radius,
        bottomRight = radius,
        placeHolder = placeHolder
    )
}

fun ImageView.intoCorners(
    url: Any,
    topLeft: Float = 0f,
    topRight: Float = 0f,
    bottomLeft: Float = 0f,
    bottomRight: Float = 0f,
    placeHolder: Int = 0
) {
    this.into(
        url,
        topLeft = topLeft,
        topRight = topRight,
        bottomLeft = bottomLeft,
        bottomRight = bottomRight,
        placeHolder = placeHolder
    )
}

fun ImageView.intoCircle(url: Any, placeHolder: Int = 0) {
    this.load(url) {
        placeholder(placeHolder)
        error(placeHolder)
        transformations(CircleCropTransformation())
    }
}

fun ImageView.into(
    url: Any,
    topLeft: Float = 0f,
    topRight: Float = 0f,
    bottomLeft: Float = 0f,
    bottomRight: Float = 0f,
    success: ((drawable: Drawable) -> Unit)? = null,
    error: ((drawable: Drawable?) -> Unit)? = null,
    start: ((drawable: Drawable?) -> Unit)? = null,
    placeHolder: Int = 0
) {
    this.load(url) {
        placeholder(placeHolder)
        error(placeHolder)
        size(ViewSizeResolver(this@into))
        crossfade(true)
        if(topLeft > 0f || topRight > 0f || bottomLeft > 0f || bottomRight > 0f) {
            transformations(
                RoundedCornersTransformation(
                    topLeft,
                    topRight,
                    bottomLeft,
                    bottomRight
                )
            )
        }
        target(
            onSuccess = { drawable ->
                success?.invoke(drawable)
                load(drawable)
            },
            onError = { error?.invoke(it) },
            onStart = { start?.invoke(it) }
        )
    }
}

fun ImageView.intoCache(url: Any, placeHolder: Int = 0) {
    this.load(url) {
        placeholder(placeHolder)
        error(placeHolder)
        crossfade(true)
    }
}

