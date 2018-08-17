package com.mycodefu;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class HandlerTest {

    @org.junit.Test
    public void drawStars_bytes() throws IOException {
        File file = new File("./test.png");
        Handler handler = new Handler();

        Map<String,Object> event = new HashMap<>();

        HashMap<String, String> queryStringParameters = new HashMap<>();

        queryStringParameters.put("width", "1024");
        queryStringParameters.put("height", "768");

        event.put("queryStringParameters", queryStringParameters);

        ApiGatewayResponse apiGatewayResponse = handler.handleRequest(event, null);
        byte[] bytes = Base64.getDecoder().decode(apiGatewayResponse.getBody());
        Files.write(file.toPath(), bytes);

        BufferedImage image = ImageIO.read(file);

        assertEquals(1024, image.getWidth());
        assertEquals(768, image.getHeight());
    }
}