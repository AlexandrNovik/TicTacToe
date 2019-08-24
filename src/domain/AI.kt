package domain

import domain.entity.Position
import domain.entity.Seed
import domain.entity.reverse

object AI {
    private var currentSeed: Seed = Seed.Empty
    private var oppositeSeed: Seed = Seed.Empty
    fun findBestPosition(board: Board, seed: Seed): Position {
        currentSeed = seed
        oppositeSeed = seed.reverse()
        return findMinMax(board, seed).position
    }

    private fun findMinMax(board: Board, seed: Seed): Score {
        var bestScore = if (seed == currentSeed) Int.MIN_VALUE else Int.MAX_VALUE
        var bestPosition = Position(0, 0)

        if (board.getStatus().isFinished) {
            bestScore = calculate(board)
        } else {
            for (position in board.getEmptyPositions()) {
                val copiedBoard = board.copy(gameTable = board.gameTable.toMutableMap())
                copiedBoard.setPosition(position, seed)
                if (seed == currentSeed) {
                    val value = findMinMax(copiedBoard, oppositeSeed).value
                    if (value > bestScore) {
                        bestScore = value
                        bestPosition = position
                    }
                } else {
                    val value = findMinMax(copiedBoard, currentSeed).value
                    if (value < bestScore) {
                        bestScore = value
                        bestPosition = position
                    }
                }
            }
        }
        return Score(bestPosition, bestScore)
    }

    // TODO: implement evristic for the lines
    private fun calculate(board: Board): Int {
        if (board.getWinner() == currentSeed) return 100
        if (board.getWinner() == oppositeSeed) return -100
        return 0
    }

    data class Score(
        val position: Position = Position(0, 0),
        val value: Int = Int.MIN_VALUE
    )
}