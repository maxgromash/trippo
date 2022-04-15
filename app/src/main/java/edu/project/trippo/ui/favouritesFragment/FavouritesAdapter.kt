package edu.project.trippo.ui.favouritesFragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import edu.project.trippo.databinding.ItemStarredBinding
import edu.project.trippo.model.DestinationVO

class FavouritesAdapter(
    private val navigate: (Long) -> Unit
) : RecyclerView.Adapter<FavouritesAdapter.DestinationViewHolder>() {

    private val diffUtilCallBack = object : DiffUtil.ItemCallback<DestinationVO>() {
        override fun areItemsTheSame(oldItem: DestinationVO, newItem: DestinationVO) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: DestinationVO, newItem: DestinationVO) =
            oldItem == newItem
    }
    private val differ = AsyncListDiffer(this, diffUtilCallBack)

    fun submitList(list: List<DestinationVO>) = differ.submitList(list)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DestinationViewHolder {
        val binding = ItemStarredBinding.inflate(LayoutInflater.from(parent.context))
        return DestinationViewHolder(binding, navigate)
    }

    override fun onBindViewHolder(holder: DestinationViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
    }

    override fun getItemCount() = differ.currentList.size

    class DestinationViewHolder(
        private val binding: ItemStarredBinding,
        private val navigate: (Long) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(destination: DestinationVO) {
            Glide.with(itemView.context)
                .load(destination.cardImageLink)
                .apply(RequestOptions.bitmapTransform(RoundedCorners(14)))
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(binding.citiBgIV)
            binding.titleTV.text = destination.city
            itemView.setOnClickListener { navigate(destination.id) }
        }
    }
}