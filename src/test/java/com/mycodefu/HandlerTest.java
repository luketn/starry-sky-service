package com.mycodefu;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class HandlerTest {

    @org.junit.Test
    public void drawStars_bytes() throws IOException {
        File file = new File("./test2.png");
        Handler handler = new Handler();

        byte[] bytes = handler.drawStars(640, 480);
        Files.write(file.toPath(), bytes);
    }
}