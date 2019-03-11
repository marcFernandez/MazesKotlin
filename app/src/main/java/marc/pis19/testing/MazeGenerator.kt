package marc.pis19.testing

import kotlin.random.Random

class MazeGenerator(w: Int, h: Int) {

    val WALL = 1
    val EMPTY = 0
    val width: Int = w
    val height: Int = h
    var out: Pair<Int, Int>
    lateinit var maze: HashMap<Int, HashMap<Int, Int>>

    init{
        this.maze = HashMap()
        this.out = Pair(this.width-2, this.height-2)
        for(i in 0 until this.width){
            this.maze[i] = HashMap()
            for(j in 0 until this.height){
                this.maze[i]!![j] = this.WALL
            }
        }
    }

    fun generateMaze(){
        this.maze[1]!![1] = this.EMPTY
        this.carveMaze(1,1)
    }

    private fun carveMaze(i: Int, j: Int){
        var dir = Random.nextInt(4)
        var ct = 0
        while (ct < 15){
            var di = 0
            var dj = 0
            if(dir == 0){ // right
                di++
            }
            else if(dir == 1){ // up
                dj++;
            }
            else if(dir == 2){ // left
                di -= 1
            }
            else{ // down
                dj -= 1
            }
            val i1 = i+di
            val j1 = j+dj
            val i2 = i1+di
            val j2 = j1+dj
            if(0<i2 && i2<this.width && 0<j2 && j2<this.height && (this.maze[i1]!![j1]!!+this.maze[i2]!![j2]!!) == 2){
                this.maze[i1]!![j1] = this.EMPTY
                this.maze[i2]!![j2] = this.EMPTY
            }
            ct++
            dir = (dir+1)%4
        }
    }

    fun printMaze(){
        println()
        print("----------------")
        print(this.height)
        println()
        print(this.width)
        println()
        print("----------------")
        println()
        for(j in 0 until this.height){
            for(i in 0 until this.width){
                if(this.maze[i]!![j] == this.EMPTY){
                    print("   ")
                }
                else{
                    print("|||")
                }
            }
            println()
        }
    }
}