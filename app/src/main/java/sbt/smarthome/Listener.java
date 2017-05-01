package sbt.smarthome;

/**
 * Created by jormelcn on 1/05/17.
 *
 */

public interface Listener<T> {
    void onSuccess(T reply);
}
