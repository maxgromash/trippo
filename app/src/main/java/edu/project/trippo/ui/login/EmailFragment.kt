package edu.project.trippo.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import edu.project.trippo.databinding.FragmentEmailBinding
import edu.project.trippo.ui.main.MainActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class EmailFragment : Fragment() {
    lateinit var binding: FragmentEmailBinding


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentEmailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.progressBar.isVisible = false

        binding.backIV.setOnClickListener {
            activity?.supportFragmentManager?.popBackStack()
        }

        binding.buttonRegistration.setOnClickListener {
            lifecycleScope.launch {
                binding.progressBar.isVisible = true
                delay(1000)
                val user = Firebase.auth.currentUser!!
                if (!user.isEmailVerified)
                    Toast.makeText(context, "Email is not confirmed!", Toast.LENGTH_SHORT).show()
                else {
                    Toast.makeText(context, "Success!", Toast.LENGTH_SHORT).show()
                    val intent = Intent(context, MainActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    startActivity(intent)
                }
                binding.progressBar.isVisible = false
            }

        }
    }
}