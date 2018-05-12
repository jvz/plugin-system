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

import org.musigma.plugin.core.java.util.ISet;

import java.lang.reflect.TypeVariable;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public final class TypeParams {
    private static final String[] EMPTY_STRING = new String[0];
    private static final ReifiedType[] EMPTY_TYPE = new ReifiedType[0];
    private static final TypeParams EMPTY = new TypeParams(EMPTY_STRING, EMPTY_TYPE, null);

    private final String[] names;
    private final ReifiedType[] params;
    private final ISet<String> unbound;
    private final int hashCode;

    private TypeParams(final String[] names, final ReifiedType[] params, final ISet<String> unbound) {
        this.names = names == null ? EMPTY_STRING : names;
        this.params = params == null ? EMPTY_TYPE : params;
        if (this.names.length != this.params.length) throw new IllegalArgumentException("Names and params have different lengths");
        this.unbound = unbound == null ? ISet.empty(String[]::new) : unbound;
        int hashCode = Objects.hash(unbound);
        hashCode = 31 * hashCode + Arrays.hashCode(names);
        hashCode = 31 * hashCode + Arrays.hashCode(params);
        this.hashCode = hashCode;
    }

    public int paramCount() {
        return names.length;
    }

    public String paramName(final int index) {
        return names[index];
    }

    public ReifiedType paramType(final int index) {
        return params[index];
    }

    public boolean hasUnboundParam(final String name) {
        return unbound.contains(name);
    }

    public TypeParams withUnboundParam(final String name) {
        return new TypeParams(names, params, unbound.add(name));
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final TypeParams that = (TypeParams) o;
        return Arrays.equals(names, that.names) &&
                Arrays.equals(params, that.params) &&
                Objects.equals(unbound, that.unbound);
    }

    @Override
    public int hashCode() {
        return hashCode;
    }

    public static TypeParams empty() {
        return EMPTY;
    }

    public static TypeParams create(final Class<?> rawType, final List<ReifiedType> params) {
        ReifiedType[] types = params == null || params.isEmpty() ? EMPTY_TYPE : params.toArray(new ReifiedType[0]);
        return create(rawType, types);
    }

    public static TypeParams create(final Class<?> rawType, final ReifiedType[] params) {
        TypeVariable<? extends Class<?>>[] typeParameters = rawType.getTypeParameters();
        String[] names;
        if (typeParameters == null || typeParameters.length == 0) {
            names = EMPTY_STRING;
        } else {
            names = new String[typeParameters.length];
            for (int i = 0; i < typeParameters.length; i++) {
                names[i] = typeParameters[i].getName();
            }
        }
        return new TypeParams(names, params == null ? EMPTY_TYPE : params, null);
    }
}
