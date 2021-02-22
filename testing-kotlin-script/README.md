# Example of a Kotlin Script and How to Write Tests for It

The script file is [script.main.kts](src/script.main.kts) located in *src* directory.  
The test file is [ScriptTest.kt](test/ScriptTest.kt) located in *test* directory.  
See the Gradle [build](build.gradle) file for dependencies and libraries used.

Run the tests using Gradle *test* task:

```shell
gradlew test
```

---

### Manually Adding Test Sources and Dependencies in IntelliJ IDEA

If you do not want to use Gradle, then follow these steps to create test sources:  
Created the *test* directory and then in IntelliJ marked it as *Test Sources Root*.  
Then in *Project Structure* 🡲 *Modules* 🡲 *Dependencies* 🡲 *+* 🡲 *Library...* 🡲
*New Library...* 🡲 *From Maven...* 🡲 Search `org.junit.jupiter:junit-jupiter-api:5.7.0`
🡲 *OK* 🡲 *Add Selected* 🡲 And set the scope to *Test*.
