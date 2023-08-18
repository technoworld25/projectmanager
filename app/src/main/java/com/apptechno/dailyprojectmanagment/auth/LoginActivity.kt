package com.apptechno.dailyprojectmanagment.auth

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.apptechno.dailyprojectmanagment.HomeActivity
import com.apptechno.dailyprojectmanagment.databinding.ActivityLoginBinding
import com.apptechno.dailyprojectmanagment.model.UserRequest
import com.apptechno.dailyprojectmanagment.network.GetDataService
import com.apptechno.dailyprojectmanagment.network.RetrofitClientInstance
import com.apptechno.dailyprojectmanagment.utility.ProjectUtility
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class LoginActivity : AppCompatActivity() {

    private val TAG = "RegisterActivity"

    lateinit var binding: ActivityLoginBinding
    private lateinit var viewModel: AuthViewModel
    private lateinit var mContext: Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
         binding = ActivityLoginBinding.inflate(layoutInflater);
        setContentView(binding.root)
        mContext= this@LoginActivity
        supportActionBar!!.hide()
    }

    fun onLoginClicked(view: View) {

        startActivity(Intent(mContext, HomeActivity::class.java))
        return
        viewModel = ViewModelProvider(this).get(AuthViewModel::class.java)
        val username = binding.inputUsername.text.toString()
        val password = binding.inputPassword.text.toString()

        if (!username.isNullOrEmpty() && !password.isNullOrEmpty()) {

            // launching a new coroutine
            lifecycleScope.launch(Dispatchers.IO) {
                if(ProjectUtility.isConnectedToInternet(mContext)) {
                    viewModel.onLoginClicked(UserRequest(username, password))
                }else{

                    ProjectUtility.showToastMessage(mContext,"Internet is not available.")

                }
            }

            viewModel.response.observe(this, Observer {

                if(it!=null && it.data != null) {
                    Toast.makeText(this,it.message,Toast.LENGTH_SHORT).show()
                    if (username.equals(it.data.username)) {

                        startActivity(Intent(mContext, HomeActivity::class.java))
                    }
                }
            })

        }else{
            ProjectUtility.showToastMessage(mContext,"Please add your username and password")

        }
    }


    fun onCreateNewAccountClicked(view: View){

        startActivity( Intent(this, RegisterActivity::class.java))

    }




}