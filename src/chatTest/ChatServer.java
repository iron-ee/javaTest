package chatTest;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

public class ChatServer {

	private static final String TAG = "ChatServer : ";
	private ServerSocket serverSocket;
	private Vector<ClientInfo> vc; // ����� Ŭ���̾�Ʈ ������ ��� �÷���
	
	private Socket socket;
//	private FileWriter fileWriter;
	
	public ChatServer() {
		try {
			vc = new Vector<>();
			serverSocket = new ServerSocket(9900);
			System.out.println(TAG + "Ŭ���̾�Ʈ ���� �����...");
			//���� ������ ����
			while(true) {
				Socket socket = serverSocket.accept(); // Ŭ���̾�Ʈ ������
				System.out.println(TAG + "��û ����");
				ClientInfo clientInfo = new ClientInfo(socket);
//				SaveThread savethread = new SaveThread(socket);
				clientInfo.start();
//				savethread.start();
				vc.add(clientInfo);	
				
			}
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	class ClientInfo extends Thread{
		
		Socket socket;
		BufferedReader reader;
		PrintWriter writer; // BufferedWriter�� �ٸ����� �������� �Լ� ����
		
		public ClientInfo(Socket socket) {
			this.socket = socket;
			
		}
		
		// ���� : Ŭ���̾�Ʈ�� ���� ���� �޽����� ��� Ŭ���̾�Ʈ���� ������
		@Override
		public void run() {
			try {
				reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				writer = new PrintWriter(socket.getOutputStream(), true);
				String text = null;
				while((text = reader.readLine()) != null) {
					System.out.println("from client : " + text);
					for (int i = 0; i < vc.size(); i++) {
						if(vc.get(i) != this) {
							vc.get(i).writer.println(text);
						}
					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println("���� ���� ����" + e.getMessage());
			}
		}
	}
	
//	class SaveThread extends Thread {
//		
//		Socket socket;
//		
//		public SaveThread (Socket socket) {
//			this.socket = socket;
//		}
//		@Override
//		
//		public void run() {
//			String saveText = ("c:\\saveText.txt");
//			try {
//				FileWriter fileWriter = new FileWriter(saveText,true);
//				BufferedReader reader 
//				= new BufferedReader(new InputStreamReader(socket.getInputStream()));
//				String text = null;
//				while ((text = reader.readLine()) != null) {
//					fileWriter.write(text +"\n");
//					fileWriter.close();
//				}
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}
//	}
	public static void main(String[] args) {
		new ChatServer();
	}

}
