package example.myapp01.adapter.itemRepresentation

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import example.myapp01.R
import example.myapp01.adapter.item.ItemA
import example.myapp01.adapter.item.ItemB
import example.myapp01.itemRepresentation.ItemRepresentation
import example.myapp01.itemRepresentation.SimpleViewHolder

class VH01A(val onItemClick: (ItemA) -> Unit) : ItemRepresentation<ItemA> {
    override fun createViewHolder(parent: ViewGroup) =
        ViewHolder(parent.viewBy(R.layout.text_row_item))

    override val helper = HelperPlusPosition(ViewHolder::class.java, ItemA::class.java) { item, _ ->
        view.setOnClickListener { onItemClick(item) }
        textView.text = "A " + item.label
    }

    class ViewHolder(v: View) : SimpleViewHolder(v) {
        val textView = v.findViewById<TextView>(R.id.textView)
    }
}


class VH01B(val onItemClick: (ItemB) -> Unit, val onItemDClick: (Int) -> Unit) :
    ItemRepresentation<ItemB> {
    override fun createViewHolder(parent: ViewGroup) =
        ViewHolder(parent.viewBy(R.layout.text_row_item))

    override val helper = HelperPlusPosition(ViewHolder::class.java, ItemB::class.java) { item, position ->
        textView.text = "B ${item.labl} $position"
        view.setOnClickListener { onItemClick(item) }
        view.setOnLongClickListener { onItemDClick(position); true }
    }

    class ViewHolder(v: View) : SimpleViewHolder(v) {
        val textView = v.findViewById<TextView>(R.id.textView)
    }
}