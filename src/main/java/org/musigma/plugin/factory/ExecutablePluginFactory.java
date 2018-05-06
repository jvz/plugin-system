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

import org.musigma.plugin.Todo;
import org.musigma.plugin.config.ConfigValue;
import org.musigma.plugin.registry.PluginRegistry;

import java.lang.reflect.Executable;
import java.lang.reflect.Parameter;
import java.util.List;
import java.util.Map;

public abstract class ExecutablePluginFactory<T, E extends Executable> extends PluginFactory<T> {
    final E executable;
    private final Parameter[] parameters;

    ExecutablePluginFactory(final PluginRegistry registry, final E executable) {
        super(registry);
        this.executable = executable;
        this.parameters = executable.getParameters();
    }

    abstract T newInstance(final Object... args);

    @Override
    public T create(final ConfigValue configValue) {
        if (parameters.length == 0) {
            return newInstance();
        }
        Object[] args = new Object[parameters.length];
        if (configValue.isMap()) {
            Map<String, ConfigValue> parent = configValue.asMap();
            for (int i = 0; i < parameters.length; i++) {
                Parameter parameter = parameters[i];
                args[i] = getParameterValue(parameter, parent).orElseThrow(() -> new Todo("Handle optional parameters"));
            }
        } else if (configValue.isList()) {
            List<ConfigValue> params = configValue.asList();
            throw new Todo("Handle list of arguments");
        } else {
            throw new Todo("Should we accept single arg construction as well?");
        }
        return newInstance(args);
    }

}
