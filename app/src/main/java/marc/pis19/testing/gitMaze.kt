package marc.pis19.testing

import android.os.Build
import android.support.annotation.RequiresApi
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.abs
import kotlin.math.exp

class Maze(val width: Int, val height: Int) {

    private enum class Cell {
        WALL, SPACE
    }

    private val data = Array(width) { i ->
        Array(height) { i -> Cell.WALL}
    }
    private val rand = java.util.Random()
    private val destination = Pair(this.width-3, this.height-2)

    init {
        generate()
    }

    private fun carve(x: Int, y: Int) {

        val upx = intArrayOf(1, -1, 0, 0)
        val upy = intArrayOf(0, 0, 1, -1)

        var dir = rand.nextInt(4)
        var count = 0
        while(count < 30) {
            val x1 = x + upx[dir]
            val y1 = y + upy[dir]
            val x2 = x1 + upx[dir]
            val y2 = y1 + upy[dir]
            if(data[x1][y1] == Cell.WALL && data[x2][y2] == Cell.WALL) {
                data[x1][y1] = Cell.SPACE
                data[x2][y2] = Cell.SPACE
                carve(x2, y2)
            } else {
                dir = (dir + 1) % 4
                count += 1
            }
        }

    }

    fun generate() {

        for(x in 0 until width) {
            for(y in 0 until height) {
                data[x][y] = Cell.WALL
            }
        }

        for(x in 0 until width) {
            data[x][0] = Cell.SPACE
            data[x][height - 1] = Cell.SPACE
        }
        for(y in 0 until height) {
            data[0][y] = Cell.SPACE
            data[width - 1][y] = Cell.SPACE
        }

        data[2][2] = Cell.SPACE
        carve(2, 2)

        //data[2][1] = Cell.SPACE
        //data[width - 3][height - 2] = Cell.SPACE
    }

    fun manhattanDistance(pos: Pair<Int, Int>): Int{
        return abs(pos.first - this.destination.first) + abs(pos.second - this.destination.second)
    }

    fun heuristic(pos: Pair<Int, Int>): Int{
        return this.manhattanDistance(pos)
    }

    fun successors(pos: Pair<Int, Int>): ArrayList<Pair<Int, Int>>{
        var successors: ArrayList<Pair<Int, Int>> = ArrayList()
        if(this.data[pos.first+1][pos.second] == Cell.SPACE){
            successors.add(Pair(pos.first+1,pos.second))
        }
        if(this.data[pos.first-1][pos.second] == Cell.SPACE){
            successors.add(Pair(pos.first-1,pos.second))
        }
        if(this.data[pos.first][pos.second+1] == Cell.SPACE){
            successors.add(Pair(pos.first,pos.second+1))
        }
        if(this.data[pos.first][pos.second-1] == Cell.SPACE){
            successors.add(Pair(pos.first,pos.second-1))
        }
        return successors
    }

    fun comp(o1: Triple<Pair<Int, Int>,ArrayList<Pair<Int, Int>>,Int>, o2: Triple<Pair<Int, Int>,ArrayList<Pair<Int, Int>>,Int>): Int{
        return o1.third - o2.third
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun solve(): ArrayList<Pair<Int, Int>> {

        var statePriorityQueue: PriorityQueue<Triple<Pair<Int, Int>,ArrayList<Pair<Int, Int>>,Int>> = PriorityQueue()
        var explored = ArrayList<Pair<Int, Int>>()

        val origin: Pair<Int, Int> = Pair(2, 2)
        val destination: Pair<Int, Int> = Pair(this.width-3, this.height-2)
        val path: ArrayList<Pair<Int, Int>> = ArrayList()

        var h: Int = this.heuristic(origin)
        var g: Int = 0
        path.add(origin)
        statePriorityQueue.add(Triple(origin, path, h))

        while(statePriorityQueue.isNotEmpty()){
            val current = statePriorityQueue.poll()

            if(current.first == destination){
                return current.second
            }

            if(current.first !in explored ){
                for(successor in this.successors(current.first)){
                    h = heuristic(successor)
                    g = current.second.size+1
                    var list: ArrayList<Pair<Int, Int>> = ArrayList()
                    list = current.second
                    list.add(successor)
                    statePriorityQueue.add(Triple(successor, list, g+h))
                }
            }
            explored.add(current.first)
        }
        return explored
    }

    fun print(solution: ArrayList<Pair<Int, Int>>) {
        for(y in 0 until height) {
            for(x in 0 until width) {
                when {
                    Pair(x, y) in solution -> print(" . ")
                    data[x][y] == Cell.WALL -> print("|||")
                    else -> print("   ")
                }
            }
            println()
        }
    }

}

fun main() {
    val m = Maze(39, 23)
    m.generate()
    m.print(m.solve())
}