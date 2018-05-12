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

package org.musigma.plugin.core.java.util;

public final class Throwables {

    public static boolean isFatal(final Throwable ex) {
        return ex instanceof VirtualMachineError ||
                ex instanceof ThreadDeath ||
                ex instanceof InterruptedException ||
                ex instanceof LinkageError;
    }

    public static void rethrow(final Throwable ex) {
        rethrow0(ex);
    }

    @SuppressWarnings("unchecked")
    private static <T extends Throwable> void rethrow0(final Throwable ex) throws T {
        throw (T) ex;
    }

    public static void rethrowIfFatal(final Throwable ex) {
        if (isFatal(ex)) rethrow(ex);
    }

    private Throwables() {
    }
}
