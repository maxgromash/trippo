package edu.project.trippo.ui.quizFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.navGraphViewModels
import edu.project.trippo.R
import edu.project.trippo.databinding.LayoutQuizFragmentBinding
import edu.project.trippo.model.Quiz
import edu.project.trippo.ui.main.MainViewModel

class QuizFragment : Fragment() {

    companion object {
        val argId: String = "quiz"
    }

    lateinit var binding: LayoutQuizFragmentBinding
    private val viewModel: MainViewModel by navGraphViewModels(R.id.nav_graph)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = LayoutQuizFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val bundle = requireArguments()
        val quiz = bundle["quiz"] as? Quiz

        quiz?.let {
            with(binding) {
                titleTV.text = quiz.title
                answer1MB.text = quiz.variantA
                answer2MB.text = quiz.variantB
                answer3MB.isVisible = quiz.variantC != null
                answer3MB.text = quiz.variantC
                answer4MB.isVisible = quiz.variantD != null
                answer4MB.text = quiz.variantD
                bannerIv.setAnimation(quiz.resId)
                answer1MB.setOnClickListener {
                    findNavController().popBackStack()
                    viewModel.setPreference(0)
                }
                answer2MB.setOnClickListener {
                    findNavController().popBackStack()
                    viewModel.setPreference(1)
                }
                answer3MB.setOnClickListener {
                    findNavController().popBackStack()
                    viewModel.setPreference(2)
                }
                answer4MB.setOnClickListener {
                    findNavController().popBackStack()
                    viewModel.setPreference(3)
                }
            }
        }
    }
}