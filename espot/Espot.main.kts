@file:CompilerOptions("-jvm-target", "11")

import java.io.IOException
import java.nio.file.FileVisitResult
import java.nio.file.FileVisitor
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.attribute.BasicFileAttributes

val root = Path.of("D:/Music/")

Files.walkFileTree(root, object : FileVisitor<Path> {

    val info = mutableMapOf<Path, MutableList<Long>>() // processed, total

    override fun preVisitDirectory(dir: Path, attrs: BasicFileAttributes): FileVisitResult {
        val childCount = Files.list(dir).count()
        info.putIfAbsent(dir, mutableListOf(childCount, 0))
        printBranches(dir)
        if (dir == root || info[dir.parent]!![0] <= info[dir.parent]!![1]) {
            println("└── \uD83D\uDDC1 ${dir.fileName}")
        } else {
            println("├── \uD83D\uDDC1 ${dir.fileName}")
        }
        return FileVisitResult.CONTINUE
    }

    override fun visitFile(file: Path, attrs: BasicFileAttributes): FileVisitResult {
        printBranches(file)
        if (info[file.parent]!![0] > info[file.parent]!![1]) {
            println("├── ${file.fileName}")
        } else {
            println("└── ${file.fileName}")
        }
        return FileVisitResult.CONTINUE
    }

    private fun printBranches(file: Path) {
        if (file != root) print("   ")
        val depth = (file - root).size - 1
        for (i in 1..depth) {
            val parent = file.root.resolve(file.subpath(0, i))
            if (info[parent]!![1] < info[parent]!![0]) print("│  ") else print("   ")
        }
        info[file.parent]?.set(1, info[file.parent]!![1] + 1)
    }

    override fun visitFileFailed(file: Path, exc: IOException?): FileVisitResult {
        println("Failed to visit $file")
        return FileVisitResult.CONTINUE
    }

    override fun postVisitDirectory(dir: Path, exc: IOException?): FileVisitResult {
        if (info[dir]!!.first() < 1) print("   └── .: Empty :.")
        info.remove(dir)
        return FileVisitResult.CONTINUE
    }
})
