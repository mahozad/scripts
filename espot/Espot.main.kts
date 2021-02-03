@file:CompilerOptions("-jvm-target", "11")

import java.io.IOException
import java.nio.file.FileVisitResult
import java.nio.file.FileVisitor
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.attribute.BasicFileAttributes

println("D:/ ┐")
Files.walkFileTree(Path.of("D:/Music/"), object : FileVisitor<Path> {
    override fun preVisitDirectory(dir: Path, attrs: BasicFileAttributes): FileVisitResult {
        val depth = dir.nameCount
        for (i in 1..depth) {
            print("   ")
        }
        println("├── ${dir.fileName}")
        return FileVisitResult.CONTINUE
    }

    override fun visitFile(file: Path, attrs: BasicFileAttributes): FileVisitResult {
        val depth = file.nameCount
        for (i in 1 until depth) {
            print("   ")
        }
        print("│  ")
        println("└── ${file.fileName}")
        return FileVisitResult.CONTINUE
    }

    override fun visitFileFailed(file: Path, exc: IOException?): FileVisitResult {
        println("$file failed. exception: $exc")
        return FileVisitResult.CONTINUE
    }

    override fun postVisitDirectory(dir: Path, exc: IOException?): FileVisitResult {
        return FileVisitResult.CONTINUE
    }
})
