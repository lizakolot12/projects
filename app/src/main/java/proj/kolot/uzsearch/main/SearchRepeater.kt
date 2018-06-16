package proj.kolot.uzsearch.main

/**
 * Created by Kolot Liza on 10/16/17.
 */
interface SearchRepeater {
    fun runRepeatingTask(id:Int, on:Boolean, repeatingInterval:Long)
}