package sockets;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.awt.event.ActionEvent;
import javax.swing.JTextArea;
import javax.swing.JLabel;
import java.awt.Font;

public class Cliente implements Runnable{

	private JFrame frame;
	private JTextField txtField_mensaje;
	private JTextArea textArea_mensajes;
	private JTextField txtField_nick;
	private JTextField txtField_destino;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Cliente window = new Cliente();
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
	public Cliente() {
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
		
		txtField_mensaje = new JTextField();
		txtField_mensaje.setBounds(86, 48, 258, 20);
		frame.getContentPane().add(txtField_mensaje);
		txtField_mensaje.setColumns(10);
		
		JButton btnEnviar = new JButton("Enviar");
		btnEnviar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textArea_mensajes.append("\nYo:" + txtField_mensaje.getText());
				try {
					Socket socket = new Socket("192.168.1.37", 9999);
					PaqueteEnvio datos = new PaqueteEnvio();
					
					datos.setNick(txtField_nick.getText());
					datos.setIp(txtField_destino.getText());
					datos.setMensaje(txtField_mensaje.getText());
					
					ObjectOutputStream paquete_datos = new ObjectOutputStream(socket.getOutputStream());
					paquete_datos.writeObject(datos);
					socket.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		
		btnEnviar.setBounds(175, 83, 89, 23);
		frame.getContentPane().add(btnEnviar);
		
		textArea_mensajes = new JTextArea();
		textArea_mensajes.setEditable(false);
		textArea_mensajes.setBounds(86, 117, 258, 133);
		frame.getContentPane().add(textArea_mensajes);
		
		JLabel lblChat = new JLabel("Chat");
		lblChat.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblChat.setBounds(190, 11, 67, 14);
		frame.getContentPane().add(lblChat);
		
		txtField_nick = new JTextField();
		txtField_nick.setBounds(68, 10, 86, 20);
		frame.getContentPane().add(txtField_nick);
		txtField_nick.setColumns(10);
		
		JLabel lblNick = new JLabel("Nick:");
		lblNick.setBounds(10, 13, 46, 14);
		frame.getContentPane().add(lblNick);
		
		txtField_destino = new JTextField();
		txtField_destino.setBounds(338, 10, 86, 20);
		frame.getContentPane().add(txtField_destino);
		txtField_destino.setColumns(10);
		
		JLabel lblIpDestino = new JLabel("IP destino:");
		lblIpDestino.setBounds(258, 11, 86, 14);
		frame.getContentPane().add(lblIpDestino);
		
		Thread hilo = new Thread(this);
		hilo.start();
	}

	@Override
	public void run() {
		try {
			ServerSocket servidor_cliente = new ServerSocket(9090);
			Socket cliente;
			PaqueteEnvio paqueteRecibido;
			
			while(true) {
				cliente = servidor_cliente.accept();
				ObjectInputStream flujo_entrada = new ObjectInputStream(cliente.getInputStream());
				paqueteRecibido = (PaqueteEnvio) flujo_entrada.readObject();
				textArea_mensajes.append("\n" + paqueteRecibido.getNick() + ": " + paqueteRecibido.getMensaje());
				
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
}
