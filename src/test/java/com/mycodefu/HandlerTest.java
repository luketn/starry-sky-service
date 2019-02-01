package com.mycodefu;

import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class HandlerTest {

    @Test
    public void drawStars_bytes() throws IOException {
        File file = new File("./test.png");
        Handler handler = new Handler();

        Map<String,Object> event = new HashMap<>();

        HashMap<String, String> queryStringParameters = new HashMap<>();

        queryStringParameters.put("width", "1200");
        queryStringParameters.put("height", "900");

        event.put("queryStringParameters", queryStringParameters);

        ApiGatewayResponse apiGatewayResponse = handler.handleRequest(event, null);
        byte[] bytes = Base64.getDecoder().decode(apiGatewayResponse.getBody());
        Files.write(file.toPath(), bytes);

        BufferedImage image = ImageIO.read(file);

        assertEquals(1200, image.getWidth());
        assertEquals(900, image.getHeight());

        if (getOsName().startsWith("Mac OS")) {
            Desktop.getDesktop().open(file);
        }
    }

    private static String getOsName() {
        String property = System.getProperty("os.name");
        return property;
    }
}