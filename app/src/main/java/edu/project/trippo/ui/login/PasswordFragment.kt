package edu.project.trippo.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import edu.project.trippo.R
import edu.project.trippo.databinding.FragmentPasswordBinding

class PasswordFragment : Fragment() {
    lateinit var binding: FragmentPasswordBinding


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentPasswordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.backIV.setOnClickListener {
            activity?.supportFragmentManager?.popBackStack()
        }

        binding.buttonRegistration.setOnClickListener {

            val email = binding.editTextEmail.text.toString().trim()

            Firebase.auth.sendPasswordResetEmail(email)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        binding.anumLAV.setAnimation(R.raw.done)
                        Toast.makeText(context, "Check your email", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }
}