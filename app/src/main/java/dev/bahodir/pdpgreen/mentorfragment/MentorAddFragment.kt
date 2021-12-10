package dev.bahodir.pdpgreen.mentorfragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import dev.bahodir.pdpgreen.classes.Course
import dev.bahodir.pdpgreen.classes.Mentor
import dev.bahodir.pdpgreen.database.My_DbHelper
import dev.bahodir.pdpgreen.databinding.FragmentMentorAddBinding


class MentorAddFragment : Fragment() {

    lateinit var fraging: FragmentMentorAddBinding
    lateinit var dbHelper: My_DbHelper
    lateinit var course: Course

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            course = it.getSerializable("course_add_mentor") as Course
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fraging = FragmentMentorAddBinding.inflate(inflater, container, false)
        dbHelper = My_DbHelper.getInstance(requireContext())
        fraging.save.setOnClickListener {
            if (fraging.lastname.text.isNotEmpty() && fraging.name.text.isNotEmpty() && fraging.fatherName.text.isNotEmpty()) {
                val mentor = Mentor(
                    course_id = course.id,
                    name = fraging.name.text.toString(),
                    surname = fraging.lastname.text.toString(),
                    lastname = fraging.fatherName.text.toString())
                dbHelper.roomdao().addMentor(mentor)
                findNavController().popBackStack()
            } else Toast.makeText(
                requireContext(),
                "Fill in all boxes",
                Toast.LENGTH_SHORT
            ).show()
        }
        fraging.backBtn.setOnClickListener {
            findNavController().popBackStack()
        }
        return fraging.root
    }
}