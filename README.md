# dangerous-test-detector

This plugin detects test methods of Unittest which name contains the letter 'c'

You can see the total number of such methods in the tool window (below)

Also, you can fix these methods: press Alt + Enter to delete all 'c' from method name

## Requirements

* JDK 11

## How to execute

* from command line: `./run_plugin.sh`
* or from IntelliJ IDEA: run `runIde` gradle task



<!-- Plugin description -->
This Fancy IntelliJ Platform Plugin is going to be your implementation of the brilliant ideas that you have.

This specific section is a source for the [plugin.xml](/src/main/resources/META-INF/plugin.xml) file which will be extracted by the [Gradle](/build.gradle.kts) during the build process.

To keep everything working, do not remove `<!-- ... -->` sections. 
<!-- Plugin description end -->
