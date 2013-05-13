package server;

public interface Encodable {

	/**
	 * for encoding the specific class's information so it can be sent from the server to the client (or vice versa)
	 * @return
	 */
	public String Encode();
	
	/**
	 * for decoding the specific information sent from the client to the server (or vice versa)
	 * @param encoding
	 */
	public void Decode(String encoding);
}
