package com.mycodefu;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import javax.imageio.ImageIO;

public class Handler implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {

	private static final Logger LOG = LogManager.getLogger(Handler.class);
	public static final int DEFAULT_WIDTH = 640;
	public static final int DEFAULT_HEIGHT = 480;

	@Override
	public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {
		LOG.info("received: {}", input);

		byte[] response = drawStars();

		return ApiGatewayResponse.builder()
				.setStatusCode(200)
				.setBinaryBody(response)
				.setHeaders(Collections.singletonMap("Content-Type", "image/png"))
				.build();
	}

	private byte[] drawStars() {
		try {
			BufferedImage bufferedImage = new BufferedImage(DEFAULT_WIDTH, DEFAULT_HEIGHT, BufferedImage.TYPE_INT_RGB);
			drawStars(bufferedImage.getGraphics(), DEFAULT_WIDTH, DEFAULT_HEIGHT);
			ByteArrayOutputStream imageByteArrayOutputStream = new ByteArrayOutputStream();
			ImageIO.write(bufferedImage, "png", imageByteArrayOutputStream);
			return imageByteArrayOutputStream.toByteArray();
		} catch (IOException e) {
			return new byte[] {};
		}
	}

	private void drawStars(Graphics g, int defaultWidth, int defaultHeight) {
		g.setColor(Color.BLACK);

		int width = defaultWidth;
		int height = defaultHeight;

		g.fillRect(0, 0, width, height);

		g.setColor(Color.WHITE);

		List<Color> colors = Arrays.asList(Color.WHITE, new Color(154, 191, 249), new Color(249, 238, 154), new Color(237, 179, 249));

		Random random = new Random();
		int numberOfStars = random.nextInt(200) + 300;
		for( int starNo = 0; starNo < numberOfStars; starNo++) {
			int diameter = random.nextInt(5) + 1;
			int starX = random.nextInt(width - 15);
			int starY = random.nextInt(height - 15);

			g.setColor(colors.get(random.nextInt(4)));

			g.fillOval(starX, starY, diameter, diameter);
		}
	}
}
