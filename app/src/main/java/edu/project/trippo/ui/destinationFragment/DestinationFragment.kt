package edu.project.trippo.ui.destinationFragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.navGraphViewModels
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import edu.project.trippo.R
import edu.project.trippo.databinding.LayoutDetailsFragmentBinding
import edu.project.trippo.model.DestinationVO
import edu.project.trippo.ui.main.MainViewModel

class DestinationFragment : Fragment(), OnMapReadyCallback {

    private lateinit var binding: LayoutDetailsFragmentBinding
    private val viewModel: MainViewModel by navGraphViewModels(R.id.nav_graph)
    private lateinit var destination: DestinationVO
    private lateinit var mMapView: MapView
    private val adapter = BestLocationAdapter()

    companion object {
        val argId = "id"
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val bundle = requireArguments()
        val pos = bundle[argId]
        destination = viewModel.filteredData().value?.first { it.id == pos }!!
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = LayoutDetailsFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {

            bestLocationRV.addItemDecoration(BestLocationDecoration(requireContext()))
            bestLocationRV.adapter = adapter
            bestLocationRV.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter.submitList(destination.bestLocations)

            processStarredEvent()
            starredIV.setOnClickListener {
                destination.isStared = !destination.isStared
                viewModel.saveStarred()

                processStarredEvent()
            }

            binding.backIV.setOnClickListener {
                findNavController().popBackStack()
            }

            destination.conformity?.let {
                iconWeatherIV.isSelected = it.weather == Status.YES
                moneyIV.isSelected = it.budget == Status.YES
                seaIV.isSelected = it.sea == Status.YES
                iconPopularIV.isSelected = it.popularity == Status.YES
            }

            weatherTV.text = destination.averageTemp.toString() + " °C"
            DistanceTV.text = destination.distance.toString() + " km"
            TimeTV.text = destination.timeDiff.toString() + " hours"
            LangTV.text = destination.engLevel.toString() + "%"
            CoronaTV.text = destination.corona
            textHotel3TV.text = destination.hotels.threeStar.toString() + "$"
            textHotel4TV.text = destination.hotels.FourStar.toString() + "$"
            textHotel5TV.text = destination.hotels.FiveStar.toString() + "$"
            cityTAV.text = destination.city
            countryTAV.text = destination.country
            descriptionTAV.text = destination.description

            Glide.with(bannerIv.context)
                .load(destination.cardImageLink)
                .apply(RequestOptions.bitmapTransform(RoundedCorners(14)))
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(bannerIv)
        }

        MapsInitializer.initialize(requireActivity())
        mMapView = requireActivity().findViewById(R.id.mapMV)
        mMapView.onCreate(savedInstanceState)
        mMapView.getMapAsync(this)

    }

    private fun processStarredEvent() {
        binding.starredIV.setImageResource(
            if (destination.isStared)
                R.drawable.ic_baseline_star_24
            else
                R.drawable.ic_baseline_star_border_24
        )
    }

    override fun onMapReady(p0: GoogleMap) {
        p0.apply {
            uiSettings.isZoomControlsEnabled = false
            setMapStyle(MapStyleOptions.loadRawResourceStyle(requireContext(), R.raw.map_style));
            val position = LatLng(destination.latitude, destination.longitude)
            moveCamera(CameraUpdateFactory.newLatLngZoom(position, 10F))
        }
    }

    override fun onResume() {
        mMapView.onResume()
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
        mMapView.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mMapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mMapView.onLowMemory()
    }
}