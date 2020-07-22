package com.kenton.tictactoe

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.*
import com.kenton.tictactoe.databinding.TicTacToeBoxBinding

class TicTacToeAdapter :
    ListAdapter<TicTacToeBoardItem, TicTacToeBoxViewHolder>(TicTacToeMoveDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TicTacToeBoxViewHolder {
        return TicTacToeBoxViewHolder(parent, R.layout.tic_tac_toe_box)
    }

    override fun onBindViewHolder(holder: TicTacToeBoxViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun submitList(list: List<TicTacToeBoardItem>?) {
        super.submitList(list)
        notifyDataSetChanged()
    }
}

class TicTacToeBoxViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    constructor(parent: ViewGroup, @LayoutRes layoutRes: Int) :
            this(LayoutInflater.from(parent.context).inflate(layoutRes, parent, false))

    private var binding: TicTacToeBoxBinding = TicTacToeBoxBinding.bind(view)

    fun bind(item: TicTacToeBoardItem) {
        binding.ticTacToePiece.setImageResource(0)
        item.ticTacToeMove.owner.let {
            binding.ticTacToePiece.setImageResource(it.pieceDrawable)
        }
        itemView.setOnClickListener {
            item.clickListener.invoke(item.ticTacToeMove.coordinates)
        }
        if (item.highlightBackground) {
            binding.root.setBackgroundColor(Color.GREEN)
        } else {
            binding.root.setBackgroundColor(Color.TRANSPARENT)
        }
    }
}

class TicTacToeMoveDiffCallback : DiffUtil.ItemCallback<TicTacToeBoardItem>() {

    override fun areItemsTheSame(
        oldItem: TicTacToeBoardItem,
        newItem: TicTacToeBoardItem
    ): Boolean {
        return oldItem.ticTacToeMove.coordinates == newItem.ticTacToeMove.coordinates
    }

    override fun areContentsTheSame(
        oldItem: TicTacToeBoardItem,
        newItem: TicTacToeBoardItem
    ): Boolean {
        return oldItem == newItem
    }
}

class TicTacToeGridLayoutManager(context: Context, boardSize: Int) :
    GridLayoutManager(context, boardSize, LinearLayoutManager.VERTICAL, false) {

    override fun canScrollHorizontally(): Boolean = false

    override fun canScrollVertically(): Boolean = false

    override fun generateDefaultLayoutParams(): RecyclerView.LayoutParams {
        return setLayoutParams(super.generateDefaultLayoutParams())
    }

    override fun generateLayoutParams(lp: ViewGroup.LayoutParams?): RecyclerView.LayoutParams {
        return setLayoutParams(super.generateLayoutParams(lp))
    }

    override fun generateLayoutParams(
        c: Context?,
        attrs: AttributeSet?
    ): RecyclerView.LayoutParams {
        return setLayoutParams(super.generateLayoutParams(c, attrs))
    }

    override fun checkLayoutParams(lp: RecyclerView.LayoutParams): Boolean {
        val layoutParams = generateDefaultLayoutParams()
        return super.checkLayoutParams(lp) && layoutParams.height == lp.height
    }

    private fun setLayoutParams(params: RecyclerView.LayoutParams): RecyclerView.LayoutParams {
        params.height = height / spanCount
        return params
    }
}

data class TicTacToeBoardItem(
    val ticTacToeMove: TicTacToeMove,
    val clickListener: (Pair<Int, Int>) -> Unit,
    val highlightBackground: Boolean = false
) {

    // helper variable
    val ownerOrdinal
        get() = ticTacToeMove.owner.ordinal
}