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
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;

import java.util.concurrent.ThreadLocalRandom;

import static org.mockito.quality.Strictness.LENIENT;

/**
 * A class for unit-testing {@link HelloWorld#set(byte[])} method.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 */
@MockitoSettings(strictness = LENIENT)
@ExtendWith({MockitoExtension.class})
@Slf4j
class HelloWorld_SetArrayTest extends HelloWorldTest {

    /**
     * Asserts {@link HelloWorld#set(byte[])} method throws a {@link NullPointerException} when the {@code array}
     * argument is {@code null}.
     */
    @DisplayName("set(null) throws NullPointerException")
    @Test
    void setArray_NullPointerException_ArrayIsNull() {
        Assertions.assertThrows(NullPointerException.class, () -> helloWorld.set(null));
    }

    /**
     * Asserts {@link HelloWorld#set(byte[])} method throws an {@link IndexOutOfBoundsException} when {@code
     * array.length} is less than {@link HelloWorld#BYTES}.
     */
    @DisplayName("set(array) throws IndexOutOfBoundsException when array.length is less than BYTES")
    @Test
    void setArray_IndexOutOfBoundsException_ArrayLengthIsLessThanBYTES() {
        final byte[] array = new byte[ThreadLocalRandom.current().nextInt(0, HelloWorld.BYTES)];
        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> helloWorld.set(array));
    }

    /**
     * Asserts {@link HelloWorld#set(byte[])} method invokes {@link HelloWorld#set(byte[], int)} method with given
     * {@code array} and {@code 0} and returns the result.
     */
    @DisplayName("set(array) invokes set(array, 0)")
    @Test
    void setArray_InvokesSetArrayWithArrayAndZero() {
        final ArgumentCaptor<byte[]> arrayCaptor = ArgumentCaptor.forClass(byte[].class); // <1>
        final ArgumentCaptor<Integer> indexCaptor = ArgumentCaptor.forClass(int.class); // <2>
        final byte[] array = new byte[HelloWorld.BYTES]; // <3>
        helloWorld.set(array); // <4>
        Mockito.verify(helloWorld, Mockito.times(1)).set(arrayCaptor.capture(), indexCaptor.capture()); // <5>
        Assertions.assertSame(array, arrayCaptor.getValue()); // <6>
        Assertions.assertEquals(0, indexCaptor.getValue()); // <7>
    }

    /**
     * Asserts {@link HelloWorld#set(byte[])} method returns given {@code array}.
     */
    @DisplayName("set(array) returns array")
    @Test
    void setArray_ReturnArray_() {
        final byte[] expected = new byte[HelloWorld.BYTES];
        final byte[] actual = helloWorld.set(expected);
        Assertions.assertSame(expected, actual);
    }
}
