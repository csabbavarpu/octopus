package framework.util;


public class ByteArrayBuffer 
{
	private byte[] buffer;
	int length;
	
	public ByteArrayBuffer(int initalsize)
	{
		super();
		buffer = new byte[initalsize];
		length = 0;
	}
	
	public void append(byte[] bytes)
	{
		append(bytes,0,bytes.length);
	}
	
	public void append(byte[] bytes, int off, int len)
	{
		expandBuffer(length + len);
		System.arraycopy(bytes, off, buffer, length, len);
		length += len;
	}
	
	public byte[] getBytes()
	{
		byte[] copy = new byte[length];
		System.arraycopy(buffer, 0, copy, 0, length);
		return copy;
	}
	
	private void expandBuffer(int neededcapacity)
	{
		if (buffer.length > neededcapacity)
			return;
		
		int newlength = buffer.length * 2;
		while (newlength < neededcapacity)
			newlength *= 2;
		
		byte[] newbuffer = new byte[newlength];
		System.arraycopy(buffer, 0, newbuffer, 0, length);
		buffer = newbuffer;
	}
}
