package example.myapp01

import android.app.Application
import example.myapp01.resources.AndroidResourcesContext

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        AndroidResourcesContext.onCreateApplication(this)
    }
}
