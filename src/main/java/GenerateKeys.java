

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;

public class GenerateKeys {

	private KeyPairGenerator keyGen;
	private KeyPair pair;
	private PrivateKey privateKey;
	private PublicKey publicKey;

	public GenerateKeys(int keylength) throws NoSuchAlgorithmException, NoSuchProviderException {
		this.keyGen = KeyPairGenerator.getInstance("RSA");
		this.keyGen.initialize(keylength);
	}

	public void createKeys() {
		this.pair = this.keyGen.generateKeyPair();
		this.privateKey = pair.getPrivate();
		this.publicKey = pair.getPublic();
	}

	public PrivateKey getPrivateKey() {
		return this.privateKey;
	}

	public PublicKey getPublicKey() {
		return this.publicKey;
	}

	public void writeToFile(String path, byte[] key) throws IOException {
		FileOutputStream fos = new FileOutputStream(new File(path));
		fos.write(key);
		fos.flush();
		fos.close();
	}

	public static void main(String[] args) throws Exception {
		GenerateKeys gk = new GenerateKeys(1024);
		gk.createKeys();
		gk.writeToFile("publicKey.txt", gk.getPublicKey().getEncoded());
		gk.writeToFile("privateKey.txt", gk.getPrivateKey().getEncoded());
	}

}
