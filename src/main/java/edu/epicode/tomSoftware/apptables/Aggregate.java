package edu.epicode.tomSoftware.apptables;

/**
 * The aggregate interface for the iteration of the tables.
 * @param <T>
 */
public interface Aggregate<T> {

    Iterator<T> createIterator();

}
