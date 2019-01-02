package example.myapp01.smartList

import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import example.myapp01.R


typealias BinderId = Int

typealias BindedListener = Any?

interface ListItemWithBinderId {
    val binderId: BinderId
}

abstract class ListItemByBinder(b: BinderSelector.AbstractBinder<out ListItemWithBinderId, out BinderSelector.ViewHolderI>) :
    ListItemWithBinderId {
    override val binderId: BinderId = b.runtimeId
}

class BinderSelector {

    interface ViewHolderI {
        val view: View
    }

    // End supertype of this class must be only 'object' : 'object ... : BinderSelector.BinderA()...'
    abstract class AbstractBinder<IT : ListItemWithBinderId, VHT : ViewHolderI> {

        companion object {
            val items = ArrayList<AbstractBinder<out ListItemWithBinderId, out ViewHolderI>>()
        }


        //        abstract val itemClass: Class<IT>
        abstract val viewHolderClass: Class<VHT>
////        abstract val onItemClick: (VHT) -> Unit

        val runtimeId: BinderId = items.size

        init {
            items.add(this)
        }

        abstract fun createViewHolder(parent: ViewGroup): VHT

        abstract fun onBind(
            item: ListItemWithBinderId, viewHolder: ViewHolderI, position: Int, bindedListener: BindedListener
        )
    }

    companion object {

        @Suppress("NOTHING_TO_INLINE")
        inline fun findBinder(binderId: BinderId): AbstractBinder<out ListItemWithBinderId, out ViewHolderI> {
            return AbstractBinder.items[binderId]
        }
    }
}


fun <IT : ListItemWithBinderId, VHT : BinderSelector.ViewHolderI> BindHelper(
    itemClass: Class<IT>,
    viewHolderClass: Class<VHT>,
    layoutId: Int,
    vhNew: (View) -> VHT,
    bindBlock: (IT, VHT, Int) -> Unit
): BinderSelector.AbstractBinder<IT, VHT> {
    return object : BinderSelector.AbstractBinder<IT, VHT>() {
        override fun createViewHolder(parent: ViewGroup): VHT {
            val v = LayoutInflater.from(parent.context).inflate(R.layout.text_row_item, parent, false)
            return vhNew(v)
        }

        override fun onBind(
            item: ListItemWithBinderId,
            viewHolder: BinderSelector.ViewHolderI,
            position: Int,
            bindedListener: BindedListener
        ) {

            @Suppress("UNCHECKED_CAST")
            if (viewHolderClass.isInstance(viewHolder)) {
                val vh = viewHolder as VHT
                if (itemClass.isInstance(item)) {
                    val i = item as IT
                    bindBlock(i, vh, position)
                }
            }
        }

        override val viewHolderClass = viewHolderClass
//        override val itemClass = itemClass
    }
}


fun <IT : ListItemWithBinderId, VHT : BinderSelector.ViewHolderI, LT : BindedListener> BindHelper(
    itemClass: Class<IT>,
    viewHolderClass: Class<VHT>,
    layoutId: Int,
    bindedListenerClass: Class<LT>,
    vhNew: (View) -> VHT,
    bindBlock: (IT, VHT, Int, LT?) -> Unit
): BinderSelector.AbstractBinder<IT, VHT> {
    return object : BinderSelector.AbstractBinder<IT, VHT>() {
        override fun createViewHolder(parent: ViewGroup): VHT {
            val v = LayoutInflater.from(parent.context).inflate(R.layout.text_row_item, parent, false)
            return vhNew(v)
        }

        override fun onBind(
            item: ListItemWithBinderId,
            viewHolder: BinderSelector.ViewHolderI,
            position: Int,
            bindedListener: BindedListener
        ) {

            @Suppress("UNCHECKED_CAST")
            if (viewHolderClass.isInstance(viewHolder)) {
                val vh = viewHolder as VHT
                if (itemClass.isInstance(item)) {
                    val i = item as IT
                    if (bindedListenerClass.isInstance(bindedListener)) {
                        bindBlock(i, vh, position, bindedListener as LT)
                    } else {
                        bindBlock(i, vh, position, null)
                    }
                }
            }
        }

        override val viewHolderClass = viewHolderClass
    }
}


abstract class AbstractRecyclerViewAdapter : RecyclerView.Adapter<AbstractRecyclerViewAdapter.ViewHolder>() {
    abstract val items: List<ListItemWithBinderId>
    private var listenersMap = SparseArray<BindedListener>()

    fun setListener(
        b: BinderSelector.AbstractBinder<out ListItemWithBinderId, out BinderSelector.ViewHolderI>,
        listener: BindedListener
    ) {
        listenersMap.put(b.runtimeId, listener)
    }

    override fun getItemCount(): Int = items.size
    override fun getItemViewType(position: Int): Int = items[position].binderId

    override fun onCreateViewHolder(parent: ViewGroup, binderId: Int): ViewHolder {
        val viewHolder = BinderSelector.findBinder(binderId).createViewHolder(parent)
        if (viewHolder is ViewHolder) {
            return viewHolder
        }
        throw Exception("!!!!")
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val item = items[position]
        BinderSelector.findBinder(item.binderId)
            .onBind(item, viewHolder, position, listenersMap.get(item.binderId, null))
    }

    open class ViewHolder(override val view: View) : RecyclerView.ViewHolder(view), BinderSelector.ViewHolderI

}
