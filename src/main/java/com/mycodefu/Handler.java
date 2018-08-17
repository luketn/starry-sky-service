package com.mycodefu;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import javax.imageio.ImageIO;

public class Handler implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {

	public static final int DEFAULT_WIDTH = 1024;
	public static final int DEFAULT_HEIGHT = 768;

	@Override
	public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {

		Map<String, String> parameters = (Map<String, String>) input.get("queryStringParameters");
		int width;
		int height;
		if (parameters != null && parameters.size() == 2 && parameters.containsKey("width") && parameters.containsKey("height")) {
			width = Integer.parseInt(parameters.get("width"));
			height = Integer.parseInt(parameters.get("height"));
		} else {
			width = DEFAULT_WIDTH;
			height = DEFAULT_HEIGHT;
		}

		byte[] response = getStarsPngBytes(width, height);

		return ApiGatewayResponse.builder()
				.setStatusCode(200)
				.setBinaryBody(response)
				.setHeaders(Collections.singletonMap("Content-Type", "image/png"))
				.build();
	}

	byte[] getStarsPngBytes(int width, int height) {
		try {
			BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
			Graphics graphics = bufferedImage.getGraphics();
			drawStars(graphics, width, height);
			ByteArrayOutputStream byteArrayStream = new ByteArrayOutputStream();
			ImageIO.write(bufferedImage, "png", byteArrayStream);
			return byteArrayStream.toByteArray();
		} catch (IOException e) {
			System.out.println("An error occurred drawing stars: " + e.getMessage());
			e.printStackTrace();
			return new byte[] {};
		}
	}

	void drawStars(Graphics g, int width, int height) {
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, width, height);

		List<Color> colors = Arrays.asList(Color.WHITE, new Color(154, 191, 249), new Color(249, 238, 154), new Color(237, 179, 249));

		Random random = new Random();
		double factor = ((double)width * (double)height) / 786432d;
		int bound = (int)(200d * factor);
		int minimum = (int)(300d * factor);
		int numberOfStars = random.nextInt(bound) + minimum;
		for( int starNo = 0; starNo < numberOfStars; starNo++) {
			int diameter = random.nextInt(5) + 1;
			int starX = random.nextInt(width - 15);
			int starY = random.nextInt(height - 15);

			g.setColor(colors.get(random.nextInt(4)));
			g.fillOval(starX, starY, diameter, diameter);
		}
	}
}
