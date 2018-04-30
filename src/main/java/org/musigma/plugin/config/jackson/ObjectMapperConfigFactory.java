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

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.musigma.plugin.config.Config;
import org.musigma.plugin.config.ConfigFactory;
import org.musigma.plugin.config.ConfigSource;
import org.musigma.plugin.config.ConfigValue;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public abstract class ObjectMapperConfigFactory implements ConfigFactory {

    private static final TypeReference<Map<String, Object>> MAP_REF = new TypeReference<Map<String, Object>>() {};

    abstract ObjectMapper getObjectMapper();

    @Override
    public Config load(final ConfigSource source) throws IOException {
        Map<String, Object> contents = getObjectMapper().readValue(source.open(), MAP_REF);
        Map<String, ConfigValue> values = new HashMap<>(contents.size());
        for (Map.Entry<String, Object> entry : contents.entrySet()) {
            values.put(entry.getKey(), ConfigValue.of(entry.getValue()));
        }
        return new Config(values);
    }

}
