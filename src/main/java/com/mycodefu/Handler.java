package com.mycodefu;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.List;

public class Handler implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {

	private static final int DEFAULT_WIDTH = 1024;
	private static final int DEFAULT_HEIGHT = 768;

	private static final Random random = new Random();
	private static final List<Color> starColors = Arrays.asList(new Color(154, 191, 249), new Color(249, 238, 154), new Color(237, 179, 249));
	private static final int BORDER = 15;


	@Override
	public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {

		Map<String, String> parameters = (Map<String, String>) input.get("queryStringParameters");
		int width;
		int height;
		if (parameters != null) {
			if (parameters.containsKey("width")) {
				width = Integer.parseInt(parameters.get("width"));
			} else {
				width = DEFAULT_WIDTH;
			}
			if (parameters.containsKey("height")) {
				height = Integer.parseInt(parameters.get("height"));
			} else {
				height = DEFAULT_HEIGHT;
			}
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

	private byte[] getStarsPngBytes(int width, int height) {
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


	private static int numberOfStars(double width, double height) {
		double factor = (width * height) / 786432d;
		int bound = (int)(200d * factor);
		int minimum = (int)(300d * factor);
		return random.nextInt(bound) + minimum;
	}

	void drawStars(Graphics g, int width, int height) {
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, width, height);

		int numberOfStars = numberOfStars(width, height);
		for( int starNo = 0; starNo < numberOfStars; starNo++) {
			int diameter = random.nextInt(4) + 1;
			int starX = random.nextInt(width - BORDER);
			int starY = random.nextInt(height - BORDER);

			if (starNo % 7 == 0) {
				g.setColor(starColors.get(random.nextInt(starColors.size())));
			} else {
				g.setColor(Color.WHITE);
			}
			g.fillOval(starX, starY, diameter, diameter);
		}
	}
}
