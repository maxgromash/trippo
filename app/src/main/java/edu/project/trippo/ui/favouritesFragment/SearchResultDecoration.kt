package edu.project.trippo.ui.favouritesFragment

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import edu.project.trippo.extensions.toPx

class FavouritesDecoration(context: Context) : RecyclerView.ItemDecoration() {

    private val betweenOffset = 24.toPx(context)

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        outRect.bottom = betweenOffset
    }
}