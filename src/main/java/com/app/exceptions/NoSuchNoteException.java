package com.app.exceptions;

import java.util.NoSuchElementException;

public class NoSuchNoteException extends NoSuchElementException {

    public NoSuchNoteException() {
        super("There are no notes with given title\n");
    }
}
