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

import com.fasterxml.jackson.core.type.TypeReference;
import org.musigma.plugin.api.type.TypeRef;

import java.lang.reflect.Type;

class TypeReferenceAdapter<T> extends TypeReference<T> {
    private final TypeRef<T> ref;

    TypeReferenceAdapter(final TypeRef<T> ref) {
        this.ref = ref;
    }

    @Override
    public Type getType() {
        return ref.type();
    }
}
