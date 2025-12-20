package com.williamcallahan.tui4j.examples.error;

import com.williamcallahan.tui4j.Command;
import com.williamcallahan.tui4j.Message;
import com.williamcallahan.tui4j.Model;
import com.williamcallahan.tui4j.Program;
import com.williamcallahan.tui4j.UpdateResult;

/**
 * Example program for error.
 * tui4j: examples/generic/src/main/java/com/williamcallahan/tui4j/examples/error/ErrorExample.java
 */
public class ErrorExample implements Model {

    @Override
    public Command init() {
        return this::failingCommand;
    }

    private Message failingCommand() {
        throw new RuntimeException("Error!");
    }

    @Override
    public UpdateResult<? extends Model> update(Message msg) {
        return UpdateResult.from(this);
    }

    @Override
    public String view() {
        return "Error?";
    }

    public static void main(String[] args) {
        new Program(new ErrorExample()).run();
    }
}
