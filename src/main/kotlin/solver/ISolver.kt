package solver

interface ISolver {
    fun solve(cookie: String?): Pair<String, String?>
}