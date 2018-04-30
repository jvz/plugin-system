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

package org.musigma.plugin.config;

import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.musigma.plugin.config.jackson.ObjectMapperConfigFactory;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

class ConfigFactoryTest {

    @TestFactory
    Stream<DynamicTest> configFactoryProviders() {
        return ConfigFactory.all(ObjectMapperConfigFactory.class.getClassLoader())
                .flatMap(factory -> factory.supportedFileExtensions().stream().map(extension -> dynamicTest(extension, () -> {
                    ConfigSource source = ConfigSource.fromContextLoader("simple." + extension);
                    Config config = factory.load(source);
                    String title = config.get("head").get("title").getAs(String.class);
                    assertEquals("Hello, world!", title);
                    List<ConfigValue> body = config.get("body").asList();
                    assertEquals(2, body.size());
                    String p0 = body.get(0).get("div").get("p").getAs(String.class);
                    assertEquals("foo", p0);
                    String p1 = body.get(1).get("div").get("p").getAs(String.class);
                    assertEquals("bar", p1);
                })));
    }

}