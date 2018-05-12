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

package org.musigma.plugin.jackson.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.musigma.plugin.api.util.Sets;

import java.util.Set;

public class JsonConfigFactory extends JacksonConfigFactory {

    private static final Set<String> EXTENSIONS = Sets.ofUnmodifiable("json");

    private final ObjectMapper mapper = new ObjectMapper().findAndRegisterModules();

    @Override
    ObjectMapper getObjectMapper() {
        return mapper;
    }

    @Override
    public Set<String> supportedFileExtensions() {
        return EXTENSIONS;
    }
}
