package example.myapp01

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import example.myapp01.itemRepresentation.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        val representationMap = ItemRepresentationMap()
        representationMap[Representation.RItemA] = VH01A { println("MainActivity.onItemClick " + it.label) }
        representationMap[Representation.RItemAA] = VH01AA()
        representationMap[Representation.RItemB] = VH01B(
            { println("MainActivity.onItemClick  - " + it.labl) },
            { println("MainActivity.onIteDmClick - " + it) })

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }

        listView.adapter = object : ItemRepresentationListViewAdapter() {
            override val itemRepresentationMap = representationMap
            override val items: List<ItemWithViewType> = Array(15, { i ->
                if (i < 9) ItemA("lv0$i") else ItemB("lv$i")
            }).asList()
        }

//        recyclerView = rootView.findViewById(R.id.recyclerView)
//        val layoutManager = LinearLayoutManager(activity)
//        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView1.adapter = object : ItemRepresentationRecyclerViewAdapter() {
            override val itemRepresentationMap = representationMap

            override val items: List<ItemWithViewType> = Array(15, { i ->
                if (i < 7) ItemA("item0$i") else ItemB("item$i")
            }).asList()
        }

        recyclerView2.adapter = object : ItemRepresentationRecyclerViewAdapter() {
            override val itemRepresentationMap = representationMap
            override val items: List<ItemWithViewType> = Array(25, { i ->
                if (i < 10) {
                    ItemAA("itemAA2 0$i")
                } else ItemB("itemAA2 $i")
            }).asList()
        }

    }

    class ItemAA(val label: String, override val viewType: Int = Representation.RItemAA.ordinal) :
        ItemWithViewType

    class ItemA(val label: String, override val viewType: Int = Representation.RItemA.ordinal) :
        ItemWithViewType

    class ItemB(val labl: String, override val viewType: Int = Representation.RItemB.ordinal) :
        ItemWithViewType

    enum class Representation {
        RItemA,
        RItemAA,
        RItemB,
    }

    class VH01A(val onItemClick: (ItemA) -> Unit) : ItemRepresentation {
        override fun createViewHolder(parent: ViewGroup) = ViewHolder(parent.viewBy(R.layout.text_row_item))
        override val bindHelper = SimpleBindHelper<ViewHolder, ItemA> { item ->
            view.setOnClickListener { onItemClick(item) }
            textView.text = "A " + item.label
        }

        class ViewHolder(v: View) : SimpleViewHolder(v) {
            val textView = v.findViewById<TextView>(R.id.textView)
        }
    }

    class VH01AA : ItemRepresentation {
        override fun createViewHolder(parent: ViewGroup) = ViewHolder(parent.viewBy(R.layout.text_row_item))
        override val bindHelper = SimpleBindHelper<ViewHolder, ItemAA> {
            textView.text = "AA " + it.label
        }

        class ViewHolder(v: View) : SimpleViewHolder(v) {
            val textView = v.findViewById<TextView>(R.id.textView)
        }
    }

    class VH01B(val onItemClick: (ItemB) -> Unit, val onItemDClick: (Int) -> Unit) : ItemRepresentation {
        override fun createViewHolder(parent: ViewGroup) = ViewHolder(parent.viewBy(R.layout.text_row_item))
        override val bindHelper = BindHelperWithPosition<ViewHolder, ItemB> { item, position ->
            textView.text = "B ${item.labl} $position"
            view.setOnClickListener { onItemClick(item) }
            view.setOnLongClickListener { onItemDClick(position); true }
        }

        class ViewHolder(v: View) : SimpleViewHolder(v) {
            val textView = v.findViewById<TextView>(R.id.textView)
        }
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}
