package example.myapp01.adapter.itemRepresentation

import example.myapp01.itemRepresentation.ItemRepresentation
import example.myapp01.itemRepresentation.ItemWithViewType


class Representation {
    companion object {

        private val map = mutableMapOf<String, ItemRepresentation<out ItemWithViewType>>()

        private val all = mutableListOf<ItemRepresentation<out ItemWithViewType>>()

        operator fun get(representationKey: Int) = all[representationKey]
        val values: List<ItemRepresentation<out ItemWithViewType>> = all
        fun representationKey(itemRepresentation: ItemRepresentation<out ItemWithViewType>): Int {
            if (ItemRepresentation.keyUndefined == itemRepresentation.helper.representationIndex) {
                return map[itemRepresentation.javaClass.name]?.helper?.representationIndex ?: TODO()
            }
            return itemRepresentation.helper.representationIndex
        }

        private inline val <reified T : ItemRepresentation<out ItemWithViewType>> T.add: T
            get() {
                map[this.javaClass.name] = this
                this.helper.representationIndex = all.size
                all.add(this)
                return this
            }


        val VH01A = VH01A({}).add
        val VH01AA = VH01AA().add
        val VH01B = VH01B({}, {}).add


    }
}
