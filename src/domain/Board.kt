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
    fun setPosition(position: Position, seed: Seed) {
        val tableSeed = gameTable[position]
        if (tableSeed == Seed.Empty && tableSeed != seed) {
            gameTable[position] = seed
        }
    }

    fun getEmptyPositions() = gameTable.toMap().filter { it.value == Seed.Empty }.toList().map { it.first }

    fun getWinner(): Seed {
        return getSessionStatus().winner
    }

    fun getStatus(): SessionStatus = getSessionStatus()


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
            val win = it.positions
                .fold(true) { acc, pos -> gameTable[pos] == first && gameTable[pos] != Seed.Empty && acc }
            if (win) {
                val winSeed = gameTable[it.positions.first()] ?: Seed.Empty
                return SessionStatus(isFinished = true, winner = winSeed)
            }
        }
        return SessionStatus(getEmptyPositions().isEmpty(), Seed.Empty)
    }

    fun initGameTable() {
        (0 until size).forEach { external ->
            (0 until size).forEach { internal ->
                gameTable[Position(external, internal)] = Seed.Empty
            }
        }
    }

    fun evaluateLines(ourSeed: Seed, oppSeed: Seed): Int {
        var score = 0
        lines.forEach {
            score += evaluateLine(it, ourSeed, oppSeed)
        }
        return score
    }

    /**
     * Heuristic fun for 3 positions
     *
     * @return +100, +10, +1 for 3-, 2-, 1- in line seeds ourSeed.
     * -100, -10, -1 for 3-, 2-, 1- in line seeds  oppSeed.
     * 0 if line contains the both: X и O
     */
    private fun evaluateLine(line: Line, ourSeed: Seed, oppSeed: Seed): Int {
        var score = 0

        val cell1 = gameTable[line.positions[0]]
        val cell2 = gameTable[line.positions[1]]
        val cell3 = gameTable[line.positions[2]]

        if (cell1 == ourSeed) {
            score = 1
        } else if (cell1 == oppSeed) {
            score = -1
        }

        if (cell2 == ourSeed) {
            if (score == 1) {   // cell1 is ourSeed
                score = 10
            } else if (score == -1) {  // cell1 is oppSeed
                return 0
            } else {  // cell1 is empty
                score = 1
            }
        } else if (cell2 == oppSeed) {
            if (score == -1) { // cell1 is oppSeed
                score = -10
            } else if (score == 1) { // cell1 is ourSeed
                return 0
            } else {  // cell1 is empty
                score = -1
            }
        }

        if (cell3 == ourSeed) {
            if (score > 0) {  // cell1 and/or cell2 is ourSeed
                score *= 10
            } else if (score < 0) {  // cell1 and/or cell2 is oppSeed
                return 0
            } else {  // cell1 and cell2 are empty
                score = 1
            }
        } else if (cell3 == oppSeed) {
            if (score < 0) {  // cell1 and/or cell2 is oppSeed
                score *= 10
            } else if (score > 1) {  // cell1 and/or cell2 is ourSeed
                return 0
            } else {  // cell1 and cell2 are empty
                score = -1
            }
        }
        return score
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



