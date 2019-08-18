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
        return findMinMax(board, seed).position
    }

    private fun findMinMax(board: Board, seed: Seed): Score {
        var bestScore = 0
        var bestPosition = Position(0, 0)
        if (board.hasEmptyPositions()) {
            for (i in board.getEmptyPositions()) {
                val copiedBoard = board.copy()
                copiedBoard.setPosition(i, seed)
                if (seed == currentSeed) {
                    val score = findMinMax(copiedBoard, seed.reverse())
                    if (score.value > bestScore) {
                        bestScore = score.value
                        bestPosition = i
                    }
                } else {
                    val score = findMinMax(copiedBoard, seed.reverse())
                    if (score.value < bestScore) {
                        bestScore = score.value
                        bestPosition = i
                    }
                }
            }
        } else {
            bestScore = calculate(board)
        }
        return Score(bestPosition, bestScore)
    }

    fun calculate(board: Board): Int {
        // TODO:
        return 0
    }

    data class Score(
        val position: Position = Position(0, 0),
        val value: Int = Int.MIN_VALUE
    )
}