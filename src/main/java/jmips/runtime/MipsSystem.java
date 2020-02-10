package jmips.runtime;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Random;
import java.util.Scanner;

public class MipsSystem {
    private static final Random RANDOM = new Random();

    /* Buffers */
    public static MipsBuffer buffer(byte[] value) {
        if (!new Throwable().getStackTrace()[1].getMethodName().equals("<clinit>")) {
            throw new IllegalStateException("buffers must be declared in static fields only");
        }

        return new MipsBuffer(value, 0);
    }

    public static MipsBuffer buffer(int size) {
        if (!new Throwable().getStackTrace()[1].getMethodName().equals("<clinit>")) {
            throw new IllegalStateException("buffers must be declared in static fields only");
        }

        return new MipsBuffer(new byte[size], 0);
    }

    public static MipsBuffer buffer(String value) {
        if (!new Throwable().getStackTrace()[1].getMethodName().equals("<clinit>")) {
            throw new IllegalStateException("buffers must be declared in static fields only");
        }

        byte[] bytes = value.getBytes(StandardCharsets.UTF_8);
        byte[] zeroTerminatedBytes = new byte[bytes.length + 1];
        System.arraycopy(bytes, 0, zeroTerminatedBytes, 0, bytes.length);
        return new MipsBuffer(zeroTerminatedBytes, 0);
    }

    public static MipsBuffer allocateBuffer(int length) {
        return new MipsBuffer(new byte[length], 0);
    }

    /* Printing */
    public static void print(int i) {
        System.out.print(i);
    }

    public static void print(float f) {
        System.out.print(f);
    }

    public static void print(double d) {
        System.out.print(d);
    }

    public void print(char c) {
        System.out.print(c);
    }

    public static void print(MipsBuffer string) {
        for (int i = string.offset; i < string.bytes.length; i++) {
            byte b = string.bytes[string.offset + i];
            if (b == 0) {
                return;
            }

            System.out.print((char) b);
        }

        throw new IllegalStateException("string was missing 0 terminator");
    }

    /* Reading */
    public static int readInt() {
        return Integer.parseInt(new Scanner(System.in).nextLine());
    }

    public static float readFloat() {
        return Float.parseFloat(new Scanner(System.in).nextLine());
    }

    public static double readDouble() {
        return Double.parseDouble(new Scanner(System.in).nextLine());
    }

    public static char readChar() {
        return (char) readByte();
    }

    public static String readString(int bufferSize) {
        byte[] buffer = new byte[bufferSize];

        for (int i = 0; i < bufferSize; i++) {
            int c = readByte();

            if (c == -1) {
                return new String(buffer, 0, i);
            }

            buffer[i] = (byte) c;

            if (c == '\n') {
                return new String(buffer, 0, i + 1);
            }
        }

        return new String(buffer);
    }

    /* Exit */
    public static void exit() {
        System.exit(0);
    }

    public static void exit(int value) {
        System.exit(value);
    }

    /* IO */
    public static MipsInput openFileRead(String fileName) {
        try {
            return new MipsInput(new FileInputStream(fileName));
        } catch (IOException e) {
            System.err.println("JMIPS: Exception while opening file");
            e.printStackTrace();
            return new MipsInput(null);
        }
    }

    public static MipsOutput openFileWrite(String fileName) {
        try {
            return new MipsOutput(new FileOutputStream(fileName));
        } catch (IOException e) {
            System.err.println("JMIPS: Exception while opening file");
            e.printStackTrace();
            return new MipsOutput(null);
        }
    }

    public static MipsOutput openFileAppend(String fileName) {
        try {
            return new MipsOutput(new FileOutputStream(fileName, true));
        } catch (IOException e) {
            System.err.println("JMIPS: Exception while opening file");
            e.printStackTrace();
            return new MipsOutput(null);
        }
    }

    public static MipsInput getStandardInput() {
        return new MipsInput(System.in);
    }

    public static MipsOutput getStandardOutput() {
        return new MipsOutput(System.out);
    }

    public static MipsOutput getStandardError() {
        return new MipsOutput(System.err);
    }

    /* Time */
    public static long getTime() {
        return System.currentTimeMillis();
    }

    private static int readByte() {
        try {
            return System.in.read();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
