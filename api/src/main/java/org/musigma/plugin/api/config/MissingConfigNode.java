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

package org.musigma.plugin.api.config;

import org.musigma.plugin.api.type.TypeRef;

import java.util.NoSuchElementException;

public enum MissingConfigNode implements ConfigNode {
    MISSING;

    @Override
    public int size() {
        return 0;
    }

    @Override
    public boolean isValueNode() {
        return false;
    }

    @Override
    public boolean isContainerNode() {
        return false;
    }

    @Override
    public boolean isMissingNode() {
        return true;
    }

    @Override
    public boolean isList() {
        return false;
    }

    @Override
    public boolean isRecord() {
        return false;
    }

    @Override
    public boolean hasKey(final String key) {
        return false;
    }

    @Override
    public ConfigNode get(final String key) {
        return MISSING;
    }

    @Override
    public ConfigNode get(final int index) {
        return MISSING;
    }

    @Override
    public Object get() {
        throw new NoSuchElementException();
    }

    @Override
    public <T> T getAs(final Class<T> clazz) {
        throw new NoSuchElementException();
    }

    @Override
    public <T> T getAs(final TypeRef<T> ref) {
        throw new NoSuchElementException();
    }
}
