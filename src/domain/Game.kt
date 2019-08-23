package domain

import domain.entity.Player
import domain.entity.Seed
import domain.entity.reverse

class Game(
    private val ai: Player = Player.AI(Seed.O),
    private val user: Player = Player.User,
    size: Int = 3
) {
    private var board = Board(size, hashMapOf()).apply { initGameTable() }

    fun playAIWithAI() {
        var seed : Seed = Seed.O
        while (board.getStatus().isFinished.not()) {
            seed = seed.reverse()
            val pos = (ai as Player.AI).findBestMove(board, seed)
            // TODO: always firs position issue
            board.setPosition(pos, seed)
            board.printInConsole()
        }
    }
}