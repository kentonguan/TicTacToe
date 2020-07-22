package com.kenton.tictactoe

typealias TicTacToeBoardState = Array<Array<TicTacToeBoardItem>>

fun TicTacToeBoardState.isHorizontallyComplete(lastMove: TicTacToeMove): Boolean {
    val lastMoveOwnerOrdinal = lastMove.owner.ordinal
    for (column in 0 until size) {
        val owner = this[lastMove.coordinates.first][column].ticTacToeMove.owner
        if (owner.ordinal != lastMoveOwnerOrdinal) {
            return false
        }
    }

    return true
}

fun TicTacToeBoardState.isVerticallyComplete(lastMove: TicTacToeMove): Boolean {
    val lastMoveOwnerOrdinal = lastMove.owner.ordinal
    for (row in 0 until size) {
        val owner = this[row][lastMove.coordinates.second].ticTacToeMove.owner
        if (owner.ordinal != lastMoveOwnerOrdinal) {
            return false
        }
    }

    return true
}

fun TicTacToeBoardState.isDownwardsDiagonalComplete(lastMove: TicTacToeMove): Boolean {
    val lastMoveOwnerOrdinal = lastMove.owner.ordinal
    for (coordinate in 0 until size) {
        val owner = this[coordinate][coordinate].ticTacToeMove.owner
        if (owner.ordinal != lastMoveOwnerOrdinal) {
            return false
        }
    }

    return true
}

fun TicTacToeBoardState.isUpwardsDiagonalComplete(lastMove: TicTacToeMove): Boolean {
    val lastMoveOwnerOrdinal = lastMove.owner.ordinal
    for (coordinate in 0 until size) {
        val owner = this[coordinate][size - coordinate - 1].ticTacToeMove.owner
        if (owner.ordinal != lastMoveOwnerOrdinal) {
            return false
        }
    }

    return true
}

fun TicTacToeBoardState.isCornerComplete(lastMove: TicTacToeMove): Boolean {
    val lastMoveOwner = lastMove.owner
    return this[0][0].ownerOrdinal == lastMoveOwner.ordinal &&
            this[0][lastIndex].ownerOrdinal == lastMoveOwner.ordinal &&
            this[lastIndex][0].ownerOrdinal == lastMoveOwner.ordinal &&
            this[lastIndex][lastIndex].ownerOrdinal == lastMoveOwner.ordinal
}

fun TicTacToeBoardState.isBoxComplete(lastMove: TicTacToeMove): Boolean {
    val coordinates = lastMove.coordinates
    val row = coordinates.first
    val column = coordinates.second
    val ownerOrdinal = lastMove.owner.ordinal

    val boxCompleteTopLeft = isTopLeftComplete(row, column, ownerOrdinal)
    val boxCompleteTopRight = isTopRightComplete(row, column, ownerOrdinal)
    val boxCompleteBottomLeft = isBottomLeftComplete(row, column, ownerOrdinal)
    val boxCompleteBottomRight = isBottomRightComplete(row, column, ownerOrdinal)

    return boxCompleteBottomLeft || boxCompleteBottomRight || boxCompleteTopLeft || boxCompleteTopRight
}

fun TicTacToeBoardState.toList(): List<TicTacToeBoardItem> {
    val items = mutableListOf<TicTacToeBoardItem>()

    for (element in this) {
        items.addAll(element)
    }

    return items
}

fun TicTacToeBoardState.getBoxHighlightCoordinates(lastMove: TicTacToeMove): Array<Pair<Int, Int>> {
    val row = lastMove.coordinates.first
    val column = lastMove.coordinates.second
    val ownerOrdinal = lastMove.owner.ordinal
    return when {
        isTopLeftComplete(row, column, ownerOrdinal) -> {
            arrayOf(
                Pair(row, column),
                Pair(row, column + 1),
                Pair(row + 1, column),
                Pair(row + 1, column + 1)
            )
        }
        isTopRightComplete(row, column, ownerOrdinal) -> {
            arrayOf(
                Pair(row, column),
                Pair(row, column - 1),
                Pair(row + 1, column),
                Pair(row + 1, column - 1)
            )
        }
        isBottomLeftComplete(row, column, ownerOrdinal) -> {
            arrayOf(
                Pair(row, column),
                Pair(row, column + 1),
                Pair(row - 1, column),
                Pair(row - 1, column + 1)
            )
        }
        isBottomRightComplete(row, column, ownerOrdinal) -> {
            arrayOf(
                Pair(row, column),
                Pair(row, column - 1),
                Pair(row - 1, column),
                Pair(row - 1, column - 1)
            )
        }
         else -> arrayOf()
    }
}

// Check Move as top left quadrant of box. If it's on the bottom row or rightmost column return false
private fun TicTacToeBoardState.isTopLeftComplete(
    row: Int,
    column: Int,
    ownerOrdinal: Int
): Boolean {
    return if (row == lastIndex || column == lastIndex) {
        false
    } else {
        this[row][column + 1].ownerOrdinal == ownerOrdinal &&
                this[row + 1][column].ownerOrdinal == ownerOrdinal &&
                this[row + 1][column + 1].ownerOrdinal == ownerOrdinal
    }
}

// Check Move as top right quadrant of box. If it's on the bottom row or leftmost column return false
private fun TicTacToeBoardState.isTopRightComplete(
    row: Int,
    column: Int,
    ownerOrdinal: Int
): Boolean {
    return if (row == lastIndex || column == 0) {
        false
    } else {
        this[row][column - 1].ownerOrdinal == ownerOrdinal &&
                this[row + 1][column].ownerOrdinal == ownerOrdinal &&
                this[row + 1][column - 1].ownerOrdinal == ownerOrdinal
    }
}

// Check Move as bottom left quadrant of box. If it's on the top row or the rightmost column return false
private fun TicTacToeBoardState.isBottomLeftComplete(
    row: Int,
    column: Int,
    ownerOrdinal: Int
): Boolean {
    return if (row == 0 || column == lastIndex) {
        false
    } else {
        this[row][column + 1].ownerOrdinal == ownerOrdinal &&
                this[row - 1][column].ownerOrdinal == ownerOrdinal &&
                this[row - 1][column + 1].ownerOrdinal == ownerOrdinal
    }
}

// Check Move as bottom right quadrant of box. If it's on the top row or the leftmost column return false
private fun TicTacToeBoardState.isBottomRightComplete(
    row: Int,
    column: Int,
    ownerOrdinal: Int
): Boolean {
    return if (row == 0 || column == 0) {
        false
    } else {
        this[row][column - 1].ownerOrdinal == ownerOrdinal &&
                this[row - 1][column].ownerOrdinal == ownerOrdinal &&
                this[row - 1][column - 1].ownerOrdinal == ownerOrdinal
    }
}