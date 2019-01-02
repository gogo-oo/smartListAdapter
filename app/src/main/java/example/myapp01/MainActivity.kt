package example.myapp01

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import example.myapp01.smartList.*
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }

        listView.adapter = object : AbstractListViewAdapter() {
            override val items: List<ListItemWithBinderId> = Array(15, { i ->
                if (i < 9) ItemA("lv0$i") else ItemB("lv$i")
            }).asList()
        }

//        recyclerView = rootView.findViewById(R.id.recyclerView)
//        val layoutManager = LinearLayoutManager(activity)
//        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView1.adapter = object : AbstractRecyclerViewAdapter() {

            override val items: List<ListItemWithBinderId> = Array(15, { i ->
                if (i < 7) ItemA("item0$i") else ItemB("item$i")
            }).asList()
        }.apply {
//            setListener(Representotion.RItemA.bh) { item: ItemA ->
//                println("MainActivity.onItemClick@ " + item.label)
//            }

            setListener(Representotion.RItemA.bh, object : Representotion.RItemA.Listener {
                override fun onItemClick(item: ItemA) {
                    println("MainActivity.onItemClick " + item.label)
                }
            })
        }
        recyclerView2.adapter = object : AbstractRecyclerViewAdapter() {
            override val items: List<ListItemWithBinderId> = Array(25, { i ->
                if (i < 10) {
                    ItemAA("itemAA2 0$i", Representotion.RItemAA.bh.runtimeId)
                } else ItemB("itemAA2 $i")
            }).asList()
        }

    }

    class ItemAA(val label: String, override val binderId: BinderId) : ListItemWithBinderId

    class ItemA(val label: String) : ListItemWithBinderId {
        override val binderId: BinderId = Representotion.RItemA.bh.runtimeId
    }

    class ItemB(val labl: String) : ListItemByBinder(Representotion.RItemB.bh)


    class Representotion {
        object RItemA {
            class ViewHolder(v: View) : AbstractRecyclerViewAdapter.ViewHolder(v) {
                val textView = v.findViewById<TextView>(R.id.textView)
            }

            interface Listener {
                fun onItemClick(item: ItemA)
            }

            val bh = BindHelper(ItemA::class.java,
                ViewHolder::class.java, R.layout.text_row_item,
                Listener::class.java,
                { ViewHolder(it) }) { item, viewHolder, position, listener ->
                listener?.apply {
                    viewHolder.view.setOnClickListener { onItemClick(item) }
                }
                viewHolder.textView.text = "A " + item.label
            }
        }

        object RItemAA {
            val bh = BindHelper(ItemAA::class.java,
                RItemA.ViewHolder::class.java, R.layout.text_row_item,
                { RItemA.ViewHolder(it) }) { item, viewHolder, position ->
                viewHolder.textView.text = "AA " + item.label
            }
        }

        object RItemB {
            class ViewHolder(v: View) : AbstractRecyclerViewAdapter.ViewHolder(v) {
                val textView = v.findViewById<TextView>(R.id.textView)
            }

            val bh = BindHelper(
                ItemB::class.java,
                ViewHolder::class.java,
                R.layout.text_row_item,
                { ViewHolder(it) }) { item, viewHolder, position ->
                viewHolder.textView.text = "B " + item.labl
            }

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
