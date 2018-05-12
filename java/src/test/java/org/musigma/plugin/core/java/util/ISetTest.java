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

import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

import static java.util.stream.Collectors.toSet;
import static org.assertj.core.api.Assertions.assertThat;

class ISetTest {
    private final ISet<Integer> empty = ISet.empty(Integer[]::new);

    @Test
    void canAddElements() {
        int[] values = ThreadLocalRandom.current()
                .ints()
                .distinct()
                .limit(100)
                .toArray();
        ISet<Integer> set = empty;
        for (int value : values) {
            assertThat(set).doesNotContain(value);
            set = set.add(value);
            assertThat(set).contains(value);
        }
        assertThat(set).hasSize(100);
    }

    @Test
    void canRemoveElements() {
        Set<Integer> values = ThreadLocalRandom.current()
                .ints()
                .distinct()
                .limit(100)
                .boxed()
                .collect(toSet());
        ISet<Integer> set = empty.addAll(values);
        for (Integer value : values) {
            assertThat(set).contains(value);
            set = set.remove(value);
            assertThat(set).doesNotContain(value);
        }
        assertThat(set).isEmpty();
    }

}