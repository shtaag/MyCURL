/**
 * 
 */
package shtaag.network.curl.impl.socket;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
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
	
	private static final String VERBOSE_REQ = "> ";
	private static final String VERBOSE_RES = "< ";
	public ByteBuffer doHttpClient(Command command, String path) throws IOException {
		state = state.SEND_REQ;
		ByteBuffer reqBytes = null;
		// TODO for exercise, here use selector and socketChannel
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
							createResponseToWrite(reqBytes, resBytes, command.isVerbose());
//							System.out.println(new String(reqBytes.array(), "UTF-8"));
//							System.out.println(new String(resBytes.array(), "UTF-8"));
							return resBytes;
						}
					}
					selector.selectedKeys().clear();
				} else {
					break loop;
				}
			}
		return null;
	}
	
	/**
	 * @param reqBytes
	 * @param resBytes
	 * @param verbose
	 */
	private ByteBuffer createResponseToWrite(ByteBuffer reqBytes,
			ByteBuffer resBytes, boolean verbose) {
		ByteBuffer result = ByteBuffer.allocate(reqBytes.capacity() + reqBytes.capacity());
		if (verbose) {
			// TODO insert > or <
		}
		return result.put(reqBytes).put(resBytes);
		
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
		}
		if (! command.getHeaders().containsKey("Host")) {
			buffer.put(("Host: localhost" + LS).getBytes());
		}
		if (! command.getHeaders().containsKey("Connection")) {
			buffer.put(("Connection: close" + LS).getBytes());
		}
		// -d, -F
		//TODO URL encode
		if (command.getRequestBody() != null) {
			buffer.put(LS.getBytes());
			for (Entry<String, String> entry : command.getRequestBody().entrySet()) {
				buffer.put((entry.getKey() + "=" + entry.getValue() + LS).getBytes());
			}
		}
		buffer.put(LS.getBytes());
		buffer.flip();
	}

}
