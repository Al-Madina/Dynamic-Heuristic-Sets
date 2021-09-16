# Contribution

# General Guidelines
1. Please adhere to the package structure and do not create new packages unnecessarily.
2. Data for your problem domains should reside in a subdirectory of the [Data](#Data) directory.
If your data is large, please do not host it here since it will slow the pull and clone requests.
3. Documents related to the repository should reside in the [docs](#docs) directory.
4. Images related to the repository should reside in the [images](#images) directory.
5. Some more rules for contributing are coming up!

# How to Contribute
You can contribute to JDHS in the following ways:
1. Implement new dynamic sets or new strategies for dynamic sets.
2. Implement new hyper-heuristics.
3. Implement new problem domains.
4. Improves the documentation (javadoc) and the [User Guide](../README.md).
5. Report issues.
6. Fix bugs.


## Implement New Dynamic Sets
To extend DHS, you need to implement new concrete classes for the core abstract classes: `Measure`, 
`Remove`, `Reset`, and `Update`. These core classes define dynamic sets and specify the
way they operate.

### Extend `Measure`
The core abstract class `Measure` has one abstract method that should be implemented 
in your concrete class which is `public abstract double measure(int heurIndex)`. 
This method expects an integer which is the _index_ of the heuristic in the universal
set and returns a `double` that represents the _value_ of the heuristic. 

Note that your class that extends the `Measure` class has a field `runStat`
of type `RunStat` which stores the information you need about each low-level heuristic
such as how long the heuristic ran so far, how much improvement/deterioration
the heuristic has made/caused.

For an example on how to extend the `Measure` class, 
please see [this](../src/dynheurset/measure/PerfDurMeasure.java).


### Extend `Remove`
The core class `Remove` has two abstract methods that should be implemented.
`public abstract boolean canRemove()` returns `true` if a permanent removal of
some heuristics should be executed and `false` otherwise. `public abstract void remove()`
determines which heuristics to remove permanently and places those heuristics
in a `public` field `removedHeurSet` of type `Set<Integer>.

Please note that your class that extends `Remove` has a field `runStat` of type 
`RunStat` and a field `measure` of type `Measure` that can be used to implement
your logic for the removal strategy.

For an example on how to extend the `Remove` class, 
please see [this](../src/dynheurset/update/remove/PatientWorstRemoval.java).

### Extend `Reset`
The core class `Reset` has one abstract method that should be implemented.
 `public abstract boolean canReset()` which specifies the condition
for triggering a _reset operation_ that forces all heuristics to be included in
the active set except for the permanently removed heuristic.

Please note that your class that extends `Reset` has a field `runStat` of type 
`RunStat` and a field `measure` of type `Measure` that can be used to implement
your logic for the reset condition.

**Note:** The reset operation should be implemented in your
class that extends the `Update` class. Recall that `Remove` and `Reset` are aggregated
into `Update` (the `Update` object has a field of type `Remove` and a field of type `Reset`).
 Therefore, from the `Reset` class, the `Update` object knows when to 
trigger the reset operation and from the `Remove` class, the `Update` object knows 
the permanently removed heuristics that should not be included in the active set 
when the reset operation is performed.

For an example on how to extend the `Reset` class,
please see [this](../src/dynheurset/update/reset/PatientReset.java).

### Extend `Update`
The core class `Update` has two abstract methods that should be implemented. 
`protected abstract boolean canUpdate()` determines when to update the active set 
and `protected abstract void performUpdate()` which contains the logic for updating the
active set.

The method `public List<Integer> updateActiveList()` called
by the hyper-heuristic to update the active set [(see here)](../README.md), calls
`performUpdate` to decide which heuristic to include in the active set but also does
more work than that. First, it removes some heuristics permanently if necessary. 
Then, it resets the active set to include all heuristics except for permanently removed ones 
if a reset condition is met. If the reset is done the active set is immediately returned 
to the hyper-heuristic. Otherwise, the update is carried out by calling the `performUpdate` method.

For an example on how to extend the `Update` class,
please see [this](../src/dynheurset/update/PhaseDominanceUpdate.java).

## Implement New Hyper-Heuristics
You can implement new hyper-heuristics by extending `GenericHyperHeuristic` class. You need
to implement the `solve` method which should contain the logic of your hyper-heuristic. In this
method you need to call `hasTimeExpired` frequently to record the best solution value and
make sure that you do not exceed the time limit. 

Your hyper-heuristic will have access to a field `problem` of type `Problem` which is
the problem instance that will be solved. You need to load the problem into your
your hyper-heuristic by using `loadProblem` method of `GenericHyperHeuristic`.

Then, to run your hyper-heuristic, you need to call the `run` method which will call the `solve`
method to solve the problem instance referenced by the field `problem`. Your hyper-heuristic should
like as follows:
```java
public class MyHyperHeuristic extends GenericHyperHeuristic{

    //Some fields here if your hyper-heuristic needs ones for instance:
    private double temperature; //for Metropolis acceptance


    public MyHyperHeuristic(long seed, double temperature) {
        super(seed);
        this.temperature = temperature;
    }

    
    //Some initialization if needed
    private void init(){
        //Create initial solution and observe the initial value
        //Initialize some hyper-heuristic fields
        //Set up dynamic sets
        //etc
    }

    //Your hyper-heuristic logic should go here:
    @Override
    public void solve() {
    
        //Initialize your hyper-heuristic
        init();

        //Start the optimization loop
        while(!hasTimeExpired()){
            //Solve the problem
        }
    }
}
```

**Note:** if your hyper-heuristic extends a superclass, in this case you will not be able
to extend `GenericHyperHeuristic`. Instead, you need to implement `HyperHeuristicIntrf` and follow
the same steps described above. This interface has a default implementation that throws exception.
Therefore, you can only implement the methods you need in your hyper-heuristic.
If you want to use dynamic sets, you will have to implement `setDynSet` method of 
`HyperHeuristicIntrf`. For examples on how to implement the `HyperHeuristicIntrf`
interface, please refer to [this class](../src/hyperheuristic/GenericHyperHeuristic.java).

**Note:** if your hyper-heuristic extends the abstract class `HyperHeuristic` of HyFlex
you do not have to implement any methods of `HyperHeuristicIntrf` as these methods are
satisfied by the `HyperHeuristic` class of HyFlex except for dynamic-set-specific methods. 
See [this](../src/hyperheuristic/examples/HyFlexExampleHyperHeuristic1.java) for an example.

For complete examples on how to implement new hyper-heuristics, 
please see [this](../src/hyperheuristic/examples).

## Implement New Problem Domains
You can implement a new problem domain by implementing the `Problem` interface. 
For a complete example, please see [this class](../src/problem/skilodge/SkiLodge.java)
which implements the Ski-Lodge Problem described 
[here](https://link.springer.com/chapter/10.1007/978-3-319-27400-3_18)
This is a _toy problem_.

## Improve Documentation
Please follow the standards used in javadoc if you want to contribute to improving the documentation.

This library is maintained by Ahmed Hassan (ahmedhassan@aims.ac.za). Please contact me
if you have any inquiry.