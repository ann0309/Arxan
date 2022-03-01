package pet.ca.simplearxan;

public class JNIUtils {

    static {
        System.loadLibrary("native-lib");
    }

    public static native void setEnv();

    public static native void root();

    public static native void hook();

    public static native void fdg();

    public static native void debugger();

    public static native void checksum();

}
