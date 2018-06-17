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

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.concurrent.Callable;

public abstract class TypeRef<T> implements Callable<T> {
    private final Type type;
    private final Class<? super T> rawType;
    private final int hashCode;

    @SuppressWarnings("unchecked")
    protected TypeRef() {
        Type superclass = getClass().getGenericSuperclass();
        if (superclass instanceof Class<?>) {
            throw new IllegalArgumentException("No type parameter given");
        }
        type = ((ParameterizedType) superclass).getActualTypeArguments()[0];
        rawType = (Class<? super T>) Types.getRawType(type);
        hashCode = type.hashCode();
    }

    @Override
    public T call() throws Exception {
        return null;
    }

    public Type type() {
        return type;
    }

    public Class<? super T> rawType() {
        return rawType;
    }

    @Override
    public boolean equals(final Object o) {
        return o instanceof TypeRef<?> && Types.equals(type, ((TypeRef<?>) o).type);
    }

    @Override
    public int hashCode() {
        return hashCode;
    }

    @Override
    public String toString() {
        return "TypeRef<" + type + '>';
    }
}
