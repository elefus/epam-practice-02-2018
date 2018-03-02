package com.epam.interpreter;

import java.io.*;

public class ViewFromConToCon extends ViewAbstract{
    public ViewFromConToCon(InputStream inputStream, OutputStream outputStream) {
        reader = new BufferedReader(new InputStreamReader(inputStream));
        writer = new BufferedWriter(new OutputStreamWriter(outputStream));
        fromConsole = true;
    }
}
