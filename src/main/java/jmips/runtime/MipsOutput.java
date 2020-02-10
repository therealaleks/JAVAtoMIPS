package jmips.runtime;

import java.io.IOException;
import java.io.OutputStream;

public class MipsOutput {
    private final OutputStream out;

    public MipsOutput(OutputStream out) {
        this.out = out;
    }

    public int write(MipsBuffer buffer, int length) {
        if (length > buffer.bytes.length - buffer.offset) {
            throw new IllegalArgumentException("trying to write more than buffer size");
        }

        try {
            out.write(buffer.bytes, buffer.offset, length);
            return length;
        } catch (IOException e) {
            System.err.println("JMIPS: Exception while reading file");
            e.printStackTrace();
            return -1;
        }
    }

    public void close() {
        try {
            out.close();
        } catch (IOException e) {
            System.err.println("JMIPS: Exception while closing file");
            e.printStackTrace();
        }
    }
}
