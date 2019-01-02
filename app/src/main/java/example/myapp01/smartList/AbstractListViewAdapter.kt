package example.myapp01.smartList

import android.util.SparseArray
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter

abstract class AbstractListViewAdapter : BaseAdapter() {

    abstract val items: List<ListItemWithBinderId>

    private var listenersMap = SparseArray<BindedListener>()

    fun setListener(
        b: BinderSelector.AbstractBinder<out ListItemWithBinderId, out BinderSelector.ViewHolderI>,
        listener: BindedListener
    ) {
        listenersMap.put(b.runtimeId, listener)
    }

    override fun hasStableIds(): Boolean = false

    override fun getItemId(position: Int): Long = 0

    override fun getCount(): Int = items.size

    override fun getItem(position: Int): ListItemWithBinderId = items[position]

    //    override fun isEnabled(position: Int): Boolean = true

    override fun getItemViewType(position: Int): Int = items[position].binderId

    override fun getViewTypeCount(): Int = 100 // BinderSelector.AbstractBinder.items.size

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val convertViewTag = convertView?.tag
        val item = items[position]
        val binder = BinderSelector.findBinder(item.binderId)
        val holder: BinderSelector.ViewHolderI = if (binder.viewHolderClass.isInstance(convertViewTag)) {
            convertViewTag as BinderSelector.ViewHolderI
        } else {
            binder.createViewHolder(parent)
        }
        holder.view.tag = holder
        binder.onBind(item, holder, position, listenersMap.get(binder.runtimeId, null))
        return holder.view
    }
}
