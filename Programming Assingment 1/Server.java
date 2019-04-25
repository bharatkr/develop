
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

class FileServerThread implements Runnable {

	private Socket serverSocket;

	public FileServerThread(Socket serverSocket) {
		this.serverSocket = serverSocket;
	}

	@Override
	public void run() {
		try {
			DataInputStream dataInputStream = new DataInputStream(serverSocket.getInputStream());
			DataOutputStream dataOutputStream = new DataOutputStream(serverSocket.getOutputStream());
			String inputFromClient = dataInputStream.readUTF().trim();
			if (inputFromClient.equals("download")) {
				String clientFileName = dataInputStream.readUTF().trim();
				File clientFile = new File(clientFileName);
				try {
					if (!clientFile.exists()) {
						dataOutputStream.writeBoolean(false);
						return;
					}
					dataOutputStream.writeBoolean(true);
					dataOutputStream.writeLong(clientFile.length());
					long bytesDownloaded = dataInputStream.readLong();
					FileInputStream fileInputStream = new FileInputStream(clientFileName);
					for (long i = 0; i < bytesDownloaded; i++) {
						fileInputStream.read();
					}
					byte[] data = new byte[1024];
					int read;
					while ((read = fileInputStream.read(data)) >= 0) {
						dataOutputStream.write(data, 0, read);
					}
					dataOutputStream.flush();
					fileInputStream.close();

				} catch (Exception e) {
					e.printStackTrace();
				}
			} else if (inputFromClient.equals("upload")) {
				String clientFileName = dataInputStream.readUTF().trim();
				File clientFile = new File(clientFileName);
				if (clientFile.exists()) {
					clientFile.delete();
				}
				File tempUploadFile = new File(clientFileName + ".tmp");
				long bytesToUpload = dataInputStream.readLong();

				try {
					long bytesUploaded = 0;
					if (tempUploadFile.exists()) {
						bytesUploaded = tempUploadFile.length();
					} else {
						tempUploadFile.createNewFile();
					}

					dataOutputStream.writeLong(bytesUploaded);
					FileOutputStream fos = new FileOutputStream(tempUploadFile, true);
					byte[] data = new byte[1024];
					int read = 0;
					try {
						while ((read = dataInputStream.read(data, 0, data.length)) >= 0) {
							fos.write(data, 0, read);
						}
						fos.flush();
						fos.close();
					} catch (Exception e) {
						fos.flush();
						fos.close();
					}
				} catch (Exception e) {
					System.out.println(e.getMessage());
				}
				if (tempUploadFile.length() == bytesToUpload) {
					tempUploadFile.renameTo(new File(clientFileName));
					tempUploadFile.delete();
				}
			} else if (inputFromClient.equalsIgnoreCase("ls")) {
				String path = dataInputStream.readUTF().trim();

				if (!(new File(path).exists()) || !(new File(path).isDirectory())) {
					dataOutputStream.writeBoolean(false);
					return;
				}
				dataOutputStream.writeBoolean(true);
				String listing = "";
				try {
					for (File file : new File(path).listFiles()) {
						if (file.isDirectory()) {
							listing += file.getName() + " <DIR>\n";
						} else {
							listing += file.getName() + " <FILE>\n";
						}
					}
					listing = listing.trim();
					dataOutputStream.writeUTF(listing);
				} catch (Exception e) {
					System.out.println(e.getMessage());
				}
			} else if (inputFromClient.equalsIgnoreCase("mkdir")) {
				String path = dataInputStream.readUTF().trim();
				dataOutputStream.writeBoolean(new File(path).mkdir());
			} else if (inputFromClient.equalsIgnoreCase("rmdir")) {
				String path = dataInputStream.readUTF().trim();
				if (!(new File(path).exists()) || !(new File(path).isDirectory())) {
					dataOutputStream.writeBoolean(false);
					return;
				}
				dataOutputStream.writeBoolean(new File(path).delete());
			} else if (inputFromClient.equalsIgnoreCase("rmfile")) {
				String path = dataInputStream.readUTF().trim();
				if (!(new File(path).exists()) || (new File(path).isDirectory())) {
					dataOutputStream.writeBoolean(false);
					return;
				}
				dataOutputStream.writeBoolean(new File(path).delete());
			} else if (inputFromClient.equalsIgnoreCase("shutdown")) {

				System.exit(0);
			}
			dataInputStream.close();
			dataOutputStream.close();
		} catch (Exception e) {

		}
	}
}

public class Server {
	public static void start(int port) {
		System.out.println("Server is running successfully at" + port);
		ServerSocket serverSocket = null;
		try {
			serverSocket = new ServerSocket(port);
		} catch (Exception e) {
			System.out.println("Server failed to start");
			return;
		}
		while (true) {

			try {
				Socket socket = serverSocket.accept();

				Thread t = new Thread(new FileServerThread(socket));
				t.start();
			} catch (Exception e) {

			}
		}
	}

	public static void main(String[] args) {
		if (args.length < 1) {
			System.out.println("Error: Invalid argument");
			return;
		}
		if (args[0].equals("start")) {

			int portNumber = Integer.parseInt(args[1]);
			Server.start(portNumber);
		}
	}
}
