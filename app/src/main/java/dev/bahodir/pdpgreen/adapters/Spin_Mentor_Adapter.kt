package dev.bahodir.pdpgreen.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import dev.bahodir.pdpgreen.classes.Mentor
import dev.bahodir.pdpgreen.databinding.ItemSpinnerBinding

class Spin_Mentor_Adapter(var list: List<Mentor>) : BaseAdapter() {

    override fun getCount(): Int = list.size

    override fun getItem(position: Int): Mentor = list[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val binding: ItemSpinnerBinding = if (convertView == null) {
            ItemSpinnerBinding.inflate(LayoutInflater.from(parent?.context), parent, false)
        } else {
            ItemSpinnerBinding.bind(convertView)
        }

        binding.txt.text = list[position].name

        return binding.root
    }

}