package com.example.task.utils

import android.content.Context
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import coil.ImageLoader
import coil.imageLoader
import coil.load
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.example.task.App
import com.example.task.di.AppComponent
import com.example.task.presentation.di.ViewModelFactory
import java.util.concurrent.CancellationException

public suspend inline fun <T> runSuspendCatching(block : () -> T) : Result<T> {
    return try {
        Result.success(block())
    } catch (e : CancellationException) {
        throw e
    } catch (e : Throwable) {
        Result.failure(e)
    }
}


public inline fun <reified T : ViewModel> Fragment.lazyViewModel(
    noinline create: (SavedStateHandle) -> T
) : Lazy<T> {
    return viewModels {
        ViewModelFactory(this, create)
    }
}

val Context.component : AppComponent
    get() = when(this) {
        is App -> component
        else -> applicationContext.component
    }

inline fun ImageView.loadCaching(
    data: Any?,
    imageLoader: ImageLoader = context.imageLoader,
    crossinline builder: ImageRequest.Builder.() -> Unit = {}
) = load(data, imageLoader) {
    memoryCachePolicy(CachePolicy.ENABLED)
    diskCachePolicy(CachePolicy.ENABLED)
    builder()
}
