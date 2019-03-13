package example.myapp01.resources

import android.content.res.Resources
import example.myapp01.R
import example.mylib01.resources.ResourcesString

class ResourcesStringImpl(private val resources: Resources) : ResourcesString {

    override val str01: String get() = resources.getString(R.string.str01)
    override fun str02(arg1: String, arg2: String, arg3: String): String = resources.getString(R.string.str02, arg1, arg2, arg3)
    override fun str03(arg1: Int): String = resources.getString(R.string.str03, arg1)

}
