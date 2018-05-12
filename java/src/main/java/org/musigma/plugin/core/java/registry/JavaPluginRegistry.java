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

package org.musigma.plugin.core.java.registry;

import org.musigma.plugin.api.PluginNotFoundException;
import org.musigma.plugin.api.annotation.Alias;
import org.musigma.plugin.api.annotation.Factory;
import org.musigma.plugin.api.config.ConfigNode;
import org.musigma.plugin.api.factory.PluginFactory;
import org.musigma.plugin.api.registry.PluginRegistry;
import org.musigma.plugin.core.java.factory.BuilderPluginFactory;
import org.musigma.plugin.core.java.factory.ConstructorPluginFactory;
import org.musigma.plugin.core.java.factory.FactoryMethodPluginFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class JavaPluginRegistry implements PluginRegistry {

    private final Map<Class<?>, PluginFactory<?>> byTypes = new ConcurrentHashMap<>();
    private final Map<String, PluginFactory<?>> byNames = new ConcurrentHashMap<>();

    @Override
    public void add(final Class<?> type) {
        PluginFactory<?> factory = factoryOf(type);
        byTypes.put(type, factory);
        byNames.put(type.getName(), factory);
        for (final Alias alias : type.getDeclaredAnnotationsByType(Alias.class)) {
            byNames.put(alias.value(), factory);
        }
    }

    private <T> PluginFactory<T> factoryOf(final Class<T> type) {
        for (Constructor<?> constructor : type.getDeclaredConstructors()) {
            @SuppressWarnings("unchecked")
            Constructor<T> c = ((Constructor<T>) constructor);
            if (c.isAnnotationPresent(Factory.class)) {
                return new ConstructorPluginFactory<>(this, c);
            }
        }
        for (Method method : type.getDeclaredMethods()) {
            if (Modifier.isStatic(method.getModifiers()) && method.isAnnotationPresent(Factory.class)) {
                return new FactoryMethodPluginFactory<>(this, method);
            }
        }
        for (Class<?> innerClass : type.getDeclaredClasses()) {
            if (Modifier.isStatic(innerClass.getModifiers()) && innerClass.isAnnotationPresent(Factory.class)) {
                return new BuilderPluginFactory<>(this, innerClass);
            }
        }
        throw new IllegalArgumentException("The provided class " + type.getName() + " does not provide any @Factory");
    }

    @Override
    public <T> T create(final Class<T> type, final ConfigNode node) {
        if (!byTypes.containsKey(type)) {
            throw new PluginNotFoundException(type);
        }
        @SuppressWarnings("unchecked")
        PluginFactory<T> factory = (PluginFactory<T>) byTypes.get(type);
        return factory.create(node);
    }

    @Override
    public Object create(final String name, final ConfigNode node) {
        if (!byNames.containsKey(name)) {
            throw new PluginNotFoundException(name);
        }
        return byNames.get(name).create(node);
    }

}
