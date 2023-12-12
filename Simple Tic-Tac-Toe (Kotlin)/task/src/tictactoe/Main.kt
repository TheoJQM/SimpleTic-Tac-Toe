package tictactoe

import kotlin.math.abs

// Every state possible for the Tic-Tac-Toe game
enum class State(val meaning: String) {
    GAMENOTFINISHED("Game not finished"),
    DRAW("Draw"),
    XWINS("X wins"),
    OWINS("O wins"),
    IMPOSSIBLE("Impossible")
}

class TicTacToe {
    private val gridSize = 3
    private val nbBoxes = gridSize * gridSize
    private var state = State.GAMENOTFINISHED
    private val grid = mutableListOf(
            mutableListOf(' ', ' ', ' '),
            mutableListOf(' ', ' ', ' '),
            mutableListOf(' ', ' ', ' ')
    )
    private var symbol = 'X'

    init {
        loadGameGrid()
    }

    fun play() {
        printGameGrid()
        while (state == State.GAMENOTFINISHED) {
            askMove()
        }
    }

    /**
     * Print the Tic-Tac-Toe grid
     */
    private fun printGameGrid() {
        val line = "---------"

        println("""
        $line
        | ${grid[0][0]} ${grid[0][1]} ${grid[0][2]} |
        | ${grid[1][0]} ${grid[1][1]} ${grid[1][2]} |
        | ${grid[2][0]} ${grid[2][1]} ${grid[2][2]} |
        $line
    """.trimIndent())
    }

    /**
     * Fill the `grid` variable with `_`
     */
    private fun loadGameGrid() {
        for (i in 0 until gridSize) {
            for (j in 0 until gridSize) {
                grid[i][j] = '_'
            }
        }
    }

    /**
     * Ask the user where he wants to put the symbol (`X` or `O`)
     * Loop on itself while the input is incorrect
     */
    private fun askMove() {
        val coords = readln().split(" ")

        when {
            // Conditions to check if the input is exactly two numbers
            coords.size != 2 || coords[0].toIntOrNull() == null || coords[1].toIntOrNull() == null -> {
                println("You should enter numbers!")
                askMove()
            }

            // Conditions to check if the numbers given are not out of range
            coords[0].toInt() !in 1..3 || coords[1].toInt() !in 1..3  -> {
                println("Coordinates should be from 1 to 3!")
                askMove()
            }

            // Condition to check if the cell chosen is empty or not
             !emptyCell(coords[0].toInt(), coords[1].toInt()) -> {
                 println("This cell is occupied! Choose another one!")
                 askMove()
             }

            // Update the grid with the new `X`
            else -> updateGrid(coords[0].toInt(), coords[1].toInt())
        }
    }

    /**
     * Check if the cell chosen by the user is empty or not
     * @param x : The chosen line
     * @param y : The chosen column
     *
     * @return True if the cell is empty, false if not
     */
    private fun emptyCell(x: Int, y: Int): Boolean {
        return grid[x - 1][y - 1] == '_'
    }

    /**
     * Update the grid with the new symbol (`X` or `O`)
     * @param x : The chosen line
     * @param y : The chosen column
     */
    private fun updateGrid(x: Int, y: Int) {
        grid[x - 1][y - 1] = symbol
        symbol = if (symbol == 'X') 'O' else 'X'
        printGameGrid()
        checkState()
    }

    /**
     * Check the current state of the grid
     */
    private fun checkState() {
        val (xCount, oCount) = countXAndO()
        val diagonal = checkDiagonal()
        val vertical = checkVertical()
        val horizontal = checkHorizontal()

        when {
            // Every conditions that make the game impossible
            diagonal.first + vertical.first + horizontal.first > 1 ||
                    abs(xCount - oCount) > 1 ->  state = State.IMPOSSIBLE

            // Conditions for the player O to win
            diagonal == Pair(1, 'O') || vertical == Pair(1, 'O') ||
                    horizontal == Pair(1, 'O') -> state = State.OWINS

            // Conditions for the player X to win
            diagonal == Pair(1, 'X') || vertical == Pair(1, 'X') ||
                    horizontal == Pair(1, 'X') -> state = State.XWINS

            // Condition for the game to be a draw
            xCount + oCount == nbBoxes -> state = State.DRAW

            // By default, the state is on GAMENOTFINISHED
        }
        if (state != State.GAMENOTFINISHED) println(state.meaning)
    }

    /**
     * Count the number of X and O in the grid
     *
     * @return Pair<Int, Int> :
     * The first element is the number of X
     * The second element is the number of O
     */
    private fun countXAndO(): Pair<Int, Int> {
        var x = 0
        var o = 0

        for (i in 0 until gridSize) {
            for (j in 0 until gridSize) {
                when(grid[i][j]) {
                    'X' -> x++
                    'O' -> o++
                }
            }
        }

        return Pair(x,o)
    }

    /**
     * Check every column of the grid to see if there is a winner
     *
     * @return Pair<Int, Char> :
     * The first element is the number of vertical winning lines( not suppose to be more than 1)
     * The second element is the winning character (X or O)
     */
    private fun checkVertical(): Pair<Int, Char> {
        var line = Pair(0, ' ')
        for (j in 0 until gridSize) {
            val i = 0
            when  {
                grid[i][j] == 'X' && grid[i + 1][j] == 'X' && grid[i + 2][j] == 'X'-> {
                    line = line.copy(first = line.first +1, second = 'X')
                }
                grid[i][j] == 'O' && grid[i + 1][j] == 'O' && grid[i + 2][j] == 'O'-> {
                    line = line.copy(first = line.first +1, second = 'O')
                }
            }
        }
        return line
    }

    /**
     * Check every line of the grid to see if there is a winner
     *
     * @return Pair<Int, Char> :
     * The first element is the number of horizontal winning lines( not suppose to be more than 1)
     * The second element is the winning character (X or O)
     */
    private fun checkHorizontal(): Pair<Int, Char> {
        var line = Pair(0, ' ')
        for (i in 0 until gridSize) {
            val j = 0
            when  {
                grid[i][j] == 'X' && grid[i][j + 1] == 'X' && grid[i][j + 2] == 'X'-> {
                    line = line.copy(first = line.first +1, second = 'X')
                }
                grid[i][j] == 'O' && grid[i][j + 1] == 'O' && grid[i][j + 2] == 'O'-> {
                    line = line.copy(first = line.first +1, second = 'O')
                }
            }
        }
        return line
    }

    /**
     * Check every diagonal ( always 2 diagonals)  of the grid to see if there is a winner
     *
     * @return Pair<Int, Char> :
     * The first element is the number of diagonal winning lines( not suppose to be more than 1)
     * The second element is the winning character (X or O)
     */
    private fun checkDiagonal(): Pair<Int, Char> {
        var line = Pair(0, ' ')
        val i = 0
        val listPossibility = listOf('X', 'O')
        listPossibility.forEach{ char ->
            when {
                grid[i][i] == char && grid[i + 1][i + 1] == char && grid[i + 2][i + 2] == char ->
                    line = line.copy(first = line.first +1, second = char)

                grid[i][i + 2] == char && grid[i + 1][i + 1] == char && grid[i + 2][i] == char ->
                    line = line.copy(first = line.first +1, second = char)

            }
        }
        return line
    }
}

fun main() {
    val game = TicTacToe()
    game.play()
}