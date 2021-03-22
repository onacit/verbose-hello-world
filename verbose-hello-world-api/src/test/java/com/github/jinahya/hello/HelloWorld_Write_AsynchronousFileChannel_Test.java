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

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousByteChannel;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import static java.nio.channels.AsynchronousFileChannel.open;
import static java.nio.file.Files.createTempFile;
import static java.nio.file.Files.size;
import static java.nio.file.StandardOpenOption.WRITE;
import static java.util.concurrent.ThreadLocalRandom.current;
import static java.util.stream.Collectors.toList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentCaptor.forClass;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * A class for testing {@link HelloWorld#write(AsynchronousByteChannel)} method.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 */
@Slf4j
class HelloWorld_Write_AsynchronousFileChannel_Test extends HelloWorldTest {

    /**
     * Asserts {@link HelloWorld#write(AsynchronousFileChannel, long)} throws a {@link NullPointerException} when {@code
     * channel} argument is {@code null}.
     */
    @DisplayName("write(channel, position) throws NullPointerException when channel is null")
    @Test
    void write_NullPointerException_ChannelIsNull() {
        assertThrows(NullPointerException.class, () -> helloWorld.write((AsynchronousFileChannel) null, 0L));
    }

    /**
     * Asserts {@link HelloWorld#write(AsynchronousFileChannel, long)} throws an {@link IllegalArgumentException} when
     * {@code position} argument is negative.
     */
    @DisplayName("write(channel, position) throws NullPointerException when position is negative")
    @Test
    void write_NullPointerException_PositionIsNegative() {
        final long position = current().nextLong() | Long.MIN_VALUE;
        assertThrows(IllegalArgumentException.class,
                     () -> helloWorld.write(mock(AsynchronousFileChannel.class), position));
    }

    /**
     * Asserts {@link HelloWorld#write(AsynchronousFileChannel, long)} method invokes {@link HelloWorld#put(ByteBuffer)}
     * and writes the buffer to {@code channel}.
     *
     * @throws IOException          if an I/O error occurs.
     * @throws ExecutionException   if failed to work.
     * @throws InterruptedException if interrupted while executing.
     */
    @DisplayName("write(channel, position) invokes put(buffer) writes the buffer to channel")
    @Test
    void write_InvokePutBufferWriteBufferToChannel_(final @TempDir Path tempDir)
            throws IOException, ExecutionException, InterruptedException {
        final Path file = createTempFile(tempDir, null, null);
        final long size = size(file);
        try (AsynchronousFileChannel channel = spy(open(file, WRITE))) { // APPEND not allowed!
            helloWorld.write(channel, 0L);
            final ArgumentCaptor<ByteBuffer> bufferCaptor = forClass(ByteBuffer.class);
            verify(helloWorld, times(1)).put(bufferCaptor.capture());
            final ByteBuffer buffer = bufferCaptor.getValue();
            final ArgumentCaptor<ByteBuffer> srcCaptor = forClass(ByteBuffer.class);
            final ArgumentCaptor<Long> positionCaptor = forClass(long.class);
            verify(channel, atLeastOnce()).write(srcCaptor.capture(), positionCaptor.capture());
            srcCaptor.getAllValues().forEach(s -> {
                assertSame(buffer, s);
            });
            final List<Long> positions = positionCaptor.getAllValues();
            positions.forEach(p -> {
                assertTrue(p >= 0L);
            });
            assertEquals(positions, positions.stream().sorted().collect(toList()));
            channel.force(false);
        }
        assertEquals(size + HelloWorld.BYTES, size(file));
    }
}
