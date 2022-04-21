import java.awt.EventQueue;
import java.awt.FileDialog;
import java.awt.Image;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.security.PrivateKey;
import java.security.PublicKey;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JRadioButton;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.JMenuBar;


public class MainGUI extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4752097233485510549L;
	/**
	 * 
	 */
	private JPanel contentPane;
	private JTextField textField;
	private ButtonGroup bgGroup = new ButtonGroup();
	private String path;
	private JTextField dataField;
	private JLabel lblImage, lblImage0, lblOriginalImage, lblTextImage;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainGUI frame = new MainGUI();
					frame.setLocationRelativeTo(null);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public MainGUI() {
		setResizable(false);
		try {
			UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
		} catch (ClassNotFoundException | InstantiationException
				| IllegalAccessException | UnsupportedLookAndFeelException e1) {
			e1.printStackTrace();
		}
		setTitle("Project");		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1200, 700);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu mnGen = new JMenu("Keys");
		menuBar.add(mnGen);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		JMenuItem menuItem = new JMenuItem("Generate Keys");
		mnGen.add(menuItem);
		menuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				GenerateKeys gk;
				try {
					gk = new GenerateKeys(1024);
					gk.createKeys();
					gk.writeToFile("publicKey.txt", gk.getPublicKey().getEncoded());
					gk.writeToFile("privateKey.txt", gk.getPrivateKey().getEncoded());
				} catch (Exception e) {
					JOptionPane.showMessageDialog(contentPane, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		contentPane.setLayout(null);
		
		JLabel lblInputImage = new JLabel("Select the Image");
		lblInputImage.setBounds(185, 42, 115, 16);
		lblInputImage.setFont(new Font("Tahoma", Font.BOLD, 13));
		contentPane.add(lblInputImage);
		
		textField = new JTextField();
		textField.setBounds(310, 36, 500, 30);
		contentPane.add(textField);
		textField.setColumns(10);
		
		JButton btnOpen = new JButton("Open...");
		btnOpen.setBounds(820, 36, 89, 30);
		contentPane.add(btnOpen);
		
		final JButton btnProcess = new JButton("Process");
		btnProcess.setBounds(520, 110, 79, 40);
		btnProcess.setEnabled(false);
		btnProcess.setFont(new Font("Tahoma", Font.BOLD, 12));
		contentPane.add(btnProcess);
		
		
		
		final JRadioButton rdbtnEncodingMode = new JRadioButton("Encoding mode");
		rdbtnEncodingMode.setBounds(310, 73, 135, 23);
		rdbtnEncodingMode.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				path = null;
				textField.setText("");
				btnProcess.setEnabled(false);
				lblImage.setIcon(null);
				lblImage0.setIcon(null);
				dataField.setText("");
				lblOriginalImage.setVisible(true);
			}
		});
		bgGroup.add(rdbtnEncodingMode);
		rdbtnEncodingMode.setSelected(true);
		rdbtnEncodingMode.setFont(new Font("Tahoma", Font.BOLD, 12));
		contentPane.add(rdbtnEncodingMode);
		
		final JRadioButton rdbtnDecodingMode = new JRadioButton("Decoding mode");
		rdbtnDecodingMode.setBounds(689, 73, 121, 23);
		rdbtnDecodingMode.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				path = null;
				textField.setText("");
				btnProcess.setEnabled(false);
				lblImage.setIcon(null);
				lblImage0.setIcon(null);
				dataField.setText("");
				lblOriginalImage.setVisible(false);
			}
		});
		bgGroup.add(rdbtnDecodingMode);
		rdbtnDecodingMode.setFont(new Font("Tahoma", Font.BOLD, 12));
		contentPane.add(rdbtnDecodingMode);
		
		btnOpen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				dataField.setText("");
				FileDialog fileDialog = new FileDialog(MainGUI.this, "Choose an Image...", FileDialog.LOAD);
				fileDialog.setVisible(true);
				String directoryName = fileDialog.getDirectory();
				String fileName = fileDialog.getFile();
			
				if (directoryName == null || fileName == null)
					return;
				
				if (fileName.endsWith(".jpg") || fileName.endsWith(".png") || fileName.endsWith(".JPG")) {
					path = directoryName + fileName; 	
					textField.setText(path);
					btnProcess.setEnabled(true);
				} else {
					JOptionPane.showMessageDialog(contentPane, "Please select either the jpeg or png image", "Invalid Image Input", JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				try {
					if (rdbtnEncodingMode.isSelected())
						lblImage.setIcon(new ImageIcon(ImageIO.read(new File(path)).getScaledInstance(530, 370, Image.SCALE_SMOOTH)));
					else
						lblImage0.setIcon(new ImageIcon(ImageIO.read(new File(path)).getScaledInstance(530, 370, Image.SCALE_SMOOTH)));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		
		JLabel lblImageStegnography = new JLabel("Secret Communication Using Image Stegnography ");
		lblImageStegnography.setBounds(390, 11, 360, 20);
		lblImageStegnography.setFont(new Font("Tahoma", Font.BOLD, 13));
		contentPane.add(lblImageStegnography);
		
		lblImage = new JLabel();
		lblImage.setBounds(10, 206, 530, 370);
		contentPane.add(lblImage);
		
		lblImage0 = new JLabel();
		lblImage0.setBounds(644, 206, 530, 370);
		contentPane.add(lblImage0);
		
		dataField = new JTextField();
		dataField.setFont(new Font("Tahoma", Font.BOLD, 12));
		dataField.setBounds(273, 587, 636, 40);
		contentPane.add(dataField);
		dataField.setColumns(10);
		
		lblOriginalImage = new JLabel("Original Image");
		lblOriginalImage.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblOriginalImage.setBounds(211, 181, 100, 14);
		contentPane.add(lblOriginalImage);
		
		lblTextImage = new JLabel("Text contained Image");
		lblTextImage.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblTextImage.setBounds(840, 182, 144, 14);
		contentPane.add(lblTextImage);
		
		JLabel lblText = new JLabel("Given Text");
		lblText.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblText.setBounds(194, 599, 69, 14);
		contentPane.add(lblText);
		
		btnProcess.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				FileDialog fileDialog = null;
				String directoryName = null, fileName = null;
				
				if (rdbtnEncodingMode.isSelected()) {
					fileDialog = new FileDialog(MainGUI.this, "Choose the Text File", FileDialog.LOAD);
					fileDialog.setVisible(true);
					directoryName = fileDialog.getDirectory();
					fileName = fileDialog.getFile();
					
					if (directoryName == null || fileName == null)
						return;
					
					if (!fileName.endsWith(".txt")) {
						JOptionPane.showMessageDialog(contentPane, "Select File is not a Text file", "Invalid", JOptionPane.ERROR_MESSAGE);
						return;
					}
					
					String txtPath = directoryName + fileName;
					
					fileDialog = new FileDialog(MainGUI.this, "Select Where To Save the Stegno-Image...", FileDialog.SAVE);
					fileDialog.setVisible(true);
					directoryName = fileDialog.getDirectory();
					fileName = fileDialog.getFile();
					
					if (directoryName == null || fileName == null)
						return;
					
					try {
						String text = new String(Files.readAllBytes(new File(txtPath).toPath()));
						dataField.setText(text);
						encode(path, text, directoryName + fileName);
					} catch (Exception e) {
						JOptionPane.showMessageDialog(contentPane, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
					}
					
					path = null;
					textField.setText("");
					btnProcess.setEnabled(false);
					
				} else if (rdbtnDecodingMode.isSelected()) {
					
					fileDialog = new FileDialog(MainGUI.this, "Select Where To Save Data", FileDialog.SAVE);
					fileDialog.setVisible(true);
					directoryName = fileDialog.getDirectory();
					fileName = fileDialog.getFile();
					
					if (directoryName == null || fileName == null)
						return;
					
					String txtPath = directoryName + fileName;
					try {
						decode(path, txtPath);
					} catch (Exception e) {
						JOptionPane.showMessageDialog(contentPane, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
					}
					
					path = null;
					textField.setText("");
					btnProcess.setEnabled(false);
				}
			}
		});
	}
	
	public void encode(String inputFilename, String msg, String outputFileName) throws Exception {
		RSA rsa = new RSA();
		PublicKey publicKey = rsa.getPublic("publicKey.txt");
		byte[] encrypted = rsa.encrypt(publicKey, msg);
		
		BufferedImage image =  ImageIO.read(new java.io.File(inputFilename));
		int width = image.getWidth();
		int height = image.getHeight();
		
		BufferedImage image1 =  new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
		for (int row = 0; row < width; row++)
			for (int col = 0; col < height; col++)
				image1.setRGB(row, col, image.getRGB(row, col));
		
		if (8 + encrypted.length > width * height) {
			JOptionPane.showMessageDialog(contentPane, "Image is not Big enough for data", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		byte[] start = ("?start?" + encrypted.length + "?").getBytes();
		
		LSBEncoder.encode(image, image1, start, width, height);
		LSBEncoder.encode(image, image1, encrypted, width, height);
		
		ImageIO.write(image1, "png", new File(outputFileName));
		lblImage0.setIcon(new ImageIcon(image1.getScaledInstance(530, 370, Image.SCALE_SMOOTH)));
		
		JOptionPane.showMessageDialog(contentPane, "Data Encoded Successfully.", "Encoded", JOptionPane.INFORMATION_MESSAGE);
	}
	
	public void decode(String inputFileName, String txtPath) throws Exception {
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
			JOptionPane.showMessageDialog(contentPane, "It's not an Encoded Image", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		byte[] decrypted = rsa.decrypt(privateKey, cipherData);
		
		JOptionPane.showMessageDialog(contentPane, "Data Decoded Successfully", "Decoded", JOptionPane.INFORMATION_MESSAGE);
		
		PrintWriter pWriter = new PrintWriter(txtPath);
		String text = new String(decrypted);
		pWriter.write(text);
		pWriter.close();
		
		dataField.setText(text);
	}
}
