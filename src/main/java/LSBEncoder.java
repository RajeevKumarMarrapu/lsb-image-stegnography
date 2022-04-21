

import java.awt.image.BufferedImage;

public class LSBEncoder {
	static int row = 0, col = 0;
	
	public static void encode(BufferedImage imgInput, BufferedImage imgOut, byte[] chars, int width, int height) {
		int count = 0;
		
		loop: for (; row < width; row++) {
			for (; col < height; col++) {
				
				if (count == chars.length)
					break loop;
				
				int pixel = imgInput.getRGB(row, col);
				
				int alpha = (pixel >> 24) & 255;
				int red = (pixel >> 16) & 255;
				int green = (pixel >> 8) & 255;
				int blue = (pixel) & 255;
				
				alpha = (alpha & 252) | (chars[count] >> 6 & 3);
				red = (red & 252) | (chars[count] >> 4 & 3);
				green = (green & 252) | (chars[count] >> 2 & 3);
				blue = (blue & 252) | (chars[count] & 3);
				
				
				pixel = alpha << 24 | red << 16 | green << 8 | blue;
				imgOut.setRGB(row, col, pixel);
				
				count++;
			}
			col = 0;
		}
	}
}