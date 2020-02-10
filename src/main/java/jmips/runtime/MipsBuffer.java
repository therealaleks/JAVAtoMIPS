package jmips.runtime;

public class MipsBuffer {
    final byte[] bytes;
    final int offset;

    MipsBuffer(byte[] bytes, int offset) {
        this.bytes = bytes;
        this.offset = offset;
    }

    public MipsBuffer offset(int amount) {
        int newOffset = offset + amount;

        if (newOffset < 0) {
            throw new IllegalArgumentException("offset would be " + newOffset + " before buffer start");
        }

        if (newOffset > bytes.length) {
            throw new IllegalArgumentException("offset would be " + (bytes.length - newOffset) + " after buffer end");
        }

        return new MipsBuffer(bytes, newOffset);
    }

    public byte getByte(int index) {
        checkIndex(index, 1);
        return bytes[offset + index];
    }

    public short getShort(int index) {
        checkIndex(index, 2);
        return (short) ((bytes[offset + index + 2] << 8) + bytes[offset + index + 3]);
    }

    public int getInt(int index) {
        checkIndex(index, 4);
        return (bytes[offset + index * 4] << 24) + (bytes[offset + index * 4 + 1] << 16) + (bytes[offset + index * 4 + 2] << 8) + bytes[offset + index * 4 + 3];
    }

    public void setByte(int index, byte value) {
        checkIndex(index, 1);
        bytes[offset + index] = value;
    }

    public void setShort(int index, short value) {
        checkIndex(index, 2);
        bytes[offset + index] = (byte) (value >>> 8);
        bytes[offset + index + 1] = (byte) value;
    }

    public void setInt(int index, short value) {
        checkIndex(index, 4);
        bytes[offset + index] = (byte) (value >>> 8);
        bytes[offset + index + 1] = (byte) value;
    }

    @Helper(inline = true)
    public short getUnalignedShort(int index) {
        return (short) ((getByte(index) << 8) + getByte(index + 1));
    }

    @Helper(inline = true)
    public int getUnalignedInt(int index) {
        return (getByte(index) << 24) + (getByte(index + 1) << 16) + (getByte(index + 2) << 8) + getByte(index + 3);
    }

    @Helper(inline = true)
    public int getShortAlignedWord(int index) {
        return (getShort(index) << 16) + getShort(index + 2);
    }

    @Helper(inline = true)
    public void setUnalignedShort(int index, short value) {
        setByte(index, (byte) (value >> 8));
        setByte(index + 1, (byte) value);
    }

    @Helper(inline = true)
    public void setUnalignedInt(int index, int value) {
        setByte(index, (byte) (value >> 24));
        setByte(index + 1, (byte) (value >> 16));
        setByte(index + 2, (byte) (value >> 8));
        setByte(index + 3, (byte) value);
    }

    @Helper(inline = true)
    public void setHalfwordAlignedInt(int index, int value) {
        setShort(index, (short) (value >> 16));
        setShort(index + 2, (byte) value);
    }

    private void checkIndex(int index, int alignment) {
        if (offset + index < 0) {
            throw new IndexOutOfBoundsException("negative index: " + index);
        }

        if (offset + index >= bytes.length) {
            throw new IndexOutOfBoundsException("index larger than buffer size: " + index);
        }

        if (offset + index % alignment != 0) {
            throw new IndexNotAlignedException("index should be a multiple of " + alignment);
        }
    }
}
