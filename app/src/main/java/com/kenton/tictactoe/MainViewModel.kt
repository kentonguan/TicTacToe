package com.kenton.tictactoe

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

@Suppress("UNCHECKED_CAST")
class MainViewModelFactory(private val boardSize: Int) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass == MainViewModel::class.java) {
            MainViewModel(boardSize) as T
        } else {
            super.create(modelClass)
        }
    }
}

class MainViewModel(private val boardSize: Int) : ViewModel() {

    // O always goes first
    var currentPiece: MutableLiveData<TicTacToePiece> = MutableLiveData(TicTacToePiece.O)
    var ticTacToeBoard = MutableLiveData<List<TicTacToeBoardItem>>()

    private var lastMove: TicTacToeMove? = null
    private var internalGameState: TicTacToeBoardState

    init {
        internalGameState = clearBoard()
        ticTacToeBoard.value = internalGameState.toList()
    }

    fun onNewGameClick() {
        currentPiece.value = TicTacToePiece.O
        internalGameState = clearBoard()
        ticTacToeBoard.value = internalGameState.toList()
    }

    private fun onPiecePlaced(coordinates: Pair<Int, Int>) {
        val oldMoveState = internalGameState[coordinates.first][coordinates.second]
        // Don't let someone replace a move
        if (oldMoveState.ticTacToeMove.owner != TicTacToePiece.EMPTY) {
            return
        }
        // If the move is valid, update the move and store it as our last made move
        val newMoveState = TicTacToeMove(coordinates, currentPiece.value!!)
        lastMove = newMoveState

        // Update internal game board state
        internalGameState[coordinates.first][coordinates.second] =
            TicTacToeBoardItem(newMoveState, this::onPiecePlaced)

        checkIfMoveCompletes()

        ticTacToeBoard.value = internalGameState.toList()
        currentPiece.value = currentPiece.value?.invert()
    }

    private fun checkIfMoveCompletes() {
        if (internalGameState.isVerticallyComplete(lastMove)) {
            highlightVerticalRow(lastMove!!.coordinates)
        }

        if (internalGameState.isHorizontallyComplete(lastMove)) {
            Log.e("YAY", "${currentPiece.value?.name} won horizontally!")
        }

        if (internalGameState.isLeftDiagonalComplete(lastMove)) {
            Log.e("YAY", "${currentPiece.value?.name} won leftDiagonally!")
        }

        if (internalGameState.isRightDiagonalComplete(lastMove)) {
            Log.e("YAY", "${currentPiece.value?.name} won rightDiagonally!")
        }

        if (internalGameState.isCornerComplete(lastMove)) {
            Log.e("YAY", "${currentPiece.value?.name} won corners!")
        }

        if (internalGameState.isBoxComplete(lastMove)) {
            Log.e("YAY", "${currentPiece.value?.name} made a box!")
        }
    }

    private fun highlightVerticalRow(coordinates: Pair<Int, Int>) {
        for (row in 0 until boardSize) {
            val originalState = internalGameState[row][coordinates.second]
            val newState = originalState.copy(highlightBackground = true)
            internalGameState[row][coordinates.second] = newState
        }
    }

    private fun clearBoard(): TicTacToeBoardState {
        return TicTacToeBoardState(boardSize) {
            generateDefaultBoardRow(it)
        }
    }

    private fun generateDefaultBoardRow(rowNumber: Int): Array<TicTacToeBoardItem> {
        return Array(boardSize) {
            TicTacToeBoardItem(TicTacToeMove(Pair(rowNumber, it)), this::onPiecePlaced)
        }
    }
}