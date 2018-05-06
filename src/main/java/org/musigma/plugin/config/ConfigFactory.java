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

import org.musigma.plugin.ConfigNotSupportedException;

import java.io.IOException;
import java.util.ServiceLoader;
import java.util.Set;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public interface ConfigFactory {
    static Stream<ConfigFactory> all(final ClassLoader loader) {
        return StreamSupport.stream(ServiceLoader.load(ConfigFactory.class, loader).spliterator(), false);
    }

    static Config loadFromContextLoader(final String name) throws IOException {
        return load(Thread.currentThread().getContextClassLoader(), ConfigSource.fromContextLoader(name));
    }

    static Config load(final ClassLoader loader, final ConfigSource source) throws IOException {
        String filename = source.filename();
        int lastDot = filename.lastIndexOf('.');
        if (lastDot == -1) {
            throw new ConfigNotSupportedException("Provided source does not have a file extension: " + filename);
        }
        String ext = filename.substring(lastDot + 1);
        ConfigFactory factory = all(loader).filter(f -> f.supportedFileExtensions().contains(ext)).findFirst()
                .orElseThrow(() -> new ConfigNotSupportedException("Could not find any matching config factories for file extension " + ext));
        return factory.load(source);
    }

    Config load(ConfigSource source) throws IOException;

    Set<String> supportedFileExtensions();
}
