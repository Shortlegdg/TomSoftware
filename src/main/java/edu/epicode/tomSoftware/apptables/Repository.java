package edu.epicode.tomSoftware.apptables;

import edu.epicode.tomSoftware.exceptionhandling.TomsException;

/**
 * A generic contract for a collection of stored elements.
 *
 * @param <K> The type of the identifier of an element.
 * @param <T> The type of the stored element.
 */
public interface Repository<K, T> {

    /**
     * @param id The identifier to look for.
     * @return true if an element with that identifier is present.
     */
    boolean exists(K id);

    /**
     * @param id The identifier of the wanted element.
     * @return The element, never null.
     * @throws TomsException If no element has that identifier. Reporting the
     *                       absence as an exception, instead of returning null,
     *                       means the caller cannot forget to check it.
     */
    T findById(K id) throws TomsException;

    /**
     * @param id The identifier of the element to remove.
     * @return true if something has been removed.
     * @throws TomsException If the element cannot be removed.
     */
    boolean deleteById(K id) throws TomsException;
}
