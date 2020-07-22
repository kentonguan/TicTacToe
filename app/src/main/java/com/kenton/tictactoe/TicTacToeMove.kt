package com.kenton.tictactoe

import androidx.annotation.DrawableRes

data class TicTacToeMove(val coordinates: Pair<Int, Int>, val owner: TicTacToePiece? = null) {


}

enum class TicTacToePiece(@DrawableRes val pieceDrawable: Int) {
    X(R.drawable.ic_x_piece),
    O(R.drawable.ic_o_piece);

    fun invert(): TicTacToePiece {
        return if (this == X) {
            O
        } else {
            X
        }
    }
}