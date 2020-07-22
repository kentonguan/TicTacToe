package com.kenton.tictactoe

import androidx.annotation.DrawableRes

data class TicTacToeMove(val coordinates: Pair<Int, Int>, val owner: TicTacToePiece = TicTacToePiece.EMPTY)

enum class TicTacToePiece(@DrawableRes val pieceDrawable: Int) {
    X(R.drawable.ic_x_piece),
    O(R.drawable.ic_o_piece),
    EMPTY(0);

    fun invert(): TicTacToePiece {
        return when(this) {
            X -> O
            O -> X
            else -> EMPTY
        }
    }
}