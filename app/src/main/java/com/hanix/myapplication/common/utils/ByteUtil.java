package com.hanix.myapplication.common.utils;

public class ByteUtil {

    //public static Byte DEFAULT_BYTE = new Byte((byte) 0);

    /**
     * <p>unsigned byte(바이트) 배열을 16진수 문자열로 바꾼다.</p>
     *
     * <pre>
     * ByteUtils.toHexString(null)                   = null
     * ByteUtils.toHexString([(byte)1, (byte)255])   = "01ff"
     * </pre>
     *
     * @param bytes unsigned byte's array
     * @return
     */
    public static String toHexString(byte[] bytes) {
        if (bytes == null) {
            return null;
        }

        StringBuffer result = new StringBuffer();
        for (byte b : bytes) {
            result.append(Integer.toString((b & 0xF0) >> 4, 16));
            result.append(Integer.toString(b & 0x0F, 16));
        }
        return result.toString();
    }

    /**
     * <p>8, 10, 16진수 문자열을 바이트 배열로 변환한다.</p>
     * <p>8, 10진수인 경우는 문자열의 3자리가, 16진수인 경우는 2자리가, 하나의 byte로 바뀐다.</p>
     *
     * <pre>
     * ByteUtils.toBytes(null)     = null
     * ByteUtils.toBytes("0E1F4E", 16) = [0x0e, 0xf4, 0x4e]
     * ByteUtils.toBytes("48414e", 16) = [0x48, 0x41, 0x4e]
     * </pre>
     *
     * @param digits 문자열
     * @param radix 진수(8, 10, 16만 가능)
     * @return
     * @throws NumberFormatException
     */
    public static byte[] toBytes(String digits, int radix) throws IllegalArgumentException, NumberFormatException {
        if (digits == null) {
            return null;
        }
        if (radix != 16 && radix != 10 && radix != 8) {
            throw new IllegalArgumentException("For input radix: \"" + radix + "\"");
        }
        int divLen = (radix == 16) ? 2 : 3;
        int length = digits.length();
        if (length % divLen == 1) {
            throw new IllegalArgumentException("For input string: \"" + digits + "\"");
        }
        length = length / divLen;
        byte[] bytes = new byte[length];
        for (int i = 0; i < length; i++) {
            int index = i * divLen;
            bytes[i] = (byte)(Short.parseShort(digits.substring(index, index+divLen), radix));
        }
        return bytes;
    }

    /**
     * <p>16진수 문자열을 바이트 배열로 변환한다.</p>
     * <p>문자열의 2자리가 하나의 byte로 바뀐다.</p>
     *
     * <pre>
     * ByteUtils.toBytesFromHexString(null)     = null
     * ByteUtils.toBytesFromHexString("0E1F4E") = [0x0e, 0xf4, 0x4e]
     * ByteUtils.toBytesFromHexString("48414e") = [0x48, 0x41, 0x4e]
     * </pre>
     *
     * @param digits 16진수 문자열
     * @return
     * @throws NumberFormatException
     * @see HexUtils.toBytes( String )
     */
    public static byte[] toBytesFromHexString(String digits) throws IllegalArgumentException, NumberFormatException {
        if (digits == null) {
            return null;
        }

        int length = digits.length();
        if (length % 2 == 1) {
            throw new IllegalArgumentException("For input string: \"" + digits + "\"");
        }

        length = length / 2;
        byte[] bytes = new byte[length];

        for (int i = 0; i < length; i++) {
            int index = i * 2;
            bytes[i] = (byte)(Short.parseShort(digits.substring(index, index+2), 16));
        }

        return bytes;
    }


}
