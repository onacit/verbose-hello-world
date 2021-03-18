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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;

import static java.io.File.createTempFile;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.spy;

/**
 * A class for testing {@link HelloWorld#write(RandomAccessFile)} method.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 */
@Slf4j
class HelloWorld_WriteRandomAccessFileTest extends HelloWorldTest {

    /**
     * Asserts {@link HelloWorld#write(RandomAccessFile)} method throws a {@link NullPointerException} when {@code file}
     * argument is {@code null}.
     */
    @DisplayName("write(file) throws NullPointerException when file is null")
    @Test
    void writeFile_NullPointerException_FileIsNull() {
        assertThrows(NullPointerException.class, () -> helloWorld.write((RandomAccessFile) null));
    }

    /**
     * Asserts {@link HelloWorld#write(RandomAccessFile)} invokes {@link HelloWorld#set(byte[])} method with an array of
     * {@value com.github.jinahya.hello.HelloWorld#BYTES} bytes and invokes {@link RandomAccessFile#write(byte[])}
     * method on {@code file} with the array.
     *
     * @param tempDir a temporary directory to test with.
     * @throws IOException if an I/O error occurs.
     */
    @DisplayName("write(file) invokes set(array) method and writes the array to file")
    @Test
    void appendFile_InvokeWriteStream_(final @TempDir File tempDir) throws IOException {
        final File file = File.createTempFile("tmp", null, tempDir); // <1>
        helloWorld.append(file); // <2>
        final ArgumentCaptor<OutputStream> streamCaptor = ArgumentCaptor.forClass(OutputStream.class); // <3>
        Mockito.verify(helloWorld, Mockito.times(1)).write(streamCaptor.capture()); // <4>
        Assertions.assertTrue(streamCaptor.getValue() instanceof FileOutputStream); // <5>
    }

    /**
     * Asserts {@link HelloWorld#write(RandomAccessFile)} method returns specified {@code file}.
     *
     * @param tempDir a temporary directory to test with.
     * @throws IOException if an I/O error occurs.
     */
    @DisplayName("write(file) returns file")
    @Test
    void appendFile_ReturnFile_(final @TempDir File tempDir) throws IOException {
        final File expected = File.createTempFile("tmp", null, tempDir);
        final File actual = helloWorld.append(expected);
        Assertions.assertSame(expected, actual);
    }
}
