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
        var bestScore = 0
        var bestPosition = Position(0, 0)

        if (board.getStatus().isFinished) {
            bestScore = calculate(board)
        } else {
            for (position in board.getEmptyPositions()) {
                if (board.getEmptyPositions().isEmpty()) return Score(bestPosition, bestScore)
                val copiedBoard = Board(board.size, board.gameTable.toMutableMap())
                copiedBoard.setPosition(position, seed)
                if (seed == currentSeed) {
                    val score = findMinMax(copiedBoard, oppositeSeed)
                    if (score.value > bestScore) {
                        bestScore = score.value
                        bestPosition = position
                    }
                } else {
                    val score = findMinMax(copiedBoard, currentSeed)
                    if (score.value < bestScore) {
                        bestScore = score.value
                        bestPosition = position
                    }
                }
            }
        }
        return Score(bestPosition, bestScore)
    }

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