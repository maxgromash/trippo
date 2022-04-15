package edu.project.trippo.ui.login

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import edu.project.trippo.R
import edu.project.trippo.extensions.isOnline
import edu.project.trippo.ui.main.MainActivity
import edu.project.trippo.ui.info.NoInternetActivity

class StartActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    }

    override fun onStart() {
        super.onStart()

        val user = Firebase.auth.currentUser

        if (!isOnline(this)) {
            val intent = Intent(this, NoInternetActivity::class.java)
            startActivity(intent)
        } else {
            if (user != null && user.isEmailVerified) {
                val intent = Intent(this, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(intent)
            } else {
                supportFragmentManager.beginTransaction()
                    .add(R.id.mainFragmentContainer, LoginFragment(), "tag")
                    .addToBackStack("login")
                    .commit()
            }
        }

    }
}
