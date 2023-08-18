package com.apptechno.dailyprojectmanagment.ui.project

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apptechno.dailyprojectmanagment.model.*
import com.apptechno.dailyprojectmanagment.network.GetDataService
import com.apptechno.dailyprojectmanagment.network.RetrofitClientInstance
import kotlinx.coroutines.launch

class ProjectViewModel : ViewModel() {


    private val TAG = "ProjectViewModel"

    val response : LiveData<NetworkResponse<Project>> =MutableLiveData()
    val taskResponse : LiveData<NetworkResponse<Task>> =MutableLiveData()


    fun onSaveProjectClicked(projectDetails: Project){
        val retrofitService = RetrofitClientInstance.getRetrofitInstance()?.create(GetDataService::class.java)
        viewModelScope.launch {
            response as MutableLiveData
            val result = retrofitService?.registerProject(projectDetails)
            if(result!=null && result!!.isSuccessful()){
                response.value = result!!.body()

            }

        }

    }

    fun onSaveTaskClicked(taskDetails: Task){
        val retrofitService = RetrofitClientInstance.getRetrofitInstance()?.create(GetDataService::class.java)
        viewModelScope.launch {
            taskResponse as MutableLiveData
            val result = retrofitService?.addTask(taskDetails)
            if(result!=null && result!!.isSuccessful()){
                taskResponse.value = result!!.body()

            }

        }

    }
}