package edu.project.trippo.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import edu.project.trippo.R
import edu.project.trippo.databinding.FragmentRegistrationBinding
import edu.project.trippo.extensions.isEmailValid

class RegistrationFragment : Fragment() {

    private lateinit var binding: FragmentRegistrationBinding
    lateinit var viewModel: RegistrationViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegistrationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = RegistrationViewModel(requireActivity())
        setupObservers()

        binding.buttonRegistration.setOnClickListener {
            binding.editTextPassword.clearFocus()
            binding.editTextConfirmPassword.clearFocus()
            val pass = binding.editTextConfirmPassword.text.trim()
            val pass2 = binding.editTextPassword.text.trim()
            if (!binding.editTextEmail.isEmailValid())
                Toast.makeText(context, "Неверный формат email!", Toast.LENGTH_SHORT).show()
            else if (pass != pass2)
                Toast.makeText(context, "Пароли должны совпадать!", Toast.LENGTH_SHORT).show()
            else {
                binding.progressBarRegister.isVisible = true
                viewModel.register(
                    "@" + binding.editTextUsername.text.trim().toString(),
                    binding.editTextEmail.text.trim().toString(),
                    binding.editTextConfirmPassword.text.trim().toString()
                )
            }
        }

        binding.progressBarRegister.isVisible = false
        binding.imageViewBack.setOnClickListener {
            activity?.supportFragmentManager?.popBackStack()
        }

        binding.textViewHaveAccount.setOnClickListener {
            activity?.supportFragmentManager?.popBackStack()
        }
    }

    private fun setupObservers() {
        viewModel.livedata.observe(viewLifecycleOwner) {
            if (it == 200) {
                binding.progressBarRegister.isVisible = false
                login()
            } else if (it == 404) {
                Toast.makeText(context, "Ошибка! Проверьте логин или пароль.", Toast.LENGTH_SHORT).show()
                binding.progressBarRegister.isVisible = false
            }
        }
    }

    private fun login() {
        activity?.supportFragmentManager
            ?.beginTransaction()
            ?.replace(R.id.mainFragmentContainer, EmailFragment(), "")
            ?.addToBackStack("name")
            ?.commit()

    }
}