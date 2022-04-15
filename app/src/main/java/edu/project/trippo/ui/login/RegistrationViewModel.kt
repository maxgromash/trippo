package edu.project.trippo.ui.login

import android.app.Activity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.coroutines.launch


class RegistrationViewModel(val activity: Activity) : ViewModel() {

    var livedata = MutableLiveData(1)

    fun register(username: String, email: String, password: String) {
        viewModelScope.launch {
            val mAuth = FirebaseAuth.getInstance()
            mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(
                    activity
                ) { task ->
                    if (task.isSuccessful) {

                        val profileUpdates = UserProfileChangeRequest.Builder()
                            .setDisplayName(username)
                            .build()

                        val user: FirebaseUser? = mAuth.currentUser

                        user?.let {
                            user.updateProfile(profileUpdates)
                                .addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        confirmEmail()
                                    } else
                                        livedata.value = 404
                                }
                        }
                    } else {
                        livedata.value = 404
                    }
                }
        }
    }

    private fun confirmEmail() {
        viewModelScope.launch {
            val mAuth = FirebaseAuth.getInstance()
            val user: FirebaseUser? = mAuth.currentUser
            user?.let {
                user.sendEmailVerification()
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            livedata.value = 200
                        } else
                            livedata.value = 404
                    }

            } ?: run { livedata.value = 404 }
        }
    }

}