package jmips.runtime;

import java.io.IOException;
import java.io.InputStream;

public class MipsInput {
    private final InputStream in;

    MipsInput(InputStream in) {
        this.in = in;
    }

    public int read(MipsBuffer buffer, int limit) {
        if (limit > buffer.bytes.length - buffer.offset) {
            throw new IllegalArgumentException("trying to read more than buffer size");
        }

        try {
            return Math.min(in.read(buffer.bytes, buffer.offset, limit), 0);
        } catch (IOException e) {
            System.err.println("JMIPS: Exception while reading file");
            e.printStackTrace();
            return -1;
        }
    }

    public void close() {
        try {
            in.close();
        } catch (IOException e) {
            System.err.println("JMIPS: Exception while closing file");
            e.printStackTrace();
        }
    }
}
