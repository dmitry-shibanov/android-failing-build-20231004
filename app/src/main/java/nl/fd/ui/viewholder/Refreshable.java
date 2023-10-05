package nl.fd.ui.viewholder;

import io.reactivex.Flowable;

/**
 * Marks an object
 */
public interface Refreshable {

    void subscribe(Flowable<Long> refreshInterval);

    void unsubscribe();

}
