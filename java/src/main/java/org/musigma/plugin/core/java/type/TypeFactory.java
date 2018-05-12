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

package org.musigma.plugin.core.java.type;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;

public interface TypeFactory {
    default ReifiedType fromType(Type type) {
        if (type instanceof ReifiedType) {
            return (ReifiedType) type;
        }
        if (type instanceof Class<?>) {
            return fromClass((Class<?>) type);
        }
        if (type instanceof ParameterizedType) {
            return fromParamType((ParameterizedType) type);
        }
        if (type instanceof GenericArrayType) {
            return fromArrayType((GenericArrayType) type);
        }
        if (type instanceof TypeVariable<?>) {
            return fromTypeVariable((TypeVariable<?>) type);
        }
        if (type instanceof WildcardType) {
            return fromWildcard((WildcardType) type);
        }
        throw new IllegalArgumentException("Unknown type: " + type.getTypeName());
    }

    ReifiedType fromClass(Class<?> clazz);

    ReifiedType fromParamType(ParameterizedType type);

    ReifiedType fromArrayType(GenericArrayType type);

    ReifiedType fromTypeVariable(TypeVariable<?> variable);

    ReifiedType fromWildcard(WildcardType type);
}
