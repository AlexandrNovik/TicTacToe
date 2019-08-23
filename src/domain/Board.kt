package domain

import domain.entity.Line
import domain.entity.Position
import domain.entity.Seed
import domain.entity.print

/*
   *         Board coordinates
   *
   *         Y |---|---|---|
   *         2 |   |   |   |
   *         1 |   |   |   |
   *         0 |   |   |   |
   *         --|---|---|---|
   *         X | 0 | 1 | 2 |
   *
   * */
data class Board(val size: Int = 3, val gameTable: MutableMap<Position, Seed>) {

    init {
        initGameTable()
    }

    fun setPosition(position: Position, seed: Seed) {
        gameTable[position] = seed
    }

    fun getEmptyPositions() = gameTable.toMap().filter { it.value == Seed.Empty }.toList().map { it.first }
    fun hasEmptyPositions() = gameTable.toMap().filter { it.value == Seed.Empty }.isNotEmpty()

    fun getWinner(): Seed {
        return getSessionStatus().winner
    }

    fun printInConsole2() {
        println(
            """ 
            Y |-----|-----|-----|
            2 |  ${gameTable[Position(2, 0)]?.print()}  |  ${gameTable[Position(2, 1)]?.print()}  |  ${gameTable[Position(2, 2)]?.print()}  |
            1 |  ${gameTable[Position(1, 0)]?.print()}  |  ${gameTable[Position(1, 1)]?.print()}  |  ${gameTable[Position(1, 2)]?.print()}  |
            0 |  ${gameTable[Position(0, 0)]?.print()}  |  ${gameTable[Position(0, 1)]?.print()}  |  ${gameTable[Position(0, 2)]?.print()}  |
            --|-----|-----|-----|
            X |  0  |  1  |  2  |
        """.trimIndent()
        )
    }
    fun printInConsole() {
        println(
            """ 
             |-----|-----|-----|
             |  ${gameTable[Position(2, 0)]?.print()}  |  ${gameTable[Position(2, 1)]?.print()}  |  ${gameTable[Position(2, 2)]?.print()}  |
             |  ${gameTable[Position(1, 0)]?.print()}  |  ${gameTable[Position(1, 1)]?.print()}  |  ${gameTable[Position(1, 2)]?.print()}  |
             |  ${gameTable[Position(0, 0)]?.print()}  |  ${gameTable[Position(0, 1)]?.print()}  |  ${gameTable[Position(0, 2)]?.print()}  |
             |-----|-----|-----|
        """.trimIndent()
        )
    }

    private fun getSessionStatus(): SessionStatus {
        lines.forEach {
            val first = gameTable[it.positions.first()]
            val win = it.positions.fold(true) { acc, pos -> gameTable[pos] == first && acc }
            if (win) {
                val winSeed = gameTable[it.positions.first()] ?: Seed.Empty
                return SessionStatus(isFinished = true, winner = winSeed)
            }
        }
        return SessionStatus()
    }

    private fun initGameTable() {
        (0 until size).forEach { external ->
            (0 until size).forEach { internal ->
                gameTable[Position(external, internal)] = Seed.Empty
            }
        }
    }

    val lines = arrayOf(
        Line(arrayListOf(Position(0, 2), Position(1, 2), Position(2, 2))),   // top line
        Line(arrayListOf(Position(0, 1), Position(1, 1), Position(2, 1))),   // middle line
        Line(arrayListOf(Position(0, 0), Position(1, 0), Position(2, 0))),   // bottom line
        Line(arrayListOf(Position(0, 0), Position(0, 1), Position(0, 2))),   // left row
        Line(arrayListOf(Position(1, 0), Position(1, 1), Position(1, 2))),   // middle row
        Line(arrayListOf(Position(2, 0), Position(2, 1), Position(2, 2))),   // right row
        Line(arrayListOf(Position(0, 2), Position(1, 1), Position(2, 0))),   // diagonal
        Line(arrayListOf(Position(2, 2), Position(1, 1), Position(0, 0)))    // opposite diagonal
    )

    data class SessionStatus(
        val isFinished: Boolean = false,
        val winner: Seed = Seed.Empty
    )
}



