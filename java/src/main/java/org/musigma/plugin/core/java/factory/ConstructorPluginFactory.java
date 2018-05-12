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

package org.musigma.plugin.core.java.factory;

import org.musigma.plugin.api.PluginException;
import org.musigma.plugin.api.registry.PluginRegistry;

import java.lang.reflect.Constructor;

public class ConstructorPluginFactory<T> extends ExecutablePluginFactory<T, Constructor<T>> {
    public ConstructorPluginFactory(final PluginRegistry registry, final Constructor<T> constructor) {
        super(registry, constructor);
    }

    @Override
    T newInstance(final Object... params) {
        try {
            return executable.newInstance(params);
        } catch (Throwable e) {
            throw new PluginException(String.format("Could not invoke constructor on %s", executable), e);
        }
    }
}
