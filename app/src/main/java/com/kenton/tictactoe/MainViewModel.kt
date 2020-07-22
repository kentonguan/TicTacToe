package com.kenton.tictactoe

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

@Suppress("UNCHECKED_CAST")
class MainViewModelFactory(private val boardSize: Int): ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass == MainViewModel::class.java) {
            MainViewModel(boardSize) as T
        } else {
            super.create(modelClass)
        }
    }
}

class MainViewModel(private val boardSize: Int) : ViewModel() {

    private val xMoves = mutableListOf<Pair<Int, Int>>()
    private val oMoves = mutableListOf<Pair<Int, Int>>()

    // O always goes first
    var currentPiece: MutableLiveData<TicTacToePiece> = MutableLiveData(TicTacToePiece.O)
    var ticTacToeBoard = MutableLiveData<MutableList<TicTacToeBoardItem>>()

    init {
        ticTacToeBoard.value = clearBoard()
    }

    fun onNewGameClick() {
        ticTacToeBoard.value = clearBoard()
        currentPiece.value = TicTacToePiece.O
    }

    private fun onPiecePlaced(itemId: Int) {
        val newBoardState = ticTacToeBoard.value!!
        val oldMoveState = newBoardState[itemId].ticTacToeMove
        // Don't let someone replace a move
        if (oldMoveState.owner != null) {
            return
        }
        val newMoveState = TicTacToeMove(oldMoveState.coordinates, currentPiece.value)

        newBoardState.removeAt(itemId)
        newBoardState.add(itemId, TicTacToeBoardItem(itemId, newMoveState, this::onPiecePlaced))
        ticTacToeBoard.value = newBoardState

        if (currentPiece.value == TicTacToePiece.X) {
            xMoves.add(newMoveState.coordinates)
        } else {
            oMoves.add(newMoveState.coordinates)
        }
        currentPiece.value = currentPiece.value?.invert()
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

    private fun clearBoard(): MutableList<TicTacToeBoardItem> {
        val moves = mutableListOf<TicTacToeBoardItem>()
        for (item in 0 until (boardSize * boardSize)) {
            val row = item / boardSize
            val column = item.rem(boardSize)
            val coordinates = Pair(row, column)
            val move = TicTacToeMove(coordinates)

            moves.add(TicTacToeBoardItem(item, move, this::onPiecePlaced))
        }

        return moves
    }
}