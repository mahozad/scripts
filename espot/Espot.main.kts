@file:CompilerOptions("-jvm-target", "11")

import java.io.IOException
import java.nio.file.FileVisitResult
import java.nio.file.FileVisitor
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.attribute.BasicFileAttributes

data class Info(val fileCount: Long, var visited: Int = 0)

val root = Path.of("D:/Music/")
val pathInfo = mutableMapOf<Path, Info>()
val folderSymbol = "🗁"
val Path.info get() = pathInfo[this] ?: Info(0)

Files.walkFileTree(root, object : FileVisitor<Path> {

    override fun preVisitDirectory(dir: Path, attrs: BasicFileAttributes): FileVisitResult {
        val childCount = Files.list(dir).count()
        pathInfo.putIfAbsent(dir, Info(childCount))
        printLineageOf(dir)
        val prefix = if (dir == root) "✱" else "${terminalFor(dir)} $folderSymbol"
        println("$prefix ${dir.fileName}")
        return FileVisitResult.CONTINUE
    }

    private fun terminalFor(dir: Path): String {
        val (fileCount, visited) = dir.parent.info
        return if (visited < fileCount) "├──" else "└──"
    }

    override fun visitFile(file: Path, attrs: BasicFileAttributes): FileVisitResult {
        printLineageOf(file)
        println("${terminalFor(file)} ${file.fileName}")
        return FileVisitResult.CONTINUE
    }

    private fun printLineageOf(file: Path) {
        val depth = (file - root).size - 1
        for (i in 1..depth) {
            val parent = file.root.resolve(file.subpath(0, i))
            val (fileCount, visited) = parent.info
            if (visited < fileCount) print("│  ") else print("   ")
        }
        file.parent.info.visited++
    }

    override fun visitFileFailed(file: Path, exc: IOException?): FileVisitResult {
        println("Failed to visit $file")
        return FileVisitResult.CONTINUE
    }

    override fun postVisitDirectory(dir: Path, exc: IOException?): FileVisitResult {
        if (dir.info.fileCount < 1) print("   └── .: Empty :.")
        pathInfo.remove(dir)
        return FileVisitResult.CONTINUE
    }
})
