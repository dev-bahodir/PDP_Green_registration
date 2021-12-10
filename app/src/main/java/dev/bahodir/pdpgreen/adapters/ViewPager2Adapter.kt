package dev.bahodir.pdpgreen.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dev.bahodir.pdpgreen.classes.Groups
import dev.bahodir.pdpgreen.database.My_DbHelper
import dev.bahodir.pdpgreen.databinding.ItemGroupPagerBinding

class ViewPager2Adapter(
    var boolean: Boolean,
    var dbHelper: My_DbHelper,
    var list: List<Groups>,
    var listener: OnClickMyListener
) :
    RecyclerView.Adapter<ViewPager2Adapter.VH>() {


    inner class VH(var binding: ItemGroupPagerBinding) : RecyclerView.ViewHolder(binding.root)

    interface OnClickMyListener {
        fun onclickShow(group: Groups, position: Int)
        fun onClickDelete(mentor: Groups, position: Int)
        fun onClickEdit(mentor: Groups, position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        return VH(ItemGroupPagerBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.binding.apply {
            courseName.text = list[position].name
            lastname.text = "Number of students: ${dbHelper.roomdao().getListStudentById(list[position].id).size}"

            if (!boolean) {
                show.visibility = View.GONE
            }

            edit.setOnClickListener {
                listener.onClickEdit(list[position], position)
            }

            delete.setOnClickListener {
                listener.onClickDelete(list[position], position)
            }

            show.setOnClickListener {
                listener.onclickShow(
                    list[position], dbHelper.roomdao().getListStudentById(list[position].id).size
                )
            }
        }
    }

    override fun getItemCount(): Int = list.size

}