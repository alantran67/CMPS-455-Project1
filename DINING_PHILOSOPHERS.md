# Dining Philosophers integration and verification

Brendan can call `DiningPhilosophers.run()` from his eventual CLI. It prompts
for positive integer P and M, joins every philosopher, and prints the completed
meal total before returning. `src/Main.java` is unchanged; no command-line
options or Readers-Writers implementation were added. Input ending before both
values are available returns without starting threads. System.in stays open.
Avoid another buffered reader/Scanner consuming System.in alongside this method;
command-line arguments themselves do not pose this problem.

The implementation uses only semaphores for shared-state synchronization,
respecting the README's restriction against AtomicInteger and other locks.
One simulation cycle is 20 ms; each thinking/eating phase randomly takes 3–6
cycles. Semaphore waits block rather than spin.

For P > 1, a fair admission semaphore allows at most P - 1 philosophers to
acquire chopsticks. Everyone acquires left then right. With at least one
philosopher excluded, a full circular wait cannot form: a contender can obtain
its second chopstick, eat, and release both. Non-neighbors can eat concurrently.
For P = 1, left and right refer to the single physical chopstick; the simulation
acquires/releases it once. This explicit convention permits the required 1/1
case to finish.

A semaphore protects the check-and-increment that reserves each meal number.
Once all M meals are reserved, further claims fail. A separate completed count
advances only after eating. A worker that was already thinking/waiting can still
acquire chopsticks, discover there is no meal, and release them without eating.
No counter lock is held while thinking, eating, or waiting for chopsticks.
Nested finally blocks release acquired chopsticks and admission permits.

Separate one-use semaphore barriers ensure everyone enters before activity and
everyone finishes/releases resources before anybody leaves. Interrupts are
deferred: workers finish the simulation and restore their interrupt status;
an interrupted caller continues joining all workers and restores its status
before returning. This is a finite simulation, not an interrupt-driven cancellation
API. Resource exhaustion or fatal JVM errors are outside normal completion guarantees.

## Run the regression checks (PowerShell, Java 17)

```powershell
$build = Join-Path $env:TEMP ('dining-test-' + [guid]::NewGuid())
New-Item -ItemType Directory $build | Out-Null
javac -Xlint:all -d $build src/Main.java src/DiningPhilosophers.java tests/DiningPhilosophersTest.java
java -cp $build DiningPhilosophersTest
```

The standalone test calls the public run method with simulated console input;
it does not require a main method in Main.java. It repeats all five required
cases three times: 5/10, 3/2, 2/10, 10/25, and 1/1. It also tests invalid values,
integer overflow, and end-of-input. Each invocation has a 10-second timeout.

The trace assertions verify:

- Every meal number is unique and between 1 and M; exactly M meals finish.
- Each chopstick has one owner, only its owner releases it, and all are released.
- Eating requires ownership of both neighboring positions.
- Everyone enters before activity; everyone is ready and M meals finish before departure.
- All P philosophers leave, followed by the final total and a marker printed after run returns.
- Multiple philosophers eat in these multi-philosopher cases. With M < P,
  not everyone can eat; equal meal distribution is not promised.

Repeated successful runs provide runtime evidence of termination; the admission
argument above explains deadlock prevention independently of scheduling.

## Later assignment experiments (currently disabled)

For the yield experiment, insert `Thread.yield();` at the marked point between
chopstick acquisitions in `eatOneMeal()`, repeat the same cases, record timing,
and remove it afterward. Yield is a scheduler hint, not synchronization.

For maximum parallelism, use the marked eating region to temporarily increment
and decrement an active-eater counter under a separate semaphore, tracking its
peak. Put the decrement in a finally block and do not hold the measuring lock
during the pause. For P > 1 the physical bound is floor(P / 2) simultaneous
eaters (one for the special P = 1 convention). Test with enough meals and both
even and odd P. A random run need not attain the bound. Console output affects
timings, so keep output settings consistent when comparing runs. No experiment
results are claimed by these regression checks.
