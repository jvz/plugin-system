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

package org.musigma.plugin.test;

import org.musigma.plugin.annotation.Factory;
import org.musigma.plugin.annotation.Plugin;
import org.musigma.plugin.annotation.Alias;

import static java.util.Objects.requireNonNull;

@Plugin
@Alias("builder")
public class BuilderBasedPlugin {
    private final String name;
    private final String description;

    private BuilderBasedPlugin(Builder builder) {
        name = builder.name;
        description = builder.description;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    @Factory
    public static class Builder {
        // required param example
        private final String name;
        // optional param example
        private String description;

        // required params should be inferred from a plugin factory class constructor
        public Builder(final String name) {
            this.name = requireNonNull(name);
        }

        // optional params should be inferred from single-arg methods that return a subclass of Builder
        public Builder description(final String description) {
            this.description = description;
            return this;
        }

        // and the builder should have a no-arg build() method that returns a subclass of the plugin class
        public BuilderBasedPlugin build() {
            return new BuilderBasedPlugin(this);
        }
    }
}
