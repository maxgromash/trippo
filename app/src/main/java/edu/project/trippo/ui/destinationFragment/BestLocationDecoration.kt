package edu.project.trippo.ui.destinationFragment

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import edu.project.trippo.extensions.toPx

class BestLocationDecoration(context: Context) : RecyclerView.ItemDecoration() {

    private val borderOffset = 24.toPx(context)
    private val betweenOffset = 12.toPx(context)

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)
        val position = parent.getChildAdapterPosition(view)
        outRect.set(
            if (position == 0) borderOffset else 0,
            0,
            if (position == state.itemCount - 1) borderOffset else betweenOffset,
            0,
        )
    }
}