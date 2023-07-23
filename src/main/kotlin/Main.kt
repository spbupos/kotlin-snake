import java.awt.*
import java.awt.event.*
import javax.swing.JFrame
import javax.swing.JPanel
import kotlin.time.DurationUnit
import kotlin.time.ExperimentalTime
import kotlin.time.measureTime

class GamePanel : JPanel() {
    init {
        preferredSize = Dimension(WINDOW_WIDTH, WINDOW_HEIGHT)
    }
    private var timeout: Long = 300L
    val keyListener = object : KeyAdapter() {
        override fun keyPressed(e: KeyEvent) {
            when {
                e.keyCode == KeyEvent.VK_UP && game.snake.direction != 2 -> game.snake.direction = 0
                e.keyCode == KeyEvent.VK_RIGHT && game.snake.direction != 3 -> game.snake.direction = 1
                e.keyCode == KeyEvent.VK_DOWN && game.snake.direction != 0 -> game.snake.direction = 2
                e.keyCode == KeyEvent.VK_LEFT && game.snake.direction != 1 -> game.snake.direction = 3
                e.keyCode == KeyEvent.VK_PLUS && timeout > 100L -> timeout -= 50L
                e.keyCode == KeyEvent.VK_MINUS && timeout < 600L -> timeout += 50L
                e.keyCode == KeyEvent.VK_ESCAPE -> game.snake.gameOver = true
            }
        }
    }
    val game = Game()
    override fun paintComponent(g: Graphics) {
        super.paintComponent(g)
        game.render(g)
    }

    private fun update(timePassed: Double) {
        // put updates here
        game.update(timePassed)
    }

    private var lastFrameLength = 0.0

    @OptIn(ExperimentalTime::class)
    fun loop() {
        val totalDuration = measureTime {
            update(lastFrameLength)
            repaint()
            Thread.sleep(timeout)
        }
        lastFrameLength = totalDuration.toDouble(DurationUnit.SECONDS)
    }
}

class GameWindow(val gamePanel: GamePanel) : JFrame() {
    init {
        add(gamePanel)
        addKeyListener(gamePanel.keyListener)
        isResizable = false
        pack()
        title = "Game"
        setLocationRelativeTo(null)
        defaultCloseOperation = EXIT_ON_CLOSE
    }
}

fun main() {
    val gameWindow = GameWindow(GamePanel())
    gameWindow.isVisible = true
    while (!gameWindow.gamePanel.game.snake.gameOver) {
        gameWindow.gamePanel.loop()
    }
    println("Game over, you result - ${gameWindow.gamePanel.game.snake.length}!")
}