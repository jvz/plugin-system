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
import org.musigma.plugin.Todo;
import org.musigma.plugin.annotation.Alias;
import org.musigma.plugin.annotation.Plugin;
import org.musigma.plugin.config.ConfigValue;
import org.musigma.plugin.registry.PluginRegistry;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Parameter;
import java.util.List;
import java.util.Map;

public class ConstructorPluginFactory<T> implements PluginFactory<T> {
    private final PluginRegistry registry;
    private final Constructor<T> constructor;
    private final Parameter[] parameters;

    public ConstructorPluginFactory(final PluginRegistry registry, final Constructor<T> constructor) {
        this.registry = registry;
        this.constructor = constructor;
        this.parameters = constructor.getParameters();
    }

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
                ConfigValue value = getConfigValue(parameter, parent);
                if (value == null) {
                    throw new Todo("Handle optional parameters");
                } else if (parameter.isAnnotationPresent(Plugin.class)) {
                    Class<?> type = parameter.getType();
                    args[i] = registry.create(type, value);
                } else {
                    args[i] = value.get();
                }
            }
        } else if (configValue.isList()) {
            List<ConfigValue> params = configValue.asList();
            throw new Todo("Handle list of arguments");
        } else {
            throw new Todo("Should we accept single arg construction as well?");
        }
        return newInstance(args);
    }

    private T newInstance(final Object... params) {
        try {
            return constructor.newInstance(params);
        } catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
            throw new PluginException(e);
        }
    }

    private static ConfigValue getConfigValue(final Parameter param, final Map<String, ConfigValue> parent) {
        if (param.isNamePresent() && parent.containsKey(param.getName())) {
            return parent.get(param.getName());
        }
        for (Alias alias : param.getDeclaredAnnotationsByType(Alias.class)) {
            if (parent.containsKey(alias.value())) {
                return parent.get(alias.value());
            }
        }
        return null;
    }

}
