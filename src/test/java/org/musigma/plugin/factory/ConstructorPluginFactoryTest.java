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
import org.musigma.plugin.test.ConstructorBasedPlugin;
import org.musigma.plugin.test.NestedConstructorPlugin;
import org.musigma.plugin.test.TrivialPlugin;
import org.musigma.plugin.config.Config;
import org.musigma.plugin.config.ConfigFactory;
import org.musigma.plugin.registry.BasicPluginRegistry;
import org.musigma.plugin.registry.PluginRegistry;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ConstructorPluginFactoryTest {
    private final PluginRegistry registry = new BasicPluginRegistry();

    @Test
    void createTrivialPlugin() throws IOException {
        registry.add(TrivialPlugin.class);
        Config config = ConfigFactory.loadFromContextLoader("trivial.yml");
        TrivialPlugin plugin = registry.create(TrivialPlugin.class, config.get("trivial"));
        assertNotNull(plugin);
    }

    @Test
    void createConstructorBasedPlugin() throws IOException {
        registry.add(ConstructorBasedPlugin.class);
        Config config = ConfigFactory.loadFromContextLoader("constructor.yml");
        ConstructorBasedPlugin plugin = registry.create(ConstructorBasedPlugin.class, config.get("constructor"));
        assertNotNull(plugin);
        assertEquals("Hello, world!", plugin.getLine());
    }

    @Test
    void createNestedConstructorPlugin() throws IOException {
        registry.add(NestedConstructorPlugin.class);
        registry.add(TrivialPlugin.class);
        registry.add(ConstructorBasedPlugin.class);
        Config config = ConfigFactory.loadFromContextLoader("nested.yml");
        NestedConstructorPlugin plugin = registry.create(NestedConstructorPlugin.class, config.get("nested"));
        assertNotNull(plugin);
        assertNotNull(plugin.getTrivial());
        assertNotNull(plugin.getCons());
        String line = plugin.getCons().getLine();
        assertEquals("Test contents", line);
    }
}