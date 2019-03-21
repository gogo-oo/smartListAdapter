package example.myapp01

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import example.myapp01.adapter.item.ItemA
import example.myapp01.adapter.item.ItemB
import example.myapp01.adapter.itemRepresentation.TextViewItemRepresentation
import example.myapp01.adapter.itemRepresentation.VH01A
import example.myapp01.adapter.itemRepresentation.VH01B
import example.myapp01.itemRepresentation.ItemRepresentationRecyclerViewAdapter
import example.myapp01.itemRepresentation.ItemWithViewType
import example.myapp01.itemRepresentation.SingleItemTypeRecyclerViewAdapter
import example.mylib01.Lib
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        val textViewIR01 = TextViewItemRepresentation(viewId = R.id.textView01) {
            println(it)
            Snackbar.make(recyclerView1, "text = $it", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }

        textViewIR01.update(contentMain, Lib.str01())

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }

//        listView.adapter = object : ItemRepresentationListViewAdapter() {
//            override val itemRepresentationMap = representationMap
//            override val items: List<ItemWithViewType> = Array(15, { i ->
//                if (i < 9) ItemA("lv0$i") else ItemB("lv$i")
//            }).asList()
//        }

//        recyclerView = rootView.findViewById(R.id.recyclerView)
//        val layoutManager = LinearLayoutManager(activity)
//        recyclerView.layoutManager = LinearLayoutManager(this)

        recyclerView1.adapter = object : SingleItemTypeRecyclerViewAdapter<String>(TextViewItemRepresentation {
            println(it)
            textViewIR01.update(contentMain, it)
            Snackbar.make(recyclerView1, "text = $it", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }) {
            override val items = Lib.list01()
        }
//

        recyclerView2.adapter = object : ItemRepresentationRecyclerViewAdapter() {
            override val items: List<ItemWithViewType> = Array(25) { i ->
                if (i < 10) {
                    ItemA("itemAA2 0$i")
                } else ItemB("itemAA2 $i")
            }.asList()
        }.apply {
            this configureItemRepresentation VH01A {
                println("MainActivity.onItemClick " + it.label)
            }
            this configureItemRepresentation VH01B(
                {
                    println("MainActivity.onItemClick  - " + it.labl)
                    Snackbar.make(recyclerView1, "Btext = ${it.labl}", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show()
                },
                {
                    println("MainActivity.onIteDmClick - " + it)

                    Snackbar.make(recyclerView1, "Btext = $it", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show()
                })

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
