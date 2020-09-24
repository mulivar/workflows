# Workflows

## General info
An example project that demonstrates how to build a workflow app. The application starts by offering a standard CLI interface that allows you to load an external JSON file that represents a worfklow, and execute it.
	
## Setup
To build the entire project you will need maven, so building is easy with the standard set of commands:

```
$ cd workflows
$ mvn package
```
Once the binary has been built, in form of a standalone JAR file, you can run it in a console via the following command:

```
$ java -jar target/workflows-jar-with-dependencies.jar
```

## Commands
There are currently a total of five commands available to use, which are displayed once you start the application:

```
create workflow $path/to/workflow.json
create session $sessionId
resume session $sessionId
set timeout $timeout_in_seconds
get session $sessionId
```


### create workflow
Creates a workflow by loading the JSON file. In case the file contains invalid JSON an error will be reported on the the console. Successful command execution renders a unique workflow ID that is necessary to create a session. Please note that creating a workflow does not start the execution immediately, it merely registers the loaded workflow by the system. You can load the same workflow only once, although you may load as many different workflows as desired.

### create session
Creates a session based on the previously loaded workflow. The session starts executing immediately, sequentially following the tasks outlined in the JSON file, and where every proper and NOP task last by default for 60 seconds. Invoking this command with the same workflow ID will create that many instances of the same workflow, and start executing them immediately.

### resume session
In case there are tasks flowing through multiple lanes in the executed worfklow (i.e. session), the execution will pause on the lanes boundary and wait for the user input - it is necessary to issue the ``resume session`` to continue.

### get session
Retrieves the current state of the session indicated via the command argument (i.e. session ID).

### set timeout
The default timeout of 60 seconds encountered when executing task and NOP nodes can be altered by issuing this command, which will change the timeout for all nodes whose execution still hasn't started.

## Usage example

```
create workflow src/main/resources/workflow_definition.json
Created workflow, ID = d3a4283888bdf4800b6c5070607790a5

create session d3a4283888bdf4800b6c5070607790a5
Created session, ID = fc14c878-8863-4b43-9639-332990563eff

get session fc14c878-8863-4b43-9639-332990563eff
Task1 in progress
Task1 completed

get session fc14c878-8863-4b43-9639-332990563eff
Task2 in progress
set timeout 5

Task2 completed
get session fc14c878-8863-4b43-9639-332990563eff
lane2

resume session fc14c878-8863-4b43-9639-332990563eff
Task3 completed
get session fc14c878-8863-4b43-9639-332990563eff
lane3
resume session fc14c878-8863-4b43-9639-332990563eff
NOP completed

Task4 completed
Session fc14c878-8863-4b43-9639-332990563eff completed
get session fc14c878-8863-4b43-9639-332990563eff
Ended
```
