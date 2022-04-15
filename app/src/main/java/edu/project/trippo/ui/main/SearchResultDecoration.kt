package edu.project.trippo.ui.main

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import edu.project.trippo.extensions.toPx

class SearchResultDecoration(context: Context) : RecyclerView.ItemDecoration() {

    private val horizontalOffset = 16.toPx(context)
    private val betweenOffset = 24.toPx(context)

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        outRect.set(horizontalOffset, 0, horizontalOffset, betweenOffset)
    }
}