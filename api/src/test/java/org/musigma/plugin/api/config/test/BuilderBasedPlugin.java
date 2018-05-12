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
@Alias("builder")
public class BuilderBasedPlugin extends AbstractBuilderPlugin {
    private final String description;

    private BuilderBasedPlugin(Builder b) {
        super(b.name, b.id, b.enabled);
        description = b.description;
    }

    public String getDescription() {
        return description;
    }

    @Factory
    public static class Builder extends AbstractBuilderPlugin.Builder<Builder> {
        private String description;

        // required params should be inferred from a plugin factory class constructor
        public Builder(final String name) {
            super(name);
        }

        // optional params should be inferred from single-arg methods that return a subclass of Builder
        public Builder description(final String description) {
            this.description = description;
            return self();
        }

        @Override
        public BuilderBasedPlugin build() {
            return new BuilderBasedPlugin(self());
        }

        @Override
        public Builder self() {
            return this;
        }
    }
}
