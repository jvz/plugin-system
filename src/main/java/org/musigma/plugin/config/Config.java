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

package org.musigma.plugin.config;

import java.util.Map;
import java.util.function.BiFunction;
import java.util.stream.Stream;

public final class Config {
    private final Map<String, ConfigValue<?>> values;

    public Config(final Map<String, ConfigValue<?>> values) {
        this.values = values;
    }

    @SuppressWarnings("unchecked")
    public <T> ConfigValue<T> get(final String name) {
        return (ConfigValue<T>) values.get(name);
    }

    public <T> Stream<T> map(final BiFunction<String, ConfigValue<?>, ? extends T> function) {
        return values.entrySet().stream().map(entry -> function.apply(entry.getKey(), entry.getValue()));
    }

    @Override
    public String toString() {
        return values.toString();
    }
}