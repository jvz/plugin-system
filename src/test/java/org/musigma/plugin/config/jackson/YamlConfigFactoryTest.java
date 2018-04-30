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

package org.musigma.plugin.config.jackson;

import org.junit.jupiter.api.Test;
import org.musigma.plugin.config.Config;
import org.musigma.plugin.config.ConfigSource;
import org.musigma.plugin.config.ConfigValue;

import java.io.IOException;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class YamlConfigFactoryTest {

    private final YamlConfigFactory factory = new YamlConfigFactory();

    @Test
    void loadSimpleConfig() throws IOException {
        Config config = factory.load(ConfigSource.fromContextLoader("config-using-aliases.yml"));
        assertNotNull(config);
        ConfigValue<?> plugins = config.get("plugins");
        assertNotNull(plugins);

        ConfigValue<Map<String, ConfigValue<?>>> p0 = plugins.get(0);
        assertNotNull(p0);
        assertEquals(1, p0.get().size());
        ConfigValue<?> builder = p0.get("builder");
        assertNotNull(builder);
        ConfigValue<String> name = builder.get("name");
        assertEquals("James Bond", name.get());
        ConfigValue<String> description = builder.get("description");
        assertEquals("Secret agent", description.get());

        ConfigValue<Map<String, ConfigValue<?>>> p1 = plugins.get(1);
        assertNotNull(p1);
        assertEquals(1, p1.get().size());
        ConfigValue<?> factory = p1.get("factory");
        assertNotNull(factory);
        ConfigValue<String> fName = factory.get("name");
        assertEquals("Universe", fName.get());
        ConfigValue<Integer> age = factory.get("size");
        assertEquals(42, age.get().intValue());
    }
}