package com.bigos.other;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * Created by luke on 2017-05-05.
 */
public class MethodReference {

    public static void main(String[] args) {

        // To save keystrokes, you can replace the lambda with a method reference,
        // which is a compact reference to an existing method.
        // 1. lambda
        Consumer<String> consumer = (s) -> System.out.println(s);
        consumer.accept("hello from lambda");
        // 2. Method reference
        Consumer<String> consumer1 = System.out::println;
        consumer1.accept("Hello from method reference");

        // You can use method references to refer to a class's static methods, bound and unbound non-static methods,
        // and constructors. You can also use method references to refer to
        // instance methods in superclass and current class types.

        // References to static methods
        System.out.println("<<<<<<<<<<<<<<<<<<References to static methods>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");

        int[] array = { 10, 2, 19, 5, 17 };
        Consumer<int[]> consumer2 = Arrays::sort;
        consumer2.accept(array);
        //Arrays.stream(array).forEach(System.out::print);
        System.out.println(Arrays.toString(array));
        // doing list from array
        List<Integer> list = Arrays.stream(array).boxed().collect(Collectors.toList());
        System.out.println(list.toString());
        //the same with lambda
        int[] array1 = { 19, 5, 14, 3, 21, 4 };
        Consumer<int[]> consumer3 = (a) -> Arrays.sort(a);
        consumer3.accept(array1);
        System.out.println(Arrays.toString(array1));

        // References to bound non-static methods
        System.out.println("<<<<<<<<<<<<<<<<<<References to bound non-static methods>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        String s = "Trala Buha fdsa Gda dfsa A";
        // s::length introduces a closure that closes over s.
        Supplier<Integer> supplier = s::length;
        System.out.println("s length from method reference: " + supplier.get());
        Supplier<Integer> supplier1 = () -> s.length();
        System.out.println("s length from lambda: " + supplier1.get());

        // References to unbound non-static methods
        System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<<References to unbound non-static methods>>>>>>>>>>>>>>>>>>>>>>");
        Function<String, String> function = String::toLowerCase;
        System.out.println("STRING TO LOWER CASE with method reference: " + function.apply("STRING TO LOWER CASE"));
        Function<String, String> function1 = (string) -> string.toLowerCase();
        System.out.println("STRING TO LOWER CASE with lambda: " + function1.apply("STRING TO LOWER CASE"));

        // References to constructors
        System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<References to constructors>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        Supplier<MethodReference> supplier2 = MethodReference::new;
        System.out.println(supplier2.get());

        // References to instance methods in superclass and current class types
        System.out.println("<<<<<<<<<<<References to instance methods in superclass and current class types>>>>>>>>>>>");
    }
}
