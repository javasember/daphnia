package org.daphnia.json;

import java.io.IOException;
import java.io.Reader;

public class StepReader {
    int c;
    Reader r;
    
    public StepReader(Reader r) {
        this.r = r;
        this.c = -1;
    }

    public int peek() throws IOException {
        return c > -1 ? c : (c = r.read());
    }
    
    public int pop() throws IOException {
        int r = peek();
        c = -1;
        return r;
    }
    
    public int readUniChar() throws IOException {
        char[] b = new char[4];
        r.read(b);
        return Integer.parseInt(new String(b), 16);
    }
}
