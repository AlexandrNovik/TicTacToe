package domain

import domain.entity.Player
import domain.entity.Seed
import domain.entity.reverse

class Game(size: Int = 3) {
    private var board = Board(size, hashMapOf()).apply { initGameTable() }

    fun playAIWithAI() {
        val startTime = System.currentTimeMillis()
        val ai = Player.AI(Seed.O)
        var seed: Seed = Seed.O
        while (board.getStatus().isFinished.not()) {
            seed = seed.reverse()
            val pos = ai.findBestMove(board, seed)
            board.setPosition(pos, seed)
            board.printInConsole()
        }
        println("Time: ${System.currentTimeMillis() - startTime}ms")
    }
}