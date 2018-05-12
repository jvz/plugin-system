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
@Alias("nestedFactory")
public class NestedFactoryPlugin {
    private final TrivialFactoryPlugin trivial;
    private final FactoryBasedPlugin factory;
    private final String setting;

    private NestedFactoryPlugin(final TrivialFactoryPlugin trivial, final FactoryBasedPlugin factory, final String setting) {
        this.trivial = trivial;
        this.factory = factory;
        this.setting = setting;
    }

    public TrivialFactoryPlugin getTrivial() {
        return trivial;
    }

    public FactoryBasedPlugin getFactory() {
        return factory;
    }

    public String getSetting() {
        return setting;
    }

    @Factory
    public static NestedFactoryPlugin newInstance(@Plugin final TrivialFactoryPlugin trivial,
                                                  @Plugin final FactoryBasedPlugin factory,
                                                  final String setting) {
        return new NestedFactoryPlugin(trivial, factory, setting);
    }
}
