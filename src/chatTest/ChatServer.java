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
	private Vector<ClientInfo> vc; // 연결된 클라이언트 소켓을 담는 컬렉션
	
	private Socket socket;
//	private FileWriter fileWriter;
	
	public ChatServer() {
		try {
			vc = new Vector<>();
			serverSocket = new ServerSocket(9900);
			System.out.println(TAG + "클라이언트 연결 대기중...");
			//메인 스레드 역할
			while(true) {
				Socket socket = serverSocket.accept(); // 클라이언트 연결대기
				System.out.println(TAG + "요청 받음");
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
		PrintWriter writer; // BufferedWriter와 다른점은 내려쓰기 함수 지원
		
		public ClientInfo(Socket socket) {
			this.socket = socket;
			
		}
		
		// 역할 : 클라이언트로 부터 받은 메시지를 모든 클라이언트에게 재전송
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
				System.out.println("서버 연결 실패" + e.getMessage());
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
