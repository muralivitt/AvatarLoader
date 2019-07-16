package mindvalley.loader

import android.content.Context
import android.widget.ImageView
import mindvalley.demo.view.MainActivity
import org.junit.Assert.assertNotEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner


/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see [Testing documentation](http://d.android.com/tools/testing)
 */
@RunWith(RobolectricTestRunner::class)
class AvatarLoaderTest {
    var appContext: Context? = null

    @Before
    fun setup(){
        val activity = Robolectric.buildActivity(MainActivity::class.java!!)
        appContext = activity.get()
        assertNotEquals("appContext should not be Null",appContext)
    }


    @Test (expected = IllegalArgumentException::class)
    fun load_passNullUrl_shouldThrowException() {
        AvatarLoader(appContext!!).load(ImageView(appContext),null)
    }

    @Test (expected = IllegalArgumentException::class)
    fun load_passEmptyUrl_shouldThrowException() {
        AvatarLoader(appContext!!).load(ImageView(appContext),"")
    }

    @Test (expected = IllegalArgumentException::class)
    fun load_passInvalidUrl_shouldThrowException() {
        var invalidUrl = "../images/image1.png"
        AvatarLoader(appContext!!).load(ImageView(appContext),invalidUrl)
    }

    fun load_passInvalidUrl_shouldNotThrowException(){
        var validUrl = "http://valid/images/image1.png"
        AvatarLoader(appContext!!).load(ImageView(appContext),validUrl)
    }

    @Test (expected = IllegalArgumentException::class)
    fun load_passNullPlaceholder_shouldThrowException(){
        var validUrl = "http://valid/images/image1.png"
        AvatarLoader(appContext!!).load(ImageView(appContext),validUrl,null)
    }

    @Test
    fun load_passValidPlaceholder_shouldNotThrowException(){
        var validUrl = "http://valid/images/image1.png"
        AvatarLoader(appContext!!).load(ImageView(appContext),validUrl,R.drawable.notfound)
    }
}
