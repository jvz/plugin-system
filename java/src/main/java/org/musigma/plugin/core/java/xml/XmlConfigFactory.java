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

package org.musigma.plugin.core.java.xml;

import org.musigma.plugin.api.ConfigException;
import org.musigma.plugin.api.config.ConfigFactory;
import org.musigma.plugin.api.config.ConfigNode;
import org.musigma.plugin.api.config.ConfigSource;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.Collections;
import java.util.Set;

public class XmlConfigFactory implements ConfigFactory {
    private final DocumentBuilder documentBuilder;

    public XmlConfigFactory() {
        this(DocumentBuilderFactory.newInstance());
    }

    public XmlConfigFactory(final DocumentBuilderFactory factory) {
        try {
            factory.setXIncludeAware(false);
            factory.setExpandEntityReferences(false);
            factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
            documentBuilder = factory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            throw new ConfigException(e);
        }
    }


    @Override
    public ConfigNode load(final ConfigSource source) throws IOException {
        try {
            Document document = documentBuilder.parse(source.open());
            Node root = document.getDocumentElement();
            return new XmlConfigNode(root);
        } catch (SAXException e) {
            throw new ConfigException(e);
        }
    }

    @Override
    public Set<String> supportedFileExtensions() {
        return Collections.singleton("xml");
    }
}
