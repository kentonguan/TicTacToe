package com.kenton.tictactoe

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
    private var gameOver: Boolean = false

    init {
        internalGameState = clearBoard()
        ticTacToeBoard.value = internalGameState.toList()
    }

    fun onNewGameClick() {
        currentPiece.value = TicTacToePiece.O
        internalGameState = clearBoard()
        ticTacToeBoard.value = internalGameState.toList()
        gameOver = false
    }

    private fun onPiecePlaced(coordinates: Pair<Int, Int>) {
        val oldMoveState = internalGameState[coordinates.first][coordinates.second]
        // Don't let someone replace a move
        if (oldMoveState.ticTacToeMove.owner != TicTacToePiece.EMPTY || gameOver) {
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
        // Since this.lastMove is mutable, Kotlin complains that it can change in between us checking
        // for null and using it, so assigning it to a val will guarantee that it never changes.
        val lastMove = this.lastMove
        if (lastMove != null) {
            gameOver = true
            when {
                internalGameState.isVerticallyComplete(lastMove) -> highlightVerticalColumn()
                internalGameState.isHorizontallyComplete(lastMove) -> highlightHorizontalRow()
                internalGameState.isDownwardsDiagonalComplete(lastMove) -> highlightDownwardsDiagonal()
                internalGameState.isUpwardsDiagonalComplete(lastMove) -> highlightUpwardsDiagonal()
                internalGameState.isCornerComplete(lastMove) -> highlightCorners()
                internalGameState.isBoxComplete(lastMove) -> highlightBox()
                else -> gameOver = false
            }
        }
    }

    private fun highlightVerticalColumn() {
        val column = lastMove?.coordinates?.second ?: return
        for (row in 0 until boardSize) {
            highlightCell(row, column)
        }
    }

    private fun highlightHorizontalRow() {
        val row = lastMove?.coordinates?.first ?: return
        for (column in 0 until boardSize) {
            highlightCell(row, column)
        }
    }

    private fun highlightDownwardsDiagonal() {
        for (coordinate in 0 until boardSize) {
            highlightCell(coordinate, coordinate)
        }
    }

    private fun highlightUpwardsDiagonal() {
        for (coordinate in 0 until boardSize) {
            highlightCell(coordinate, boardSize - coordinate - 1)
        }
    }

    private fun highlightCorners() {
        val lastIndex = boardSize - 1
        highlightCell(0, 0)
        highlightCell(0, lastIndex)
        highlightCell(lastIndex, 0)
        highlightCell(lastIndex, lastIndex)
    }

    private fun highlightBox() {
        for ((row, column) in internalGameState.getBoxHighlightCoordinates(lastMove!!)) {
            highlightCell(row, column)
        }
    }

    private fun highlightCell(row: Int, column: Int) {
        val originalState = internalGameState[row][column]
        internalGameState[row][column] = originalState.copy(highlightBackground = true)
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