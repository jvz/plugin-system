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

@Plugin
@Alias("nestedBuilder")
public class NestedBuilderPlugin {
    private final String name;
    private final TrivialPlugin trivial;
    private final FactoryBasedPlugin factory;
    private final int size;
    private final boolean enabled;

    private NestedBuilderPlugin(final Builder builder) {
        name = builder.name;
        trivial = builder.trivial;
        factory = builder.factory;
        size = builder.size;
        enabled = builder.enabled;
    }

    public String getName() {
        return name;
    }

    public TrivialPlugin getTrivial() {
        return trivial;
    }

    public FactoryBasedPlugin getFactory() {
        return factory;
    }

    public int getSize() {
        return size;
    }

    public boolean isEnabled() {
        return enabled;
    }

    @Factory
    public static class Builder {
        private final String name;
        private final TrivialPlugin trivial;
        private FactoryBasedPlugin factory;
        private int size = 1024;
        private boolean enabled;

        public Builder(final String name, @Plugin final TrivialPlugin trivial) {
            this.name = name;
            this.trivial = trivial;
        }

        public Builder factory(@Plugin final FactoryBasedPlugin factory) {
            this.factory = factory;
            return this;
        }

        public Builder size(final int size) {
            this.size = size;
            return this;
        }

        public Builder enabled(final boolean enabled) {
            this.enabled = enabled;
            return this;
        }

        public NestedBuilderPlugin build() {
            return new NestedBuilderPlugin(this);
        }
    }
}
