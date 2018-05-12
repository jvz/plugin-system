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

package org.musigma.plugin.api.config;

import org.junit.jupiter.api.DynamicTest;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

public class ConfigFactoryTestFactory {
    public static Stream<DynamicTest> simpleTests(final ConfigFactory factory) {
        return factory.supportedFileExtensions().stream()
                .map(extension -> "simple." + extension)
                .map(filename -> dynamicTest(factory.getClass().getSimpleName() + ' ' + filename, () -> {
                    ConfigSource source = ConfigSource.fromContextLoader(filename);
                    assertNotNull(source);
                    ConfigNode config = factory.load(source);
                    assertNotNull(config);
                    ConfigNode html = config.get("html");
                    assertNotNull(html);
                    ConfigNode head = html.get("head");
                    assertNotNull(head);
                    ConfigNode title = head.get("title");
                    assertNotNull(title);
                    assertEquals("Hello, world!", title.get());
                    ConfigNode body = html.get("body");
                    assertNotNull(body);
                    assertEquals(2, body.size());
                    ConfigNode body0 = body.get(0);
                    System.out.println(body0);
                    ConfigNode div0 = body0.get("div");
                    assertNotNull(div0);
                    ConfigNode p0 = div0.get("p");
                    assertNotNull(p0);
                    assertEquals("foo", p0.get());
                    ConfigNode div1 = body.get(1).get("div");
                    assertNotNull(div1);
                    ConfigNode p1 = div1.get("p");
                    assertEquals("bar", p1.get());
                }));
    }
}