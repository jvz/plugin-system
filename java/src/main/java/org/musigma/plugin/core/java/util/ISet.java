/*
 * Copyright 2018 Matt Sicker
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.musigma.plugin.core.java.util;

import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.IntFunction;

import static java.util.stream.Collectors.joining;

public final class ISet<T> implements Iterable<T> {
    private final IntFunction<T[]> generator;
    private final Comparator<T> comparator;
    private final T[] set;
    private final int hashCode;

    private ISet(final IntFunction<T[]> generator, final Comparator<T> comparator, final T[] set) {
        this.generator = generator;
        this.comparator = comparator;
        this.set = set;
        this.hashCode = Arrays.hashCode(set);
    }

    private ISet<T> copy(final T[] set) {
        return new ISet<>(generator, comparator, set);
    }

    public ISet<T> add(final T item) {
        int pos = Arrays.binarySearch(set, item, comparator);
        if (pos >= 0) return this;
        int insertionPoint = -1 * (pos + 1);
        T[] copy = generator.apply(set.length + 1);
        System.arraycopy(set, 0, copy, 0, insertionPoint);
        copy[insertionPoint] = item;
        System.arraycopy(set, insertionPoint, copy, insertionPoint + 1, copy.length - insertionPoint - 1);
        return copy(copy);
    }

    public ISet<T> addAll(final Collection<T> items) {
        ISet<T> set = this;
        for (T item : items) {
            set = set.add(item);
        }
        return set;
    }

    public ISet<T> remove(final T item) {
        if (set.length == 0) return this;
        int pos = Arrays.binarySearch(set, item, comparator);
        if (pos < 0) return this;
        if (set.length == 1) return copy(generator.apply(0));
        T[] copy = generator.apply(set.length - 1);
        System.arraycopy(set, 0, copy, 0, pos);
        System.arraycopy(set, pos + 1, copy, pos, copy.length - pos);
        return copy(copy);
    }

    public boolean contains(final T item) {
        return set.length > 0 && Arrays.binarySearch(set, item, comparator) >= 0;
    }

    public int size() {
        return set.length;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final ISet<?> that = (ISet<?>) o;
        return Arrays.equals(set, that.set);
    }

    @Override
    public int hashCode() {
        return hashCode;
    }

    @Override
    public String toString() {
        return Arrays.stream(set)
                .map(String::valueOf)
                .collect(joining(", ", "{", "}"));
    }

    @Override
    public Iterator<T> iterator() {
        return IArray.of(set);
    }

    @Override
    public void forEach(final Consumer<? super T> action) {
        for (T item : set) {
            action.accept(item);
        }
    }

    @Override
    public Spliterator<T> spliterator() {
        return Arrays.spliterator(set);
    }

    public static <T extends Comparable<T>> ISet<T> empty(final IntFunction<T[]> generator) {
        return new ISet<>(generator, Comparator.naturalOrder(), generator.apply(0));
    }

    public static <T> ISet<T> empty(final IntFunction<T[]> generator, final Comparator<T> comparator) {
        return new ISet<>(generator, comparator, generator.apply(0));
    }
}
