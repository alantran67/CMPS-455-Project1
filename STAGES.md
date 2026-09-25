# Project Stage / Responsibility Tracker

## CMPS 455 Project 1 — Synchronization

This file tracks each team member's responsibilities, implementation progress, testing requirements, and final integration steps for the project.

---

## Team Assignments

| Member | Primary Responsibility | Branch | Status |
|---|---|---|---|
| Alan Tran | Dining Philosophers | `alan-dining-philosophers` | Not Started |
| Hans Trosclair | Readers-Writers | `hans-readers-writers` | Not Started |
| Olivia Deshotel | Final Report / Documentation | `olivia-report` | Not Started |
| Brendan Daigle | Command-Line Interface / Integration | `brendan-command-line` | Not Started |

### Status Values

Use only the following values in the table above:

- `Not Started`
- `In Progress`
- `Ready for Review`
- `Completed`

A task should only be marked **Completed** after it has been tested and merged into `main`.

---

# Stage 1 — Project Setup

## Shared Setup Tasks

- [ ] Repository created
- [ ] All team members added as collaborators
- [ ] `src/` folder created
- [ ] `src/Main.java` created
- [ ] `.gitignore` added
- [ ] README added
- [ ] STAGE file added
- [ ] Each member creates their assigned branch
- [ ] Everyone confirms they can clone/pull/push to the repository

### Recommended Project Structure

```text
project-root/
├── README.md
├── STAGE.md
├── .gitignore
├── report/
└── src/
    ├── Main.java
    ├── DiningPhilosophers.java
    └── ReadersWriters.java
```

The exact class names may change, but each synchronization problem should stay separated enough that the code is easy to test and integrate.

---

# Stage 2 — Alan Tran: Dining Philosophers

## Required Implementation

- [ ] Prompt the user for `P`, the number of philosophers
- [ ] Prompt the user for `M`, the total number of meals
- [ ] Validate that `P` is a valid positive integer
- [ ] Validate that `M` is a valid positive integer
- [ ] Create one philosopher thread for each philosopher
- [ ] Create semaphore-based chopsticks
- [ ] Implement left-chopstick acquisition
- [ ] Implement right-chopstick acquisition
- [ ] Release both chopsticks correctly after eating
- [ ] Prevent deadlock
- [ ] Make all philosophers enter before anyone sits down
- [ ] Make all philosophers wait for the required completion condition before leaving
- [ ] Implement eating for a random 3–6 cycles
- [ ] Implement thinking for a random 3–6 cycles
- [ ] Maintain a shared meal counter
- [ ] Prevent the total number of eaten meals from exceeding `M`
- [ ] Stop philosophers correctly when the meal limit is reached
- [ ] Print the required philosopher/chopstick/meal output

## Testing / Experiment Requirements

- [ ] Test with a small number of philosophers
- [ ] Test with a larger number of philosophers
- [ ] Test with `M < P`
- [ ] Test with `M > P`
- [ ] Confirm the program never exceeds the requested meal total
- [ ] Confirm all acquired chopsticks are eventually released
- [ ] Confirm the program terminates without deadlock
- [ ] Record runtimes needed for the report
- [ ] Run the yield-between-chopsticks experiment
- [ ] Record yield experiment results
- [ ] Run the maximum-parallelism experiment
- [ ] Record maximum-parallelism results
- [ ] Restore the normal implementation after experiments

## Before Merge

- [ ] Remove temporary debugging code
- [ ] Confirm no forbidden synchronization approach was used
- [ ] Pull the latest `main`
- [ ] Resolve merge conflicts
- [ ] Push final branch changes
- [ ] Open Pull Request
- [ ] Have at least one teammate review the Pull Request

---

# Stage 3 — Hans Trosclair: Readers-Writers

## Required Implementation

- [ ] Prompt the user for `R`
- [ ] Prompt the user for `W`
- [ ] Prompt the user for `N`
- [ ] Validate all three inputs
- [ ] Create reader threads
- [ ] Create writer/coordinator threads as required by the assignment
- [ ] Use semaphores for synchronization
- [ ] Allow no more than `N` readers at the same time
- [ ] Allow only one writer/coordinator at a time
- [ ] Prevent readers and writers/coordinators from using the shared resource simultaneously
- [ ] Enforce the required alternating-access behavior
- [ ] Correctly handle situations where no reader is currently available
- [ ] Correctly handle situations where no writer/coordinator is currently available
- [ ] Prevent deadlock
- [ ] Prevent invalid shared-state access

## Testing

- [ ] Test with one reader
- [ ] Test with one writer/coordinator
- [ ] Test with multiple readers
- [ ] Test with multiple writers/coordinators
- [ ] Test when `N = 1`
- [ ] Test when `N` is larger than the number of readers
- [ ] Test a very large `N`
- [ ] Test invalid values for `R`, `W`, and `N`
- [ ] Confirm the program terminates correctly
- [ ] Provide implementation/testing notes to Olivia for the report

## Before Merge

- [ ] Remove temporary debugging code
- [ ] Confirm semaphore behavior matches assignment requirements
- [ ] Pull the latest `main`
- [ ] Resolve merge conflicts
- [ ] Push final branch changes
- [ ] Open Pull Request
- [ ] Have at least one teammate review the Pull Request

---

# Stage 4 — Brendan Daigle: Command-Line Interface

## Required Command-Line Behavior

The user should choose which synchronization problem to run from the command line.

Expected commands:

```bash
java Main -A 1
java Main -A 2
```

- `-A 1` runs Dining Philosophers
- `-A 2` runs Readers-Writers

## Required Work

- [ ] Read command-line arguments from `args`
- [ ] Recognize `-A 1`
- [ ] Recognize `-A 2`
- [ ] Connect `-A 1` to Alan's Dining Philosophers implementation
- [ ] Connect `-A 2` to Hans's Readers-Writers implementation
- [ ] Reject an unsupported task number
- [ ] Reject a missing task number
- [ ] Reject a missing `-A`
- [ ] Reject extra/unexpected arguments
- [ ] Reject non-numeric task values
- [ ] Print clear usage information after an invalid command

## Recommended Error Messages

The professor may not require these exact sentences, but the program should make the problem obvious to the user.

### Missing Arguments

```text
Error: Missing command-line arguments.
Usage: java Main -A <1|2>
```

### Missing Task Number

```text
Error: Missing task number after -A.
Usage: java Main -A <1|2>
```

### Invalid Task Number

```text
Error: Invalid task number. Valid options are 1 or 2.
Usage: java Main -A <1|2>
```

### Invalid Flag

```text
Error: Expected -A as the first argument.
Usage: java Main -A <1|2>
```

### Too Many Arguments

```text
Error: Unexpected command-line arguments.
Usage: java Main -A <1|2>
```

### Non-Numeric Task

```text
Error: Task number must be 1 or 2.
Usage: java Main -A <1|2>
```

The most important requirement is that invalid input does **not** crash the program and clearly tells the user how to run it correctly.

## Command-Line Testing

- [ ] Test `java Main -A 1`
- [ ] Test `java Main -A 2`
- [ ] Test `java Main`
- [ ] Test `java Main -A`
- [ ] Test `java Main -A 3`
- [ ] Test `java Main -B 1`
- [ ] Test `java Main -A hello`
- [ ] Test extra arguments
- [ ] Confirm valid commands enter the correct task
- [ ] Confirm invalid commands terminate cleanly

## Before Merge

- [ ] Pull the latest `main`
- [ ] Integrate both synchronization tasks
- [ ] Resolve merge conflicts
- [ ] Push final branch changes
- [ ] Open Pull Request
- [ ] Have at least one teammate review the Pull Request

---

# Stage 5 — Olivia Deshotel: Final Report

## Required Report Content

- [ ] Add every group member's full name
- [ ] Add every required ULID
- [ ] Explain the Dining Philosophers implementation
- [ ] Explain how deadlock was prevented
- [ ] Document Dining Philosophers bugs and fixes
- [ ] Add the required Dining Philosophers runtime table
- [ ] Add yield-between-chopsticks experiment results
- [ ] Explain the effect of yielding
- [ ] Add maximum-parallelism results/table
- [ ] Discuss maximum-parallelism results
- [ ] Explain the Readers-Writers implementation
- [ ] Explain how Task 2 differs from earlier synchronization problems
- [ ] Discuss what happens when `N` becomes very large
- [ ] Explain command-line argument handling
- [ ] Describe important data structures used
- [ ] Describe important synchronization algorithms used
- [ ] Document known bugs or incomplete features, if any
- [ ] Make sure reported results match the final code
- [ ] Proofread the final report
- [ ] Export the report to an accepted submission format

## Information Needed From Teammates

### Alan Provides

- Implementation explanation
- Deadlock-prevention explanation
- Bugs/fixes
- Runtime measurements
- Yield experiment results
- Maximum-parallelism results

### Hans Provides

- Readers-Writers explanation
- Semaphore strategy
- Edge-case results
- Large-`N` observations

### Brendan Provides

- Command-line implementation explanation
- Error-handling behavior
- Integration notes

## Before Merge

- [ ] Add the final report to the repository if required
- [ ] Confirm names and IDs are correct
- [ ] Confirm tables/results match final program output
- [ ] Open Pull Request if the report is stored on Olivia's branch

---

# Stage 6 — Integration

Do not wait until the final day to combine everyone's work.

## Merge Order

Recommended order:

1. Alan — Dining Philosophers
2. Hans — Readers-Writers
3. Brendan — Command-Line Integration
4. Olivia — Final Report

Brendan should integrate after Alan and Hans have stable implementations because the command-line code depends on both tasks.

## Integration Checklist

- [ ] Alan's branch merged
- [ ] Hans's branch merged
- [ ] Brendan's branch merged
- [ ] Olivia's report merged/added
- [ ] `Main.java` correctly selects Task 1 or Task 2
- [ ] Project compiles from a clean checkout
- [ ] No unresolved merge-conflict markers remain
- [ ] Task 1 runs successfully
- [ ] Task 2 runs successfully
- [ ] Invalid command-line input is handled correctly
- [ ] Invalid user input is handled correctly
- [ ] All threads terminate correctly
- [ ] No obvious deadlocks occur during testing
- [ ] No prohibited synchronization methods remain
- [ ] Output is readable enough to verify synchronization behavior
- [ ] README matches the final project structure
- [ ] STAGE file reflects final completion status

---

# Stage 7 — Final Submission Review

Before submission, every team member should pull the latest `main` branch and run the final integrated program.

```bash
git checkout main
git pull
```

Compile from the repository's source directory using the command that matches the final project structure.

Then test both required task selections:

```bash
java Main -A 1
java Main -A 2
```

Also test invalid commands:

```bash
java Main
java Main -A
java Main -A 3
java Main -B 1
java Main -A hello
```

## Final Checklist

- [ ] Final code compiles
- [ ] Dining Philosophers works
- [ ] Readers-Writers works
- [ ] Command-line task selection works
- [ ] Error messages are useful
- [ ] Required experiments are complete
- [ ] Final report is complete
- [ ] Final report results match the code
- [ ] All required files are included
- [ ] No `.class` files are included unless specifically required
- [ ] No `.jar` files are included unless specifically required
- [ ] No IDE-specific build/output folders are included unless required
- [ ] No temporary/debug files are included
- [ ] Repository is clean
- [ ] Final submission package is created
- [ ] Every team member confirms the final version

---

# Git Workflow Reminder

Each person should work on their own branch instead of coding directly on `main`.

Example:

```bash
git checkout main
git pull
git checkout -b alan-dining-philosophers
```

While working:

```bash
git add .
git commit -m "Implement Dining Philosophers synchronization"
git push -u origin alan-dining-philosophers
```

Before opening a Pull Request:

```bash
git checkout main
git pull
git checkout alan-dining-philosophers
git merge main
```

Resolve any conflicts, test again, then push the branch and open the Pull Request.

Any integration problem discovered after merging should be fixed on a **new branch** and merged through another Pull Request instead of editing `main` directly.
