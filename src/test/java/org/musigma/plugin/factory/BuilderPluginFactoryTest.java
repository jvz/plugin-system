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
import org.musigma.plugin.test.*;
import org.musigma.plugin.config.Config;
import org.musigma.plugin.config.ConfigFactory;
import org.musigma.plugin.registry.BasicPluginRegistry;
import org.musigma.plugin.registry.PluginRegistry;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class BuilderPluginFactoryTest {
    private final PluginRegistry registry = new BasicPluginRegistry();

    @Test
    void createTrivialBuilderPlugin() throws IOException {
        registry.add(TrivialBuilderPlugin.class);
        Config config = ConfigFactory.loadFromContextLoader("trivial.yml");
        TrivialBuilderPlugin plugin = registry.create(TrivialBuilderPlugin.class, config.get("trivial"));
        assertNotNull(plugin);
    }

    @Test
    void createBuilderBasedPlugin() throws IOException {
        registry.add(BuilderBasedPlugin.class);
        Config config = ConfigFactory.loadFromContextLoader("builder.yml");
        BuilderBasedPlugin plugin = registry.create(BuilderBasedPlugin.class, config.get("builder"));
        assertNotNull(plugin);
        assertEquals("Bob", plugin.getName());
        assertEquals("Builds things", plugin.getDescription());
    }

    @Test
    void createNestedBuilderPlugins() throws IOException {
        registry.add(NestedBuilderPlugin.class);
        registry.add(TrivialPlugin.class);
        registry.add(FactoryBasedPlugin.class);
        Config config = ConfigFactory.loadFromContextLoader("nested-builder.yml");
        NestedBuilderPlugin plugin = registry.create(NestedBuilderPlugin.class, config.get("nestedBuilder"));
        assertNotNull(plugin);
        assertEquals("World", plugin.getName());
        assertNotNull(plugin.getTrivial());
        assertEquals(1024, plugin.getSize());
        assertTrue(plugin.isEnabled());
        assertNotNull(plugin.getFactory());
        assertEquals("Hello", plugin.getFactory().getName());
        assertEquals(5, plugin.getFactory().getSize());
    }
}