package dev.bahodir.pdpgreen.groupfragment

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import dev.bahodir.pdpgreen.adapters.ViewPager3Adapter
import dev.bahodir.pdpgreen.classes.Course
import dev.bahodir.pdpgreen.classes.Groups
import dev.bahodir.pdpgreen.classes.Mentor
import dev.bahodir.pdpgreen.classes.Student
import dev.bahodir.pdpgreen.database.My_DbHelper
import com.google.android.material.snackbar.Snackbar
import dev.bahodir.pdpgreen.R
import dev.bahodir.pdpgreen.databinding.FragmentGroupAboutBinding


class GroupAboutFragment : Fragment() {

    lateinit var fraging: FragmentGroupAboutBinding
    lateinit var dbHelper: My_DbHelper
    lateinit var recAdapter3: ViewPager3Adapter
    lateinit var list: ArrayList<Student>
    lateinit var group: Groups
    lateinit var mentor: Mentor
    lateinit var course: Course
    lateinit var student1: Student
    private var size = 0
    var isDelete: Boolean? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            group = it.getSerializable("group") as Groups
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fraging = FragmentGroupAboutBinding.inflate(inflater, container, false)
        dbHelper = My_DbHelper.getInstance(requireContext())
        mentor = dbHelper.roomdao().getByIdMentor(group.mentor)
        course = dbHelper.roomdao().getCourceById(mentor.course_id)
        list = ArrayList(dbHelper.roomdao().getListStudentById(group.id))
        student1 = Student(0, "", "", "", "", group.id, group.mentor)
        recAdapter3 = ViewPager3Adapter(false, list, object : ViewPager3Adapter.OnClickMyListener {

            override fun onClickDelete(student: Student, position: Int) {
                student1 = student
                val snackbar = Snackbar.make(requireView(), "Delete student", Snackbar.LENGTH_LONG)
                snackbar.setAction("undo") {
                    list.add(position, student)
                    isDelete = false
                    recAdapter3.notifyItemInserted(position)
                }
                snackbar.show()
                list.removeAt(position)
                recAdapter3.notifyItemRemoved(position)
                recAdapter3.notifyItemChanged(position)
                isDelete = true
            }

            override fun onClickEdit(student: Student, position: Int) {
                val bundle = Bundle()
                bundle.putSerializable("course_add", course)
                bundle.putSerializable("student", student)
                bundle.putBoolean("boolean", false)
                findNavController().navigate(R.id.coursAddFragment, bundle)
            }
        })
        fraging.apply {

            recycle.adapter = recAdapter3
            groupTxt.text = group.name
            timeTxt.text = "time: ${group.time}"
            mentorTxt.text = "mentor: ${mentor.name}"

            if (group.isOpen == 1) {
                started.visibility = View.INVISIBLE
            }

            started.setOnClickListener {
                if (group.isOpen == -1 && list.size >= 3) {
                    /* if (list.size >= 3) {
                         Toast.makeText(
                             requireContext(),
                             "O`quvchi yetarlik emas",
                             Toast.LENGTH_SHORT
                         ).show()
                     } else {*/
                    group.isOpen = 1
                    dbHelper.roomdao().editGroup(group)
                    Toast.makeText(
                        requireContext(),
                        "Successfully added",
                        Toast.LENGTH_SHORT
                    ).show()
                    findNavController().popBackStack()
                    //}
                }
                else {
                    Toast.makeText(
                        requireContext(),
                        "Number of students must be at least 3",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            backBtn.setOnClickListener { findNavController().popBackStack() }
            plus.setOnClickListener {
                if (isDelete == null) isDelete = false
                delete(student1, isDelete!!)
                val bundle = Bundle()
                bundle.putBoolean("boolean", false)
                bundle.putSerializable("course_add", course)
                bundle.putSerializable(
                    "student",
                    Student(-1, "", "", "", "", group.id, group.mentor)
                )
                findNavController().navigate(R.id.coursAddFragment, bundle)
            }
        }

        fraging.apply {
            backBtn.setOnClickListener {
                findNavController().popBackStack()
            }
        }
        return fraging.root
    }

    @SuppressLint("SetTextI18n")
    override fun onResume() {
        super.onResume()
        isDelete = false
        size = list.size
        fraging.countTxt.text = "Number of students: $size"
    }

    override fun onDestroy() {
        super.onDestroy()
        if (isDelete == null) isDelete = false
        delete(student1, isDelete!!)
    }

    fun delete(student: Student, boolean: Boolean) {
        if (boolean) {
            dbHelper.roomdao().deleteStudent(student)
            isDelete = false
        }
    }
}