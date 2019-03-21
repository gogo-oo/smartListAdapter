package example.myapp01.adapter.itemRepresentation

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import example.myapp01.R
import example.myapp01.adapter.item.ItemA
import example.myapp01.adapter.item.ItemB
import example.myapp01.itemRepresentation.ItemRepresentation
import example.myapp01.itemRepresentation.SimpleViewHolder

class VH01A(val onItemClick: (ItemA) -> Unit) : ItemRepresentation<ItemA>() {
    override fun createViewHolder(parent: ViewGroup) = ViewHolder(parent.viewBy(R.layout.text_row_item))

    override val helper = withPositionHelper<ViewHolder> { item, _ ->
        view.setOnClickListener { onItemClick(item) }
        textView.text = context.getString(R.string.itemA, item.label)
    }

    class ViewHolder(v: View) : SimpleViewHolder(v) {
        val textView = v.findViewById<TextView>(R.id.textView)
    }
}


class VH01B(val onItemClick: (ItemB) -> Unit, val onItemDClick: (Int) -> Unit) : ItemRepresentation<ItemB>() {
    override fun createViewHolder(parent: ViewGroup) = ViewHolder(parent.viewBy(R.layout.text_row_item))

    override val helper = withPositionHelper<ViewHolder> { item, position ->
        textView.text = context.getString(R.string.itemB, item.labl, position)
        view.setOnClickListener { onItemClick(item) }
        view.setOnLongClickListener { onItemDClick(position); true }
    }

    class ViewHolder(v: View) : SimpleViewHolder(v) {
        val textView = v.findViewById<TextView>(R.id.textView)
    }
}