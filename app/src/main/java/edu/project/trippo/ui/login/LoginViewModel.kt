package edu.project.trippo.ui.login

import android.app.Activity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class LoginViewModel(val activity: Activity) : ViewModel() {

    var livedata = MutableLiveData(1)

    fun login(email: String, password: String) =
        viewModelScope.launch {
            delay(2000)
            val mAuth = FirebaseAuth.getInstance()
            try {
                mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(
                        activity
                    ) { task ->
                        if (task.isSuccessful) {
                            livedata.value = 200
                        } else {
                            livedata.value = 404
                        }
                    }

            } catch (ex: Exception) {
                livedata.value = 404
            }
        }
}