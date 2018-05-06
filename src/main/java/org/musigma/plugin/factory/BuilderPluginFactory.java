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
import org.musigma.plugin.config.ConfigValue;
import org.musigma.plugin.registry.PluginRegistry;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class BuilderPluginFactory<T> extends PluginFactory<T> {
    private final Constructor<?> builderConstructor;
    private final Parameter[] requiredParameters;
    private final Method build;
    private final Method[] methods;

    public BuilderPluginFactory(final PluginRegistry registry, final Class<?> builderClass) {
        super(registry);
        Constructor<?>[] constructors = builderClass.getConstructors();
        if (constructors.length != 1) {
            // TODO: support @Factory to indicate which one is the proper one?
            throw new PluginException("Plugin builder can only have one constructor");
        }
        builderConstructor = constructors[0];
        requiredParameters = builderConstructor.getParameters();
        try {
            build = builderClass.getMethod("build");
        } catch (NoSuchMethodException e) {
            throw new PluginException("No build method found on builder class", e);
        }
        List<Method> methods = new ArrayList<>();
        for (Method method : builderClass.getMethods()) {
            if (method.getDeclaringClass() == Object.class) continue;
            if (method.getName().equals("build") && method.getParameterCount() == 0) continue;
            methods.add(method);
        }
        this.methods = methods.toArray(new Method[0]);
    }

    @Override
    public T create(final ConfigValue configValue) {
        Object[] args = new Object[requiredParameters.length];
        if (configValue.isMap()) {
            Map<String, ConfigValue> parent = configValue.asMap();
            for (int i = 0; i < requiredParameters.length; i++) {
                Parameter parameter = requiredParameters[i];
                args[i] = getParameterValue(parameter, parent).orElseThrow(() -> new Todo("Handle missing arguments"));
            }
        } else if (configValue.isList()) {
            throw new Todo("Handle list of arguments");
        } else {
            throw new Todo("Handle single argument");
        }
        Object builder = newBuilder(args);
        if (configValue.isMap()) {
            Map<String, ConfigValue> parent = configValue.asMap();
            for (Method method : methods) {
                if (method.getDeclaringClass() != Object.class && method.getParameterCount() == 1) {
                    Parameter parameter = method.getParameters()[0];
                    Optional<Object> arg = getParameterValue(parameter, parent);
                    if (arg.isPresent()) {
                        try {
                            method.invoke(builder, arg.get());
                        } catch (IllegalAccessException | InvocationTargetException e) {
                            throw new PluginException(e);
                        }
                    }
                }
            }
        } else if (configValue.isList()) {
            throw new Todo("Handle list arguments?");
        } else {
            throw new Todo("Handle single arguments?");
        }
        return build(builder);
    }

    private Object newBuilder(final Object... params) {
        try {
            return builderConstructor.newInstance(params);
        } catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
            throw new PluginException(e);
        }
    }

    @SuppressWarnings("unchecked")
    private T build(final Object builder) {
        try {
            return (T) build.invoke(builder);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new PluginException(e);
        }
    }
}
