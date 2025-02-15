package harmony.nbt.io

import harmony.nbt.compression.*
import it.unimi.dsi.fastutil.io.*
import java.io.*

/**
 * Custom DataOutputStream for writing NBT tags.
 *
 * @param stream The underlying output stream.
 */
class TagDataOutputStream(
  stream: OutputStream,
  compressor: TagCompressor = GZIPTagCompressor,
) : DataOutputStream(FastBufferedOutputStream(compressor.output(stream)))

/**
 * Custom DataInputStream for reading NBT tags.
 *
 * @param stream The underlying input stream.
 */
class TagDataInputStream(
  stream: InputStream,
  compressor: TagCompressor = GZIPTagCompressor
) : DataInputStream(FastBufferedInputStream(compressor.input(stream)))
