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

import org.musigma.plugin.api.Todo;
import org.musigma.plugin.api.config.ConfigNode;
import org.musigma.plugin.api.factory.PluginFactory;
import org.musigma.plugin.api.registry.PluginRegistry;
import org.musigma.plugin.core.java.util.Parameters;

import java.lang.reflect.Executable;
import java.lang.reflect.Parameter;

public abstract class ExecutablePluginFactory<T, E extends Executable> implements PluginFactory<T> {
    private final PluginRegistry registry;
    final E executable;
    private final Parameter[] parameters;

    ExecutablePluginFactory(final PluginRegistry registry, final E executable) {
        this.registry = registry;
        this.executable = executable;
        this.parameters = executable.getParameters();
    }

    abstract T newInstance(final Object... args);

    @Override
    public T create(final ConfigNode node) {
        if (parameters.length == 0) {
            return newInstance();
        }
        Object[] args = new Object[parameters.length];
        if (node.isRecord()) {
            for (int i = 0; i < parameters.length; i++) {
                Parameter parameter = parameters[i];
                args[i] = Parameters.createPlugin(registry, node, parameter);
            }
        } else if (node.isList()) {
            throw new Todo("Handle list of arguments");
        } else {
            throw new Todo("Should we accept single arg construction as well?");
        }
        return newInstance(args);
    }

}
