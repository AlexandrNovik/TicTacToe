package domain

import domain.entity.Position
import domain.entity.Seed
import domain.entity.reverse
import javafx.geometry.Pos

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
            board.getEmptyPositions().forEachIndexed { index, position ->
                val copiedBoard = Board(size = board.size, gameTable = board.gameTable.toMutableMap())
                // TODO: WTF hash tag wrong copy
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
        } else {
            bestScore = calculate(board)
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