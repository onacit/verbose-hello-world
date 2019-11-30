package com.github.jinahya.hello;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;

import java.io.DataOutput;
import java.io.File;
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

import static com.github.jinahya.hello.HelloWorld.BYTES;
import static java.nio.charset.StandardCharsets.US_ASCII;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.quality.Strictness.LENIENT;

/**
 * A class for unit-testing {@link HelloWorld} interface.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 */
@MockitoSettings(strictness = LENIENT)
@ExtendWith({MockitoExtension.class})
@Slf4j
public class HelloWorldTest {

    // ----------------------------------------------------------------------------------------------------------- BYTES

    /**
     * Asserts the value of {@link HelloWorld#BYTES} constant equals to the actual number of bytes of "{@code hello,
     * world}" string encoded in {@code US-ASCII} character set.
     *
     * @see String#getBytes(Charset)
     * @see StandardCharsets#US_ASCII
     */
    @DisplayName("BYTES equals to actual number of \"hello, world\" bytes")
    @Test
    void assertHelloWorldBytesEqualsToActualNumberOfHelloWorldBytes() {
        final int expected = "hello, world".getBytes(US_ASCII).length;
        assertEquals(expected, BYTES);
    }

    // ----------------------------------------------------------------------------------------------------- set(byte[])

    /**
     * Asserts {@link HelloWorld#set(byte[])} method throws {@link NullPointerException} when {@code array} argument is
     * {@code null}.
     */
    @DisplayName("set(byte[]) method throws a NullPointerException when array is null")
    @Test
    public void assertSetArrayThrowsNullPointerExceptionWhenArrayIsNull() {
        // TODO: implement!
    }

    /**
     * Asserts {@link HelloWorld#set(byte[])} method throws {@link IndexOutOfBoundsException} when {@code array.length}
     * is less than {@link HelloWorld#BYTES}.
     */
    @DisplayName("set(byte[]) method throws an IndexOutOfBoundsException when array.length is less than BYTES")
    @Test
    public void assertSetArrayThrowsIndexOufOfBoundsExceptionWhenArrayLengthIsLessThanHelloWorldBytes() {
        // TODO: implement!
    }

    /**
     * Asserts {@link HelloWorld#set(byte[])} method invokes {@link HelloWorld#set(byte[], int)} with specified {@code
     * array} and {@code 0}.
     */
    @DisplayName("set(byte[]) method invokes set(byte[], int) method with specified array and 0")
    @Test
    public void assertSetArrayInvokesSetWithArrayAndZero() {
        // TODO: implement!
    }

    /**
     * Asserts {@link HelloWorld#set(byte[])} method returns specified {@code array}.
     */
    @DisplayName("set(byte[]) method returns specified array")
    @Test
    public void assertSetArrayReturnsSpecifiedArray() {
        // TODO: implement!
    }

    // --------------------------------------------------------------------------------------------- write(OutputStream)

    /**
     * Asserts {@link HelloWorld#write(OutputStream)} method throws {@link NullPointerException} when {@code stream}
     * argument is {@code null}.
     */
    @DisplayName("write(OutputStream) method throws a NullPointerException when stream is null")
    @Test
    public void assertWriteStreamThrowsNullPointerExceptionWhenStreamIsNull() {
        // TODO: implement!
    }

    /**
     * Asserts {@link HelloWorld#write(OutputStream)} method invokes {@link HelloWorld#set(byte[])} method with an array
     * of {@value com.github.jinahya.hello.HelloWorld#BYTES} bytes and writes the returned array to specified output
     * stream.
     *
     * @throws IOException if an I/O error occurs.
     */
    @DisplayName("write(OutputStream) method invokes set(byte[12]) and write the returned array to the stream")
    @Test
    public void assertWriteStreamInvokesSetArrayAndWritesTheReturnedArrayToStream() throws IOException {
        // TODO: implement!
    }

    /**
     * Asserts {@link HelloWorld#write(OutputStream)} method returns specified output stream.
     *
     * @throws IOException if an I/O error occurs.
     */
    @DisplayName("write(OutputStream) method returns specified output stream")
    @Test
    public void assertWriteStreamReturnsSpecifiedStream() throws IOException {
        // TODO: implement!
    }

    // ----------------------------------------------------------------------------------------------------- write(File)

    /**
     * Asserts {@link HelloWorld#write(File)} method throws {@link NullPointerException} when {@code file} argument is
     * {@code null}.
     */
    @DisplayName("write(File) method throws a NullPointerException when file is null")
    @Test
    public void assertWriteFileThrowsNullPointerExceptionWhenFileIsNull() {
        assertThrows(NullPointerException.class, () -> helloWorld.write((File) null));
    }

    /**
     * Asserts {@link HelloWorld#write(File)} method invokes {@link HelloWorld#write(OutputStream)} method.
     *
     * @param tempDir a temporary directory to test with.
     * @throws IOException if an I/O error occurs.
     */
    @DisplayName("write(File) method invokes write(OutputStream) method")
    @Test
    public void assertWriteFileInvokesWriteStream(@TempDir final File tempDir) throws IOException {
        // TODO: implement!
    }

    /**
     * Asserts {@link HelloWorld#write(File)} method returns specified file.
     *
     * @param tempDir a temporary directory to test with.
     * @throws IOException if an I/O error occurs.
     */
    @DisplayName("write(File) method returns specified file")
    @Test
    public void assertWriteFileReturnsSpecifiedFile(@TempDir final File tempDir) throws IOException {
        // TODO: implement!
    }

    // ---------------------------------------------------------------------------------------------------- send(Socket)

    /**
     * Asserts {@link HelloWorld#send(Socket)} method throws {@link NullPointerException} when the {@code socket}
     * argument is {@code null}.
     */
    @DisplayName("send(Socket) method throws a NullPointerException when socket is null")
    @Test
    public void assertSendSocketThrowsNullPointerExceptionWhenSocketIsNull() {
        assertThrows(NullPointerException.class, () -> helloWorld.send((Socket) null));
    }

    /**
     * Asserts {@link HelloWorld#send(Socket)} method invokes {@link HelloWorld#write(OutputStream)} method with
     * specified socket's {@link Socket#getOutputStream() outputStream}.
     *
     * @throws IOException if an I/O error occurs.
     */
    @DisplayName("send(Socket) method invokes write(OutputStream) with socket's outputStream")
    @Test
    public void assertSendSocketInvokesWriteOutputWithSocketOutputStream() throws IOException {
        // TODO: implement!
    }

    /**
     * Asserts {@link HelloWorld#send(Socket)} method returns the specified socket.
     *
     * @throws IOException if an I/O error occurs.
     */
    @DisplayName("send(Socket) method returns specified socket")
    @Test
    public void assertSendSocketReturnsSpecifiedSocket() throws IOException {
        // TODO: implement!
    }

    // ----------------------------------------------------------------------------------------------- write(DataOutput)

    /**
     * Asserts {@link HelloWorld#write(DataOutput)} method throws {@link NullPointerException} when {@code data}
     * argument is {@code null}.
     */
    @DisplayName("write(DataOutput) method throws a NullPointerException when data output is null")
    @Test
    public void assertWriteDataThrowsNullPointerExceptionWhenDataIsNull() {
        assertThrows(NullPointerException.class, () -> helloWorld.write((DataOutput) null));
    }

    /**
     * Asserts {@link HelloWorld#write(DataOutput)} method invokes {@link HelloWorld#set(byte[])} with an array of
     * {@value com.github.jinahya.hello.HelloWorld#BYTES} bytes and write the array to specified data output.
     *
     * @throws IOException if an I/O error occurs.
     */
    @DisplayName("write(DataOutput) method writes as many bytes as BYTES")
    @Test
    public void assertWriteDataInvokesSetArrayAndWritesTheArrayToData() throws IOException {
        // TODO: implement!
    }

    /**
     * Asserts {@link HelloWorld#write(DataOutput)} method returns the specified data output.
     *
     * @throws IOException if an I/O error occurs.
     */
    @DisplayName("write(DataOutput) method returns specified data output")
    @Test
    public void assertWriteDataReturnsSpecifiedData() throws IOException {
        // TODO: implement!
    }

    // ----------------------------------------------------------------------------------------- write(RandomAccessFile)

    /**
     * Asserts {@link HelloWorld#write(RandomAccessFile)} method throws {@link NullPointerException} when {@code file}
     * argument is {@code null}.
     */
    @DisplayName("write(RandomAccessFile) method throws a NullPointerException when file is null")
    @Test
    public void assertWriteRandomAccessFileThrowsNullPointerExceptionWhenFileIsNull() {
        assertThrows(NullPointerException.class, () -> helloWorld.write((RandomAccessFile) null));
    }

    /**
     * Asserts {@link HelloWorld#write(RandomAccessFile)} invokes {@link HelloWorld#set(byte[])} with an array of
     * {@value com.github.jinahya.hello.HelloWorld#BYTES} bytes and writes the array to specified random access file.
     *
     * @throws IOException if an I/O error occurs.
     */
    @DisplayName("write(RandomAccessFile) method invokes set(byte[]) and writes the array to file")
    @Test
    public void assertWriteRandomAccessFileInvokesSetArrayAndWritesTheArrayToFile() throws IOException {
        // TODO: implement!
    }

    /**
     * Asserts {@link HelloWorld#write(RandomAccessFile)} method returns the specified random access file.
     *
     * @throws IOException if an I/O error occurs.
     */
    @DisplayName("write(RandomAccessFile) method returns specified random access file")
    @Test
    public void assertWriteRandomAccessFileReturnsSpecifiedFile() throws IOException {
        // TODO: implement!
    }

    // ------------------------------------------------------------------------------------------------- put(ByteBuffer)

    /**
     * Asserts {@link HelloWorld#put(ByteBuffer)} method throws {@link NullPointerException} when {@code buffer} is
     * {@code null}.
     */
    @DisplayName("put(ByteBuffer) method throws a NullPointerException when buffer is null")
    @Test
    public void assertPutBufferThrowsNullPointerExceptionWhenBufferIsNull() {
        assertThrows(NullPointerException.class, () -> helloWorld.put(null));
    }

    /**
     * Asserts {@link HelloWorld#put(ByteBuffer)} method throws {@link BufferOverflowException} when {@code buffer}'s
     * {@link ByteBuffer#remaining() remaining} is less than {@link HelloWorld#BYTES}.
     */
    @DisplayName("put(ByteBuffer) method throws a BufferOverflowException when buffer.remaining is not enough")
    @Test
    public void assertPutBufferThrowsBufferOverflowExceptionWhenBufferRemainingIsLessThanHelloWorldBytes() {
        // TODO: implement!
    }

    /**
     * Asserts {@link HelloWorld#put(ByteBuffer)} method invokes {@link HelloWorld#set(byte[], int)} method when the
     * {@code buffer} has a backing array and, also, assert the {@code buffer}'s position is incremented by {@value
     * com.github.jinahya.hello.HelloWorld#BYTES}.
     */
    @DisplayName("put(ByteBuffer) invokes set(byte[], int) method with buffer's backing array")
    @Test
    public void assertPutBufferInvokesSetArrayIndexWhenBufferHasBackingArray() {
        // TODO: implement!
    }

    /**
     * Asserts {@link HelloWorld#put(ByteBuffer)} method invokes {@link HelloWorld#set(byte[])} method when the {@code
     * buffer} is a direct buffer and, also, assert the {@code buffer}'s position is increments by {@value
     * com.github.jinahya.hello.HelloWorld#BYTES}.
     */
    @DisplayName("put(ByteBuffer) invokes set(byte[]) method and put the array to the buffer")
    @Test
    public void assertPutBufferInvokesSetWhenBufferIsDirect() {
        // TODO: implement!
    }

    /**
     * Asserts {@link HelloWorld#put(ByteBuffer)} method returns specified byte buffer.
     */
    @DisplayName("put(ByteBuffer) method returns specified byte buffer")
    @Test
    public void assertPutBufferReturnsSpecifiedBuffer() {
        // TODO: implement!
    }

    // -------------------------------------------------------------------------------------- write(WritableByteChannel)

    /**
     * Asserts {@link HelloWorld#write(WritableByteChannel)} method throws {@link NullPointerException} when {@code
     * channel} argument is {@code null}.
     */
    @DisplayName("write(WritableByteChannel) method throws a NullPointerException when channel is null")
    @Test
    public void assertWriteChannelThrowsNullPointerExceptionWhenChannelIsNull() {
        assertThrows(NullPointerException.class, () -> helloWorld.write((WritableByteChannel) null));
    }

    /**
     * Asserts {@link HelloWorld#write(WritableByteChannel)} method writes as many bytes as {@link HelloWorld#BYTES} to
     * specified channel.
     *
     * @throws IOException if an I/O error occurs.
     */
    @DisplayName("write(WritableByteChannel) writes as many bytes as BYTES to the stream")
    @Test
    public void assertWriteChannelWritesAsManyBytesAsHelloWorldBytesToChannel() throws IOException {
        // TODO: implement!
    }

    /**
     * Asserts {@link HelloWorld#write(WritableByteChannel)} method returns the specified channel.
     *
     * @throws IOException if an I/O error occurs.
     */
    @DisplayName("write(WritableByteChannel) method returns specified channel")
    @Test
    public void assertWriteChannelReturnsSpecifiedChannel() throws IOException {
        // TODO: implement!!
    }

    // ----------------------------------------------------------------------------------------------------- write(Path)

    /**
     * Asserts {@link HelloWorld#write(Path)} method throws {@link NullPointerException} when specified {@code path}
     * argument is {@code null}.
     */
    @DisplayName("write(Path) method throws a NullPointerException when path is null")
    @Test
    public void assertWritePathThrowsNullPointerExceptionWhenPathIsNull() {
        assertThrows(NullPointerException.class, () -> helloWorld.write((Path) null));
    }

    /**
     * Asserts {@link HelloWorld#write(Path)} method writes as many bytes as {@link HelloWorld#BYTES} to specified
     * path.
     *
     * @param tempDir a temporary directory to test with.
     * @throws IOException if an I/O error occurs.
     */
    @DisplayName("write(Path) methods writes as many bytes as BYTES")
    @Test
    public void assertWritePathWritesAsManyBytesAsHelloWorldBytesToPath(@TempDir final Path tempDir)
            throws IOException {
        // TODO: implement!!
    }

    /**
     * Asserts {@link HelloWorld#write(Path)} method returns the specified path.
     *
     * @param tempDir a temporary directory to test with.
     * @throws IOException if an I/O error occurs.
     */
    @DisplayName("write(Path) method returns specified path")
    @Test
    public void assertWritePathReturnsSpecifiedPath(@TempDir final Path tempDir) throws IOException {
        // TODO: implement!!
    }

    // --------------------------------------------------------------------------------------------- send(SocketChannel)
    @Deprecated
    @Test
    void testSendSocketChannel() throws IOException {
        final SocketChannel expected = mock(SocketChannel.class);
        when(expected.write(any(ByteBuffer.class))).then(i -> {
            final ByteBuffer buffer = i.getArgument(0);
            final int written = buffer.remaining();
            buffer.position(buffer.limit());
            return written;
        });
        final SocketChannel actual = helloWorld.send(expected);
        assertEquals(expected, actual);
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Stubs {@link HelloWorld#set(byte[], int)} method of {@link Spy spied} {@code helloWorld} instance to return
     * specified {@code array} argument.
     */
    @BeforeEach
    private void stubSetArrayWithIndexToReturnSpecifiedArray() {
        when(helloWorld.set(any(byte[].class), anyInt())) // <1>
                .thenAnswer(i -> i.getArgument(0));       // <2>
    }

    // -----------------------------------------------------------------------------------------------------------------
    @Spy
    HelloWorld helloWorld;
}
