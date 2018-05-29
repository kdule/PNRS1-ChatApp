// IBinderForNotif.aidl
package kenjic.dusan.chatapplication;

// Declare any non-default types here with import statements
import kenjic.dusan.chatapplication.ICallbackForNotif;

interface IBinderForNotif {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    /* void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat,
            double aDouble, String aString);*/

    void setCallback(in ICallbackForNotif callback);

}
