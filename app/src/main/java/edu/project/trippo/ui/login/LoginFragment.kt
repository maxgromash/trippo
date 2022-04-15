package edu.project.trippo.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import edu.project.trippo.R
import edu.project.trippo.databinding.FragmentStartBinding
import edu.project.trippo.ui.main.MainActivity

class LoginFragment : Fragment() {
    private lateinit var binding: FragmentStartBinding
    private lateinit var viewModel: LoginViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = LoginViewModel(requireActivity())
        setupObservers()
        setOnClicks()

        binding.ForgotPassTextView.setOnClickListener {
            activity?.supportFragmentManager
                ?.beginTransaction()
                ?.replace(R.id.mainFragmentContainer, PasswordFragment(), "")
                ?.addToBackStack(null)
                ?.commit()
        }
        binding.progressBar.isVisible = false
    }

    private fun setupObservers() {
        viewModel.livedata.observe(viewLifecycleOwner) {
            if (it == 200) {
                binding.progressBar.isVisible = false
                val user = Firebase.auth.currentUser!!
                if (user.isEmailVerified)
                    login()
                else
                    Toast.makeText(requireContext(), "Email is not confirmed!", Toast.LENGTH_SHORT).show()
            } else if (it == 404) {
                binding.progressBar.isVisible = false
                Toast.makeText(context, "Ошибка! Проверьте логин или пароль.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setOnClicks() {
        with(binding) {
            NoAccountTextView.setOnClickListener {
                activity?.supportFragmentManager
                    ?.beginTransaction()
                    ?.replace(R.id.mainFragmentContainer, RegistrationFragment(), "")
                    ?.addToBackStack(null)
                    ?.commit()
            }

            loginButton.setOnClickListener {
                progressBar.isVisible = true
                viewModel.login(UserNameEditText.text.toString().trim(), PasswordEditText.text.toString().trim())
            }
        }
    }

    private fun login() {
        val intent = Intent(context, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
    }
}