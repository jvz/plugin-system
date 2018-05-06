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

import org.junit.jupiter.api.Test;
import org.musigma.plugin.test.FactoryBasedPlugin;
import org.musigma.plugin.test.NestedFactoryPlugin;
import org.musigma.plugin.test.TrivialFactoryPlugin;
import org.musigma.plugin.config.Config;
import org.musigma.plugin.config.ConfigFactory;
import org.musigma.plugin.registry.BasicPluginRegistry;
import org.musigma.plugin.registry.PluginRegistry;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class FactoryMethodPluginFactoryTest {
    private final PluginRegistry registry = new BasicPluginRegistry();

    @Test
    void createTrivialFactoryPlugin() throws IOException {
        registry.add(TrivialFactoryPlugin.class);
        Config config = ConfigFactory.loadFromContextLoader("trivial-factory.yml");
        TrivialFactoryPlugin plugin = registry.create(TrivialFactoryPlugin.class, config.get("trivialFactory"));
        assertNotNull(plugin);
        assertSame(TrivialFactoryPlugin.instance, plugin);
    }

    @Test
    void createFactoryBasedPlugin() throws IOException {
        registry.add(FactoryBasedPlugin.class);
        Config config = ConfigFactory.loadFromContextLoader("factory.yml");
        FactoryBasedPlugin plugin = registry.create(FactoryBasedPlugin.class, config.get("factory"));
        assertNotNull(plugin);
        assertEquals("Job Factory", plugin.getName());
        assertEquals(100, plugin.getSize());
    }

    @Test
    void createNestedFactoryPlugin() throws IOException {
        registry.add(NestedFactoryPlugin.class);
        registry.add(FactoryBasedPlugin.class);
        registry.add(TrivialFactoryPlugin.class);
        Config config = ConfigFactory.loadFromContextLoader("nested-factory.yml");
        NestedFactoryPlugin plugin = registry.create(NestedFactoryPlugin.class, config.get("nested"));
        assertNotNull(plugin);
        assertSame(TrivialFactoryPlugin.instance, plugin.getTrivial());
        assertEquals("Build Factory", plugin.getFactory().getName());
        assertEquals(100, plugin.getFactory().getSize());
        assertEquals("something", plugin.getSetting());
    }
}