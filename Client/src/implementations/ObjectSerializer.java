package implementations;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import interfaces.iObjectSerializer;

public class ObjectSerializer implements iObjectSerializer {
	
	public byte[] serializeObject(Object object) throws IOException {
	    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
	    ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
	    objectOutputStream.writeObject(object);
	    byteArrayOutputStream.close();
	    objectOutputStream.close();
	    return byteArrayOutputStream.toByteArray();
	}
}
