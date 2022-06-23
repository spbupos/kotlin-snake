import java.awt.*
import kotlin.random.Random

val WINDOW_HEIGHT = 600
val WINDOW_WIDTH = 600

data class Point(var x: Int, var y: Int)

class Snake(
    public var direction: Int,
    private val widthOfBlock: Int
) {
    public var length: Int = 1
    private val H = WINDOW_HEIGHT / widthOfBlock
    private val W = WINDOW_WIDTH / widthOfBlock
    private val rnd = Random.Default
    private val startPosition: Point = Point(rnd.nextInt(W), rnd.nextInt(H))
    private var foodPosition: Point = Point(rnd.nextInt(W), rnd.nextInt(H))
    private var blockList = mutableListOf<Point>(startPosition)
    public var gameOver: Boolean = false

    private fun fillCircle(x: Int, y: Int, radius: Int, g: Graphics) {
        g.color = Color.GREEN
        g.fillOval(x - radius, y - radius, 2 * radius, 2 * radius)
    }
    fun render(g: Graphics) {
        blockList.forEachIndexed { index, point ->
            val color = if (index == 0) Color.RED else Color.YELLOW
            renderSingleBlock(g, point, color)
        }
        renderFood(g, foodPosition)
    }
    private fun renderFood(g: Graphics, foodPosition: Point) {
        fillCircle(
            foodPosition.x * widthOfBlock + widthOfBlock / 2,
            foodPosition.y * widthOfBlock + widthOfBlock / 2,
            widthOfBlock / 2, g
        )
    }
    fun update(timePassed: Double) {
        if (isNextBlockIsFood(blockList[0]))
            stepEat()
        else
            stepMove()
    }
    private fun stepMove() {
        val next = nextBlockPosition(blockList[0])
        if (next in blockList || next.x < 0 || next.x >= W || next.y < 0 || next.y >= H)
            gameOver = true
        else {
            blockList.removeAt(length - 1)
            blockList.add(0, next)
        }
    }
    private fun nextBlockPosition(head: Point): Point = when (direction) {
        0 -> Point(head.x, (head.y - 1))
        1 -> Point((head.x + 1), head.y)
        2 -> Point(head.x, (head.y + 1))
        3 -> Point((head.x - 1), head.y)
        else -> head
    }
    private fun stepEat() {
        length++
        blockList.add(0, foodPosition)
        do {
            foodPosition = Point(rnd.nextInt(W), rnd.nextInt(H))
        } while (foodPosition in blockList)
    }

    fun renderSingleBlock(g: Graphics, position: Point, color: Color) {
        val block = Polygon(
            intArrayOf(
                position.x * widthOfBlock + widthOfBlock / 10,
                position.x * widthOfBlock + widthOfBlock / 10,
                (position.x + 1) * widthOfBlock - widthOfBlock / 10,
                (position.x + 1) * widthOfBlock - widthOfBlock / 10,
            ),
            intArrayOf(
                position.y * widthOfBlock + widthOfBlock / 10,
                (position.y + 1) * widthOfBlock - widthOfBlock / 10,
                (position.y + 1) * widthOfBlock - widthOfBlock / 10,
                position.y * widthOfBlock + widthOfBlock / 10,
            ),
            4
        )
        g.color = color
        g.fillPolygon(block)
    }
    fun isNextBlockIsFood(head: Point): Boolean = (nextBlockPosition(head) == foodPosition)
}

class Game {
    val snake = Snake(0, 30)
    fun render(g: Graphics) {
        snake.render(g)
    }
    fun update(timePassed: Double) {
        snake.update(timePassed)
    }
}