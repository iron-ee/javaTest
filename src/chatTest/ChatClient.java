package chatTest;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.ScrollPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class ChatClient extends JFrame{

	private final static String TAG = "ChatClient : ";
	private ChatClient chatClient = this;
	
	private static final int PORT = 9900;
	
	private JButton btnConnect, btnSend;
	private JTextField tfHost, tfChat;
	private JTextArea taChatList;
	private ScrollPane scrollPane;
	
	private JPanel topPanel, bottomPanel;
	
	private Socket socket;
	private PrintWriter writer;
	private BufferedReader reader;
	private Scanner scanner; 
	
	public ChatClient() {
		init();
		setting();
		batch();
		listener();
		setVisible(true);
	}
	
	private void init() {
		btnConnect = new JButton("connect");
		btnSend = new JButton("send");
		tfHost = new JTextField("127.0.0.1", 20);
		tfChat = new JTextField(20);
		taChatList = new JTextArea(10, 30);		// row, column
		scrollPane = new ScrollPane();
		topPanel = new JPanel();
		bottomPanel = new JPanel();
	}
	
	private void setting() {
		setTitle("ä�� �ٴ�� Ŭ���̾�Ʈ");
		setSize(450, 500);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		taChatList.setBackground(Color.ORANGE);
		taChatList.setForeground(Color.BLUE);
	}
	private void batch() {
		topPanel.add(tfHost);
		topPanel.add(btnConnect);
		bottomPanel.add(tfChat);
		bottomPanel.add(btnSend);
		scrollPane.add(taChatList);
		
		add(topPanel, BorderLayout.NORTH);
		add(bottomPanel, BorderLayout.SOUTH);
		add(scrollPane, BorderLayout.CENTER);
	}
	private void listener() {
		btnConnect.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				connect();
			}
		});
		
		btnSend.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				send();
			}
		});
	}
	
	private void connect() {
		String host = tfHost.getText();
		try {
			socket = new Socket(host, PORT);
			writer = new PrintWriter(socket.getOutputStream());
			reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			ReaderThreadC rtC = new ReaderThreadC();
			rtC.start();
		} catch (Exception e1) {
			System.out.println(TAG+"���� ���� ���� : "+e1.getMessage());
		}
	}
	
	private void send() {
		String chat = tfChat.getText();
		// 1�� taChatList �Ѹ���
		taChatList.append("[�� �޼���]"+chat+"\n");
		// 2�� ������ ����
		try {
			writer = new PrintWriter(socket.getOutputStream(),true);
			writer.println(chat);
			writer.flush();
		} catch (Exception e) {
			// TODO: handle exception
		}
		// 3�� tfChat ����
		tfChat.setText("");
	}
	
	class ReaderThreadC extends Thread {
		// while�� ���鼭 ������ ���� �޽����� �޾Ƽ� taChatList�� �Ѹ���
		@Override
		public void run() {
			String input = null;
			try {
				reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				while ((input = reader.readLine()) != null) {
					System.out.println(writer);
					taChatList.append("[��� �޼���]"+input+"\n");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void main(String[] args) {
		new ChatClient();
	}
}

