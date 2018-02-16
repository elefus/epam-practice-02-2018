package com.epam;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;

/*
 * A = 0b00
 * T = 0b01
 * G = 0b10
 * C = 0b11
 */
public class FastGenomeSearcher {
    class Window {
        int limitedSize;
        long[] data;
        int startIndex;
        final int size;
        final long bitSizeModMask;
        final long bitSizeMod;
        final int bigSize;
        /* limitedSize is natural size of array + virtual space
         * size is size in SYMBOL number (size of ACG = 3 but used 6 bits)
         * startIndex is physic index where data begins
         * bitSizeMod is amount of symbols which doesn't fill last bit
         * bigSize is size in long cell
         * bitSizeModMask -- mask which useful and consists of (64 - bitSizeMod) zeros and bitSizeMod ones
         */
        Window(long[] data, int size) {
            this.size = size;
            this.startIndex = 0;
            this.bigSize = ((size & 0x1F) == 0 ? 0 : 1) + (size >> 5);

            this.limitedSize = bigSize *  (64 - Utils.clz[size]);
            this.data = new long[limitedSize];
            System.arraycopy(data, 0, this.data, 0, data.length);
            this.bitSizeMod = (size << 1) & 0x3F;
            this.bitSizeModMask = 1 << bitSizeMod;
        }

        Window bigShiftAndBigAdd(long value) {
            if (startIndex < limitedSize) {
                data[startIndex++ + bigSize] = value;
            } else {
                System.arraycopy(data, startIndex + 1, data, 0, bigSize - 1);
                data[bigSize] = value;
                startIndex = 0;
            }
            return this;
        }

        Window shiftAndAdd(long value) {
            for (int i = startIndex; i < startIndex + bigSize; i++) {
                data[i] >>>= 2;
                data[i] += (data[i + 1] & 0b11) << 62;
            }
            data[startIndex + bigSize - 1] += ((value & 0b11) << 62) >>> (64 - bitSizeMod);
            return this;
        }

        public boolean equalToData(long[] data) {
            int t = startIndex + bigSize - 1;
            for (int i = startIndex; i < t; ++i) {
                if (this.data[i] != data[i]) {
                    return false;
                }
            }
            ++t;
            return (this.data[t] & bitSizeModMask) == (data[t] & bitSizeModMask);
        }

        public long[] getCleanData() {
            long[] data = new long[bigSize];
            System.arraycopy(this.data, startIndex, data, 0, bigSize);
            data[bigSize - 1] &= ~bitSizeModMask;
            return data;
        }
        @Override
        public String toString() {
            return "Size: " + size + ", BIG size: " + bigSize + " limited size: " + limitedSize + " start index: " + startIndex
                    + "\nData: " + Utils.printBinLongArray(data);
        }
    }

    private class LongArrayWrapper {
        long[] data;
        LongArrayWrapper(long[] data) {
            this.data = data;
        }
    }
    /*
     * size in symbols
     *
     */
    /*private static HashMap<LongArrayWrapper, LongArrayWrapper> _findAllSubseq(BufferedReader input, int size) {
        char[] buffer = new char[1 << 16];
        int actualSize = 0;
        try (input){
            actualSize = input.read(buffer, 0, buffer.length);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/

    /*private HashMap<LongArrayWrapper, LongArrayWrapper> _findAllSubseq(byte[] buffer, int size) {
        long[] compressed = new long[buffer.length >> 5];
        Window[] windows = new Window[32];
        int[] pointers = new int[32];
        for (byte i = 0; i < 32; ++i) {
            windows[i] = new Window(Utils.unsafeCompress(,(byte) 32), size);
        }
    }*/
}
