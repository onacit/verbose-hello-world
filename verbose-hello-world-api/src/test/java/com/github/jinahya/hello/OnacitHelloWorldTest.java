package com.github.jinahya.hello;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.ArgumentCaptor;
import org.mockito.junit.jupiter.MockitoSettings;

import java.io.DataOutput;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.net.Socket;
import java.nio.BufferOverflowException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.WritableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.atomic.LongAdder;

import static com.github.jinahya.hello.HelloWorld.BYTES;
import static java.nio.ByteBuffer.wrap;
import static java.util.concurrent.ThreadLocalRandom.current;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentCaptor.forClass;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.quality.Strictness.LENIENT;

@MockitoSettings(strictness = LENIENT)
@Slf4j
class OnacitHelloWorldTest extends HelloWorldTest {

    // ------------------------------------------------------------------------------------------------------ set(array)
    @Test
    @Override
    public void assertSetArrayThrowsNullPointerExceptionWhenArrayIsNull() {
        try {
            helloWorld.set(null);                                                                  // <1>
            fail("Expected java.lang.NullPointerException to be thrown, but nothing was thrown."); // <2>
        } catch (final NullPointerException npe) {                                                 // <3>
            // expected
        }
    }

    @Test
    @Override
    public void assertSetArrayThrowsIndexOufOfBoundsExceptionWhenArrayLengthIsLessThanHelloWorldBytes() {
        final byte[] array = new byte[current().nextInt(BYTES)];                    // <1>
        assertThrows(IndexOutOfBoundsException.class, () -> helloWorld.set(array)); // <2>
    }

    @Test
    @Override
    public void assertSetArrayInvokesSetArrayIndex() {
        final byte[] array = new byte[BYTES]; // <1>
        helloWorld.set(array);                // <2>
        verify(helloWorld).set(array, 0);     // <3>
    }

    @Test
    @Override
    public void assertSetArrayReturnsArray() {
        final byte[] expected = new byte[BYTES];        // <1>
        final byte[] actual = helloWorld.set(expected); // <2>
        assertEquals(expected, actual);                 // <3>
    }

    // --------------------------------------------------------------------------------------------------- write(stream)
    @Test
    @Override
    public void assertWriteStreamThrowsNullPointerExceptionWhenStreamIsNull() {
        assertThrows(NullPointerException.class, () -> helloWorld.write((OutputStream) null)); // <1>
    }

    @Test
    @Override
    public void assertWriteStreamInvokesSetArrayAndWritesTheResultToStream() throws IOException {
        final OutputStream stream = mock(OutputStream.class); // <1>
        helloWorld.write(stream);                             // <2>
        final ArgumentCaptor<byte[]> captor = forClass(byte[].class); // <1>
        verify(helloWorld).set(captor.capture());                     // <2>
        final byte[] array = captor.getValue();                       // <3>
        verify(stream, times(1)).write(array); // <1>
        verifyNoMoreInteractions(stream);      // <2>

        verify(stream, times(1)).write(arrayCaptor.getResult());
    }

    @Override
    @Test
    public void assertWriteStreamReturnsStream() throws IOException {
        final OutputStream expected = mock(OutputStream.class); // <1>
        final OutputStream actual = helloWorld.write(expected); // <2>
        assertEquals(expected, actual);                         // <3>
    }

    // ----------------------------------------------------------------------------------------------------- write(File)
    @Override
    @Test
    public void assertAppendFileThrowsNullPointerExceptionWhenFileIsNull() {
        assertThrows(NullPointerException.class, () -> helloWorld.append((File) null));
    }

    @Override
    @Test
    public void assertAppendFileInvokesWriteStream(@TempDir final File tempDir) throws IOException {
        final File file = File.createTempFile("tmp", null, tempDir);     // <1>
        helloWorld.append(file);                                         // <2>
        verify(helloWorld, times(1)).write(any(FileOutputStream.class)); // <1>
        assertEquals(BYTES, file.length());                              // <2>
    }

    @Override
    @Test
    public void assertAppendFileReturnsFile(@TempDir final File tempDir) throws IOException {
        final File file = File.createTempFile("tmp", null, tempDir); // <1>
        final File actual = helloWorld.append(file);                 // <2>
        assertEquals(file, actual);                                  // <3>
    }

    // ---------------------------------------------------------------------------------------------------- send(Socket)
    @Override
    @Test
    public void assertSendSocketThrowsNullPointerExceptionWhenSocketIsNull() {
        assertThrows(NullPointerException.class, () -> helloWorld.send((Socket) null));
    }

    @Test
    @Override
    public void assertSendSocketInvokesWriteStreamWithSocketOutputStream() throws IOException {
        final OutputStream stream = mock(OutputStream.class); // <1>
        final Socket socket = mock(Socket.class);             // <2>
        when(socket.getOutputStream()).thenReturn(stream);    // <3>
        helloWorld.send(socket);                              // <4>
        verify(socket, times(1)).getOutputStream();           // <1>
        verify(helloWorld, times(1)).write(stream);           // <2>
        verifyNoMoreInteractions(socket);                     // <3>
    }

    @Test
    @Override
    public void assertSendSocketReturnsSocket() throws IOException {
        final Socket expected = mock(Socket.class);                            // <1>
        when(expected.getOutputStream()).thenReturn(mock(OutputStream.class)); // <2>
        final Socket actual = helloWorld.send(expected);                       // <3>
        assertEquals(expected, actual);                                        // <4>
    }

    // ----------------------------------------------------------------------------------------------- write(DataOutput)
    @Test
    @Override
    public void assertWriteDataThrowsNullPointerExceptionWhenDataIsNull() {
        assertThrows(NullPointerException.class, () -> helloWorld.write((DataOutput) null));
    }

    @Test
    @Override
    public void assertWriteDataInvokesSetArrayAndWritesTheArrayToData() throws IOException {
        final DataOutput data = mock(DataOutput.class); // <1>
        helloWorld.write(data);                         // <2>
        final ArgumentCaptor<byte[]> captor = forClass(byte[].class); // <1>
        verify(helloWorld, times(1)).set(captor.capture());
        final byte[] array = captor.getValue();
        verify(data, times(1)).write(array);                          // <2>
        verifyNoMoreInteractions(data);                               // <4>

        verify(data, times(1)).write(arrayCaptor.getResult());
    }

    @Test
    @Override
    public void assertWriteDataReturnsData() throws IOException {
        final DataOutput expected = mock(DataOutput.class);   // <1>
        final DataOutput actual = helloWorld.write(expected); // <2>
        assertEquals(expected, actual);                       // <3>
    }

    // ----------------------------------------------------------------------------------------- write(RandomAccessFile)
    @Test
    @Override
    public void assertWriteFileThrowsNullPointerExceptionWhenFileIsNull() {
        assertThrows(NullPointerException.class, () -> helloWorld.write((RandomAccessFile) null));
    }

    @Test
    @Override
    public void assertWriteFileInvokesSetArrayAndWritesTheResultToFile() throws IOException {
        final RandomAccessFile file = mock(RandomAccessFile.class); // <1>
        helloWorld.write(file);                                     // <2>
        final ArgumentCaptor<byte[]> captor = forClass(byte[].class); // <1>
        verify(helloWorld).set(captor.capture());
        final byte[] array = captor.getValue();
        verify(file, times(1)).write(array);                          // <3>
        verifyNoMoreInteractions(file);                               // <4>

        verify(file, times(1)).write(arrayCaptor.getResult());
    }

    @Test
    @Override
    public void assertWriteRandomAccessFileReturnsFile() throws IOException {
        final RandomAccessFile expected = mock(RandomAccessFile.class); // <1>
        final RandomAccessFile actual = helloWorld.write(expected);     // <2>
        assertEquals(expected, actual);                                 // <3>
    }

    // ------------------------------------------------------------------------------------------------- put(ByteBuffer)
    @Override
    @Test
    public void assertPutBufferThrowsNullPointerExceptionWhenBufferIsNull() {
        assertThrows(NullPointerException.class, () -> helloWorld.put(null));
    }

    @Override
    @Test
    public void assertPutBufferThrowsBufferOverflowExceptionWhenBufferRemainingIsLessThanHelloWorldBytes() {
        final ByteBuffer buffer = mock(ByteBuffer.class);                          // <1>
        when(buffer.remaining()).thenReturn(current().nextInt(BYTES));             // <2>
        assertThrows(BufferOverflowException.class, () -> helloWorld.put(buffer)); // <3>
    }

    @Test
    @Override
    public void assertPutBufferInvokesSetArrayIndexWhenBufferHasBackingArray() {
        final ByteBuffer wrapped = wrap(new byte[BYTES * 3]);         // <1>
        wrapped.position(current().nextInt(1, BYTES));                // <2>
        final ByteBuffer sliced = wrapped.slice();                     // <1>
        sliced.position(current().nextInt(1, BYTES));                  // <2>
        assertTrue(sliced.hasArray());
        assertTrue(sliced.arrayOffset() > 0);
        assertTrue(sliced.position() > 0);
        assertTrue(sliced.remaining() >= BYTES);
        final int position = sliced.position();                         // <1>
        helloWorld.put(sliced);                                         // <2>
        final byte[] expectedArray = sliced.array();                    // <1>
        final int expectedIndex = sliced.arrayOffset() + position;      // <2>
        verify(helloWorld, times(1)).set(expectedArray, expectedIndex); // <3>
        assertEquals(position + BYTES, sliced.position());              // <4>
    }

    @Test
    @Override
    public void assertPutBufferInvokesSetWhenBufferHasNoBackingArray() {
        final ByteBuffer buffer = mock(ByteBuffer.class); // <1>
        when(buffer.remaining()).thenReturn(BYTES);       // <2>
        when(buffer.hasArray()).thenReturn(false);        // <3>
        helloWorld().put(buffer);                         // <4>
        final ArgumentCaptor<byte[]> captor = forClass(byte[].class); // <1>
        verify(helloWorld, times(1)).set(captor.capture());
        final byte[] array = captor.getValue();
        assertEquals(BYTES, array.length);                            // <2>
        verify(buffer, times(1)).put(array);                          // <3>

        verify(buffer, times(1)).put(arrayCaptor.getResult());
    }

    @Test
    @Override
    public void assertPutBufferReturnsBufferHasBackingArray() {
        final ByteBuffer expected = wrap(new byte[BYTES]);  // <1>
        final ByteBuffer actual = helloWorld.put(expected); // <3>
        assertEquals(expected, actual);                     // <4>
    }

    @Test
    @Override
    public void assertPutBufferReturnsBufferHasNoBackingArray() {
        final ByteBuffer expected = mock(ByteBuffer.class); // <1>
        when(expected.remaining()).thenReturn(BYTES);       // <2>
        final ByteBuffer actual = helloWorld.put(expected); // <3>
        assertEquals(expected, actual);                     // <4>
    }

    // -------------------------------------------------------------------------------------- write(WritableByteChannel)
    @Override
    @Test
    public void assertWriteChannelThrowsNullPointerExceptionWhenChannelIsNull() {
        assertThrows(NullPointerException.class, () -> helloWorld.write((WritableByteChannel) null));
    }

    @Override
    @Test
    public void assertWriteChannelInvokesPutBufferAndWritesBufferToChannel() throws IOException {
        final LongAdder adder = new LongAdder();                             // <1>
        final WritableByteChannel channel = mock(WritableByteChannel.class); // <2>
        when(channel.write(any(ByteBuffer.class))).then(i -> {               // <3>
            final ByteBuffer buffer = i.getArgument(0);
            final int written = current().nextInt(0, buffer.remaining() + 1);
            buffer.position(buffer.position() + written); // drain some bytes
            adder.add(written);
            return written;
        });
        helloWorld.write(channel);                                            // <4>
        final ArgumentCaptor<ByteBuffer> captor = forClass(ByteBuffer.class); // <2>
        verify(helloWorld, times(1)).put(captor.capture());                   // <3>
        final ByteBuffer buffer = captor.getValue();                          // <4>
        assertEquals(BYTES, buffer.capacity());                               // <5>
        verify(channel, atLeast(1)).write(buffer);                            // <1>
        assertEquals(BYTES, adder.sum());                                     // <1>

        verify(channel, atLeast(1)).write(bufferCaptor.getResult());
    }

    @Override
    @Test
    public void assertWriteChannelReturnsChannel() throws IOException {
        final WritableByteChannel expected = mock(WritableByteChannel.class); // <1>
        when(expected.write(any(ByteBuffer.class))).thenAnswer(i -> {
            final ByteBuffer buffer = i.getArgument(0); // <1>
            final int remaining = buffer.remaining();   // <2>
            buffer.position(buffer.limit());            // <3>
            return remaining;                           // <4>
        });
        final WritableByteChannel actual = helloWorld.write(expected);        // <3>
        assertEquals(expected, actual);                                       // <4>
    }

    // ----------------------------------------------------------------------------------------------------- write(Path)
    @Override
    @Test
    public void assertAppendPathThrowsNullPointerExceptionWhenPathIsNull() {
        assertThrows(NullPointerException.class, () -> helloWorld.append((Path) null));
    }

    @Override
    @Test
    public void assertAppendPathInvokesWriteChannel(@TempDir final Path tempDir) throws IOException {
        final Path path = Files.createTempFile(tempDir, null, null); // <1>
        helloWorld.append(path);                                     // <2>
        verify(helloWorld, times(1)).write(any(FileChannel.class));  // <1>
        assertEquals(BYTES, Files.size(path));                       // <2>
    }

    @Override
    @Test
    public void assertAppendPathReturnsPath(@TempDir final Path tempDir) throws IOException {
        final Path expected = Files.createTempFile(tempDir, null, null);
        final Path actual = helloWorld.append(expected);
        assertEquals(expected, actual);
    }
}
