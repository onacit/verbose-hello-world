package com.github.jinahya.hello;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.BufferOverflowException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousByteChannel;
import java.nio.channels.FileChannel;
import java.nio.channels.WritableByteChannel;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

/**
 * An interface for generating <a href="#hello-world-bytes">hello-world-bytes</a> to various targets.
 *
 * <h2 id="hello-world-bytes">hello-world-bytes</h2>
 * A sequence of {@value #BYTES} bytes, representing the "{@code hello, world}" string encoded in {@link
 * java.nio.charset.StandardCharsets#US_ASCII US-ASCII} character set, which consists of {@code 0x68('h')} followed by
 * {@code 0x65('e')}, {@code 0x6C('l')}, {@code 0x6C('l')}, {@code 0x6F('o')}, {@code 0x2C(',')}, {@code 0x20(' ')},
 * {@code 0x77('w')}, {@code 0x6F('o')}, {@code 0x72('r')}, {@code 0x6C('l')}, and {@code 0x64('d')}.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 */
@FunctionalInterface
public interface HelloWorld {

    /**
     * The length of the <a href="#hello-world-bytes">hello-world-bytes</a>. The value is {@value}.
     *
     * @see <a href="#hello-world-bytes">hello-world-bytes</a>
     */
    int BYTES = 12;

    /**
     * Sets <a href="#hello-world-bytes">hello-world-bytes</a> on specified array starting at specified position.
     * <p>
     * The elements in the array, on successful return, will be set as follows.
     * <blockquote><pre>{@code
     *   0                                                               array.length
     *   |                                                               |
     * |   |...|'h'|'e'|'l'|'l'|'o'|','|' '|'w'|'o'|'r'|'l'|'d'|   |...|
     *           |                                               |
     *      0 <= index                                           (index + BYTES) <= array.length
     * }</pre></blockquote>
     *
     * @param array the array on which bytes are set.
     * @param index the starting index of the {@code array}.
     * @throws NullPointerException      if {@code array} is {@code null}.
     * @throws IndexOutOfBoundsException if {@code index} is negative or ({@code index} + {@link #BYTES}) is greater
     *                                   than {@code array.length}.
     */
    void set(byte[] array, int index);

    /**
     * Sets <a href="#hello-world-bytes">hello-world-bytes</a> on specified array starting at {@code 0}.
     *
     * @param array the array on which bytes are set.
     * @throws NullPointerException      if {@code array} is {@code null}.
     * @throws IndexOutOfBoundsException if {@code array.length} is less than {@link #BYTES}.
     * @implSpec The implementation in this class invokes {@link #set(byte[], int)} method with specified {@code array}
     * and {@code 0}.
     * @see #set(byte[], int)
     */
    default byte[] set(final byte[] array) {
        if (array == null) {
            throw new NullPointerException("array is null");
        }
        if (array.length < BYTES) {
            throw new IndexOutOfBoundsException("array.length(" + array.length + ") < " + BYTES);
        }
        set(array, 0);
        return array;
    }

    /**
     * Writes <a href="#hello-world-bytes">hello-world-bytes</a> to specified output stream.
     *
     * @param stream the output stream to which bytes are written.
     * @throws NullPointerException if {@code stream} is {@code null}.
     * @throws IOException          if an I/O error occurs.
     * @implSpec The implementation in this class invokes {@link #set(byte[])} method with an array of {@value
     * com.github.jinahya.hello.HelloWorld#BYTES} bytes and writes the array to specified {@code stream} using {@link
     * OutputStream#write(byte[])} method.
     * @see #set(byte[])
     * @see OutputStream#write(byte[])
     */
    default void write(final OutputStream stream) throws IOException {
        if (stream == null) {
            throw new NullPointerException("stream is null");
        }
        final byte[] array = new byte[BYTES];
        set(array);
        stream.write(array);
        return stream;
    }

    /**
     * Appends <a href="#hello-world-bytes">hello-world-bytes</a> to specified file.
     *
     * @param file the file to which bytes are appended.
     * @throws NullPointerException if {@code file} is {@code null}.
     * @throws IOException          if an I/O error occurs.
     * @implSpec The implementation in this class creates a {@link FileOutputStream} from {@code file} as append mode
     * and invokes {@link #write(OutputStream)} method with the stream.
     * @see java.io.FileOutputStream#FileOutputStream(File, boolean)
     * @see #write(OutputStream)
     */
    default void append(final File file) throws IOException {
        if (file == null) {
            throw new NullPointerException("file is null");
        }
        final OutputStream stream = new FileOutputStream(file, true);
        try {
            write(stream);
            stream.flush();
        } finally {
            stream.close();
        }
        return file;
    }

    /**
     * Sends <a href="#hello-world-bytes">hello-world-bytes</a> through specified socket.
     *
     * @param socket the socket to which bytes are sent.
     * @param <T>    socket type parameter
     * @return given {@code socket}.
     * @throws NullPointerException if {@code socket} is {@code null}.
     * @throws IOException          if an I/O error occurs.
     * @implSpec The implementation in this class invokes {@link #write(OutputStream)} method with {@link
     * Socket#getOutputStream() socket.outputStream} and returns {@code socket}.
     * @see Socket#getOutputStream()
     * @see #write(OutputStream)
     */
    default <T extends Socket> T send(final T socket) throws IOException {
        if (socket == null) {
            throw new NullPointerException("socket is null");
        }
        write(socket.getOutputStream());
        return socket;
    }

    /**
     * Writes <a href="#hello-world-bytes">hello-world-bytes</a> to specified data output.
     *
     * @param data the data output to which bytes are written.
     * @throws NullPointerException if {@code data} is {@code null}.
     * @throws IOException          if an I/O error occurs.
     * @implSpec The implementation in this class invokes {@link #set(byte[])} with an array of {@value
     * com.github.jinahya.hello.HelloWorld#BYTES} bytes and writes the array to specified data output using {@link
     * DataOutput#write(byte[])} method.
     * @see #set(byte[])
     * @see DataOutput#write(byte[])
     */
    default void write(final DataOutput data) throws IOException {
        if (data == null) {
            throw new NullPointerException("data is null");
        }
        final byte[] array = new byte[BYTES];
        set(array);
        data.write(array);
        return data;
    }

    /**
     * Writes <a href="#hello-world-bytes">hello-world-bytes</a> starting at the current file pointer of specified
     * random access file.
     *
     * @param file the random access file to which bytes are written.
     * @throws NullPointerException if {@code file} argument is {@code null}.
     * @throws IOException          if an I/O error occurs.
     * @implSpec The implementation in this class invokes {@link #set(byte[])} with an array of {@value
     * com.github.jinahya.hello.HelloWorld#BYTES} bytes and writes the array to specified random access file using
     * {@link RandomAccessFile#write(byte[])} method.
     * @see #set(byte[])
     * @see RandomAccessFile#write(byte[])
     */
    default void write(final RandomAccessFile file) throws IOException {
        if (file == null) {
            throw new NullPointerException("file is null");
        }
        return null;
    }

    /**
     * Puts <a href="#hello-world-bytes">hello-world-bytes</a> on specified byte buffer. The buffer's position, on
     * successful return, is incremented by {@value com.github.jinahya.hello.HelloWorld#BYTES}.
     *
     * @param buffer the byte buffer on which bytes are put.
     * @param <T>    byte buffer type parameter
     * @return given {@code buffer}.
     * @throws NullPointerException    if {@code buffer} is {@code null}.
     * @throws BufferOverflowException if {@link ByteBuffer#remaining() buffer.remaining} is less than {@value
     *                                 com.github.jinahya.hello.HelloWorld#BYTES}.
     * @implSpec The implementation in this class, if specified buffer {@link ByteBuffer#hasArray() has a
     * backing-array}, invokes {@link #set(byte[], int)} with the buffer's {@link ByteBuffer#array() backing array} and
     * ({@link ByteBuffer#arrayOffset() buffer.arrayOffset} + {@link ByteBuffer#position() buffer.position}) and then
     * manually increments the buffer's {@link ByteBuffer#position(int) position} by {@value
     * com.github.jinahya.hello.HelloWorld#BYTES}. Otherwise, this method invokes {@link #set(byte[])} method with an
     * array of {@value com.github.jinahya.hello.HelloWorld#BYTES} bytes and puts the array on the buffer using {@link
     * ByteBuffer#put(byte[])} method which increments the {@code position} by itself.
     * @see #set(byte[], int)
     * @see #set(byte[])
     * @see ByteBuffer#put(byte[])
     */
    default <T extends ByteBuffer> T put(final T buffer) {
        if (buffer == null) {
            throw new NullPointerException("buffer is null");
        }
        return buffer;
    }

    /**
     * Writes <a href="#hello-world-bytes">hello-world-bytes</a> to specified channel.
     * <p>
     * This method invokes {@link #put(ByteBuffer)} method with a newly allocated byte buffer of {@value
     * com.github.jinahya.hello.HelloWorld#BYTES} bytes and, {@link ByteBuffer#flip() flips} it, writes all remaining
     * bytes in the buffer to specified channel using {@link WritableByteChannel#write(ByteBuffer)} method.
     * <blockquote><pre>{@code
     * ByteBuffer buffer = ByteBuffer.allocate(BYTES); // position = 0, limit,capacity = 12
     * put(buffer); // position -> 12
     * buffer.flip(); // limit -> 12(position), position -> 0
     * while (buffer.hasRemaining()) { // not all remaining bytes may be written at once
     *     channel.write(buffer);
     * }
     * }</pre></blockquote>
     *
     * @param channel the channel to which bytes are written.
     * @param <T>     channel type parameter
     * @return given {@code channel}.
     * @throws NullPointerException if {@code channel} is {@code null}.
     * @throws IOException          if an I/O error occurs.
     * @implSpec The implementation in this class invokes {@link #put(ByteBuffer)} with a buffer of {@value
     * com.github.jinahya.hello.HelloWorld#BYTES} bytes and writes the buffer to {@code channel}.
     * @see #put(ByteBuffer)
     * @see WritableByteChannel#write(ByteBuffer)
     */
    default <T extends WritableByteChannel> T write(final T channel) throws IOException {
        if (channel == null) {
            throw new NullPointerException("channel is null");
        }
        return channel;
    }

    /**
     * Appends <a href="#hello-world-bytes">hello-world-bytes</a> to the end of specified path and returns the path. The
     * size of specified path, on successful return, increases by {@value com.github.jinahya.hello.HelloWorld#BYTES}.
     *
     * @param path the path to which bytes are appended.
     * @param <T>  path type parameter
     * @return given {path}.
     * @throws NullPointerException if {@code path} is {@code null}.
     * @throws IOException          if an I/O error occurs.
     * @implSpec The implementation in this class opens a {@link FileChannel}, from specified path, as append mode and
     * invokes {@link #write(WritableByteChannel)} method with it.
     * @see FileChannel#open(Path, OpenOption...)
     * @see #write(WritableByteChannel)
     */
    default <T extends Path> T append(final T path) throws IOException {
        if (path == null) {
            throw new NullPointerException("path is null");
        }
        return path;
    }

    /**
     * Sends <a href="#hello-world-bytes">hello-world-bytes</a> through specified socket channel and returns the socket
     * channel.
     * <p>
     * This method invokes {@link #write(WritableByteChannel)} method with specified socket channel and returns the
     * result.
     *
     * @param socket the socket channel to which bytes are sent.
     * @param <T>    socket channel type parameter
     * @return given {@code socket}.
     * @throws IOException if an I/O error occurs.
     * @see #write(WritableByteChannel)
     * @deprecated Use {@link #write(WritableByteChannel)}.
     */
    byte[] set(byte[] array, int index);

    /**
     * Sets {@value SIZE} bytes of {@code hello, world} string on given array starting at {@code 0} index.
     *
     * @param array the array to which {@code hello, world} bytes are set.
     * @return given array.
     * @throws NullPointerException     if {@code array} is {@code null}
     * @throws IllegalArgumentException if {@code array.length} is less than {@value SIZE}
     * @see #set(byte[], int)
     */
    default byte[] set(final byte[] array) {
        if (array == null) {
            throw new NullPointerException("array is null");
        }
        if (array.length < SIZE) {
            throw new IndexOutOfBoundsException("array.length(" + array.length + ") < " + SIZE);
        }
        return set(array, 0);
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Writes {@value #SIZE} bytes of {@code hello, world} on specified output stream and returns the output stream.
     *
     * @param stream the output stream on which bytes are written
     * @param <T>    output stream type parameter
     * @return given output stream
     * @throws IOException if an I/O error occurs.
     */
    default <T extends OutputStream> T write(final T stream) throws IOException {
        if (stream == null) {
            throw new NullPointerException("stream is null");
        }
        // @todo: implement!
        return null;
    }

    default <T extends File> T write(final T file) throws IOException {
        if (file == null) {
            throw new NullPointerException("file is null");
        }
        final OutputStream stream = new FileOutputStream(file);
        try {
            write(stream);
            stream.flush();
        } finally {
            stream.close();
        }
        return file;
    }

    default <T extends Socket> T send(final T socket) throws IOException {
        if (socket == null) {
            throw new NullPointerException("socket is null");
        }
        final OutputStream stream = socket.getOutputStream();
        write(stream);
        stream.flush();
        return socket;
    }

    // -----------------------------------------------------------------------------------------------------------------
    default <T extends ByteBuffer> T put(final T buffer) {
        if (buffer == null) {
            throw new NullPointerException("buffer is null");
        }
        if (buffer.remaining() < SIZE) {
            throw new BufferOverflowException();
        }
        // @todo: implement!
        return null;
    }

    default <T extends WritableByteChannel> T write(final T channel) throws IOException {
        if (channel == null) {
            throw new NullPointerException("channel is null");
        }
        // @todo: implement!
        return null;
    }

    default <T extends Path> T write(final T path) throws IOException {
        if (path == null) {
            throw new NullPointerException("path is null");
        }
        final WritableByteChannel channel = FileChannel.open(
                path, StandardOpenOption.CREATE, StandardOpenOption.WRITE, StandardOpenOption.DSYNC);
        try {
            write(channel);
        } finally {
            channel.close();
        }
        return path;
    }

    default <T extends SocketChannel> T send(final T socket) throws IOException {
        if (socket == null) {
            throw new NullPointerException("socket is null");
        }
        return write(socket);
    }
}
