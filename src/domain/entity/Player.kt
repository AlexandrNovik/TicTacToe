package domain.entity

import domain.Board
import domain.AI as GameAI

sealed class Player {
    object User : Player()
    class AI(private val aiSeed: Seed) : Player() {
        private val ai = GameAI
        fun findBestMove(board: Board, seed: Seed = aiSeed) = ai.findBestPosition(board, seed)
    }

}