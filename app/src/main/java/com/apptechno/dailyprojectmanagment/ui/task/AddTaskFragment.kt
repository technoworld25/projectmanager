package com.apptechno.dailyprojectmanagment.ui.task

//noinspection SuspiciousImport
import android.R
import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.apptechno.dailyprojectmanagment.HomeActivity
import com.apptechno.dailyprojectmanagment.databinding.FragmentAddTaskBinding
import com.apptechno.dailyprojectmanagment.model.Task
import com.apptechno.dailyprojectmanagment.model.TaskResponse
import com.apptechno.dailyprojectmanagment.utility.ProjectUtility
import kotlinx.coroutines.launch


class AddTaskFragment : Fragment() {
    private var _binding: FragmentAddTaskBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: TaskViewModel
    private lateinit var id :String
    private lateinit var project :String
    private lateinit var type:String
    private lateinit var taskId :String
    private lateinit var mContext:Context


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddTaskBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init(){
        (activity as HomeActivity).supportActionBar?.elevation = 0f
        (activity as HomeActivity).supportActionBar?.title = "Add New Task"
        (activity as HomeActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)

        viewModel = ViewModelProvider(this)[TaskViewModel::class.java]

        setSpinners()
        showDetailsIfAvailable()

        _binding!!.buttonSave.setOnClickListener {

           saveTask()

        }
        viewModel.taskResponse.observe(this) {

            ProjectUtility.showToastMessage(requireContext(), it.message)

        }
        viewModel.updateTasksResponse.observe(this) {

            ProjectUtility.showToastMessage(requireContext(), it.message)

        }
    }


    @SuppressLint("SetTextI18n")
    private fun showDetailsIfAvailable(){
        val customObject = arguments?.getParcelable<TaskResponse>("taskResponse")

        id = arguments?.getString("projectId").toString()
        project = arguments?.getString("projectName").toString()

        _binding!!.projectName.text = "Project Name : $project"
        _binding!!.requestedBy.text = "Requested By :  "+ "Anupam22"

        if(customObject != null) {
             type = "edit"
            taskId = customObject.taskid
             id = customObject.id.toString()
            _binding!!.inputTaskName.setText(customObject.taskname)
            _binding!!.projectName.text = "Project Name : "+ customObject.name
            _binding!!.inputTaskDescription.setText(customObject.description)
            _binding!!.requestedBy.text = "Requested By :  "+customObject.assigner
            _binding!!.assigneeSpinner.setText(customObject.assignee,false)
            _binding!!.stateSpinner.setText(customObject.state,false)
            (activity as HomeActivity).supportActionBar!!.title = "Edit Task"

        }else{
            type ="add"
            (activity as HomeActivity).supportActionBar!!.title = "Add New Task"
        }

    }
    private lateinit var assigneesAdapter: ArrayAdapter<String>
    private lateinit var statesAdapter: ArrayAdapter<String>

    private fun setSpinners(){
        val assignees = arrayOf("Anupam L","Anupam22")
        assigneesAdapter = ArrayAdapter<String>(context!!, R.layout.simple_spinner_item, assignees)

        _binding!!.assigneeSpinner.setAdapter(assigneesAdapter)

        val states = arrayOf("New","In Progress","Completed")
        statesAdapter = ArrayAdapter<String>(context!!, R.layout.simple_spinner_item, states)

        _binding!!.stateSpinner.setAdapter(statesAdapter)
    }

    private fun saveTask(){
        //val projectName = _binding!!.projectName.text.toString()
        val requestedBy = _binding!!.requestedBy.text.toString()
        val taskName = _binding!!.inputTaskName.text.toString()

        val taskDescription = _binding!!.inputTaskDescription.text.toString()
        val state = _binding!!.stateSpinner.text.toString()
        val assignee = _binding!!.assigneeSpinner.text.toString()


        lifecycleScope.launch {

            if(ProjectUtility.isConnectedToInternet(mContext)) {

                if(type == "add") {
                    val task =Task("0",requestedBy,taskName,taskDescription,state,assignee,id.toInt())
                    viewModel.onSaveTaskClicked(task)
                }else{
                    val task =Task(taskId,requestedBy,taskName,taskDescription,state,assignee,id.toInt())
                    viewModel.updateTaskClicked(task)
                }
            }else{

                ProjectUtility.showToastMessage(mContext,"Internet is not available.")

            }



        }
    }


}