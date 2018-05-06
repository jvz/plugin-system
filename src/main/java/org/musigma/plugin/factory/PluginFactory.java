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

import org.musigma.plugin.annotation.Alias;
import org.musigma.plugin.annotation.Plugin;
import org.musigma.plugin.config.ConfigValue;
import org.musigma.plugin.registry.PluginRegistry;

import java.lang.reflect.Parameter;
import java.util.Map;
import java.util.Optional;

public abstract class PluginFactory<T> {
    private final PluginRegistry registry;

    protected PluginFactory(final PluginRegistry registry) {
        this.registry = registry;
    }

    public abstract T create(ConfigValue configValue);

    protected Optional<Object> getParameterValue(final Parameter parameter, final Map<String, ConfigValue> environment) {
        return getConfigValue(parameter, environment)
                .map(value -> parameter.isAnnotationPresent(Plugin.class) ? registry.create(parameter.getType(), value) : value.get());
    }

    private static Optional<ConfigValue> getConfigValue(final Parameter parameter, final Map<String, ConfigValue> environment) {
        if (parameter.isNamePresent() && environment.containsKey(parameter.getName())) {
            return Optional.ofNullable(environment.get(parameter.getName()));
        }
        for (Alias alias : parameter.getAnnotationsByType(Alias.class)) {
            if (environment.containsKey(alias.value())) {
                return Optional.ofNullable(environment.get(alias.value()));
            }
        }
        return Optional.empty();
    }
}
