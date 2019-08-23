package domain

import domain.entity.Player
import domain.entity.Seed
import domain.entity.reverse

class Game(
    private val ai: Player = Player.AI(Seed.O),
    private val user: Player = Player.User,
    size: Int = 3
) {
    private val board = Board(size, hashMapOf())

    fun playAIWithAI() {
        var seed : Seed = Seed.O
        while (board.hasEmptyPositions()) {
            seed = seed.reverse()
            val pos = (ai as Player.AI).findBestMove(board, seed)
            board.setPosition(pos, seed)
            board.printInConsole()
        }
    }
}