package dev.bahodir.pdpgreen.groupfragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import dev.bahodir.pdpgreen.adapters.ViewAdapter
import dev.bahodir.pdpgreen.R
import dev.bahodir.pdpgreen.classes.Course
import dev.bahodir.pdpgreen.database.My_DbHelper
import dev.bahodir.pdpgreen.databinding.FragmentGroupListBinding

class GroupsListFragment : Fragment() {

    lateinit var fraging: FragmentGroupListBinding
    lateinit var dbHelper: My_DbHelper
    lateinit var list: List<Course>
    lateinit var course: Course
    lateinit var recAdapter: ViewAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fraging = FragmentGroupListBinding.inflate(inflater, container, false)
        dbHelper = My_DbHelper.getInstance(requireContext())
        list = ArrayList(dbHelper.roomdao().getAllCourses())

        recAdapter = ViewAdapter(list, object : ViewAdapter.OnClickMyListener {
            override fun onClick(course: Course) {
                val bundle = Bundle()
                bundle.putSerializable("course_group", course)
                findNavController().navigate(R.id.groupFragment, bundle)
            }
        })

        fraging.apply {
            recycle.adapter = recAdapter
            backBtn.setOnClickListener {
                findNavController().popBackStack()
            }
        }
        return fraging.root
    }
}