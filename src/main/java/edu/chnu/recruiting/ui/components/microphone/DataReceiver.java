package edu.chnu.recruiting.ui.components.microphone;

import java.io.OutputStream;

@FunctionalInterface
public interface DataReceiver {

    public OutputStream getOutputStream();

}