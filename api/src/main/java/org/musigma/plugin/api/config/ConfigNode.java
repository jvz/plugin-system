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

public interface ConfigNode {
    int size();

    boolean isValueNode();

    boolean isContainerNode();

    boolean isMissingNode();

    boolean isList();

    boolean isRecord();

    boolean hasKey(String key);

    ConfigNode get(String key);

    ConfigNode get(int index);

    Object get();

//    default ConfigNode get(final Parameter parameter) {
//        if (parameter.isNamePresent() && hasKey(parameter.getName())) {
//            return get(parameter.getName());
//        }
//        for (Alias alias : parameter.getAnnotationsByType(Alias.class)) {
//            if (hasKey(alias.value())) {
//                return get(alias.value());
//            }
//        }
//        return MissingConfigNode.INSTANCE;
//    }

}
