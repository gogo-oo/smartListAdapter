package example.myapp01.resources

import android.app.Application
import example.mylib01.resources.AndResources

class AndroidResourcesContext {

    companion object {

        fun onCreateApplication(app: Application) {

            AndResources.initialise(ResourcesStringImpl(app.resources))

        }

    }

}
