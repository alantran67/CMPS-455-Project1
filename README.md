# CMPS 455 Project 1 — Synchronization

## Overview

This repository contains our CMPS 455 Project 1 implementation for Fall 2026.

The project focuses on thread synchronization and includes four main components:

1. Dining Philosophers using semaphores
2. Readers-Writers using semaphores
3. Command-line argument handling
4. Project report

The final program should allow the user to choose which synchronization task to run using command-line arguments.

## Team Members and Responsibilities

| Team Member     | Responsibility |
|-----------------|---|
| Alan Tran       | Dining Philosophers |
| Hans Trosclair  | Readers-Writers |
| Olivia Deshotel | Report |
| Brenden Daigle  | Command Line |

## Project Tasks

### Task 1 — Dining Philosophers
**Assigned to: Alan Tran**

The Dining Philosophers implementation will:

- Prompt for the number of philosophers `P`
- Prompt for the total number of meals `M`
- Create one thread for each philosopher
- Represent chopsticks using semaphores
- Coordinate philosopher access to shared chopsticks
- Ensure all philosophers enter before sitting
- Ensure all philosophers wait to leave until everyone is ready
- Allow philosophers to eat and think for random periods of 3–6 cycles
- Stop additional meals once the shared meal total has been reached
- Produce the required output for philosopher actions
- Use semaphores for synchronization

### Task 2 — Readers-Writers
**Assigned to: Hans Trosclair**

The Readers-Writers implementation will:

- Prompt for:
  - `R` = number of reading-agents
  - `W` = number of coordinating-agents
  - `N` = maximum number of reading-agents allowed at once
- Create threads for readers and writers/coordinators
- Use semaphores to control access to the shared resource
- Allow multiple readers to access the resource concurrently
- Allow only one coordinating-agent at a time
- Prevent coordinating-agents from accessing the resource while readers are active
- Preserve the required access pattern:

```text
N readers -> 1 coordinator -> N readers -> 1 coordinator -> ...
```

### Task 3 — Command Line
**Assigned to: Brenden Daigle**

The command-line portion will manually parse the program arguments and determine which synchronization task should run.

Required options:

```text
-A 1    Run Dining Philosophers
-A 2    Run Readers-Writers
```

Invalid or missing arguments should produce a clear error message instead of crashing the program.

Example:

```bash
java Main -A 1
```

or, depending on the final project structure:

```bash
java Main -A 2
```

### Task 4 — Report
**Assigned to: Olivia Deshotel**

The report will document:

- How each task was implemented
- Bugs encountered and how they were fixed
- Data structures and algorithms used
- Required Dining Philosophers runtime experiments
- Yield experiment results
- Maximum-parallelism experiment results
- Readers-Writers discussion questions
- All group members' full names and ULIDs

## Synchronization Restrictions

For Tasks 1 and 2, synchronization should use semaphores and permitted thread methods.

The assignment specifically disallows synchronization mechanisms such as:

- `synchronized`
- `ReentrantLock`
- `ReadWriteLock`
- `CountDownLatch`
- `CyclicBarrier`
- `volatile`
- `AtomicInteger`
- `AtomicLong`

Busy waiting should only be used where explicitly permitted by the assignment.

## Suggested Repository Structure

```text
CMPS455-Project1/
├── README.md
├── STAGE.md
├── src/
│   ├── Main.java
│   ├── DiningPhilosophers.java
│   ├── ReadersWriters.java
│   └── ...
└── report/
    └── project01_report.pdf
```

The final structure may change as the project is integrated.

## Git Workflow

Each team member should work on a separate branch instead of directly editing `main`.

Suggested branches:

```text
alan-dining-philosophers
hans-readers-writers
olivia-report
brenden-command-line
```

Typical workflow:

```bash
git checkout main
git pull

git checkout -b your-branch-name

# Make changes

git add .
git commit -m "Describe your changes"
git push -u origin your-branch-name
```

Then open a Pull Request on GitHub and merge the completed work into `main` after review.

## Testing Goals

Before submission, the team should verify that:

- Invalid input does not crash the program
- Invalid command-line arguments are handled correctly
- Both synchronization tasks terminate correctly
- The Dining Philosophers solution does not deadlock
- Readers and writers follow the required access pattern
- The shared meal total is not exceeded
- Required output is displayed
- No prohibited synchronization methods are used
- All branches are merged into `main`
- The final report matches the completed implementation

## Course

**CMPS 455 — Project 1: Synchronization**  
Fall 2026  
University of Louisiana at Lafayette
