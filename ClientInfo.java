import java.net.InetAddress;

public class ClientInfo {
		public String name;
		public String base;
		public InetAddress address;
		public int port;
		private final int ID;
		public int attempt = 0;

		public ClientInfo(String name, String base, InetAddress address, int port, final int ID) {
			this.name = name;
			this.address = address;
			this.port = port;
			this.ID = ID;
			this.base = base;
		}
		
		public int getID() {
			return ID;
		}
}

