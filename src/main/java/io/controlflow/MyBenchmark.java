/*
 * Copyright (c) 2005, 2014, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */

package io.controlflow;

import org.openjdk.jmh.annotations.*;
import java.util.concurrent.TimeUnit;

/**
 * Example benchmark showing a pitfall when using micro benchmarking. The method
 * {@link #barOptimized} executes faster than {@link #bar}, but {@link #barOptimized} in combination
 * with {@link #foo} executes significantly slower than {@link #bar} in combination with
 * {@link foo}.
 */
@Warmup(iterations = 5)
@Measurement(iterations = 5)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Fork(1)
public class MyBenchmark {

    static int N = 1000;
    static int a = 1;
    static int b = 1234;

    int foo(int x) {
        return (a % b) - (x * b);
    }

    int bar(int x) {
        return (x * b) % 100 == 0 ? (int) Math.sin(x + 1) : x;
    }

    int barOptimized(int x) {
        return (x * b) % 100 == 0 ? barOptimized(x + 1) : x;
    }

    @Benchmark
    public int test1_onlyFoo() {
        int sum = 0;
        for (int i = 0; i < N; ++i) {
            sum += foo(i);
        }
        return sum;
    }

    @Benchmark
    public int test2_onlyBar() {
        int sum = 0;
        for (int i = 0; i < N; ++i) {
            sum += bar(i);
        }
        return sum;
    }

    @Benchmark
    public int test3_combined() {
        int sum = 0;
        for (int i = 0; i < N; ++i) {
            sum += foo(i);
            sum += bar(i);
        }
        return sum;
    }

    @Benchmark
    public int test4_onlyBarOptimized() {
        int sum = 0;
        for (int i = 0; i < N; ++i) {
            sum += barOptimized(i);
        }
        return sum;
    }

    @Benchmark
    public int test5_combinedOptimized() {
        int sum = 0;
        for (int i = 0; i < N; ++i) {
            sum += foo(i);
            sum += barOptimized(i);
        }
        return sum;
    }
}
