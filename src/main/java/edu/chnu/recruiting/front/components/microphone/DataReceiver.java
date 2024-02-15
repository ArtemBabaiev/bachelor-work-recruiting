package edu.chnu.recruiting.front.components.microphone;

import java.io.OutputStream;

@FunctionalInterface
public interface DataReceiver {

    public OutputStream getOutputStream();

}