package com.williamcallahan.tui4j;

/**
 * Defines the Model contract for init, update, and view.
 * tui4j: src/main/java/com/williamcallahan/tui4j/Model.java
 */
public interface Model {

    Command init();
    UpdateResult<? extends Model> update(Message msg);
    String view();
}
