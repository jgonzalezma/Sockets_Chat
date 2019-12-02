package sockets;

import java.awt.EventQueue;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JFrame;
import javax.swing.JTextArea;

import sockets.PaqueteEnvio;

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
		textArea.setEditable(false);
		textArea.setBounds(10, 11, 414, 250);
		frame.getContentPane().add(textArea);
		
		Thread hilo = new Thread(this);
		hilo.start();
	}

	@Override
	public void run() {
			ServerSocket servidor;
			String nick, ip, mensaje;
			PaqueteEnvio paquete_recibido;
			try {
				servidor = new ServerSocket(9999);
				while(true) {
					Socket socket = servidor.accept();
					ObjectInputStream paquete_datos = new ObjectInputStream(socket.getInputStream());
					paquete_recibido = (PaqueteEnvio) paquete_datos.readObject();
					nick = paquete_recibido.getNick();
					ip = paquete_recibido.getIp();
					mensaje = paquete_recibido.getMensaje();
					
					textArea.append("\n" + nick + ": " + mensaje + " para " + ip);
					
					Socket envia_destinatario = new Socket(ip, 9090);
					ObjectOutputStream paqueteReenvio = new ObjectOutputStream(envia_destinatario.getOutputStream());
					paqueteReenvio.writeObject(paquete_recibido);
					paqueteReenvio.close();
					envia_destinatario.close();
					socket.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			
		}	
}
