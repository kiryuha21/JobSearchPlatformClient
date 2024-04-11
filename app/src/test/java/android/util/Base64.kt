package android.util

import java.util.Base64

object Base64 {
    @JvmStatic
    public fun encodeToString(input: ByteArray?, flags: Int): String {
        return Base64.getEncoder().encodeToString(input)
    }

    @JvmStatic
    public fun decode(str: String?, flags: Int): ByteArray {
        return Base64.getDecoder().decode(str)
    }
}