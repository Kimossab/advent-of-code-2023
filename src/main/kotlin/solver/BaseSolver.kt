package solver

import Day
import java.io.File
import java.io.FileNotFoundException
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.reflect.full.findAnnotation

open class BaseSolver() {
    val year: Int
    val day: Int
    val inputLocation: String

    companion object {
        const val FILE_LOCATION = "./inputs"
        const val URL = "https://adventofcode.com"
    }

    init {
        val annotation = this::class.findAnnotation<Day>() ?: throw Error("Annotation `Day` needed")
        day = annotation.day
        year = annotation.year

        inputLocation = "$FILE_LOCATION/$year"

        Files.createDirectories(Paths.get(inputLocation))
    }

    private fun inputUrl(): URI = URI.create("$URL/$year/day/$day/input")

    private fun readFile(): String? {
        return try {
            File("$inputLocation/$day.txt").readText()
        } catch (e: FileNotFoundException) {
            println("File not found, making a request to the server.")
            null
        }
    }

    private fun writeFile(input: String) {
        try {
            File("$inputLocation/$day.txt").writeText(input)
        } catch (e: FileNotFoundException) {
            println("Couldn't write to file.")
        }
    }

    public fun getInput(cookie: String?): String = readFile() ?: run {
        val client = HttpClient.newHttpClient()
        val request = HttpRequest.newBuilder()
            .header("Cookie", "session=$cookie")
            .uri(inputUrl())
            .build()
        val response = client.send(request, HttpResponse.BodyHandlers.ofString())
        val data = response.body()
        writeFile(data)
        data
    } ?: throw Exception("NO INPUT FOUND")

    public fun getLines(cookie: String?) = getInput(cookie).split("\n")
}