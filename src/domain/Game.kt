package domain

import domain.entity.Player

class Game(
    private val player1: Player = Player.AI,
    private val player2: Player = Player.AI,
    private val size: Int = 3
) {
    init {
        initGame()
    }

    private fun initGame() {
        val board = Board(size)
    }

    fun playAI() {

    }
}