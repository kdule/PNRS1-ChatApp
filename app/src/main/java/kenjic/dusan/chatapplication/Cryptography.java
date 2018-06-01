package kenjic.dusan.chatapplication;

/**
 * Created by student on 1.6.2018.
 */

public class Cryptography {
    public native String cryptography(String message);

    static {
        System.loadLibrary("cryptography");
    }
}
