package com.github.jinahya.hello;

/*-
 * #%L
 * verbose-hello-world-lib
 * %%
 * Copyright (C) 2018 - 2019 Jinahya, Inc.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

<<<<<<< HEAD
=======
import java.util.Iterator;
import java.util.ServiceLoader;

>>>>>>> sketch
/**
 * A class for testing {@link HelloWorldImpl} using Service Provider Interface.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 */
class HelloWorldSpiTest extends AbstractHelloWorldTest {

    // -----------------------------------------------------------------------------------------------------------------
    @Override
    HelloWorld helloWorld() {
<<<<<<< HEAD
        // TODO: implement!
        return null;
=======
        final ServiceLoader<HelloWorld> loader = ServiceLoader.load(HelloWorld.class);
        final Iterator<HelloWorld> iterator = loader.iterator();
        while (iterator.hasNext()) {
            return iterator.next();
        }
        throw new RuntimeException("no provider for valid instances");
>>>>>>> sketch
    }
}
