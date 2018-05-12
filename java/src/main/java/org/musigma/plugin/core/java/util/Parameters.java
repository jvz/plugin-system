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

package org.musigma.plugin.core.java.util;

import org.musigma.plugin.api.Todo;
import org.musigma.plugin.api.annotation.Alias;
import org.musigma.plugin.api.annotation.Plugin;
import org.musigma.plugin.api.config.ConfigNode;
import org.musigma.plugin.api.config.MissingConfigNode;
import org.musigma.plugin.api.registry.PluginRegistry;

import java.lang.reflect.Parameter;

public final class Parameters {

    public static ConfigNode getConfig(final ConfigNode node, final Parameter param) {
        if (param.isNamePresent() && node.hasKey(param.getName())) {
            return node.get(param.getName());
        }
        for (Alias alias : param.getAnnotationsByType(Alias.class)) {
            if (node.hasKey(alias.value())) {
                return node.get(alias.value());
            }
        }
        return MissingConfigNode.INSTANCE;
    }

    public static Object createPlugin(final PluginRegistry registry, final ConfigNode node, final Parameter param) {
        ConfigNode value = getConfig(node, param);
        if (value.isMissingNode()) return null;
        Object plugin = param.isAnnotationPresent(Plugin.class) ? registry.create(param.getType(), value) : value.get();
        if (param.getType().isInstance(plugin)) {
            return plugin;
        } else {
            throw new Todo(String.format("Type conversion from %s to %s", plugin.getClass(), param.getType()));
        }
    }

    private Parameters() {
    }
}
