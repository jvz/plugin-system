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

package org.musigma.plugin.api.type;

import java.lang.reflect.Array;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.Arrays;
import java.util.Objects;

class Types {

    static Class<?> getRawType(Type type) {
        if (type instanceof Class<?>) {
            return (Class<?>) type;
        } else if (type instanceof ParameterizedType) {
            Type rawType = ((ParameterizedType) type).getRawType();
            if (rawType instanceof Class<?>) {
                return (Class<?>) rawType;
            } else {
                throw new IllegalArgumentException("Expected a class but found type: " + type);
            }
        } else if (type instanceof GenericArrayType) {
            Type componentType = ((GenericArrayType) type).getGenericComponentType();
            return Array.newInstance(getRawType(componentType), 0).getClass();
        } else if (type instanceof TypeVariable<?> || type instanceof WildcardType) {
            return Object.class;
        } else {
            throw new IllegalArgumentException(type.toString());
        }
    }

    static boolean equals(Type a, Type b) {
        if (a == b) return true;
        if (a instanceof Class<?>) return a.equals(b);

        if (a instanceof ParameterizedType) {
            if (!(b instanceof ParameterizedType)) return false;
            ParameterizedType pA = (ParameterizedType) a;
            ParameterizedType pB = (ParameterizedType) b;
            return Objects.equals(pA.getOwnerType(), pB.getOwnerType())
                    && Objects.equals(pA.getRawType(), pB.getRawType())
                    && Arrays.equals(pA.getActualTypeArguments(), pB.getActualTypeArguments());
        }

        if (a instanceof GenericArrayType) {
            if (!(b instanceof GenericArrayType)) return false;
            GenericArrayType gA = (GenericArrayType) a;
            GenericArrayType gB = (GenericArrayType) b;
            return equals(gA.getGenericComponentType(), gB.getGenericComponentType());
        }

        if (a instanceof WildcardType) {
            if (!(b instanceof WildcardType)) return false;
            WildcardType wA = (WildcardType) a;
            WildcardType wB = (WildcardType) b;
            return Arrays.equals(wA.getUpperBounds(), wB.getUpperBounds())
                    && Arrays.equals(wA.getLowerBounds(), wB.getLowerBounds());
        }

        if (a instanceof TypeVariable<?>) {
            if (!(b instanceof TypeVariable<?>)) return false;
            TypeVariable<?> tA = (TypeVariable<?>) a;
            TypeVariable<?> tB = (TypeVariable<?>) b;
            return Objects.equals(tA.getGenericDeclaration(), tB.getGenericDeclaration())
                    && Objects.equals(tA.getName(), tB.getName());
        }

        return false;
    }

    private Types() {
    }
}
