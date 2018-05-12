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

package org.musigma.plugin.api.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a class as a plugin or indicates that a parameter or method should be injected with a matching instance.
 *
 * If a class is marked @Plugin, then it is registered as a plugin. Inside that class, it must provide either a
 * static method annotated with @Factory, a constructor annotated with @Factory, or an inner static class
 * annotated with @Factory.
 *
 * If a parameter inside a plugin factory method or constructor is annotated with @Plugin, then the parameter is
 * expected to be a nested plugin. (TODO: allow plugin-refs? possible annotation?)
 *
 * If a method or parameter of a method inside a builder class is annotated with @Plugin, then that method or parameter
 * is injected with the nested plugin.
 */
@Target({ElementType.TYPE, ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface Plugin {
}
