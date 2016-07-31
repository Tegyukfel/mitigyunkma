package hu.suppoze.mitigyunkma

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.util.Log
import hu.suppoze.mitigyunkma.base.Navigator

import hu.suppoze.mitigyunkma.calculate.CalculateFragment
import hu.suppoze.mitigyunkma.list.DrinkListFragment
import hu.suppoze.mitigyunkma.model.Drink

class MainPagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {

    override fun getCount(): Int {
        return Navigator.Pages.values().count()
    }

    override fun getItem(position: Int): Fragment {
        when (position) {
            Navigator.Pages.CALCULATE.ordinal -> return CalculateFragment()
            Navigator.Pages.HISTORY.ordinal -> return DrinkListFragment(Drink::lastmod.name)
            Navigator.Pages.BEST.ordinal -> return DrinkListFragment(Drink::index.name)
            else -> {
                Log.e("Mitigyunkma", "Invalid position argument in getItem()!")
                throw RuntimeException("Invalid position argument in getItem()!")
            }
        }
    }
}
