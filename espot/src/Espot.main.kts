@file:CompilerOptions("-jvm-target", "11")

import java.io.FileOutputStream
import java.io.IOException
import java.io.PrintStream
import java.nio.file.FileVisitResult
import java.nio.file.FileVisitor
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.attribute.BasicFileAttributes

data class Info(val fileCount: Int, var visited: Int = 0)

val folderSymbol = "🗁"
val root = Path.of("D:/Music/")
val result = Path.of("result.txt")
val pathInfo = mutableMapOf<Path, Info>()
val Path.visited get() = pathInfo[this]?.visited ?: 0
val Path.fileCount get() = pathInfo[this]?.fileCount ?: 0

System.setOut(PrintStream(FileOutputStream(result.toFile())))

Files.walkFileTree(root, object : FileVisitor<Path> {
    override fun preVisitDirectory(p: Path, attrs: BasicFileAttributes): FileVisitResult {
        pathInfo[p] = Info(Files.list(p).count().toInt())
        processLineageOf(p)
        println("${prefixFor(p)} ${p.fileName}")
        return FileVisitResult.CONTINUE
    }

    override fun visitFile(p: Path, attrs: BasicFileAttributes): FileVisitResult {
        processLineageOf(p)
        println("${terminalFor(p)} ${p.fileName}")
        return FileVisitResult.CONTINUE
    }

    override fun visitFileFailed(p: Path, e: IOException) = FileVisitResult.CONTINUE

    override fun postVisitDirectory(p: Path, e: IOException?): FileVisitResult {
        if (p.fileCount == 0) print("   └── .: Empty :.")
        pathInfo.remove(p)
        return FileVisitResult.CONTINUE
    }
})

fun processLineageOf(p: Path) {
    val depth = (p - root).size - 2
    for (i in 0..depth)
        print(lineageOf(p.subPathBefore(root.count() + i)))
    pathInfo[p.parent]?.let { it.visited++ }
}

fun Path.subPathBefore(endIndex: Int) = root.resolve(subpath(0, endIndex))

fun lineageOf(p: Path) = if (p.visited < p.fileCount) "│  " else "   "

fun prefixFor(p: Path) = if (p == root) "✱" else "${terminalFor(p)} $folderSymbol"

fun terminalFor(p: Path) = if (p.parent.visited < p.parent.fileCount) "├──" else "└──"
