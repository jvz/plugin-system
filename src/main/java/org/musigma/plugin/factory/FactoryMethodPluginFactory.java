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

package org.musigma.plugin.factory;

import org.musigma.plugin.PluginException;
import org.musigma.plugin.registry.PluginRegistry;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class FactoryMethodPluginFactory<T> extends ExecutablePluginFactory<T, Method> {

    public FactoryMethodPluginFactory(final PluginRegistry registry, final Method executable) {
        super(registry, executable);
    }

    @SuppressWarnings("unchecked")
    @Override
    T newInstance(final Object... args) {
        try {
            return (T) executable.invoke(null, args);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new PluginException(e);
        }
    }
}
