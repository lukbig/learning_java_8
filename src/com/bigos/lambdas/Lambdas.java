package com.bigos.lambdas;

import java.util.Comparator;
import java.util.concurrent.Callable;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * Created by luke on 2017-05-04.
 *
 * Lambda expressions define anonymous methods that are treated as instances of functional interfaces.
 *
 * Lambdas are implemented in terms of the invokedynamic instruction and java.lang.invoke
 *
 * Lambda syntax:
 * ( formal-parameter-list ) -> { expression-or-statements }
 * formal-parameter-list is a comma-separated list of formal parameters, which must match the parameters of
 * a functional interface's single abstract method at runtime. If you omit their types, the compiler infers
 * these types from the context in which the lambda is used.
 * (double a, double b) // types explicitly specified
 * (a, b) // types inferred by compiler
 *
 *  WhereLambdaCanBeUsed?
 * - Variable declaration
 * - Assignment
 * - Return statement
 * - Array initializer
 * - Method or constructor arguments
 * - Lambda body
 * - Ternary conditional expression
 * - Cast expression
 *
 * Variables
 * Whether originating in a lambda body or in the enclosing scope,
 * a local variable must be initialized before being used.
 * Otherwise, the compiler will report an error.
 * A local variable or parameter that's defined outside a lambda body
 * and referenced from the body must be marked final or considered effectively final.
 *
 * Lambdas and this and super
 * Any this or super reference that is used in a lambda body is regarded as being equivalent
 * to its usage in the enclosing scope (because a lambda doesn't introduce a new scope).
 * However, this isn't the case with anonymous classes.
 *
 * Lambdas and exceptions
 * A lambda body is not allowed to throw more exceptions than are specified in the throws
 * clause of the functional interface method. If a lambda body throws an exception,
 * the functional interface method's throws clause must declare the same exception type or its supertype.
 *
 * Predefined functional interfaces
 * - Predicate<T> - Represents a predicate (boolean-valued function) of one argument.
 * - Supplier<T> - Represents a supplier of results (T - the type of results supplied by this supplier).
 * - Function<T,R> - Represents a function that accepts one argument and produces a result.
 *      (T - the type of the input to the function, R - the type of the result of the function)
 * - Consumer<T> - Represents an operation that accepts a single input argument and returns no result.
 *      Unlike most other functional interfaces, Consumer is expected to operate via side-effects.
 *
 * Final Thoughts on Predicates in java 8
 * - They move your conditions (sometimes business logic) to a central place. This helps in unit-testing them separately.
 * - Any change need not be duplicated into multiple places. It improves manageability of code.
 * - The code is very much readable than writing a if-else block.
 */

@FunctionalInterface
interface BinaryCalculator
{
    double calculate(double value1, double value2);
}


public class Lambdas {

    // Binary calculator
    static double calculate(BinaryCalculator calc, double v1, double v2) {
        return calc.calculate(v1, v2);
    }

    public static void main(String[] args) {

        // old way of implementing interface
        System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<old way of implementing interface>>>>>>>>>>>>>>>>>>>>");
        (new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("hello from anonymous class");
            }
        })).start();

        // using lambda on functional interface
        System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<using lambda on functional interface>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        (new Thread(() -> System.out.println("hello from lambda"))).start();

        // using functional interfaces created by yourself
        System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<using functional interfaces created by yourself>>>>>>>>>>>>>>>>>");
        System.out.printf("a * b = %3.0f\n", calculate((a, b) -> a * b, 3, 7));

        // Lambda body - a nested lambda
        try {
            Callable<Runnable> callable = () -> () -> System.out.println("called");
            callable.call().run();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // ternary conditional expression
        boolean ascSortOrder = false;
        Comparator<String> cmp = (ascSortOrder) ? (s1, s2) -> s1.compareTo(s2) : (s1, s2) -> s2.compareTo(s1);

        // Predefined functional interfaces
        System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<Predefined functional interfaces>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        // Predicate example
        // is string length greater than 5?
        Predicate<String> predicate  = (s)-> s.length() > 5;
        System.out.println("czy string 'to jest test' zawiera wiecej niz 5 znakow? " + predicate.test("to jest test"));
        // supplier example
        Supplier<String> supplier  = ()-> "string supplied by...";
        System.out.println("string supplied: " + supplier.get());
        // consumer example
        Consumer<String> consumer = (x) -> System.out.println("CONSUmer to lower case : " + x.toLowerCase());
        consumer.accept("CONSUmer");
        // function example
        Function<Integer,String> converter = (i)-> Integer.toString(i);
        System.out.println("convert integer to string : " + converter.apply(3));
    }


}