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

package org.musigma.plugin.api.config.test;

import org.musigma.plugin.api.annotation.Alias;
import org.musigma.plugin.api.annotation.Factory;
import org.musigma.plugin.api.annotation.Plugin;

import static java.util.Objects.requireNonNull;

@Plugin
@Alias("factory")
public class FactoryBasedPlugin {
    private final String name;
    private final int size;

    private FactoryBasedPlugin(final String name, final int size) {
        this.name = name;
        this.size = size;
    }

    @Factory
    public static FactoryBasedPlugin create(final String name, final int size) {
        requireNonNull(name);
        return new FactoryBasedPlugin(name, size);
    }

    public String getName() {
        return name;
    }

    public int getSize() {
        return size;
    }
}
