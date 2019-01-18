package example.myapp01.adapter.itemRepresentation

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import example.myapp01.R
import example.myapp01.adapter.item.ItemAA
import example.myapp01.itemRepresentation.ItemRepresentation
import example.myapp01.itemRepresentation.SimpleViewHolder

class VH01AA : ItemRepresentation<ItemAA> {
    override fun createViewHolder(parent: ViewGroup) = ViewHolder(parent.viewBy(R.layout.text_row_item))
    override val helper = Helper(ViewHolder::class.java, ItemAA::class.java) {
        textView.text = "AA " + it.label
    }

    class ViewHolder(v: View) : SimpleViewHolder(v) {
        val textView = v.findViewById<TextView>(R.id.textView)
    }
}