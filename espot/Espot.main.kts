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
        info[dir.parent]?.set(1, info[dir.parent]!![1] + 1)

        val depth = (dir - root).size
        for (i in 1..depth) {
            val parent = dir.root.resolve(dir.subpath(0, dir.count() - depth))
            if (info[parent]!![0] > info[parent]!![1]) {
                print("│  ")
            } else {
                print("   ")
            }
        }
        println("├── ${dir.fileName}")

        return FileVisitResult.CONTINUE
    }

    override fun visitFile(file: Path, attrs: BasicFileAttributes): FileVisitResult {
        val depth = file.nameCount
        for (i in 1 until depth) {
            val parent = file.root.resolve(file.subpath(0, file.count() - i))
            if (info[parent]!![0] > info[parent]!![1]) {
                print("│  ")
            } else {
                print("   ")
            }
        }
        if (info[file.parent]!![0] > info[file.parent]!![1]) {
            println("├── ${file.fileName}")
        } else {
            println("└── ${file.fileName}")
        }
        info[file.parent]?.set(1, info[file.parent]!![1] + 1)
        return FileVisitResult.CONTINUE
    }

    override fun visitFileFailed(file: Path, exc: IOException?): FileVisitResult {
        println("$file failed. exception: $exc")
        return FileVisitResult.CONTINUE
    }

    override fun postVisitDirectory(dir: Path, exc: IOException?): FileVisitResult {
        if (info[dir]!!.first() < 1) {
            print("   └── .: Empty :.")
        }
        info.remove(dir)
        return FileVisitResult.CONTINUE
    }
})
