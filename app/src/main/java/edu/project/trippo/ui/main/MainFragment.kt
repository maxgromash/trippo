package edu.project.trippo.ui.main

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.transition.Fade
import android.transition.Transition
import android.transition.TransitionManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.location.LocationServices
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import edu.project.trippo.R
import edu.project.trippo.databinding.LayoutMainFragmentBinding
import edu.project.trippo.extensions.isOnline
import edu.project.trippo.ui.destinationFragment.DestinationFragment
import edu.project.trippo.ui.info.NoInternetActivity
import edu.project.trippo.ui.quizFragment.QuizFragment


class MainFragment : Fragment() {

    private lateinit var binding: LayoutMainFragmentBinding
    private val viewModel: MainViewModel by navGraphViewModels(R.id.nav_graph)
    private lateinit var months: Array<String>
    private val adapter = SearchResultAdapter { position ->
        val bundle = bundleOf(DestinationFragment.argId to position)
        findNavController().navigate(R.id.action_navigation_main_to_detailsFragment, bundle)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        months = resources.getStringArray(R.array.months)
        observeData()
        getLocation()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = LayoutMainFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        if (!activity?.isOnline(requireContext())!!) {
            val intent = Intent(requireContext(), NoInternetActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupSearchView()


        val user = Firebase.auth.currentUser
        if (user?.photoUrl == null)
            setupQuiz()
        else {
            viewModel.getPrefs()
            viewModel.getStarred()
        }



        with(binding) {
            searchResultsRV.layoutManager = LinearLayoutManager(context)
            searchResultsRV.adapter = adapter
            searchResultsRV.addItemDecoration(SearchResultDecoration(searchResultsRV.context))

            monthSelectorMB.text = months[viewModel.preferences.month]
            monthSelectorMB.setOnClickListener {
                MaterialAlertDialogBuilder(requireContext())
                    .setTitle("Trip month")
                    .setPositiveButton("Select") { dialog, _ ->
                        val position = (dialog as AlertDialog).listView.checkedItemPosition
                        viewModel.changeMonth(position)
                        monthSelectorMB.text = months[position]
                    }
                    .setNegativeButton("Cancel", null)
                    .setSingleChoiceItems(months, viewModel.preferences.month, null)
                    .setIcon(R.drawable.ic_baseline_calendar_today_24)
                    .show()
            }

            sortMB.text = viewModel.sortType.key + "  ▼"
            sortMB.setOnClickListener {
                val types = arrayOf("Name", "Temperature", "Most suitable")
                MaterialAlertDialogBuilder(requireContext())
                    .setTitle("Sort by")
                    .setItems(types) { _, position ->
                        viewModel.sortBy(types[position])
                        sortMB.text = viewModel.sortType.key + "  ▼"
                    }
                    .setIcon(R.drawable.ic_baseline_sort_24)
                    .show()
            }
        }
    }

    fun getPrefs() {

    }

    private fun setupQuiz() {
        viewModel.currentQuiz.observe(requireActivity()) {
            if (it != null) {
                val bundle = bundleOf(QuizFragment.argId to it)
                findNavController().navigate(R.id.action_navigation_main_to_quizFragment, bundle)
            }
        }
    }

    private fun getLocation() {
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        val locationPermissionRequest = registerForActivityResult(ActivityResultContracts.RequestPermission()) { }
        val perm = ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
        if (perm != PackageManager.PERMISSION_GRANTED)
            locationPermissionRequest.launch(Manifest.permission.ACCESS_COARSE_LOCATION)
        fusedLocationClient.lastLocation.addOnSuccessListener { location -> viewModel.location = location }
    }

    private fun setupSearchView() {
        with(binding) {

            val searchEditText = searchSV.findViewById<View>(androidx.appcompat.R.id.search_src_text) as EditText
            searchEditText.setTextColor(resources.getColor(R.color.black))
            searchEditText.setHintTextColor(resources.getColor(R.color.black))

            searchSV.setOnClickListener {
                searchSV.onActionViewExpanded()
            }

            searchSV.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String): Boolean {
                    searchSV.clearFocus()
                    return false
                }

                override fun onQueryTextChange(newText: String): Boolean {
                    viewModel.getFilteredData(newText)
                    return true
                }
            })

            searchSV.setOnCloseListener {
                searchSV.clearFocus()
                false
            }
        }
    }

    private fun observeData() {
        val transition = getTransition()
        viewModel.filteredData().observe(this) {
            TransitionManager.beginDelayedTransition(binding.loadingLAV.parent as ConstraintLayout, transition)
            val isEmpty = it.isNullOrEmpty()
            if (isEmpty && !binding.loadingLAV.isVisible) {
                binding.loadingLAV.setAnimation(R.raw.not_found)
                binding.loadingLAV.playAnimation()
            }
            binding.loadingLAV.isVisible = isEmpty
            val x = adapter.differ.currentList
            if (it != null) {
                if (x.count() == it.count() && x.count() > 0 && it.count() > 0 && x[0] == it[0])
                    return@observe
                adapter.submitList(it)
            }
        }

        viewModel.loadingStatus().observe(this) { isLoading ->
            binding.searchResultsRV.isVisible = !isLoading
            binding.loadingLAV.isVisible = isLoading
            if (isLoading) binding.loadingLAV.setAnimation(R.raw.loading)
            binding.loadingLAV.playAnimation()
        }
    }

    private fun getTransition(): Transition {
        val transition: Transition = Fade()
        transition.duration = 300
        transition.addTarget(R.id.loadingLAV)
        return transition
    }
}