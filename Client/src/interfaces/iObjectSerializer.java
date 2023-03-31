package interfaces;

import java.io.IOException;

public interface iObjectSerializer {
	public byte[] serializeObject(Object object) throws IOException;
}
