package example.myapp01.adapter.item

import example.myapp01.itemRepresentation.ItemWithViewType

class ItemAA(val label: String) : ItemWithViewType {
    override val viewType: Int = T.VH01AA(this)
}

class ItemA(val label: String) : ItemWithViewType {
    override val viewType: Int = T.VH01A(this)


}

class ItemB(val labl: String) : ItemWithViewType {
    override val viewType: Int = T.VH01B(this)
}