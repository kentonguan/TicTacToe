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
    val lastIndex = size - 1
    return this[0][0].ownerOrdinal == lastMoveOwner.ordinal &&
            this[0][lastIndex].ownerOrdinal == lastMoveOwner.ordinal &&
            this[lastIndex][0].ownerOrdinal == lastMoveOwner.ordinal &&
            this[lastIndex][lastIndex].ownerOrdinal == lastMoveOwner.ordinal
}

fun TicTacToeBoardState.isBoxComplete(lastMove: TicTacToeMove): Boolean {
    val coordinates = lastMove.coordinates
    val xCoordinate = coordinates.first
    val yCoordinate = coordinates.second
    val ownerOrdinal = lastMove.owner.ordinal
    val lastIndex = size - 1

    // Check Move as top left quadrant of box. If it's on the bottom row or rightmost column return false
    val boxCompleteTopLeft = isTopLeftComplete(xCoordinate, yCoordinate, ownerOrdinal)

    // Check Move as top right quadrant of box. If it's on the bottom row or leftmost column return false
    val boxCompleteTopRight = if (xCoordinate == lastIndex || yCoordinate == 0) {
        false
    } else {
        this[xCoordinate][yCoordinate - 1].ownerOrdinal == ownerOrdinal &&
                this[xCoordinate + 1][yCoordinate].ownerOrdinal == ownerOrdinal &&
                this[xCoordinate + 1][yCoordinate - 1].ownerOrdinal == ownerOrdinal
    }

    // Check Move as bottom left quadrant of box. If it's on the top row or the rightmost column return false
    val boxCompleteBottomLeft = if (xCoordinate == 0 || yCoordinate == lastIndex) {
        false
    } else {
        this[xCoordinate][yCoordinate + 1].ownerOrdinal == ownerOrdinal &&
                this[xCoordinate - 1][yCoordinate].ownerOrdinal == ownerOrdinal &&
                this[xCoordinate - 1][yCoordinate + 1].ownerOrdinal == ownerOrdinal
    }

    // Check Move as bottom right quadrant of box. If it's on the top row or the leftmost column return false
    val boxCompleteBottomRight = if (xCoordinate == 0 || yCoordinate == 0) {
        false
    } else {
        this[xCoordinate][yCoordinate - 1].ownerOrdinal == ownerOrdinal &&
                this[xCoordinate - 1][yCoordinate].ownerOrdinal == ownerOrdinal &&
                this[xCoordinate - 1][yCoordinate - 1].ownerOrdinal == ownerOrdinal
    }

    return boxCompleteBottomLeft || boxCompleteBottomRight || boxCompleteTopLeft || boxCompleteTopRight
}

fun TicTacToeBoardState.toList(): List<TicTacToeBoardItem> {
    val items = mutableListOf<TicTacToeBoardItem>()

    for (element in this) {
        items.addAll(element)
    }

    return items
}

private fun TicTacToeBoardState.isTopLeftComplete(xCoordinate: Int, yCoordinate: Int, ownerOrdinal: Int): Boolean {
    return if (xCoordinate == lastIndex || yCoordinate == lastIndex) {
        false
    } else {
        this[xCoordinate][yCoordinate + 1].ownerOrdinal == ownerOrdinal &&
                this[xCoordinate + 1][yCoordinate].ownerOrdinal == ownerOrdinal &&
                this[xCoordinate + 1][yCoordinate + 1].ownerOrdinal == ownerOrdinal
    }
}