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

import com.fasterxml.jackson.databind.JsonNode;
import org.musigma.plugin.api.config.ConfigNode;
import org.musigma.plugin.api.config.MissingConfigNode;

public class JacksonConfigNode implements ConfigNode {
    private final JsonNode node;

    public JacksonConfigNode(final JsonNode node) {
        this.node = node;
    }

    @Override
    public int size() {
        return node.size();
    }

    @Override
    public boolean isValueNode() {
        return node.isValueNode();
    }

    @Override
    public boolean isContainerNode() {
        return node.isContainerNode();
    }

    @Override
    public boolean isMissingNode() {
        return node.isMissingNode();
    }

    @Override
    public boolean isList() {
        return node.isArray();
    }

    @Override
    public boolean isRecord() {
        return node.isObject();
    }

    @Override
    public boolean hasKey(final String key) {
        return node.has(key);
    }

    @Override
    public ConfigNode get(final String key) {
        JsonNode path = node.path(key);
        return path.isMissingNode() ? MissingConfigNode.INSTANCE : new JacksonConfigNode(path);
    }

    @Override
    public ConfigNode get(final int index) {
        JsonNode path = node.path(index);
        return path.isMissingNode() ? MissingConfigNode.INSTANCE : new JacksonConfigNode(path);
    }

    @Override
    public Object get() {
        if (!node.isValueNode()) {
            throw new UnsupportedOperationException();
        } else if (node.isNumber()) {
            return node.numberValue();
        } else if (node.isBoolean()) {
            return node.booleanValue();
        } else {
            return node.asText();
        }
    }
}
