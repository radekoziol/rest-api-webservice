package com.app.exceptions;

import java.util.NoSuchElementException;

public class NoSuchNoteException extends NoSuchElementException {

    public NoSuchNoteException() {
        super("There is no notes with given title\n");
    }
}
