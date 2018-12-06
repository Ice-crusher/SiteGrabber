package com.company.ice.sitegrabber.Interfaces;

/**
 * Created by Ice on 21.09.2017.
 */

public interface MainPresentFunction<V> {
    void attachView(V view);
    void detachView();
}
