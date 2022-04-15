package edu.project.trippo.ui.favouritesFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import edu.project.trippo.R
import edu.project.trippo.databinding.LayoutFavoritesFragmentBinding
import edu.project.trippo.ui.main.MainViewModel
import edu.project.trippo.ui.destinationFragment.DestinationFragment

class FavoritesFragment : Fragment() {

    private lateinit var user: FirebaseUser
    private lateinit var binding: LayoutFavoritesFragmentBinding
    private val adapter = FavouritesAdapter { position ->
        val bundle = bundleOf(DestinationFragment.argId to position)
        findNavController().navigate(R.id.action_navigation_favourites_to_destinationFragment, bundle)
    }
    private val viewModel: MainViewModel by navGraphViewModels(R.id.nav_graph)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        user = Firebase.auth.currentUser!!
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = LayoutFavoritesFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.nicknameTV.text = user.displayName

        binding.iconEmailIV.setOnClickListener {

        }

        binding.staredRV.adapter = adapter
        binding.staredRV.layoutManager = GridLayoutManager(context, 2)
        binding.staredRV.addItemDecoration(FavouritesDecoration(requireContext()))
        binding.logoutIV.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            requireActivity().finish()
        }
    }

    override fun onStart() {
        super.onStart()
        val list = viewModel.getStarredMonths()
        binding.noStarsLAV.isVisible = list?.size == 0
        list?.let {
            adapter.submitList(list)
        }
    }
}