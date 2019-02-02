package example.myapp01.adapter.itemRepresentation

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import example.myapp01.R
import example.myapp01.itemRepresentation.ItemRepresentationView
import example.myapp01.itemRepresentation.RecyclerViewHolder

class TextViewItemRepresentation(
    @LayoutRes val layoutId: Int = R.layout.text_row_item,
    @IdRes val viewId: Int = View.NO_ID,
    onClick: (String) -> Unit = {}
) : ItemRepresentationView<String>() {

    override fun createViewHolder(parent: ViewGroup) = ViewHolder(parent.viewBy(layoutId, viewId))

    override val helper = withPositionHelper<ViewHolder> { item, position ->
        textView.text = item
        itemView.setOnClickListener { onClick("click on >$item $position<") }
    }

    class ViewHolder(v: View) : RecyclerViewHolder(v) {
        val textView: TextView

        init {
            if (itemView is TextView) {
                textView = itemView
            } else {
                textView = v.findViewById(R.id.textView)
            }
        }
    }
}