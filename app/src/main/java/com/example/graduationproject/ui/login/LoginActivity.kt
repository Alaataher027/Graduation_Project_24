package com.example.graduationproject.ui.login


import android.content.Intent
import android.graphics.PorterDuff
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.example.graduationproject.R
import com.example.graduationproject.ui.mainActivity.MainActivity
import com.example.graduationproject.databinding.ActivityLoginBinding
import com.example.graduationproject.ui.ForgetPass.ForgetPassword
import com.example.graduationproject.ui.register.RegisterActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task

class LoginActivity : AppCompatActivity() {
    private lateinit var viewBinding: ActivityLoginBinding
    private lateinit var viewModelLogin: LoginViewModel
    private lateinit var tokenManager: TokenManager
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var googleSignInLauncher: ActivityResultLauncher<Intent>
    private lateinit var googleSignInViewModel: GoogleSignInViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityLoginBinding.inflate(layoutInflater)
        val view = viewBinding.root
        setContentView(view)

        googleSignInViewModel = ViewModelProvider(this).get(GoogleSignInViewModel::class.java)


        // Initialize the TokenManager
        tokenManager = TokenManager(this)

        // Pass the TokenManager to the LoginViewModel
        viewModelLogin = LoginViewModel(tokenManager)

        onClickLogin()
        if_register()
        forgetPass()
//        confingLoginGoogle()
//        onClickGoogle()

    }

//    private fun confingLoginGoogle() {
//        // Configure Google Sign-In
//        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//            .requestIdToken(getString(R.string.default_web_client_id))
//            .requestEmail()
//            .build()
//        googleSignInClient = GoogleSignIn.getClient(this, gso)
//
//        // Initialize ActivityResultLauncher
//        googleSignInLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
//            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
//            handleSignInResult(task)
//        }
//    }
//
//    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
//        try {
//            val account = completedTask.getResult(ApiException::class.java)
//            // Signed in successfully
//            // Now, you can directly navigate to the home screen
//            navToHome()
//        } catch (e: ApiException) {
//            Log.w(TAG, "signInResult:failed code=${e.statusCode}")
//            // Handle sign-in failure, if needed
//        }
//    }
//
//    private fun sendTokenToBackend(idToken: String?) {
//        // Implement logic to send the ID Token to your backend server
//        // You can use Retrofit, Volley, or any other networking library to make an HTTP POST request to your endpoint (https://alshaerawy.aait-sa.com/login/google)
//        // Pass the ID Token in the request body or as a query parameter
//        navToHome()
//    }

//    private fun onClickGoogle() {
//        viewBinding.google.setOnClickListener {
//            Log.d("LoginActivity", "Google button clicked")
////            val signInIntent = googleSignInClient.signInIntent
////            googleSignInLauncher.launch(signInIntent)
//            googleSignInViewModel.signInWithGoogle()
//
//        }
//
//    }


    private fun onClickLogin() {
        viewBinding.loginBtn.setOnClickListener {
            performLogin()
            val progressBar = viewBinding.progressBar
            val progressDrawable = progressBar.indeterminateDrawable.mutate()
            progressDrawable.setColorFilter(
                ContextCompat.getColor(this, R.color.my_light_primary),
                PorterDuff.Mode.SRC_IN
            )
            progressBar.indeterminateDrawable = progressDrawable
        }
    }

    private fun performLogin() {
        val email = viewBinding.email.editText?.text.toString()
        val password = viewBinding.password.editText?.text.toString()

        viewModelLogin.performLogin(email, password, "ar") { success, message ->
            if (success) {
                Toast.makeText(this@LoginActivity, message, Toast.LENGTH_SHORT).show()
                // Save the access token when login is successful
                tokenManager.getToken()?.let {
                    Log.d("LoginActivity", "Access Token: $it")
                }
                navToHome()
            } else {
                Toast.makeText(this@LoginActivity, message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun forgetPass() {
        viewBinding.forgetPassword.setOnClickListener {
            val intent = Intent(this, ForgetPassword::class.java)
            startActivity(intent)
        }
    }

    fun if_register() {
        viewBinding.registNow.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    fun navToHome() {
//        val userType = tokenManager.getUserType()
//        Log.d("LoginActivity", "Retrieved User Type: $userType")
//
//        if (userType == "Seller") {
//            // Navigate to SellerActivity
//            val intent = Intent(this, MainActivity::class.java)
//            startActivity(intent)
//        } else if (userType == "Customer") {
//            // Navigate to BuyerActivity
//            val intent = Intent(this, MainActivity::class.java)
//            startActivity(intent)
//        }

        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)

        finish()
    }

    companion object {
        private const val TAG = "GoogleSignIn"
    }
}