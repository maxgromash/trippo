package edu.project.trippo.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import edu.project.trippo.databinding.ItemSearchResultBinding
import edu.project.trippo.model.DestinationVO

class SearchResultAdapter(
    private val navigate: (Long) -> Unit
) : RecyclerView.Adapter<SearchResultAdapter.SearchResultViewHolder>() {

    private val diffUtilCallBack = object : DiffUtil.ItemCallback<DestinationVO>() {
        override fun areItemsTheSame(oldItem: DestinationVO, newItem: DestinationVO) = false

        override fun areContentsTheSame(oldItem: DestinationVO, newItem: DestinationVO) =
            oldItem == newItem
    }

    val differ = AsyncListDiffer(this, diffUtilCallBack)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = SearchResultViewHolder(
        ItemSearchResultBinding.inflate(LayoutInflater.from(parent.context), parent, false),
        navigate
    )

    override fun onBindViewHolder(holder: SearchResultViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
    }

    override fun getItemCount() = differ.currentList.size

    fun submitList(list: List<DestinationVO>) = differ.submitList(list)

    class SearchResultViewHolder(
        private val binding: ItemSearchResultBinding,
        val navigate: (Long) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(destination: DestinationVO) {
            with(binding) {
                destination.conformity?.let {
                    iconWeatherIV.isSelected = it.weather == Status.YES
                    moneyIV.isSelected = it.budget == Status.YES
                    seaIV.isSelected = it.sea == Status.YES
                    iconPopularIV.isSelected = it.popularity == Status.YES
                }

                tempTV.text = destination.averageTemp.toString() + " °C"
                countryTV.text = destination.country
                nameTV.text = destination.city
                Glide.with(itemView.context)
                    .load(destination.cardImageLink)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(countyAvatarIV)
                exploreB.setOnClickListener { navigate(destination.id) }
            }
        }
    }
}