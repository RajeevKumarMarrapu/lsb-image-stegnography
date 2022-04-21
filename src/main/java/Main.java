

import java.awt.image.BufferedImage;
import java.io.File;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Scanner;

import javax.imageio.ImageIO;

public class Main {
	public static void main(String[] args) throws Exception {
		Scanner sc = new Scanner(System.in);
		
		Main obj = new Main();
		int choice = 0;
		do {
			System.out.println("1.Encode\n2.Decode\n3.Exit");
			choice = sc.nextInt();sc.nextLine();
			
			switch (choice) {
				case 1: 
					    System.out.println("Enter the input image path(full path): ");
					    String inputFileName = sc.nextLine();
					
					    System.out.println("Enter the message: ");
						String msg = sc.nextLine();
					
						System.out.println("Enter the output image path(Stegno-image full path): ");
						String outputFileName = sc.nextLine();
					
						obj.encode(inputFileName, msg, outputFileName);
						break;
				case 2: 
						System.out.println("Enter the Stegno-image full path: ");
						inputFileName = sc.nextLine();
						obj.decode(inputFileName);
						break;
				case 3: 
						System.exit(0);
						break;
				default: 
						System.out.println("Enter valid choice");		
			}
		} while (choice != 3);
		sc.close();
	}
	
	public void encode(String inputFilename, String msg, String outputFileName) throws Exception {
		RSA rsa = new RSA();
		PublicKey publicKey = rsa.getPublic("publicKey.txt");
		byte[] encrypted = rsa.encrypt(publicKey, msg);
		
		BufferedImage inputImage =  ImageIO.read(new java.io.File(inputFilename));
		int width = inputImage.getWidth();
		int height = inputImage.getHeight();
		
		BufferedImage stagedOutputImaged =  new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
		for (int row = 0; row < width; row++)
			for (int col = 0; col < height; col++)
				stagedOutputImaged.setRGB(row, col, inputImage.getRGB(row, col));
		
		if (8 + encrypted.length > width * height) {
			System.out.println("Image is not Big enough for data");
			return;
		}
		
		byte[] start = ("?start?" + encrypted.length + "?").getBytes();
		
		LSBEncoder.encode(inputImage, stagedOutputImaged, start, width, height);
		LSBEncoder.encode(inputImage, stagedOutputImaged, encrypted, width, height);

		ImageIO.write(stagedOutputImaged, "png", new File(outputFileName));
		
		System.out.println("data encoded successfully");
	}
	
	public void decode(String inputFileName) throws Exception {
		RSA rsa = new RSA();
		PrivateKey privateKey = rsa.getPrivate("privateKey.txt");
		
		BufferedImage image =  ImageIO.read(new java.io.File(inputFileName));
		int width = image.getWidth();
		int height = image.getHeight();
		
		byte[] cipherData = null;
		
		if (LSBDecoder.isEncodedImage(image, width, height)) {
			int length = LSBDecoder.getEncodedLength(image, width, height);
			cipherData = LSBDecoder.decode(image, width, height, length);
		} else {	
			System.out.println("It's not an Encoded Image.");
			return;
		}
		
		byte[] decrypted = rsa.decrypt(privateKey, cipherData);
		System.out.println("data decoded successfully");
		System.out.println(new String(decrypted));
	}
}
