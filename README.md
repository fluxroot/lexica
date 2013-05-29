Lexica
======

Copyright 2013 Software Composition Group, University of Bern.
All rights reserved.  
https://github.com/fluxroot/lexica


Introduction
------------
Lexica is a keyword analyzer for programming languages. It tries to find
keywords of unknown programming languages from source code by using
heuristics.


System Requirements
-------------------
Lexica is written for Java 7. So you will have to install the Java
Runtime Environment of at least that version. There are no other runtime
dependencies.


Installation
------------
Lexica is only available as source code. However it uses [Gradle] as a
build system. So building the jar file is really straightforward.

To build it from source, use the following steps.

- get it  
`git clone https://github.com/fluxroot/lexica.git`

- build it  
`./gradlew build`

- grab it  
`cp build/distributions/lexica-$version$.zip <installation directory>`

- install it  
Use your favorite unzip tool. There is no need to do any system-wide
installation. You can run it directly from the extracted directory.


Usage
-----
Lexica can be run directly from the command line. It will create a [H2]
database in the given directory. This database can be used to do some
further analysis on the data.

### Scan the source code
`java -jar lexica.jar scan [path] [-f <file pattern>]`

- `path`  
You can specify a path where the source code can be found. It will scan
this directory recursively. If you do not specify a path, it will scan
the current directory.

Example: `java -jar lexica.jar scan /location/to/my/source/code`  
Scans every file under the directory /location/to/my/source/code.

- `-f <file pattern>`  
You can specify a file pattern to be used for matching the filenames.
Normally you really want to do this, because software projects tend to
have other files than source code files lying around. The file pattern
will be passed to FileSystem.getPathMatcher(). Have a look at the
Javadoc if you want to know a little bit more about the syntax. If you
do not specify a file pattern, it will scan every file.

Example: `java -jar lexica.jar scan -f "*.java"`  
Scans all files ending in "*.java" under the current directory.

Example: `java -jar lexica.jar scan -f "*.{c,h}"`  
Scans all files ending in either "*.c" or "*.h" under the current directory.

### Use the database
- Download the [H2] package and extract it anywhere you like.
- Start up the H2 database. Normally this is done by starting the h2
script in the bin directory.
- Fire up a browser and go to http://localhost:8082.
- The Lexica database is called `.lexica.h2.db`. Modify the JDBC URL
field to point to your lexica database. E.g.
`jdbc:h2:/location/to/my/source/code/.lexica`. Omit the `.h2.db` ending.
You can leave everything else on their default values.

[Gradle]: http://gradle.org
[H2]: http://www.h2database.com
