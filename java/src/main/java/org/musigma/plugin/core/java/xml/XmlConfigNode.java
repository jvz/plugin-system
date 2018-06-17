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

import org.musigma.plugin.api.config.ConfigNode;
import org.musigma.plugin.api.type.TypeRef;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

import static org.musigma.plugin.api.config.MissingConfigNode.MISSING;

public class XmlConfigNode implements ConfigNode {
    private final Node node;

    public XmlConfigNode(final Node node) {
        this.node = node;
    }

    @Override
    public int size() {
        return node.getChildNodes().getLength();
    }

    @Override
    public boolean isValueNode() {
        return node instanceof Text || node instanceof Attr;
    }

    @Override
    public boolean isContainerNode() {
        return node instanceof Element;
    }

    @Override
    public boolean isMissingNode() {
        return false;
    }

    @Override
    public boolean isList() {
        return false;
    }

    @Override
    public boolean isRecord() {
        return node instanceof Element;
    }

    @Override
    public boolean hasKey(final String key) {
        for (Node node = this.node.getFirstChild(); node != null; node = node.getNextSibling()) {
            if (node.getNodeName().equals(key)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public ConfigNode get(final String key) {
        for (Node node = this.node.getFirstChild(); node != null; node = node.getNextSibling()) {
            if (node.getNodeName().equals(key)) {
                return new XmlConfigNode(node);
            }
        }
        return MISSING;
    }

    @Override
    public ConfigNode get(final int index) {
        if (index < 0) return MISSING;
        NodeList children = node.getChildNodes();
        return index < children.getLength() ? new XmlConfigNode(children.item(index)) : MISSING;
    }

    @Override
    public Object get() {
        return node.getNodeValue();
    }

    @Override
    public <T> T getAs(final Class<T> clazz) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> T getAs(final TypeRef<T> ref) {
        throw new UnsupportedOperationException();
    }
}
