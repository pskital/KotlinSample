package sample.android.login

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import kotlinx.android.synthetic.main.login_layout.*
import sample.android.LoadingState
import sample.android.LoadingState.*
import sample.android.main.MainActivity
import sample.android.remote.models.LoginModel
import kotlin.sample.R

class LoginActivity : AppCompatActivity() {

    private lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_layout)

        viewModel = ViewModelProviders.of(this).get(LoginViewModel::class.java)
        viewModel.loadingState.observe(this, observer)

        loginButton.setOnClickListener {
            viewModel.loginUser(emailEditText.text.toString(), passwordEditText.text.toString())
        }
    }

    private val observer: Observer<LoadingState> = Observer { state ->
        when (state) {
            is STARTED -> progressBar.visibility = View.VISIBLE
            is FINISHED -> progressBar.visibility = View.GONE
            is ERROR -> {
                progressBar.visibility = View.GONE
                Log.d("Loading ERROR", "${state.error}")
                startActivity(Intent(this, MainActivity::class.java))
            }
            is SUCCESS<*> -> {
                progressBar.visibility = View.GONE
                val loginResponse = state.model as LoginModel;

                Log.d("Loading SUCCESS", loginResponse.data?.customer?.name)
                startActivity(Intent(this, MainActivity::class.java))
            }
        }
    }
}
