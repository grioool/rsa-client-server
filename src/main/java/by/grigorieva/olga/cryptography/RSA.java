package by.grigorieva.olga.cryptography;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.util.Random;

public class RSA {


    private static final BigInteger TWO = new BigInteger("2");
BigInteger p;
BigInteger q;
BigInteger d;

    BigInteger eulerFunc = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));
    BigInteger e = calculateE(d, eulerFunc);
    public static byte[] readBytesFromFile(File file) {
        byte[] bytes = {};
        try {
            bytes = Files.readAllBytes(file.toPath());
        } catch (IOException e) {
        }
        return bytes;
    }
        public static BigInteger fastExp(BigInteger a, BigInteger z, BigInteger r) {
            BigInteger a1 = a;
            BigInteger z1 = z;
            BigInteger x = BigInteger.ONE;
            while (z1.compareTo(BigInteger.ZERO) != 0) {
                while (z1.mod(TWO).compareTo(BigInteger.ZERO) == 0) {
                    z1 = z1.divide(TWO);
                    a1 = a1.multiply(a1).mod(r);
                }
                z1 = z1.subtract(BigInteger.ONE);
                x = x.multiply(a1).mod(r);
            }
            return x;
        }

        public static boolean testFerme(BigInteger x) {
            if (x.compareTo(TWO) == 0) {
                return true;
            }
            Random random = new Random();
            for (int i = 0; i < 100; i++) {
                BigInteger rand = new BigInteger(String.valueOf(random.nextInt()));
                BigInteger a = rand.mod(x.subtract(TWO)).add(TWO);
                if (gcd(a, x).compareTo(BigInteger.ONE) != 0) {
                    return false;
                }
                if (modpow(a, x.subtract(BigInteger.ONE), x).compareTo(BigInteger.ONE) != 0) {
                    return false;
                }
            }
            return true;
        }

        public static BigInteger gcd(BigInteger a, BigInteger b) {
            if (b.compareTo(BigInteger.ZERO) == 0) {
                return a;
            }
            return gcd(b, a.mod(b));
        }

        public static BigInteger euclid(BigInteger a, BigInteger b) {
            BigInteger d0 = a;
            BigInteger d1 = b;
            BigInteger d2;
            BigInteger y0 = BigInteger.ZERO;
            BigInteger y1 = BigInteger.ONE;
            BigInteger y2;
            BigInteger q;

            while (d1.compareTo(BigInteger.ONE) > 0) {
                q = d0.divide(d1);
                d2 = d0.mod(d1);
                y2 = y0.subtract(q.multiply(y1));
                d0 = d1;
                d1 = d2;
                y0 = y1;
                y1 = y2;
            }
            return y1;
        }

        private static BigInteger mul(BigInteger a, BigInteger b, BigInteger m) {
            if (b.compareTo(BigInteger.ONE) == 0) {
                return a;
            }
            if (b.mod(TWO).compareTo(BigInteger.ZERO) == 0) {
                BigInteger t = mul(a, b.divide(TWO), m);
                return t.multiply(TWO).mod(m);
            }
            return mul(a, b.subtract(BigInteger.ONE), m).add(a).mod(m);
        }

        private static BigInteger modpow(BigInteger a, BigInteger b, BigInteger m) {
            if (b.compareTo(BigInteger.ZERO) == 0) {
                return BigInteger.ONE;
            }
            if (b.mod(TWO).compareTo(BigInteger.ZERO) == 0) {
                BigInteger t = modpow(a, b.divide(TWO), m);
                return mul(t, t, m).mod(m);
            }
            return mul(modpow(a, b.subtract(BigInteger.ONE), m), a, m).mod(m);
        }

    public boolean isPrimeNumber(String num) {
        if (!num.equals("") && num.matches("[0-9]+")) {
            BigInteger number = new BigInteger(num);
            return testFerme(number);
        }
        return false;
    }

    public boolean checkD(String dInputField, String pInput, String qInput) {
        BigInteger p = new BigInteger(pInput);
        BigInteger q = new BigInteger(qInput);
        if (!dInputField.equals("") && dInputField.matches("[0-9]+")) {
            BigInteger d = new BigInteger(dInputField);
            BigInteger eulerFunc = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));
            if (d.compareTo(eulerFunc) < 0 && d.compareTo(BigInteger.ONE) > 0) {
                return gcd(d, eulerFunc).equals(BigInteger.ONE);
            }
        }
        return false;
    }

    private BigInteger calculateE(BigInteger d, BigInteger eulerFunc) {
        BigInteger e = euclid(eulerFunc, d);
        if (e.compareTo(BigInteger.ZERO) < 0) {
            e = e.add(eulerFunc);
        }
        return e;
    }
    }

