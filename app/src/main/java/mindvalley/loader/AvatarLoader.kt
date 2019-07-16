package mindvalley.loader

import android.content.Context
import android.graphics.Bitmap
import android.os.Handler
import android.util.LruCache
import android.widget.ImageView
import java.util.*
import java.util.Collections.synchronizedMap
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


class AvatarLoader (context: Context)  {

    private val maxCacheSize: Int = (Runtime.getRuntime().maxMemory() / 1024).toInt()/8
    private val memoryCache: LruCache<String, Bitmap>

    private val executorService: ExecutorService

    private val imageViewMap = synchronizedMap(WeakHashMap<ImageView, String>())
    private val handler: Handler

    private var imageWidth : Int = 0
    private var imageHeight : Int = 0

    init {
        memoryCache = object : LruCache<String, Bitmap>(maxCacheSize) {
            override fun sizeOf(key: String, bitmap: Bitmap): Int {
                // The cache size will be measured in kilobytes rather than number of items.
                return bitmap.byteCount / 1024
            }
        }

        executorService = Executors.newFixedThreadPool(5, Utils.ImageThreadFactory())
        handler = Handler()

        val metrics = context.resources.displayMetrics
        screenWidth = metrics.widthPixels
        screenHeight = metrics.heightPixels
    }

    companion object {

        private var INSTANCE: AvatarLoader? = null

        internal var screenWidth = 0
        internal var screenHeight = 0

        @Synchronized
        fun with(context: Context): AvatarLoader {
            require(context != null) {
                "AvatarLoader:with - Context should not be null."
            }
            return INSTANCE ?: AvatarLoader(context).also {
                INSTANCE = it
            }
        }
    }

    fun load(imageView: ImageView?, imageUrl: String?,drawableId: Int?) {

        require(imageView != null) {
            "AvatarLoader:load - ImageView should not be null."
        }

        require(imageUrl != null && imageUrl.isNotEmpty()) {
            "AvatarLoader:load - Image Url should not be empty"
        }

        require(imageUrl.startsWith("http")) {
            "AvatarLoader:loadImageIntoImageView - ImageUrl should be valid"
        }

        require(drawableId != null) {
            "AvatarLoader:loadImageIntoImageView - Bitmap should not be null"
        }

        if(drawableId > 0)
            imageView.setImageResource(drawableId)

        imageViewMap[imageView] = imageUrl

        val bitmap = getImageFromCache(imageUrl)
        bitmap?.let {
            load(imageView, it, imageUrl)
        } ?: run {
            executorService.submit(PhotosLoader(ImageRequest(imageUrl, imageView)))
        }
    }

    fun load(imageView: ImageView?,imageUrl: String?) {
        load(imageView,imageUrl,0)
    }

    @Synchronized
    private fun load(imageView: ImageView?, bitmap: Bitmap?, imageUrl: String?) {

        require(bitmap != null) {
            "AvatarLoader:loadImageIntoImageView - Bitmap should not be null"
        }

        require(imageView != null) {
            "AvatarLoader:loadImageIntoImageView - ImageView should not be null"
        }

        require(imageUrl != null) {
            "AvatarLoader:loadImageIntoImageView - ImageUrl should not be null"
        }

        require(imageUrl.startsWith("http")) {
            "AvatarLoader:loadImageIntoImageView - ImageUrl should be valid"
        }

        if(imageWidth == 0){
            imageWidth = imageView.width
        }

        if(imageHeight == 0){
            imageHeight = imageView.height
        }

        val scaledBitmap = Utils.scaleBitmapForLoad(bitmap, imageWidth, imageHeight)

        scaledBitmap?.let {
            if(!isImageViewReused(ImageRequest(imageUrl, imageView))) imageView.setImageBitmap(scaledBitmap)
        }
    }

    private fun isImageViewReused(imageRequest: ImageRequest): Boolean {
        val tag = imageViewMap[imageRequest.imageView]
        return tag == null || tag != imageRequest.imgUrl
    }

    @Synchronized
    private fun getImageFromCache(imageUrl: String): Bitmap? = memoryCache.get(imageUrl)

    inner class DisplayBitmap(private var imageRequest: ImageRequest) : Runnable {
        override fun run() {
            if(!isImageViewReused(imageRequest)) load(imageRequest.imageView, getImageFromCache(imageRequest.imgUrl), imageRequest.imgUrl)
        }
    }

    inner class ImageRequest(var imgUrl: String, var imageView: ImageView)

    inner class PhotosLoader(private var imageRequest: ImageRequest) : Runnable {

        override fun run() {

            if(isImageViewReused(imageRequest)) return

            val bitmap = Utils.downloadBitmapFromURL(imageRequest.imgUrl)
            memoryCache.put(imageRequest.imgUrl, bitmap)

            if(isImageViewReused(imageRequest)) return

            val displayBitmap = DisplayBitmap(imageRequest)
            handler.post(displayBitmap)
        }
    }
}


