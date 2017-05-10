package com.bigos.other;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.IntStream;

/**
 * Created by bigos on 10.05.17.
 */
public class StreamLearning1 {

    public static void main(String[] args) {
        List<Foo> foos = new ArrayList<>();
        // init foo and bar
        IntStream
                .range(1, 3)
                .forEach(i -> foos.add(new Foo("foo" + i)));

        foos
            .forEach(f ->
                IntStream
                    .range(1, 3)
                    .forEach(i -> f.bars.add(new Bar("bar" + i)))
            );

        System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<FlatMap>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        // FlatMap transforms each element of the stream into a stream of other objects.
        // flatMap(Function<? super T, ? extends Stream<? extends R>> mapper),
        // mapper -  a function to apply to each element which produces a stream of new values
        foos.stream()
                .flatMap(f -> f.bars.stream())
                    .forEach(System.out::println);
        // stream of 2 objects is transformed into stream of 4 objects

        // another way of constructing such list and then flat map
        System.out.println("<<<<<<<<<<<<<<<<<<<<<another way of constructing such list>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        IntStream
                .range(1, 3)
                .mapToObj(i -> new Foo("foo" + i))
                .peek(f -> IntStream
                        .range(1, 3)
                        .mapToObj(j -> new Bar("bar" + j))
                        .forEach(f.bars::add)
                ).flatMap(f -> f.bars.stream())
                    .forEach(System.out::println);
        // IntStream peek(IntConsumer action); - Returns a stream consisting of the elements of this stream,
        // additionally performing the provided action on each element as elements are consumed from the resulting stream.

        // FlatMap is also available for the Optional class introduced in Java 8
        System.out.println("<<<<<<<<<<<<<<<<optional with flat map>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        Optional.of(foos.get(0))
                .flatMap(f -> Optional.ofNullable(f.bars.get(0)))
                .flatMap(b -> Optional.ofNullable(b.name))
                .ifPresent(System.out::println);

        System.out.println("<<<<<<<<<<<<<<<<<<<<parallel streams>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        // -Djava.util.concurrent.ForkJoinPool.common.parallelism=2
        ForkJoinPool commonPool = ForkJoinPool.commonPool();
        System.out.println(commonPool.getParallelism());    // 2

        List<String> myList = Arrays.asList("a1", "a2", "b1", "c2", "c1");
        myList
                .parallelStream()
                .filter(a -> {
                    System.out.printf("filter: %s [%s]\n", a, Thread.currentThread().getName());
                    return true;
                })
                .map(a -> {
                    System.out.printf("map: %s [%s]\n", a, Thread.currentThread().getName());
                    return a.toUpperCase();
                })
                .forEach(a -> System.out.printf("forEach: %s [%s]\n", a, Thread.currentThread().getName()));
        // warning - using sort invokes Arrays.parallelSort()
        // parallelSort - If the length of the specified array is less than the minimum granularity,
        // then it is sorted using the appropriate Arrays.sort method.

    }
}

class Foo {

    public String name;
    public List<Bar> bars;

    public Foo(String name) {
        this.name = name;
        bars = new ArrayList<>();
    }
}

class Bar {
    public String name;

    public Bar(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Bar{" +
                "name='" + name + '\'' +
                '}';
    }
}