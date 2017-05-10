package com.bigos.other;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Created by luke on 2017-05-05.
 *
 * Streams are Monads, thus playing a big part in bringing functional programming to Java.
 * In functional programming, a monad is a structure that represents computations defined as sequences of steps.
 * A type with a monad structure defines what it means to chain operations, or nest functions of that type together.
 * In functional programming, monads are a way to build computer programs by joining simple components in robust ways
 *
 */
public class StreamLearning {

    public static void main(String[] args) {
        System.out.println("<<<<<<<<<<<<<<<<<<<First example>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        List<String> myList = Arrays.asList("a1", "a2", "b1", "c2", "c1");
        myList
                .stream()
                .filter((a) -> a.startsWith("c"))
                .map(String::toUpperCase)
                .sorted()
                .forEach(System.out::println);
        /* filter(Predicate<? super T> predicate) - Returns a stream consisting
        * of the elements of this stream that match the given predicate.
        *  Predicate<T> - Represents a predicate (boolean-valued function) of one argument.
        * */
        /* map(Function<? super T,? extends R> mapper)- Returns a stream consisting of
        * the results of applying the given function to the elements of this stream.
        * Function<T,R> - Represents a function that accepts one argument and produces a result.
        * (T - the type of the input to the function, R - the type of the result of the function)
        * */
        /* sorted() - Returns a stream consisting of the elements of this stream, sorted according to natural order.
        * */
        /* forEach(Consumer<? super T> action) - Performs an action for each element of this stream.
        * - Consumer<T> - Represents an operation that accepts a single input argument and returns no result.
        * */

        //  Streams does not have to be used on collections. StreamLearning.of() - create a stream from a bunch of object references.
        Stream.of("a1", "a2", "a3").findFirst().ifPresent(System.out::println);
        // Stream.of - Returns a sequential ordered stream whose elements are the specified values.
        // void ifPresent(Consumer<? super T> consumer) - If a value is present, invoke the specified consumer with the value,
        // otherwise do nothing.

        //Primitive streams
        System.out.println("<<<<<<<<<<<<<<<<<<<<<<<Primitive streams>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        // Streams working with primitive data types int, long and double
        IntStream.range(1, 4).forEach(System.out::println);
        // Primitive streams use lambda expressions, i.e. IntFunction, IntPredicate.
        // Primitive streams support aggregate operations sum() and average().
        System.out.println("print average of 1, 2, 3:");
        Arrays.stream(new int[]{1, 2, 3})
            .map((i) ->  2 * i + 1)
            .average()
            .ifPresent(System.out::println);

        // Transforming a regular object stream to a primitive stream or vice versa. (mapToInt(), mapToLong(), mapToDouble)
        System.out.println("transforming to int");
        Stream.of("a1", "a2", "a3")
                .map(s -> s.substring(1))
                .mapToInt(Integer::parseInt)
                .max()
                .ifPresent(System.out::println);

        // Primitive streams can be transformed to object streams (mapToObj())
        System.out.println("example of map to object: ");
        IntStream.range(1, 4)
                .mapToObj(i -> "a" + i)
                .forEach(System.out::println);

        System.out.println("another example of primitive streams");
        Stream.of(1.0, 2.0, 3.0)
                .mapToInt(Double::intValue)
                .mapToObj(i -> "a" + i)
                .forEach(System.out::println);
         /* DoubleStream.of(1, 2, 3)
                .mapToInt(d -> (int)d) // Double::intValue not working, I guess because of DoubleToIntFunction requires
                .mapToObj(i -> "a" + i)         // double and gets Double instead. Type of applyAsInt if interfered with
                .forEach(System.out::println);  // mapToInt body.
         */

        // Stream's intermediate operations characterise with lazy init. (forEach is terminal operation)
        // Element moves along the chain vertically. The first string "d2" passes filter then forEach
        // output: filter: d2, forEach: d2, filter: a2, forEach: a2, ...
        // This behavior can reduce the number of operations.
        System.out.println("lazy init");
        Stream.of("d2", "a2", "c")
                .filter(s -> {
                    System.out.println("filter: " + s);
                    return true;
                });
        Stream.of("d2", "a2", "c")
                .filter(s -> {
                    System.out.println("filter: " + s);
                    return true;
                })
                .forEach(s -> System.out.println("forEach: " + s));
        Stream.of("d2", "a2", "c")
                .map(s -> {
                    System.out.println("map: " + s);
                    return s.toUpperCase();
                })
                .anyMatch(s -> {
                    System.out.println("anyMatch: " + s);
                    return s.startsWith("A");
                });
        // anyMatch(Predicate<? super T> predicate) - Returns whether any elements of this stream
        // match the provided predicate

        // Intermediate operations orders matter.
        // Order (map -> filter -> foreach)
        // 1 output: map:     d2, filter:  D2,  [map:     a2, filter:  A2, forEach: A2], map:     c, filter:  c
        // Order (filter -> map -> foreach)
        // 2 output: filter:  d2, [filter:  a2, map:     a2, forEach: A2],  filter:  c

        // Reusing Streams
        System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<Reusing Streams>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        // streams can be reused with streamSupplier
        Supplier<Stream<String>> streamSupplier = () -> Stream.of("d2", "a2", "c").filter(s -> s.startsWith("a"));
        streamSupplier.get().anyMatch(s -> {
            System.out.println("anymatch: " + s);
            return true;
        });
        streamSupplier.get().noneMatch(s -> {
            System.out.println("nonematch: " + s);
            return true;
        });

        // Advanced Streams
        System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<Advanced Streams>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        List<Person> persons = Arrays.asList(new Person("Max", 14), new Person("Paul", 24), new Person("Patricia", 24),
                        new Person("Dave", 13));
        // collect
        System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<collect>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        // Collect (terminal operation) transforms the elements of the stream into a different kind of result
        // i.e. a List, Set or Map
        // list of persons which name's starts with P
        List<Person> lofp = persons
                .stream()
                .filter((p) -> p.name.startsWith("P"))
                .collect(Collectors.toList());
        System.out.println("persons starting with p: " + lofp);

        //Persons group by age
        Map<Integer, List<Person>> personsByAge = persons
                .stream()
                .collect(Collectors.groupingBy(p -> p.age));

        personsByAge
                .forEach((age, p) -> System.out.format("age %s: %s\n", age, p));
        // Collector.groupingBy -  Returns a implementation of a "group by" operation on
        // input elements of type  T, grouping elements according to a
        // classification function, and returning the results in a Map

        // average age
        Double ageAverage = persons
                .stream()
                .collect(Collectors.averagingInt(p -> p.age));
        System.out.println("average age of persons: " + ageAverage);
        // summarizingInt - gets min, max and average

        //  Joins all persons into a single string
        String singleStringPersons = persons
                .stream()
                .filter(p -> p.age >= 18)
                .map(p -> p.name)
                .collect(Collectors.joining(" and ", "", " can drink a beer."));
        System.out.println(singleStringPersons);

        //Transforming to map
        Map<Integer, String> personsToMap = persons
                .stream()
                .collect(Collectors.toMap(
                        p -> p.age,
                        p -> p.name,
                        (name1, name2) -> name1 + ";" + name2));
        System.out.println("persons to map: " + personsToMap);

        // making your own collector - Collector.of(supplier, accumulator, combiner, finisher)
        Collector<Person, StringJoiner, String> pnc = Collector.of(
                () -> new StringJoiner(" | "),
                (j, p) -> j.add(p.name.toUpperCase()),
                (j1, j2) -> j1.merge(j2),
                StringJoiner::toString
        );
        // StringJoiner - is used to construct a sequence of characters separated
        // by a delimiter and optionally starting with a supplied prefix
        // and ending with a supplied suffix.
        String personsName = persons.stream().collect(pnc);
        System.out.println(personsName);


        // REDUCE
        System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<reduce>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        // reduce - combines all elements of the stream into a single result.
        System.out.println("print the oldest person");
        persons
                .stream()
                .reduce((p1, p2) -> p1.age > p2.age ? p1 : p2)
                .ifPresent(System.out::println);
        // Optional<T> reduce(BinaryOperator<T> accumulator)
        // BinaryOperator<T> extends BiFunction<T,T,T> - Represents an operation upon
        // two operands of the same type, producing a result of the same type as the operands.
        // BiFunctions are like Function but accept two arguments.

        // reduce with identity
        Person result = persons.stream()
                .reduce(new Person("", 0), (p1, p2) -> {
                    p1.age += p2.age;
                    p1.name += p2.name;
                    return p1;
                });
        System.out.println("reduce with identity - identity is returned as a result: " + result);

        // reduce with combiner
        Integer sumOfAgeOfPersons = persons
                .stream()
                .reduce(0, (sum, p) -> {
                    System.out.println("sum: " + sum + " person: " + p);
                    return sum += p.age;
                }, (sum1, sum2) -> {
                    System.out.println("sum1: " + sum1 + " sum2: " + sum2);
                    return sum1 + sum2;
                });
        // in this example combiner never gets called
        System.out.println("sumOfAgeOfPersons: " + sumOfAgeOfPersons);
        // <U> U reduce(U identity, BiFunction<U, ? super T, U> accumulator, BinaryOperator<U> combiner);

        // reduce with combiner in parallel stream
        System.out.println("reduce with combiner in parallel stream");
        Integer sumOfAgeOfPersonsParallel = persons
                .parallelStream()
                .reduce(0, (sum, p) -> {
                    System.out.println("accumulator: sum: " + sum + " person: " + p);
                    return sum += p.age;
                }, (sum1, sum2) -> {
                    System.out.println("combiner: sum1: " + sum1 + " sum2: " + sum2);
                    return sum1 + sum2;
                });
        System.out.println("sumOfAgeOfPersonsParallel: " + sumOfAgeOfPersonsParallel);
        // Accumulator is called in parallel stream so the combiner is needed to sum up the separate accumulated values.
    }
}

class Person {
    String name;
    int age;

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

    @Override
    public String toString() {
        return "name: " + name + " age: " + age;
    }
}