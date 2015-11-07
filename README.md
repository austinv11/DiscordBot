# DiscordBot
A feature-packed and moddable bot for Discord

## Contributing to the project

### Setting up the workspace

1. [Clone the repository](https://help.github.com/articles/cloning-a-repository/)
2. In the IDE of your choice, import the project as a gradle project.
3. (Disclaimer: I am writing this with IntelliJ IDEA in mind, please do your IDE's equivalent instead if you aren't using IntelliJ) Either mark the following directories as source roots: `Discord4J/src/main/java` and `nanohttpd/core/src/main/java` or mark the binaries in the `libs` directory as libraries/dependencies.

### Coding Conventions

When contributing, *ALWAYS* use tabs and [1tbs](https://en.wikipedia.org/wiki/Indent_style#Variant:_1TBS).

### Making sure the project works

If you add a new dependency, add it through the [dependency-list.txt](https://github.com/austinv11/DiscordBot/blob/master/dependency-list.txt) and **not** in the build.gradle, it will be handled automatically. And if the new dependency requires a new repo, add it to the build.gradle normally as well as adding it to the [repo-list.txt](https://github.com/austinv11/DiscordBot/blob/master/repo-list.txt). This is nescessary for how the installer works.

### Compiling the project

(**Note:** Skip step 1 if you aren't on a UNIX system as the [CompileUnofficialBuilds.sh](https://github.com/austinv11/DiscordBot/blob/master/CompileUnofficialBuilds.sh) script is written in bash–not batch, if you wish to port it to batch please submit a pull request!)

1. Open a command prompt/terminal and cd to the project directory and then enter: `sh ./CompileUnofficialBuilds.sh`. This will ensure that the currently compiled versions of NanoHTTPD and Discord4J are up to date.
2. Enter `sh gradlew build`
3. The compiled .jar should be located in `./build/libs/DiscordBot-VERSION.jar`

### Questions?

[Open a new issue](https://github.com/austinv11/DiscordBot/issues/new)
