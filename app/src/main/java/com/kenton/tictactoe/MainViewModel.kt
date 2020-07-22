package com.kenton.tictactoe

import android.util.Log
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

    // O always goes first
    var currentPiece: MutableLiveData<TicTacToePiece> = MutableLiveData(TicTacToePiece.O)
    var ticTacToeBoard = MutableLiveData<MutableList<TicTacToeBoardItem>>()

    private var lastMove: TicTacToeMove? = null
    private var internalGameState = Array(boardSize) { IntArray(boardSize) { -1 } }

    init {
        ticTacToeBoard.value = clearBoard()
    }

    fun onNewGameClick() {
        ticTacToeBoard.value = clearBoard()
        currentPiece.value = TicTacToePiece.O
        internalGameState = Array(boardSize) { IntArray(boardSize) { -1 } }
    }

    private fun onPiecePlaced(itemId: Int) {
        val newBoardState = ticTacToeBoard.value!!
        val oldMoveState = newBoardState[itemId].ticTacToeMove
        // Don't let someone replace a move
        if (oldMoveState.owner != null) {
            return
        }
        val newMoveState = TicTacToeMove(oldMoveState.coordinates, currentPiece.value)
        lastMove = newMoveState

        newBoardState.removeAt(itemId)
        newBoardState.add(itemId, TicTacToeBoardItem(itemId, newMoveState, this::onPiecePlaced))
        ticTacToeBoard.value = newBoardState
        internalGameState[newMoveState.coordinates.first][newMoveState.coordinates.second] = newMoveState.owner?.ordinal ?: -1

        if (checkVerticalCompletion()) {
            Log.e("YAY", "${currentPiece.value?.name} won vertically!")
        }

        if (checkHorizontalCompletion()) {
            Log.e("YAY", "${currentPiece.value?.name} won horizontally!")
        }

        if (checkLeftDiagonalCompletion()) {
            Log.e("YAY", "${currentPiece.value?.name} won leftDiagonally!")
        }

        if (checkRightDiagonalCompletion()) {
            Log.e("YAY", "${currentPiece.value?.name} won rightDiagonally!")
        }

        if (checkCornerCompletion()) {
            Log.e("YAY", "${currentPiece.value?.name} won corners!")
        }

        if (checkBoxCompletion()) {
            Log.e("YAY", "${currentPiece.value?.name} made a box!")
        }
        currentPiece.value = currentPiece.value?.invert()
    }

    private fun checkIfMoveCompletes() {

    }

    private fun checkVerticalCompletion(): Boolean {
        return lastMove?.let {
            for (row in 0 until boardSize) {
                if (internalGameState[row][it.coordinates.second] != it.owner?.ordinal) {
                    return false
                }
            }
            true
        } ?: false
    }

    private fun checkHorizontalCompletion(): Boolean {
        return lastMove?.let {
            for (column in 0 until boardSize) {
                if (internalGameState[it.coordinates.first][column] != it.owner?.ordinal) {
                    return false
                }
            }
            true
        } ?: false
    }

    private fun checkLeftDiagonalCompletion(): Boolean {
        return lastMove?.let {
            for (coordinate in 0 until boardSize) {
                if (internalGameState[coordinate][coordinate] != it.owner?.ordinal) {
                    return false
                }
            }
            true
        } ?: false
    }

    private fun checkRightDiagonalCompletion(): Boolean {
        return lastMove?.let {
            for (coordinate in 0 until boardSize) {
                if (internalGameState[coordinate][boardSize - coordinate - 1] != it.owner?.ordinal) {
                    return false
                }
            }
            true
        } ?: false
    }


    private fun checkCornerCompletion(): Boolean {
        val lastMoveOwner = lastMove?.owner ?: return false
        val lastIndex = boardSize - 1
        return internalGameState[0][0] == lastMoveOwner.ordinal &&
                internalGameState[0][lastIndex] == lastMoveOwner.ordinal &&
                internalGameState[lastIndex][0] == lastMoveOwner.ordinal &&
                internalGameState[lastIndex][lastIndex] == lastMoveOwner.ordinal
    }

    private fun checkBoxCompletion(): Boolean {
        val coordinates = lastMove?.coordinates ?: return false
        val xCoordinate = coordinates.first
        val yCoordinate = coordinates.second
        val ownerOrdinal = lastMove?.owner?.ordinal
        val lastIndex = boardSize - 1

        // Check Move as top left quadrant of box. If it's on the bottom row or rightmost column return false
        val boxCompleteTopLeft = if (xCoordinate == lastIndex || yCoordinate == lastIndex) {
            false
        } else {
            internalGameState[xCoordinate][yCoordinate + 1] == ownerOrdinal &&
                    internalGameState[xCoordinate + 1][yCoordinate] == ownerOrdinal &&
                    internalGameState[xCoordinate + 1][yCoordinate + 1] == ownerOrdinal
        }

        // Check Move as top right quadrant of box. If it's on the bottom row or leftmost column return false
        val boxCompleteTopRight = if (xCoordinate == lastIndex || yCoordinate == 0) {
            false
        } else {
            internalGameState[xCoordinate][yCoordinate - 1] == ownerOrdinal &&
                    internalGameState[xCoordinate + 1][yCoordinate] == ownerOrdinal &&
                    internalGameState[xCoordinate + 1][yCoordinate - 1] == ownerOrdinal
        }

        // Check Move as bottom left quadrant of box. If it's on the top row or the rightmost column return false
        val boxCompleteBottomLeft = if (xCoordinate == 0 || yCoordinate == lastIndex) {
            false
        } else {
            internalGameState[xCoordinate][yCoordinate + 1] == ownerOrdinal &&
                    internalGameState[xCoordinate - 1][yCoordinate] == ownerOrdinal &&
                    internalGameState[xCoordinate - 1][yCoordinate + 1] == ownerOrdinal
        }

        // Check Move as bottom right quadrant of box. If it's on the top row or the leftmost column return false
        val boxCompleteBottomRight = if (xCoordinate == 0 || yCoordinate == 0) {
            false
        } else {
            internalGameState[xCoordinate][yCoordinate - 1] == ownerOrdinal &&
                    internalGameState[xCoordinate - 1][yCoordinate] == ownerOrdinal &&
                    internalGameState[xCoordinate - 1][yCoordinate - 1] == ownerOrdinal
        }

        return boxCompleteBottomLeft || boxCompleteBottomRight || boxCompleteTopLeft || boxCompleteTopRight
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