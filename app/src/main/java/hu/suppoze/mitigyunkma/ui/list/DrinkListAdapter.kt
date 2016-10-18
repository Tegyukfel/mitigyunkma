package hu.suppoze.mitigyunkma.ui.list

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import hu.suppoze.mitigyunkma.R
import hu.suppoze.mitigyunkma.entity.Drink
import hu.suppoze.mitigyunkma.extension.showPopup
import io.realm.RealmResults

class DrinkListAdapter(
        val realmDrinkDataSet: RealmResults<Drink>,
        val editAction: (Drink) -> Unit,
        val deleteAction: (Drink) -> Unit) :
        RecyclerView.Adapter<DrinkListAdapter.DrinkViewHolder>() {

    var cachedDrinkNameList: List<String>

    init {
        cachedDrinkNameList = realmDrinkDataSet.map { it.name }
        realmDrinkDataSet.addChangeListener { cachedListComparatorListener() }
    }

    private fun cachedListComparatorListener() {
        if (cachedDrinkNameList.count() > realmDrinkDataSet.count()) {
            findDeletedIndex()
        } else if (cachedDrinkNameList.count() < realmDrinkDataSet.count()) {
            findAddedIndex()
        } else {
            notifyDataSetChanged()
        }
        cachedDrinkNameList = realmDrinkDataSet.map { it.name }
    }

    private fun findDeletedIndex() {
        realmDrinkDataSet.forEachIndexed { i, drink ->
            if (cachedDrinkNameList[i] != drink.name) {
                notifyItemRemoved(i)
                notifyDataSetChanged()
                return
            }
        }
        notifyItemRemoved(realmDrinkDataSet.count())
        notifyDataSetChanged()
    }

    private fun findAddedIndex() {
        cachedDrinkNameList.forEachIndexed { i, drinkName ->
            if (realmDrinkDataSet[i].name != drinkName) {
                notifyItemInserted(i)
                notifyDataSetChanged()
                return
            }
        }
        notifyItemInserted(cachedDrinkNameList.count())
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = realmDrinkDataSet.size

    override fun getItemId(position: Int): Long = realmDrinkDataSet[position].index.toLong()

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): DrinkViewHolder? {
        val view = DrinkCardView(parent?.context)
        return DrinkViewHolder(view, { optionsIcon, drink ->
            optionsIcon.showPopup(R.menu.drinklist_row_menu) {
                menuItem ->
                when (menuItem.itemId) {
                    R.id.drinklist_row_menu_edit -> editAction(drink)
                    R.id.drinklist_row_menu_delete -> deleteAction(drink)
                }
                false
            }
        })
    }

    override fun onBindViewHolder(holder: DrinkViewHolder, position: Int) = holder.bindDrink(realmDrinkDataSet[position])

    class DrinkViewHolder(view: View, val optionsClick: (View, Drink) -> Unit) : RecyclerView.ViewHolder(view) {
        fun bindDrink(drink: Drink) {
            itemView as DrinkCardView
            itemView.drink = drink
            itemView.popupAction { optionsClick(it!!, drink) }
        }
    }
}