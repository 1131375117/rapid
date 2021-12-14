package cn.huacloud.taxpreference.common.utils;

import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;

/**
 * 密码解密工具
 * @author wangkh
 */
public class PasswordSecureUtil {

    private static final String PRIVATE_KEY = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAOs1tCS2zSRCZTM4c8juxDQlt4i0wUjhX33+gCZ8nTm4afmvdxO2l/2Rkon3jo97HU0srrYig5Xm1RFMXNs8m0NP1o1ERuTPQeP4U5D2ZyFsuceXPhD3A+n+ZgpxBH8bBaejJtyr0otEInV7IA/jv8wVZ5ALbrIeM5Wre1dHkVD5AgMBAAECgYAZDYDREkl6bboCwSYQBmKOnV/Vp08ZGKxHqsZykv5aKgrbpfVce6vnvcwqdtxP6xCdaj93tehnHI2gSI5xaZyt0/OMWu4ibDWPskL6xTv3hhF1HMUk6HJ45yt88djWIAaeNrlD7Ff5/KJhNxgPFikyDtRbZRdRC3Ka86955FdMiwJBAP117/vwzVSmSVQpK1nC6w6W1UY6OUmlab3aTUF7AuDaCUltTAkA3JgV9ByYU8LNG6D0KCrw5EctMGZG0ZeTH5cCQQDtkPUPxIVzT3RvEvrKzgd1/k2SAnE+mdgnTNI+DKrvpAcKgruKRRBLCKRNQ+aD63Au+c5mXFDrhR/3BQugSiXvAkEAoRwocaL9QcN5dQ++SI5Hz7w80SkNZSzJ7C4pUWgZykeOA6h/3nE3x0ydINgK1hZ+a28HYVRCJqQo0bpOW+Te/QJADnERdLPtqOTbnyT4OfvsWC/tZ0Lev396tGjnHv6GJatDAlCQNgecxfLwigHmsX5Og4yMD4ztHmQpXUrhOg1H4QJBAJAXKr3+z2vjzJA83OKP0Cn43BIu/vnwqvQgpbWJTSFxEwMTgzHpEjqZValGpRxM1KeKM7+7KHC2PjEuAgSk8Ak=";
    private static final String PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDrNbQkts0kQmUzOHPI7sQ0JbeItMFI4V99/oAmfJ05uGn5r3cTtpf9kZKJ946Pex1NLK62IoOV5tURTFzbPJtDT9aNREbkz0Hj+FOQ9mchbLnHlz4Q9wPp/mYKcQR/GwWnoybcq9KLRCJ1eyAP47/MFWeQC26yHjOVq3tXR5FQ+QIDAQAB";

    /**
     * RSA加密密码
     * @param password 密码
     * @return 被加密的密码
     */
    public static String encrypt(String password) {
        return Holder.RSA.encryptBase64(password, KeyType.PublicKey);
    }

    /**
     * RSA解密密码
     * @param encryptPassword 被加密的密码
     * @return 解密后的密码
     */
    public static String decrypt(String encryptPassword) {
        return Holder.RSA.decryptStr(encryptPassword, KeyType.PrivateKey);
    }

    /**
     * 登记式/静态内部类单例模式，懒加载。
     */
    static class Holder {
        static final RSA RSA = new RSA(PRIVATE_KEY, PUBLIC_KEY);
    }
}
