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
import javax.inject.Inject;
import javax.inject.Named;
=======
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import javax.inject.Named;
import java.lang.reflect.Field;
import java.util.List;

import static java.util.Arrays.stream;
import static java.util.Collections.shuffle;
import static java.util.stream.Collectors.toList;
import static org.junit.jupiter.api.Assertions.assertEquals;
>>>>>>> sketch

/**
 * An abstract class for testing {@link HelloWorld} implementations using Dependency Injection.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 */
<<<<<<< HEAD
=======
@Slf4j
>>>>>>> sketch
public abstract class HelloWorldDiTest extends AbstractHelloWorldTest {

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * An injection qualifier for {@link HelloWorldDemo}.
     */
    static final String DEMO = "demo";

    /**
     * An injection qualifier for {@link HelloWorldImpl}.
     */
    static final String IMPL = "impl";

    // -----------------------------------------------------------------------------------------------------------------
    @Override
    HelloWorld helloWorld() {
<<<<<<< HEAD
        // TODO: implement!
        return null;
=======
        final List<Field> fields
                = stream(HelloWorldDiTest.class.getDeclaredFields())   // <1>
                .filter(f -> f.getType() == HelloWorld.class           // <2>
                             && f.getAnnotation(Inject.class) != null)
                .collect(toList());                                    // <3>
        assertEquals(4, fields.size());
        shuffle(fields);
        final Field field = fields.get(0);
        if (!field.isAccessible()) {
            field.setAccessible(true);
        }
        try {
            return (HelloWorld) field.get(this);
        } catch (final IllegalAccessException iae) {
            throw new RuntimeException(iae);
        }
>>>>>>> sketch
    }

    // -----------------------------------------------------------------------------------------------------------------
    @Inject
    @Named(DEMO)
    HelloWorld namedDemo;

    @Inject
    @Named(IMPL)
    HelloWorld namedImpl;

    @Inject
    @QualifiedDemo
    HelloWorld qualifiedDemo;

    @Inject
    @QualifiedImpl
    HelloWorld qualifiedImpl;
}
