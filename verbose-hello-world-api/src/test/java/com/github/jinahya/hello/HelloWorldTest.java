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

import java.io.*;
import java.net.Socket;
import java.nio.BufferOverflowException;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.WritableByteChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.atomic.LongAdder;

import static java.nio.charset.StandardCharsets.US_ASCII;
import static java.util.concurrent.ThreadLocalRandom.current;
import static org.junit.jupiter.api.Assertions.*;
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
    public void assertSetArrayThrowsIndexOufOfBoundsExceptionWhenArrayLengthIsLessThan12() {
        final byte[] array = new byte[current().nextInt(HelloWorld.SIZE)];
        assertThrows(IndexOutOfBoundsException.class, () -> helloWorld.set(array));
    }

    /**
     * Asserts {@link HelloWorld#set(byte[])} method returns specified array argument.
     */
    @Test
    public void assertSetArrayReturnsGivenArray() {
        final byte[] expected = new byte[HelloWorld.SIZE];
        final byte[] actual = helloWorld.set(expected);
        assertEquals(expected, actual);
    }

    // ----------------------------------------------------------------------------------------------- write(DataOutput)

    /**
     * Asserts {@link HelloWorld#write(DataOutput)} method throws a {@code NullPointerException} when {@code data}
     * argument is {@code null}.
     */
    @Test
    public void assertWriteDataThrowsNullPointerExceptionWhenDataIsNull() {
        final DataOutput data = null;
        assertThrows(NullPointerException.class, () -> helloWorld.write(data));
    }

    /**
     * Asserts {@link HelloWorld#write(DataOutput)} method writes exactly {@value HelloWorld#SIZE} bytes to specified
     * data output.
     *
     * @throws IOException if an I/O error occurs.
     */
    @Test
    public void assertWriteDataMethodWritesExactly12BytesToData() throws IOException {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             DataOutputStream data = new DataOutputStream(baos)) {
            helloWorld.write((DataOutput) data);
            data.flush();
            assertEquals(HelloWorld.SIZE, baos.size());
        }
    }

    /**
     * Asserts {@link HelloWorld#write(DataOutput)} method returns the specified data output.
     */
    @Test
    public void assertWriteDataMethodReturnsSpecifiedData() throws IOException {
        final DataOutput expected = mock(DataOutput.class);
        final DataOutput actual = helloWorld.write(expected);
        assertEquals(expected, actual);
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
        assertEquals(HelloWorld.SIZE, file.length());
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
    public void assertSendSocketSendsExpectedNumberOfBytesToSocket() throws IOException {
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        final Socket socket = mock(Socket.class);
        when(socket.getOutputStream()).thenReturn(outputStream);
        helloWorld.send(socket);
        verify(helloWorld).send(socket);
        socket.getOutputStream().flush();
        assertEquals(HelloWorld.SIZE, outputStream.size());
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

    @Test
    public void assertPutBufferThrowsBufferOverflowExceptionWhenBufferRemainingIsLessThan12NonDirect(
            @NotEnoughRemaining final ByteBuffer buffer) {
        assertTrue(buffer.remaining() < HelloWorld.SIZE);
        assertThrows(BufferOverflowException.class, () -> helloWorld.put(buffer));
    }

    /**
     * Asserts {@link HelloWorld#put(ByteBuffer)} method throws a {@link java.nio.BufferOverflowException} when the
     * value of {@link ByteBuffer#remaining() remaining()} of {@code buffer} argument is less than {@link
     * HelloWorld#SIZE}.
     */
    @Test
    public void assertPutBufferThrowsBufferOverflowExceptionWhenBufferRemainingIsLessThanHelloWorldSizeDirect(
            @NotEnoughRemaining @DirectBuffer final ByteBuffer buffer) {
        assertThrows(BufferOverflowException.class, () -> helloWorld.put(buffer));
    }

    /**
     * Provides byte buffers whose {@code remaining()} is equals to {@link HelloWorld#SIZE}.
     *
     * @return a stream of arguments of bytes buffers.
     */
    @Test
    public void assertPutBufferIncreasesBufferPositionByHelloWorldSize(final ByteBuffer buffer) {
        assert buffer.remaining() >= HelloWorld.SIZE;
        final int position = buffer.position();
    }

    /**
     * Asserts {@link HelloWorld#put(ByteBuffer)} method puts exactly {@value HelloWorld#SIZE} bytes to specified byte
     * buffer.
     */
    @Test
    public void assertPutBufferIncreasesBufferPositionBy12() {
        {
            final ByteBuffer buffer = ByteBuffer.allocate(HelloWorld.SIZE);
            helloWorld.put(buffer);
        }
        {
            final ByteBuffer buffer = ByteBuffer.allocateDirect(current().nextInt(HelloWorld.SIZE));
            assertThrows(BufferOverflowException.class, () -> helloWorld.put(buffer));
        }
    }

    /**
     * Asserts {@link HelloWorld#put(ByteBuffer)} method returns specified byte buffer.
     */
    @Test
    public void assertPutBufferReturnsSpecifiedBufferDirect(@DirectBuffer final ByteBuffer expected) {
        assertTrue(expected.remaining() >= HelloWorld.SIZE);
        assertTrue(expected.isDirect());
        final ByteBuffer actual = helloWorld.put(expected);
        assertEquals(expected, actual);
    }

    // -------------------------------------------------------------------------------------- write(WritableByteChannel)

    /**
     * Asserts {@link HelloWorld#write(WritableByteChannel)} method throws a {@code NullPointerException} when {@code
     * channel} argument is {@code null}.
     */
    @Test
    public void assertWriteChannelThrowsNullPointerExceptionWhenChannelIsNull() {
        final WritableByteChannel channel = null;
        assertThrows(NullPointerException.class, () -> helloWorld.write(channel));
    }

    /**
     * Asserts {@link HelloWorld#write(WritableByteChannel)} method writes {@value HelloWorld#SIZE} bytes to the {@code
     * channel} argument.
     *
     * @throws IOException if an I/O error occurs.
     */
    @Test
    public void assertWriteChannelWritesOfHelloWorldSizeBytes() throws IOException {
        final ByteArrayOutputStream stream = new ByteArrayOutputStream();
        final WritableByteChannel channel = Channels.newChannel(stream);
        helloWorld.write(channel);
        verify(helloWorld).write(channel);
        assertEquals(HelloWorld.SIZE, stream.size());
    }

    /**
     * Asserts {@link HelloWorld#write(WritableByteChannel)} method writes {@value HelloWorld#SIZE} bytes to the {@code
     * channel} argument. This method uses a channel emulating the non-blocking mode.
     *
     * @throws IOException if an I/O error occurs.
     */
    @Test
    public void assertWriteChannelWritesOfHelloWorldSizeBytesNonBlockingEmulated() throws IOException {
        final LongAdder adder = new LongAdder();
        final WritableByteChannel channel = mock(WritableByteChannel.class);
        when(channel.write(any(ByteBuffer.class))).thenAnswer(i -> {
            final ByteBuffer buffer = i.getArgument(0);
            if (!buffer.hasRemaining()) {
                return 0;
            }
            final int written = current().nextInt(0, buffer.remaining() + 1);
            buffer.position(buffer.position() + written);
            adder.add(written);
            return written;
        });
        helloWorld.write(channel);
        verify((helloWorld)).write(channel);
        assertEquals(HelloWorld.SIZE, adder.sum());
    }

    /**
     * Asserts {@link HelloWorld#write(WritableByteChannel)} method returns specified channel.
     */
    @Test
    public void assertWriteChannelReturnsSpecifiedChannel() throws IOException {
        final WritableByteChannel expected = mock(WritableByteChannel.class);
        final WritableByteChannel actual = helloWorld.write(expected);
        verify(helloWorld).write(expected);
        assertEquals(expected, actual);
    }

    // ----------------------------------------------------------------------------------------------------- write(Path)

    /**
     * Asserts {@link HelloWorld#write(Path)} method throws a {@code NullPointerException} when {@code path} argument is
     * {@code null}.
     */
    @Test
    public void assertWritePathThrowsNullPointerExceptionWhenPathIsNull() {
        assertThrows(NullPointerException.class, () -> helloWorld.write((Path) null));
    }

    /**
     * Asserts {@link HelloWorld#write(Path)} method writes {@value HelloWorld#SIZE} bytes to specified path.
     */
    @Test
    public void assertWritePathWritesHelloWorldSizeBytesToPath() throws IOException {
        final Path path = Files.createTempFile(null, null);
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                Files.delete(path);
            } catch (final IOException ioe) {
                ioe.printStackTrace();
            }
        }));
        helloWorld.write(path);
        verify(helloWorld).write(path);
        assertEquals(HelloWorld.SIZE, Files.size(path));
    }

    /**
     * Asserts {@link HelloWorld#write(Path)} method returns specified path.
     */
    @Test
    public void assertWritePathReturnsSpecifiedPath() throws IOException {
        final Path expected = Files.createTempFile(null, null);
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                Files.delete(expected);
            } catch (final IOException ioe) {
                ioe.printStackTrace();
            }
        }));
        final Path actual = helloWorld.write(expected);
        verify(helloWorld).write(expected);
        assertEquals(expected, actual);
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
