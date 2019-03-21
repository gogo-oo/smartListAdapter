package example.myapp01.adapter.itemRepresentation

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import example.myapp01.R
import example.myapp01.adapter.item.ItemAA
import example.myapp01.itemRepresentation.ItemRepresentation
import example.myapp01.itemRepresentation.SimpleViewHolder

class VH01AA : ItemRepresentation<ItemAA>() {
    override fun createViewHolder(parent: ViewGroup) = ViewHolder(parent.viewBy(R.layout.text_row_item))
    override val helper = simpleHelper<ViewHolder> {
        textView.text = context.getString(R.string.itemAA, it.label)
    }

    class ViewHolder(v: View) : SimpleViewHolder(v) {
        val textView = find<TextView>(R.id.textView)
    }
}