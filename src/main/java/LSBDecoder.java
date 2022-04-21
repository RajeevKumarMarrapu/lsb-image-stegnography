
import java.awt.image.BufferedImage;


public class LSBDecoder {

	static int i = 0, j = 0;

	public static boolean isEncodedImage(BufferedImage image, int width, int height) {
		boolean isEncoded = new String(decode(image, width, height, 7)).equals("?start?");
		i = 0;
		j = 0;

		return isEncoded;
	}
	
	public static byte[] decode(BufferedImage image, int width, int height, int length) {
		
		int count = 0;
		byte[] plainText = new byte[length];
		
		loop: for (; i < width; i++) {
			for (; j < height; j++) {
				
				if (count == length)
					break loop;
				
				int pixel = image.getRGB(i, j);
				
				int alpha = (pixel >> 24) & 255;
				int red = (pixel >> 16) & 255;
				int green = (pixel >> 8) & 255;
				int blue = (pixel) & 255;
				
				alpha = (alpha & 3);
				red = (red & 3);
				green = (green & 3);
				blue = (blue & 3);
				
				
				pixel = alpha << 6 | red << 4 | green << 2 | blue;
				plainText[count] = (byte) pixel;
				
				count++;
			}
			j = 0;
		}
		j = 0;

		return plainText;
	}
	
	public static int getEncodedLength(BufferedImage image, int width, int height) {
		StringBuilder textLength = new StringBuilder();
		int count = 0;
		
		loop: for (; i < width; i++) {
			for (; j < height; j++) {
				
				int pixel = image.getRGB(i, j);
				
				int alpha = (pixel >> 24) & 255;
				int red = (pixel >> 16) & 255;
				int green = (pixel >> 8) & 255;
				int blue = (pixel) & 255;
				
				alpha = (alpha & 3);
				red = (red & 3);
				green = (green & 3);
				blue = (blue & 3);
				
				int temp = alpha << 6 | red << 4 | green << 2 | blue;
				
				if (temp == '?')
					count++;
				if (count == 3)
					break loop;
				
				textLength.append((char)temp);
			}
			j = 0;
		}
		j++;

		return Integer.parseInt(textLength.substring(7));
	}
}