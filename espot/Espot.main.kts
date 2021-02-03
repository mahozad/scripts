@file:CompilerOptions("-jvm-target", "11")

import java.io.FileOutputStream
import java.io.IOException
import java.io.PrintStream
import java.nio.file.FileVisitResult
import java.nio.file.FileVisitor
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.attribute.BasicFileAttributes

data class Info(val fileCount: Long, var visited: Int = 0)

val folderSymbol = "🗁"
val root = Path.of("D:/Music/")
val result = Path.of("result.txt")
val pathInfo = mutableMapOf<Path, Info>()
val Path.info get() = pathInfo[this] ?: Info(0)
val Path.visited get() = (pathInfo[this] ?: Info(0)).visited
val Path.fileCount get() = (pathInfo[this] ?: Info(0)).fileCount

System.setOut(PrintStream(FileOutputStream(result.toFile())))

Files.walkFileTree(root, object : FileVisitor<Path> {

    override fun preVisitDirectory(dir: Path, attrs: BasicFileAttributes): FileVisitResult {
        val childCount = Files.list(dir).count()
        pathInfo.putIfAbsent(dir, Info(childCount))
        printLineageOf(dir)
        val prefix = if (dir == root) "✱" else "${terminalFor(dir)} $folderSymbol"
        println("$prefix ${dir.fileName}")
        return FileVisitResult.CONTINUE
    }

    override fun visitFile(file: Path, attrs: BasicFileAttributes): FileVisitResult {
        printLineageOf(file)
        println("${terminalFor(file)} ${file.fileName}")
        return FileVisitResult.CONTINUE
    }

    override fun visitFileFailed(f: Path, exc: IOException) = FileVisitResult.CONTINUE

    override fun postVisitDirectory(dir: Path, exc: IOException?): FileVisitResult {
        if (dir.fileCount < 1) print("   └── .: Empty :.")
        pathInfo.remove(dir)
        return FileVisitResult.CONTINUE
    }
})

fun printLineageOf(file: Path) {
    val depth = (file - root).size - 1
    for (i in 1..depth) print(lineageOf(file.subPathBefore(i)))
    (file.parent ?: Path.of("")).info.visited++
}

fun Path.subPathBefore(endIndex: Int) = root.resolve(subpath(0, endIndex))

fun terminalFor(dir: Path) = if (dir.parent.visited < dir.parent.fileCount) "├──" else "└──"

fun lineageOf(file: Path) = if (file.visited < file.fileCount) "│  " else "   "
