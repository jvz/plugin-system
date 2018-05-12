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
import java.util.Iterator;
import java.util.Spliterator;

public final class IArray<T> implements Iterable<T>, Iterator<T> {
    private final T[] array;
    private int index;

    private IArray(final T[] array) {
        this.array = array;
    }

    @SafeVarargs
    public static <T> IArray<T> of(final T... values) {
        return new IArray<>(values);
    }

    @Override
    public Iterator<T> iterator() {
        return new IArray<>(array);
    }

    @Override
    public Spliterator<T> spliterator() {
        return Arrays.spliterator(array);
    }

    @Override
    public boolean hasNext() {
        return index < array.length;
    }

    @Override
    public T next() {
        return array[index++];
    }
}
