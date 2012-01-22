/**
 * 
 */
package shtaag.network.curl.impl.request;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Map;
import java.util.Map.Entry;

import shtaag.network.curl.impl.Command;

/**
 * @author takei_s
 * @date 2012/01/08
 */
public class MySocket {
	
	private static long POLL_TIMEOUT = 5000L;
	
	private SocketChannel channel = null;
	private Selector selector = null;
	private enum State {
		SEND_REQ,
		RECV_RES,
	}
	private State state;
	public MySocket(String serverHost, int serverPort) throws IOException {
		channel = SocketChannel.open();
		selector = Selector.open();
		
		InetSocketAddress address = new InetSocketAddress(serverHost, serverPort);
		channel.connect(address);
		channel.configureBlocking(false);
	}
	
	public void close() {
		if (channel != null) {
			try {
				channel.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (selector != null) {
			try {
				selector.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private static final String VERVOSE_REQ = "> ";
	private static final String VERVOSE_RES = "< ";
	public void doHttpClient(Command command, String path) throws IOException {
		state = state.SEND_REQ;
		ByteBuffer reqBytes = null;
		loop:
			while(true) {
				switch(state) {
				case SEND_REQ:
					channel.register(selector, SelectionKey.OP_WRITE); break;
				case RECV_RES:
					channel.register(selector, SelectionKey.OP_READ); break;
				default:
					assert(false);
				}
				if (selector.select(POLL_TIMEOUT) > 0) {
					for (SelectionKey socket : selector.selectedKeys()) {
						if (state == State.SEND_REQ && socket.isWritable()) {
							reqBytes = sendRequest(command, path);
							state = State.RECV_RES;
						} else if (state == State.RECV_RES && socket.isReadable()) {
							ByteBuffer resBytes = recvResponse();
							//TODO induct writer
							System.out.println(new String(reqBytes.array(), "UTF-8"));
							System.out.println(new String(resBytes.array(), "UTF-8"));
							return;
						}
					}
					selector.selectedKeys().clear();
				} else {
					break;
//					System.out.println("continue? [y/n]");
//					String input = System.console().readLine();
//					if (input.charAt(0) == 'y') {
//						state = State.SEND_REQ;
//						continue;
//					} else {
//						break;
//					}
				}
			}
	}
	
	private ByteBuffer sendRequest(Command command, String path) throws IOException {
		ByteBuffer buffer = ByteBuffer.allocate(4096);
		setCommand(buffer, command, path);
		channel.write(buffer);
		return buffer;
	}

	private ByteBuffer recvResponse() throws IOException{
		ByteBuffer buffer = ByteBuffer.allocate(4096);
		channel.read(buffer);
		return buffer;
	}

	
	public static final String LS = System.getProperty("line.separator");
	public static final String protocol = "HTTP/1.1";
	/**
	 * @param buffer
	 * @param command
	 * @param path 
	 */
	private void setCommand(ByteBuffer buffer, Command command, String path) {
		// -X
		buffer.put((command.getCommand() + " " + path + " " + protocol + LS).getBytes());
		// -H
		for (Entry<String, String> header : command.getHeaders().entrySet()) {
			buffer.put((header.getKey() + ": " + header.getValue() + LS).getBytes());
			buffer.put(("Host: localhost" + LS).getBytes());
		}
		// -p
		if (command.isProxy()) {
			
		}
		buffer.put(LS.getBytes());
		buffer.flip();
	}


}
