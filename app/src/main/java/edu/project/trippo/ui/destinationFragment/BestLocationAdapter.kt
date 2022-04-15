package edu.project.trippo.ui.destinationFragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import edu.project.trippo.databinding.ItemBestLocationBinding
import edu.project.trippo.model.BestLocationDTO

class BestLocationAdapter : RecyclerView.Adapter<BestLocationAdapter.BestLocationAdapterViewHolder>() {

    private val diffUtilCallBack = object : DiffUtil.ItemCallback<BestLocationDTO>() {
        override fun areItemsTheSame(oldItem: BestLocationDTO, newItem: BestLocationDTO) =
            oldItem.name == newItem.name

        override fun areContentsTheSame(oldItem: BestLocationDTO, newItem: BestLocationDTO) =
            oldItem == newItem
    }
    private val differ = AsyncListDiffer(this, diffUtilCallBack)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = BestLocationAdapterViewHolder(
        ItemBestLocationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: BestLocationAdapterViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
    }

    override fun getItemCount() = differ.currentList.size

    fun submitList(list: List<BestLocationDTO>) = differ.submitList(list)

    class BestLocationAdapterViewHolder(
        private val binding: ItemBestLocationBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(location: BestLocationDTO) {
            with(binding) {
                nameTV.text = location.name
                Glide.with(itemView.context)
                    .load(location.imageLink)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(imageIV)
            }
        }
    }
}