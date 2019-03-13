package example.mylib01.resources

class AndResources {

    companion object {

        private lateinit var resourcesString: ResourcesString

        val string get() = resourcesString

        fun initialise(resourcesString: ResourcesString) {

            this.resourcesString = resourcesString

        }

    }

}
