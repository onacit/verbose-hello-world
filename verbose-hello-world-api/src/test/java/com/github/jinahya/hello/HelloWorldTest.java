package com.github.jinahya.hello;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.net.Socket;
import java.nio.BufferOverflowException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.channels.WritableByteChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

import static java.nio.charset.StandardCharsets.US_ASCII;
import static java.util.concurrent.ThreadLocalRandom.current;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

/**
 * An abstract class for unit-testing {@link HelloWorld} interface.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 */
@MockitoSettings(strictness = Strictness.LENIENT)
@ExtendWith({MockitoExtension.class})
@Slf4j
public class HelloWorldTest {

    // ----------------------------------------------------------------------------------------------------------- BYTES

    /**
     * Asserts the value of {@link HelloWorld#BYTES} constant equals to the actual number of bytes of "{@code hello,
     * world}" string encoded in {@link StandardCharsets#US_ASCII US-ASCII} character set.
     *
     * @see String#getBytes(Charset)
     * @see StandardCharsets#US_ASCII
     */
    @DisplayName("BYTES equals to the actual number of \"hello, world\" bytes")
    @Test
    void assertSizeEqualsToHelloWorldBytes() {
        assertEquals("hello, world".getBytes(US_ASCII).length, HelloWorld.SIZE);
    }

    // ----------------------------------------------------------------------------------------------------- set(byte[])

    /**
     * Asserts {@link HelloWorld#set(byte[])} method throws a {@link NullPointerException} when the {@code array}
     * argument is {@code null}.
     */
    @DisplayName("set(array) throws NullPointerException when array is null")
    @Test
    public void assertSetThrowsNullPointerExceptionWhenArrayIsNull() {
        assertThrows(NullPointerException.class, () -> helloWorld.set(null));
    }

    /**
     * Asserts {@link HelloWorld#set(byte[])} method throws an {@link IndexOutOfBoundsException} when {@code
     * array.length} is less than {@link HelloWorld#BYTES}.
     */
    @DisplayName("set(array) throws IndexOutOfBoundsException when array.length is less than BYTES")
    @Test
    public void assertSetThrowsIndexOufOfBoundsExceptionWhenArrayLengthIsLessThanSize() {
        final byte[] array = new byte[current().nextInt(HelloWorld.SIZE)];
        assertThrows(IndexOutOfBoundsException.class, () -> helloWorld.set(array));
    }

    /**
     * Asserts {@link HelloWorld#set(byte[])} method returns specified array argument.
     */
    @Test
    public void assertSetArrayReturnsGivenArray() {
        final byte[] array = new byte[HelloWorld.SIZE];
        assertEquals(array, helloWorld.set(array));
    }

    // --------------------------------------------------------------------------------------------- write(OutputStream)

    /**
     * Asserts {@link HelloWorld#write(OutputStream)} method throws {@code NullPointerException} when {@code stream}
     * argument is {@code null}.
     */
    @Test
    public void assertWriteStreamThrowsNullPointerExceptionWhenStreamIsNull() {
        final OutputStream stream = null;
        assertThrows(NullPointerException.class, () -> helloWorld.write(stream));
    }

    /**
     * Asserts {@link HelloWorld#write(OutputStream)} method writes exactly {@value HelloWorld#SIZE} bytes to the
     * stream.
     */
    @Test
    public void assertWriteStreamWritesExactly12Bytes() throws IOException {
        final ByteArrayOutputStream stream = new ByteArrayOutputStream(HelloWorld.SIZE);
        helloWorld.write(stream);
        //verify(helloWorld).write(stream);
        assertEquals(stream.size(), HelloWorld.SIZE);
    }

    /**
     * Asserts {@link HelloWorld#write(OutputStream)} method returns specified {@code stream} argument.
     */
    @Test
    public void assertWriteStreamReturnsSpecifiedStream() throws IOException {
        final OutputStream expected = mock(OutputStream.class);
        final OutputStream actual = helloWorld.write(expected);
        verify(helloWorld).write(expected);
        assertEquals(expected, actual);
    }

    // ----------------------------------------------------------------------------------------------------- write(File)

    /**
     * Asserts {@link HelloWorld#write(File)} method throws {@code NullPointerException} when {@code file} argument is
     * {@code null}.
     */
    @Test
    public void assertWriteFileThrowsNullPointerExceptionWhenFileIsNull() {
        final File file = null;
        assertThrows(NullPointerException.class, () -> helloWorld.write(file));
    }

    /**
     * Asserts {@link HelloWorld#write(File)} method write exactly {@value HelloWorld#SIZE} bytes to specified file.
     */
    @Test
    public void assertWriteFileWritesExactly12BytesToFile() throws IOException {
        final File file = File.createTempFile("tmp", null);
        file.deleteOnExit();
        helloWorld.write(file);
        verify(helloWorld).write(file);
        assertEquals((long) HelloWorld.SIZE, file.length());
    }

    /**
     * Asserts {@link HelloWorld#write(File)} method returns specified file.
     */
    @Test
    public void assertWriteFileReturnsSpecifiedFile() throws IOException {
        final File expected = File.createTempFile("tmp", null);
        expected.deleteOnExit();
        final File actual = helloWorld.write(expected);
        verify(helloWorld).write(expected);
        assertEquals(expected, actual);
    }

    // ---------------------------------------------------------------------------------------------------- send(Socket)

    /**
     * Asserts {@link HelloWorld#send(Socket)} method throws {@code NullPointerException} when the {@code socket}
     * argument is {@code null}.
     */
    @Test
    public void assertSendSocketThrowsNullPointerExceptionWhenSocketIsNull() {
        final Socket socket = null;
        assertThrows(NullPointerException.class, () -> helloWorld.send(socket));
    }

    /**
     * Asserts {@link HelloWorld#send(Socket)} method sends exactly {@value HelloWorld#SIZE} bytes to the {@code
     * socket}.
     *
     * @throws IOException if an I/O error occurs.
     */
    @Test
    public void assertSendSocketSendsExactly12BytesToSocket() throws IOException {
        final Socket socket = mock(Socket.class);
        final ByteArrayOutputStream stream = new ByteArrayOutputStream(HelloWorld.SIZE);
        when(socket.getOutputStream()).thenReturn(stream);
        helloWorld.send(socket);
        verify(helloWorld).send(socket);
        stream.flush();
        assertEquals(HelloWorld.SIZE, stream.size());
    }

    /**
     * Asserts {@link HelloWorld#send(Socket)} method returns the specified {@code socket}.
     */
    @Test
    public void assertSendSocketReturnsSpecifiedSocket() throws IOException {
        final Socket expected = mock(Socket.class);
        when(expected.getOutputStream()).thenReturn(mock(OutputStream.class));
        final Socket actual = helloWorld.send(expected);
        verify(helloWorld).send(expected);
        assertEquals(expected, actual);
    }

    // ------------------------------------------------------------------------------------------------- put(ByteBuffer)

    /**
     * Asserts {@link HelloWorld#put(ByteBuffer)} method throws a {@code NullPointerException} when {@code buffer}
     * argument is {@code null}.
     */
    @Test
    public void assertPutBufferThrowsNullPointerExceptionWhenBufferIsNull() {
        final ByteBuffer buffer = null;
        assertThrows(NullPointerException.class, () -> helloWorld.put(buffer));
    }

    /**
     * Asserts {@link HelloWorld#put(ByteBuffer)} method throws a {@link java.nio.BufferOverflowException} when the
     * value of {@link ByteBuffer#remaining() remaining()} of {@code buffer} argument is less than {@link
     * HelloWorld#SIZE}.
     */
    @Test
    public void assertPutBufferThrowsBufferOverflowExceptionWhenBufferRemainingIsLessThan12() {
        // @todo: implement!
    }

    /**
     * Asserts {@link HelloWorld#put(ByteBuffer)} method puts exactly {@value HelloWorld#SIZE} bytes to specified byte
     * buffer.
     */
    @Test
    public void assertPutBufferPutsExactly12Bytes() {
        // @todo: implement!
    }

    /**
     * Asserts {@link HelloWorld#put(ByteBuffer)} method returns specified byte buffer.
     */
    @Test
    public void assertPutBufferReturnsSpecifiedBuffer() {
        // @todo: implement!
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Stubs {@link HelloWorld#set(byte[], int)} method of {@link #helloWorld} to return specified {@code array}
     * argument.
     */
    @BeforeEach
    private void stubSetArrayWithIndexToReturnSpecifiedArray() {
        when(helloWorld.set(any(), anyInt())).thenAnswer(i -> i.getArgument(0));
    }

    /**
     * Asserts {@link HelloWorld#set(byte[], int)} method of {@link #helloWorld} returns specified {@code array}
     * argument.
     */
    @Test
    private void assertSetArrayWitnIndexReturnsSpecifiedArray() {
        final byte[] array = current().nextBoolean() ? null : new byte[current().nextInt(HelloWorld.SIZE << 1)];
        final int index = current().nextInt();
        assertEquals(array, helloWorld.set(array, index));
    }

    // -----------------------------------------------------------------------------------------------------------------
    @Spy
    private HelloWorld helloWorld;
}
