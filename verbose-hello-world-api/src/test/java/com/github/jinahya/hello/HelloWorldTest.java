package com.github.jinahya.hello;

/*-
 * #%L
 * verbose-hello-world-api
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

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

/**
 * An abstract class for unit-testing {@link HelloWorld} interface.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 */
@Slf4j
abstract class HelloWorldTest {

    /**
     * Stubs {@link HelloWorld#set(byte[], int)} method of {@link #helloWorld} instance just to return given {@code
     * array} argument.
     */
    @BeforeEach
    private void stubSetArrayIndexToReturnGivenArray() {
        when(helloWorld.set(any(byte[].class), anyInt()))  // <1>
                .thenAnswer(i -> i.getArgument(0, byte[].class)); // <2>
    }

    /**
     * A spy instance of {@link HelloWorld} interface.
     */
    protected final HelloWorld helloWorld = spy(HelloWorld.class);
}
