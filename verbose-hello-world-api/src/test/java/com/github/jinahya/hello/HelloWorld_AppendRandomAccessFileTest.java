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
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * A class for testing {@link HelloWorld#append(RandomAccessFile)} method.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 */
//@MockitoSettings(strictness = LENIENT)
@ExtendWith({MockitoExtension.class})
@Slf4j
class HelloWorld_AppendRandomAccessFileTest extends HelloWorldTest {

    /**
     * Asserts {@link HelloWorld#append(RandomAccessFile)} method throws a {@link NullPointerException} when {@code
     * file} argument is {@code null}.
     */
    @DisplayName("append(file) throws NullPointerException when file is null")
    @Test
    void appendFile_NullPointerException_FileIsNull() {
        assertThrows(NullPointerException.class, () -> helloWorld.append((RandomAccessFile) null));
    }

    /**
     * Asserts {@link HelloWorld#append(RandomAccessFile)} moves the file pointer to the end of the file and invokes
     * {@link HelloWorld#write(RandomAccessFile)} method with specified {@code file} argument.
     *
     * @param tempDir a temporary directory to test with.
     * @throws IOException if an I/O error occurs.
     */
    @DisplayName("append(file) invokes set(array) method and writes the array to file")
    @Test
    void writeFile_InvokeSetArrayWriteArrayToFile_(final @TempDir File tempDir) throws IOException {
        //final RandomAccessFile file = spy(new RandomAccessFile(File.createTempFile("tmp", null, tempDir), "rw"));
        final RandomAccessFile file = mock(RandomAccessFile.class);
        helloWorld.write(file);
        final ArgumentCaptor<byte[]> arrayCaptor1 = ArgumentCaptor.forClass(byte[].class);
        verify(helloWorld, times(1)).set(arrayCaptor1.capture());
        assertEquals(HelloWorld.BYTES, arrayCaptor1.getValue().length);
        final ArgumentCaptor<byte[]> arrayCaptor2 = ArgumentCaptor.forClass(byte[].class);
        verify(file, times(1)).write(arrayCaptor2.capture());
        assertSame(arrayCaptor1.getValue(), arrayCaptor2.getValue());
    }

    /**
     * Asserts {@link HelloWorld#append(RandomAccessFile)} method returns given {@code file}.
     *
     * @param tempDir a temporary directory to test with.
     * @throws IOException if an I/O error occurs.
     */
    @DisplayName("append(file) returns file")
    @Test
    void writeFile_ReturnFile_() throws IOException {
        final RandomAccessFile expected = mock(RandomAccessFile.class);
        assertSame(expected, helloWorld.write(expected));
    }
}
