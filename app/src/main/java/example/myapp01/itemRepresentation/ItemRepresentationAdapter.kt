package example.myapp01.itemRepresentation


import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import androidx.recyclerview.widget.RecyclerView
import example.myapp01.MainActivity

interface ItemWithViewType {
    val viewType: Int
}

open class SimpleViewHolder(val view: View) : RecyclerView.ViewHolder(view)


class ItemRepresentationMap {
    private val sparseArray = SparseArray<ItemRepresentation>()

    val size get() = sparseArray.size()

    operator fun set(representationKey: MainActivity.Representation, value: ItemRepresentation) =
        sparseArray.append(representationKey.ordinal, value)

    operator fun set(representationKey: Int, value: ItemRepresentation) = sparseArray.append(representationKey, value)

    operator fun get(representationKey: Int) = sparseArray.get(representationKey)

}


interface ItemRepresentation {
    interface BindHelper {
        fun onBind(viewHolder: SimpleViewHolder, item: ItemWithViewType, position: Int)
    }

    fun createViewHolder(parent: ViewGroup): SimpleViewHolder

    val bindHelper: BindHelper

    fun ViewGroup.viewBy(layoutId: Int): View {
        return LayoutInflater.from(this.context).inflate(layoutId, this, false)
    }

}


fun <VHT : SimpleViewHolder, IT : ItemWithViewType> BindHelperWithPosition(bindBlock: VHT.(IT, Int) -> Unit): ItemRepresentation.BindHelper {
    return object : ItemRepresentation.BindHelper {
        @Suppress("UNCHECKED_CAST")
        override fun onBind(viewHolder: SimpleViewHolder, item: ItemWithViewType, position: Int) {
            (viewHolder as VHT).bindBlock(item as IT, position)
        }
    }
}

fun <VHT : SimpleViewHolder, IT : ItemWithViewType> SimpleBindHelper(bindBlock: VHT.(IT) -> Unit): ItemRepresentation.BindHelper {
    return object : ItemRepresentation.BindHelper {
        @Suppress("UNCHECKED_CAST")
        override fun onBind(viewHolder: SimpleViewHolder, item: ItemWithViewType, position: Int) {
            (viewHolder as VHT).bindBlock(item as IT)
        }
    }
}


abstract class ItemRepresentationRecyclerViewAdapter : RecyclerView.Adapter<SimpleViewHolder>() {

    abstract val items: List<ItemWithViewType>

    abstract val itemRepresentationMap: ItemRepresentationMap

    override fun getItemCount(): Int = items.size
    override fun getItemViewType(position: Int): Int = items[position].viewType

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SimpleViewHolder {
        val viewHolder = itemRepresentationMap[viewType]?.createViewHolder(parent)
        if (viewHolder is SimpleViewHolder) {
            return viewHolder
        }
        throw Exception("Error: unsupported  viewType=$viewType")
    }

    override fun onBindViewHolder(viewHolder: SimpleViewHolder, position: Int) {
        val item = items[position]
        itemRepresentationMap[item.viewType]?.bindHelper?.onBind(viewHolder, item, position)
    }

}

abstract class ItemRepresentationListViewAdapter : BaseAdapter() {

    abstract val items: List<ItemWithViewType>

    abstract val itemRepresentationMap: ItemRepresentationMap

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
        val holder: SimpleViewHolder = if (convertViewTag is SimpleViewHolder) {
            convertViewTag as SimpleViewHolder
        } else {
            itemRepresentationMap[item.viewType]?.createViewHolder(parent) ?: throw  Exception("!!!!!!")
        }
        holder.view.tag = holder
        itemRepresentationMap[item.viewType]?.bindHelper?.onBind(holder, item, position)
        return holder.view
    }
}
