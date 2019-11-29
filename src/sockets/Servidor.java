package sockets;

import java.awt.EventQueue;
import java.awt.TextArea;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JFrame;
import javax.swing.JTextArea;

public class Servidor implements Runnable{

	private JFrame frame;
	private JTextArea textArea;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Servidor window = new Servidor();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Servidor() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		textArea = new JTextArea();
		textArea.setBounds(10, 11, 414, 250);
		frame.getContentPane().add(textArea);
		
		Thread hilo = new Thread(this);
		hilo.start();
	}

	@Override
	public void run() {
		try {
			ServerSocket servidor = new ServerSocket(9999);
			Socket socket = servidor.accept();
			DataInputStream flujo_entrada = new DataInputStream(socket.getInputStream());
			String mensaje_texto = flujo_entrada.readUTF();
			textArea.append("\n" + mensaje_texto);
			socket.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
