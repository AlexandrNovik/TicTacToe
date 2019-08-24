package domain

import domain.entity.Position
import domain.entity.Seed
import domain.entity.reverse

class AI {
    private var currentSeed: Seed = Seed.Empty
    private var oppositeSeed: Seed = Seed.Empty
    fun findBestPosition(board: Board, seed: Seed): Position {
        currentSeed = seed
        oppositeSeed = seed.reverse()
        return findMinMax(board, seed, 3).position
    }

    private fun findMinMax(board: Board, seed: Seed, deep: Int): Score {
        var bestScore = if (seed == currentSeed) Int.MIN_VALUE else Int.MAX_VALUE
        var bestPosition = Position(0, 0)

        if (board.getStatus().isFinished || deep == 0) {
            bestScore = calculateHeuristic(board)
        } else {
            for (position in board.getEmptyPositions()) {
                val copiedBoard = board.copy(gameTable = board.gameTable.toMutableMap())
                copiedBoard.setPosition(position, seed)
                if (seed == currentSeed) {
                    val value = findMinMax(copiedBoard, oppositeSeed, deep - 1).value
                    if (value > bestScore) {
                        bestScore = value
                        bestPosition = position
                    }
                } else {
                    val value = findMinMax(copiedBoard, currentSeed, deep - 1).value
                    if (value < bestScore) {
                        bestScore = value
                        bestPosition = position
                    }
                }
            }
        }
        return Score(bestPosition, bestScore)
    }

    private fun calculateSimple(board: Board): Int {
        if (board.getWinner() == currentSeed) return 100
        if (board.getWinner() == oppositeSeed) return -100
        return 0
    }

    private fun calculateHeuristic(board: Board): Int {
        // calculating sum Heuristics for all 8 lines : 3 strings, 3 rows, 2 diagonals
        return board.evaluateLines(currentSeed, oppositeSeed)
    }

    data class Score(
        val position: Position = Position(0, 0),
        val value: Int = Int.MIN_VALUE
    )
}