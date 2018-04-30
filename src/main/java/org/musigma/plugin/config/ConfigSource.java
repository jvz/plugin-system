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

import java.io.*;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Enumeration;

public final class ConfigSource {
    private final String filename;
    private final InputStreamSource source;

    private ConfigSource(final String filename, final InputStreamSource source) {
        this.filename = filename;
        this.source = source;
    }

    public static ConfigSource from(final Path path) {
        return new ConfigSource(path.getFileName().toString(), () -> Files.newInputStream(path));
    }

    public static ConfigSource from(final File file) {
        return new ConfigSource(file.getName(), () -> new FileInputStream(file));
    }

    public static ConfigSource from(final URI uri) {
        return from(Paths.get(uri));
    }

    public static ConfigSource from(final String first, final String... more) {
        return from(Paths.get(first, more));
    }

    public static ConfigSource fromClassResource(final Class<?> clazz, final String name) {
        return fromClassLoader(clazz.getClassLoader(), name);
    }

    public static ConfigSource fromClassLoader(final ClassLoader loader, final String name) {
        int lastDirectory = name.lastIndexOf('/');
        String filename = lastDirectory == -1 ? name : name.substring(lastDirectory + 1);
        return new ConfigSource(filename, () -> loader.getResourceAsStream(name));
    }

    public static ConfigSource fromContextLoader(final String name) throws IOException {
        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
        Enumeration<URL> resources = contextClassLoader.getResources(name);
        if (resources.hasMoreElements()) {
            URL url = resources.nextElement();
            String path = url.getPath();
            int lastDirectory = path.lastIndexOf('/');
            String filename = lastDirectory == -1 ? path : path.substring(lastDirectory + 1);
            return new ConfigSource(filename, url::openStream);
        }
        throw new FileNotFoundException(name);
    }

    public InputStream open() throws IOException {
        return source.open();
    }

    public String filename() {
        return filename;
    }
}
