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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public final class ConfigValue {

    private final Object value;

    private ConfigValue(final Object value) {
        this.value = value;
    }

    public Object get() {
        return value;
    }

    public <T> T getAs(final Class<T> clazz) {
        return clazz.cast(value);
    }

    public boolean isList() {
        return value instanceof List;
    }

    @SuppressWarnings("unchecked")
    public List<ConfigValue> asList() {
        return (List<ConfigValue>) value;
    }

    // used when this is a List
    public ConfigValue get(final int idx) {
        if (value == null) {
            throw new NullPointerException();
        } else if (value instanceof List) {
            return asList().get(idx);
        } else {
            throw new IllegalArgumentException("This does not contain a list. Actual type: " + value.getClass());
        }
    }

    public boolean isMap() {
        return value instanceof Map;
    }

    @SuppressWarnings("unchecked")
    public Map<String, ConfigValue> asMap() {
        return (Map<String, ConfigValue>) value;
    }

    // used when this is a Map
    public ConfigValue get(final String key) {
        if (value == null) {
            throw new NullPointerException();
        } else if (value instanceof Map) {
            return asMap().get(key);
        } else {
            throw new IllegalArgumentException("This does not contain a map. Actual type: " + value.getClass());
        }
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final ConfigValue that = (ConfigValue) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return value == null ? 0 : value.hashCode();
    }

    public static ConfigValue of(Object value) {
        if (value instanceof Map) {
            @SuppressWarnings("unchecked")
            Map<String, ?> m = (Map<String, ?>) value;
            return ofMap(m);
        } else if (value instanceof List) {
            return ofList((List<?>) value);
        } else {
            return ofValue(value);
        }
    }

    public static ConfigValue ofValue(final Object value) {
        return new ConfigValue(value);
    }

    public static ConfigValue ofMap(Map<String, ?> map) {
        Map<String, ConfigValue> values = new HashMap<>(map.size());
        for (Map.Entry<String, ?> entry : map.entrySet()) {
            values.put(entry.getKey(), of(entry.getValue()));
        }
        return new ConfigValue(values);
    }

    public static ConfigValue ofList(List<?> list) {
        return new ConfigValue(list.stream().map(ConfigValue::of).collect(Collectors.toList()));
    }
}
