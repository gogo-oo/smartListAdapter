package example.myapp01.itemRepresentation


import android.content.Context
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import androidx.annotation.ColorRes
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import example.myapp01.adapter.itemRepresentation.Representation


interface ItemWithViewType {
    val viewType: Int

    val ItemWithViewType.T get() = Representation

    operator fun <IT> ItemRepresentation<IT>.invoke(i: IT) = checkType(i)

    fun <IT> ItemRepresentation<IT>.checkType(item: IT): Int {
        return helper.representationIndex
    }
}

typealias RecyclerViewHolder = RecyclerView.ViewHolder

open class SimpleViewHolder(val view: View) : RecyclerViewHolder(view) {

    protected inline fun <reified V : View> RecyclerViewHolder.find(@IdRes viewId: Int): V {
        return itemView.findViewById(viewId)
    }
}

interface ItemRepresentationMap {
    val size: Int
    operator fun get(representationKey: Int): ItemRepresentation<ItemWithViewType>
}

class ItemRepresentationMapImpl : ItemRepresentationMap {
    private val sparseArray = SparseArray<ItemRepresentation<ItemWithViewType>>()

    override val size get() = sparseArray.size()

//    @Suppress("UNCHECKED_CAST")
//    operator fun set(representationKey: Representation, value: ItemRepresentation<out ItemWithViewType>) =
//        sparseArray.append(representationKey.ordinal, value as ItemRepresentation<ItemWithViewType>)

    @Suppress("UNCHECKED_CAST")
    operator fun set(representationKey: Int, value: ItemRepresentation<out ItemWithViewType>) =
        sparseArray.append(representationKey, value as ItemRepresentation<ItemWithViewType>)

    override operator fun get(representationKey: Int) = sparseArray.get(representationKey) ?: emptyItemRepresentation

}

val emptyItemRepresentation = object : ItemRepresentation<ItemWithViewType>() {
    override fun createViewHolder(parent: ViewGroup): RecyclerViewHolder {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override val helper: ItemRepresentation.Helper<ItemWithViewType>
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.

}

interface ItemRepresentationTools {

    /*protected inline*/ fun ViewGroup.viewBy(@LayoutRes layoutId: Int): View {
        return LayoutInflater.from(this.context).inflate(layoutId, this, false)
    }

    /*protected inline*/ fun ViewGroup.viewBy(@LayoutRes layoutId: Int, @IdRes viewId: Int): View {
        if (View.NO_ID == viewId) {
            return viewBy(layoutId)
        }
        return this.findViewById(viewId) ?: return viewBy(layoutId)
    }

    /*protected inline*/ val RecyclerViewHolder.context: Context get() = itemView.context

    /*protected inline*/ fun Context.color(@ColorRes colorResourceId: Int): Int = ContextCompat.getColor(this, colorResourceId)

//    /*protected inline*/ fun Context.colorStateList(@ColorRes colorResourceId: Int): android.content.res.ColorStateList? = ContextCompat.getColorStateList(this, colorResourceId)

    /*protected inline*/ fun View.visibilityVisible() {
        this.visibility = View.VISIBLE
    }

    /*protected inline*/ fun View.visibilityInvisible() {
        this.visibility = View.INVISIBLE
    }

    /*protected inline*/ fun View.visibilityGone() {
        this.visibility = View.GONE
    }

}

abstract class ItemRepresentationView<IT> : ItemRepresentation<IT>() {
    private var viewHolder: RecyclerViewHolder? = null

    fun update(parent: ViewGroup, item: IT) {
        var vh = viewHolder
        if (null == vh) {
            vh = createViewHolder(parent)
            viewHolder = vh
        }
        helper.onBind(vh, item, 0)
    }
}

abstract class ItemRepresentation<IT> : ItemRepresentationTools {
    companion object {
        const val keyUndefined = -1
    }

    interface Helper<IT> {
        var representationIndex: Int
        fun onBind(viewHolder: RecyclerViewHolder, item: IT, position: Int)
    }

    internal abstract fun createViewHolder(parent: ViewGroup): RecyclerViewHolder

    internal abstract val helper: Helper<IT>

    protected inline fun <reified VHT : RecyclerViewHolder> simpleHelper(crossinline bindBlock: VHT.(IT) -> Unit) =
        object : ItemRepresentation.Helper<IT> {
            override var representationIndex: Int = ItemRepresentation.keyUndefined
            override fun onBind(viewHolder: RecyclerViewHolder, item: IT, position: Int) {
                if (viewHolder is VHT) {
                    viewHolder.bindBlock(item)
                } else {
                    TODO()
                }
            }
        }

    protected inline fun <reified VHT : RecyclerViewHolder> withPositionHelper(crossinline bindBlock: VHT.(IT, Int) -> Unit) =
        object : ItemRepresentation.Helper<IT> {
            override var representationIndex: Int = ItemRepresentation.keyUndefined

            override fun onBind(viewHolder: RecyclerViewHolder, item: IT, position: Int) {
                if (viewHolder is VHT) {
                    viewHolder.bindBlock(item, position)
                } else {
                    TODO()
                }
            }
        }
}


abstract class ItemRepresentationRecyclerViewAdapter : MultiItemTypeRecyclerViewAdapter<ItemWithViewType>() {

    private val itemRepresentationMap = ItemRepresentationMapImpl()

    @Suppress("UNCHECKED_CAST")
    infix fun configureItemRepresentation(ir: ItemRepresentation<out ItemWithViewType>) {
        itemRepresentationMap[Representation.representationKey(ir)] = ir
    }

    @Suppress("UNCHECKED_CAST")
    override fun itemRepresentation(viewType: Int): ItemRepresentation<ItemWithViewType> {
        val ir = itemRepresentationMap[viewType]
        if (emptyItemRepresentation == ir) {
            return Representation[viewType] as ItemRepresentation<ItemWithViewType>
        }
        return ir
    }

    override fun getItemViewType(position: Int) = items[position].viewType
}

abstract class MultiItemTypeRecyclerViewAdapter<IT> : RecyclerView.Adapter<RecyclerViewHolder>() {

    abstract val items: List<IT>

    protected abstract fun itemRepresentation(viewType: Int): ItemRepresentation<IT>

    override fun getItemCount(): Int = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        itemRepresentation(viewType).createViewHolder(parent)

    override fun onBindViewHolder(viewHolder: RecyclerViewHolder, position: Int) =
        itemRepresentation(getItemViewType(position)).helper.onBind(viewHolder, items[position], position)
}

abstract class SingleItemTypeRecyclerViewAdapter<IT>(val itemRepresentation: ItemRepresentation<IT>) :
    RecyclerView.Adapter<RecyclerViewHolder>() {

    abstract val items: List<IT>

    override fun getItemCount(): Int = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = itemRepresentation.createViewHolder(parent)

    override fun onBindViewHolder(viewHolder: RecyclerViewHolder, position: Int) =
        itemRepresentation.helper.onBind(viewHolder, items[position], position)
}

abstract class ItemRepresentationListViewAdapter : BaseAdapter() {

    abstract val items: List<ItemWithViewType>

    abstract val itemRepresentationMap: ItemRepresentationMapImpl

    override fun hasStableIds(): Boolean = false

    override fun getItemId(position: Int): Long = 0

    override fun getCount(): Int = items.size

    override fun getItem(position: Int): ItemWithViewType = items[position]

    //    override fun isEnabled(position: Int): Boolean = true

    override fun getItemViewType(position: Int): Int = items[position].viewType

    override fun getViewTypeCount(): Int = itemRepresentationMap.size

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val convertViewTag = convertView?.tag
        val item = items[position]
        val holder: RecyclerViewHolder = if (convertViewTag is RecyclerViewHolder) {
            convertViewTag
        } else {
            itemRepresentationMap[item.viewType].createViewHolder(parent)
        }
        holder.itemView.tag = holder
        itemRepresentationMap[item.viewType].helper.onBind(holder, item, position)
        return holder.itemView
    }
}
