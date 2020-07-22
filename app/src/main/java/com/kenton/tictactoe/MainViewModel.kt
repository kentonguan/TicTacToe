package com.kenton.tictactoe

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {

    private val xMoves = mutableListOf<TicTacToeMove>()
    private val oMoves = mutableListOf<TicTacToeMove>()

    var currentPiece: MutableLiveData<TicTacToePiece> = MutableLiveData(TicTacToePiece.X)
    var newGameEvent: MutableLiveData<Unit> = MutableLiveData()

    private var lastMove: TicTacToeMove? = null

    override fun onCleared() {
        super.onCleared()
        newGameEvent.value = null
    }

    fun onPiecePlaced(coordinates: Pair<Int, Int>) {
        lastMove = TicTacToeMove(coordinates, currentPiece.value!!)
        if (currentPiece.value == TicTacToePiece.X) {
            xMoves.add(lastMove!!)
        } else {
            oMoves.add(lastMove!!)
        }
        currentPiece.value = currentPiece.value?.invert()
    }

    fun onNewGameClick() {
        newGameEvent.value = Unit
    }

    private fun checkIfMoveCompletes() {

    }

    private fun checkVerticalCompletion() {

    }

    private fun checkHorizontalCompletion() {

    }

    private fun checkDiagonalCompletion() {

    }

    private fun checkCornerCompletion() {

    }

    private fun checkBoxCompletion() {

    }
}