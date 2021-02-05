Added the [Git submodule](https://git-scm.com/book/en/v2/Git-Tools-Submodules) with this command:
```
git submodule add https://github.com/mahozad/<repo-name> <optional-folder-name>
```
... and also added its folder to IntelliJ in `Settings` -> `Version Control`. 

By choosing Get -> Update, if the submodules are on a branch, they will also be updated automatically.

If a submodule is in a detached HEAD state (a detached HEAD means a specific commit is checked out instead of a branch),
IntelliJ IDEA will call git submodule update, which will check out the commit referenced in the root repository.
This means an update will only be performed if the submodule reference changes in the root repo, or if a new submodule is added.
