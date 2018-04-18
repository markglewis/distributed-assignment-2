public class compressedFiles implements java.io.Serializable {
	public String name;
	public byte[] data;
	public int count;

	public compressedFiles(String n, byte[] d, int c){
		name = n;
		data = d;
		count = c;
	}
}