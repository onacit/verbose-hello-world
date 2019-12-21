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

/**
 * A class implements the {@link HelloWorld} interface.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 */
public class HelloWorldImpl implements HelloWorld {

    // -----------------------------------------------------------------------------------------------------------------
    @Override
<<<<<<< HEAD
    public byte[] set(final byte[] array, int index) {
        // TODO: implement!
        return null;
=======
    public byte[] set(final byte[] array, final int index) {
        if (array == null) {
            throw new NullPointerException("array is null");
        }
        if (index < 0) {
            throw new ArrayIndexOutOfBoundsException("index(" + index + ") < 0");
        }
        if (index + BYTES > array.length) {
            throw new ArrayIndexOutOfBoundsException(
                    "index(" + index + ") + " + BYTES + " > array.length(" + array.length + ")");
        }
        array[index + 000] = 0x68; // 'h'
        array[index + 001] = 0x65; // 'e'
        array[index + 002] = 0x6C; // 'l'
        array[index + 003] = 0x6C; // 'l'
        array[index + 004] = 0x6F; // 'o'
        array[index + 005] = 0x2C; // ','
        array[index + 006] = 0x20; // ' '
        array[index + 007] = 0x77; // 'w'
        array[index + 010] = 0x6F; // 'o'
        array[index + 011] = 0x72; // 'r'
        array[index + 012] = 0x6C; // 'l'
        array[index + 013] = 0x64; // 'd'
        return array;
>>>>>>> sketch
    }
}
