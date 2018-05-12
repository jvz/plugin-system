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
import org.musigma.plugin.api.Todo;
import org.musigma.plugin.api.config.ConfigNode;
import org.musigma.plugin.api.factory.PluginFactory;
import org.musigma.plugin.api.registry.PluginRegistry;
import org.musigma.plugin.core.java.util.Parameters;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;

import static org.musigma.plugin.core.java.util.Throwables.rethrowIfFatal;

public class BuilderPluginFactory<T> implements PluginFactory<T> {
    private final PluginRegistry registry;
    private final Constructor<?> builderConstructor;
    private final Parameter[] requiredParameters;
    private final Method build;
    private final Method[] methods;

    public BuilderPluginFactory(final PluginRegistry registry, final Class<?> builderClass) {
        this.registry = registry;
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
            if (method.getDeclaringClass() == Object.class || method.getParameterCount() == 0) continue;
            methods.add(method);
        }
        this.methods = methods.toArray(new Method[0]);
    }

    @Override
    public T create(final ConfigNode node) {
        Object[] args = new Object[requiredParameters.length];
        if (node.isRecord()) {
            for (int i = 0; i < requiredParameters.length; i++) {
                Parameter parameter = requiredParameters[i];
                args[i] = Parameters.createPlugin(registry, node, parameter);
            }
        } else if (node.isList()) {
            throw new Todo("Handle list of arguments");
        } else {
            throw new Todo("Handle single argument");
        }
        Object builder = newBuilder(args);
        if (node.isRecord()) {
            for (Method method : methods) {
                Parameter parameter = method.getParameters()[0];
                ConfigNode arg = Parameters.getConfig(node, parameter);
                if (!arg.isMissingNode()) {
                    Object value = Parameters.createPlugin(registry, arg, parameter);
                    try {
                        method.invoke(builder, value);
                    } catch (Throwable e) {
                        rethrowIfFatal(e);
                        throw new PluginException(String.format("Could not invoke builder method %s on instance %s", method.getName(), builder), e);
                    }
                }
            }
        } else if (node.isList()) {
            throw new Todo("Handle list arguments?");
        } else {
            throw new Todo("Handle single arguments?");
        }
        return build(builder);
    }

    private Object newBuilder(final Object... params) {
        try {
            return builderConstructor.newInstance(params);
        } catch (Throwable e) {
            rethrowIfFatal(e);
            throw new PluginException(e);
        }
    }

    @SuppressWarnings("unchecked")
    private T build(final Object builder) {
        try {
            return (T) build.invoke(builder);
        } catch (Throwable e) {
            rethrowIfFatal(e);
            throw new PluginException(e);
        }
    }
}
