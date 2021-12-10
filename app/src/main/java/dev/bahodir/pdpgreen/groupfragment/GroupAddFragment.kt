package dev.bahodir.pdpgreen.groupfragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import dev.bahodir.pdpgreen.adapters.Spin_Mentor_Adapter
import dev.bahodir.pdpgreen.adapters.Spin_Time_Adapter
import dev.bahodir.pdpgreen.classes.Course
import dev.bahodir.pdpgreen.classes.Groups
import dev.bahodir.pdpgreen.classes.Mentor
import dev.bahodir.pdpgreen.database.My_DbHelper
import dev.bahodir.pdpgreen.databinding.FragmentGroupAddBinding

class GroupAddFragment : Fragment() {

    lateinit var fraging: FragmentGroupAddBinding
    lateinit var course: Course
    lateinit var dbHelper: My_DbHelper
    lateinit var list: List<Mentor>
    lateinit var spinnerAdapterMentor: Spin_Mentor_Adapter
    lateinit var spinnerAdapterTime1: Spin_Time_Adapter
    lateinit var spinnerAdapterTime2: Spin_Time_Adapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            course = it.getSerializable("course_group") as Course
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fraging = FragmentGroupAddBinding.inflate(inflater, container, false)
        dbHelper = My_DbHelper.getInstance(requireContext())
        list = dbHelper.roomdao().getListMentorById(course.id)

        val listTime = listOf("14:00-16:00", "16:30-18:30", "19:00-21:00")
        val listDate = listOf("couple", "odd")
        spinnerAdapterMentor = Spin_Mentor_Adapter(list)
        spinnerAdapterTime1 = Spin_Time_Adapter(listTime)
        spinnerAdapterTime2 = Spin_Time_Adapter(listDate)
        fraging.apply {
            backBtn.setOnClickListener {
                findNavController().popBackStack()
            }
            fraging.apply {
                backBtn.setOnClickListener { findNavController().popBackStack() }
                spinMentorEdit.adapter = spinnerAdapterMentor
                spinCourseTime.adapter = spinnerAdapterTime1
                spinCourseDate.adapter = spinnerAdapterTime2

                save.setOnClickListener {
                    val group_name = groupName.text
                    val mentor = list[spinMentorEdit.selectedItemPosition]
                    val time = listTime[spinCourseTime.selectedItemPosition]
                    val date = listDate[spinCourseDate.selectedItemPosition]

                    if (group_name.isNotEmpty() && mentor.name.isNotEmpty() && time.isNotEmpty() && date.isNotEmpty()) {
                        val group = Groups(
                            name = group_name.toString(),
                            isOpen = -1,
                            date = date,
                            time = time,
                            mentor = mentor.id
                        )
                        dbHelper.roomdao().addGroup(group)
                        findNavController().popBackStack()
                    } else {
                        Toast.makeText(
                            requireContext(),
                            "Fill in all boxes",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
//                    }
                }
            }
        }
        return fraging.root
    }
}