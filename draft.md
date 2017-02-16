# Design draft

This document describes base ideas drivig culverin.

# Design principles 
Culverin is a scala-based monitoring/alerting tool that aims to inform software developing teams when something goes wrong. 
It consists of several parts/components that are chained in a graph like architecture.
At the highest level, it has a DSL that helps the developers to define their graph and graph components as simple as possible.

# components

- DSL : Scala DSL that helps the users to define the system
- System inputs : Bring logs from varous input sources to the system
- Processors : Extract metrics and other meaningfull data from raw logs
- Counters : Aggregate and group metrics together
- Watchers : Monitor counters based on user-defined rules to detect various events based on metrics
- Actions : Pipe events (detected by watchers) to the outside world.

## DSL
(Under construction)

## Counters DSL

Sample definitions :

```SCALA

// A counter that counts number of 'error's seen in logs

 OccurrenceCounter("error")
 
// A counter that counts total items over past two minutes

SumAggregator("item") with TimeSpan(2 minutes)

// A counter that countes occurances for each status code (200, 500, etc...)

GroupCounter("status code")

// A counter that aggregates average of ages

AverageAggregator("age")

```

## Processors
A processor is a unit that is responsible for extracting metrics from input (un-structured) data.
Each processor has a specific rule that extracts it's target data structure from raw data.



# Flow
This section describes data flow from input through various components.

## Basic Flow
Basically, the process begins with a log chunk (or un-structured data). This raw data is processed in processor (matched against a set of rules) which results in a structured data object (e.g. an instance of a case class). Then it is routed through a router which decides which counters should be informed. Each counter is defined using a counter type (like the ones described above) and one (or more) function that converts a specific data structure (the result of a processor) into a basic data type that can be processed in the counter.
