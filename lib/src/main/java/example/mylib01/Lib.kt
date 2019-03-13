package example.mylib01

import example.mylib01.resources.AndResources

class Lib {
    companion object {

        fun list01(): List<String> = Array(15) { i -> AndResources.string.str03(i) }.asList()
        fun str01(): String = AndResources.string.str01

    }
}
