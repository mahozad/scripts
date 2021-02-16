# Github Action Written in Kotlin (Scripting)
The script file is [action.main.kts](src/action.main.kts).  
See [the example workflow](.github/workflows/main.yml) for how to use the action.

###

Note that kotlin annotations didn't work in testing. Some additional kotlin dependency for tests are required.

### Manually Adding Test Sources in IntelliJ

Created the *test* directory and then in IntelliJ marked it as *Test Sources Root*.  
Then in *Project Structure* 🡲 *Modules* 🡲 *Dependencies* 🡲 *+* 🡲 *Library...* 🡲
*New Library...* 🡲 *From Maven...* 🡲 Search `org.junit.jupiter:junit-jupiter-api:5.7.0`
🡲 *OK* 🡲 Select *Add Selected* 🡲 And set the scope to *Test*.

---

