The Gradle build file for scripts is mainly for the purpose of running their tests.

---

To run kotlin scripts with Gradle see [this GitHub gist](https://gist.github.com/bamboo/f29e738c2a17a36e87c814b7452afe31).  
Can scripts be run by adding Gradle *application* plugin?  
Create a Gradle task of type *Exec*.  
See [this approach](https://kotlinexpertise.com/execute-kotlin-scripts-with-gradle/).  
Also, [this SO post](https://stackoverflow.com/q/34974039) might be helpful.

Furthermore, refer to [Java scripting API](https://docs.oracle.com/en/java/javase/11/scripting/java-scripting-api.html)
and [this official Kotlin example](https://github.com/JetBrains/kotlin/blob/master/libraries/examples/kotlin-jsr223-local-example/src/test/kotlin/org/jetbrains/kotlin/script/jsr223/KotlinJsr223ScriptEngineIT.kt). 

---

To use the latest features of a new version of Kotlin, update IDE Kotlin plugin to that
latest veresion and also ensure that the Kotlin version is set to that lates version in
IDE Settings -> Build, ... -> Compiler -> Kotlin Compiler

---

To get the location of the script file, use the implicit `__FILE__` variable.
Or rename it to something else with `@file:ScriptFileLocaiton()`.
See [Script: get location of the script.main.kts file](https://github.com/JetBrains/kotlin/pull/4597).

```kotlin
// @file:ScriptFileLocation("scriptFile")
// println(scriptFile.absolutePath)
println(__FILE__.absolutePath)
```

---

Added the [Git submodule](https://git-scm.com/book/en/v2/Git-Tools-Submodules) with this command:
```shell
git submodule add https://github.com/mahozad/<repo-name> <optional-folder-name>
```
... and also added its folder to IntelliJ in `Settings` 🡲 `Version Control`. 

By choosing `Git` 🡲 `Update Project`, if the submodules are on a branch, they will also be updated automatically.

If a submodule is in a detached HEAD state (a detached HEAD means a specific commit is checked out instead of a branch),
IntelliJ IDEA will call git submodule update, which will check out the commit referenced in the root repository.
This means an update will only be performed if the submodule reference changes in the root repo, or if a new submodule is added.

---

Update the pointer to another commit of the submodule-x by running these commands:

```shell
git submodule update --remote --merge
git status
git add submodule-x
git commit -m "Update summodule-x pointer to latest commit"
```

See [this post](https://stackoverflow.com/a/8191413) for more info

---

We can access the script class instance like follows:
  1. this@*file name with first letter capitalized and words separated with underscore*  
     For example for a script named *compare-wiki.main.kts*: `this@Compare_wiki_main`
  2. Call the main function from another file (like a unit test)  
     For example to call method `setup()` in a script named *compare-wiki.main.kts*:
     `Compare_wiki_main(arrayOf("program args")).setup()`

---
