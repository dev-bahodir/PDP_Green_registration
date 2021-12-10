package dev.bahodir.pdpgreen.kurslar

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import dev.bahodir.pdpgreen.R
import dev.bahodir.pdpgreen.classes.Course
import dev.bahodir.pdpgreen.classes.Student
import dev.bahodir.pdpgreen.database.My_DbHelper
import dev.bahodir.pdpgreen.databinding.FragmentCourseAboutBinding

class CourseAboutFragment : Fragment() {

    lateinit var fraging: FragmentCourseAboutBinding
    lateinit var course: Course
    lateinit var dbHelper: My_DbHelper


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            course = it.getSerializable("course") as Course
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fraging = FragmentCourseAboutBinding.inflate(inflater, container, false)
        dbHelper = My_DbHelper.getInstance(requireContext())
        fraging.apply {
            courseName.text = course.name
            description.text = course.data

            addStudent.setOnClickListener {
                if (dbHelper.roomdao().getLisIsOpenGroup(-1).isNotEmpty()) {
                    val bundle = Bundle()
                    bundle.putSerializable("course_add", course)
                    bundle.putBoolean("boolean", true)
                    bundle.putSerializable(
                        "student",
                        Student(
                            -1,
                            "",
                            "",
                            "",
                            "",
                            dbHelper.roomdao().getLisIsOpenGroup(-1)[0].id,
                            dbHelper.roomdao().getLisIsOpenGroup(-1)[0].mentor
                        )
                    )
                    findNavController().navigate(R.id.coursAddFragment, bundle)
                } else Toast.makeText(
                    requireContext(),
                    "No group was added to ${course.name}",
                    Toast.LENGTH_SHORT
                ).show()
            }
            backBtn.setOnClickListener {
                findNavController().popBackStack()
            }
        }
        return fraging.root
    }
}