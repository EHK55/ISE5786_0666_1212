package renderer;

import org.junit.jupiter.api.Test;

import primitives.Color;

class ImageWriterTests {

	@Test
	void testImageWriter() {
		// Defining constants to avoid hard-coding (magic numbers)
		final int nX = 800;
		final int nY = 500;
		final int step = 50;

		// Background and grid colors
		final Color bgColor = new Color(java.awt.Color.YELLOW);
		final Color gridColor = new Color(java.awt.Color.RED);

		// 1. ImageWriter initialization
		ImageWriter imageWriter = new ImageWriter(nX, nY);

		// 2. Double loop to iterate over all pixels in the image
		for (int i = 0; i < nX; i++) {
			for (int j = 0; j < nY; j++) {
				// 3. Color calculation using the ternary operator:
				// If the x or y index is a multiple of 'step', we are on the edge of a square
				// -> draw the grid.
				// Otherwise, draw the background.
				imageWriter.writePixel(i, j, (i % step == 0 || j % step == 0) ? gridColor : bgColor);
			}
		}

		// 4. Generate the image file in the "images" folder
		imageWriter.writeToImage("testImageWriter");
	}
}