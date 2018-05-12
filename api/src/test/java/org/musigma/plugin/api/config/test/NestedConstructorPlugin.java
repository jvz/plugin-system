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
@Alias("nested")
public class NestedConstructorPlugin {
    private final TrivialPlugin trivial;
    private final ConstructorBasedPlugin cons;

    @Factory
    public NestedConstructorPlugin(@Plugin final TrivialPlugin trivial, @Plugin final ConstructorBasedPlugin cons) {
        this.trivial = trivial;
        this.cons = cons;
    }

    public TrivialPlugin getTrivial() {
        return trivial;
    }

    public ConstructorBasedPlugin getCons() {
        return cons;
    }
}