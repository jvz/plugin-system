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

import org.musigma.plugin.api.util.Introspective;

import static java.util.Objects.requireNonNull;

public abstract class AbstractBuilderPlugin {
    private final String name;
    private final int id;
    private final boolean enabled;

    AbstractBuilderPlugin(final String name, final int id, final boolean enabled) {
        this.name = name;
        this.id = id;
        this.enabled = enabled;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public static abstract class Builder<B extends Builder<B>> implements Introspective<B> {
        final String name;
        int id = 42;
        boolean enabled;

        Builder(String name) {
            this.name = requireNonNull(name);
        }

        public B id(int id) {
            this.id = id;
            return self();
        }

        public B enabled(boolean enabled) {
            this.enabled = enabled;
            return self();
        }

        public abstract AbstractBuilderPlugin build();
    }
}
