package example.myapp01.adapter.itemRepresentation

import example.myapp01.itemRepresentation.ItemRepresentation
import example.myapp01.itemRepresentation.ItemWithViewType


class Representation {
    companion object {

        private val map = mutableMapOf<String, ItemRepresentation<ItemWithViewType>>()

        private val all = mutableListOf<ItemRepresentation<ItemWithViewType>>()

        operator fun get(representationKey: Int) = all[representationKey]
        val values: List<ItemRepresentation<ItemWithViewType>> = all
        fun representationKey(itemRepresentation: ItemRepresentation<out ItemWithViewType>): Int {
            if (ItemRepresentation.keyUndefined == itemRepresentation.helper.vt) {
                return map[itemRepresentation.javaClass.name]?.helper?.vt ?: TODO()
            }
            return itemRepresentation.helper.vt
        }

        @Suppress("UNCHECKED_CAST")
        private val <T : ItemRepresentation<out ItemWithViewType>> T.add: T
            get() {
                this as ItemRepresentation<ItemWithViewType>
                map[this.javaClass.name] = this
                this.helper.vt = all.size
                all.add(this)
                println("tmm01 " + this.helper.vt + " ${this.javaClass.name}")
                return this
            }


        val VH01A = VH01A({}).add
        val VH01AA = VH01AA().add
        val VH01B = VH01B({}, {}).add


    }
}
