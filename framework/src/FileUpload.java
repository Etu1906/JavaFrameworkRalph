package etu1906.framework.file;
import jakarta.servlet.http.Part;
import java.io.*;
import java.util.Arrays;
public class FileUpload {
    String name;
    String path;
    byte[] bytes;

    public FileUpload( String input ,  Part part )throws Exception{
        System.out.println("niditra");
        System.out.println(" value  "+input);
        setName(input);
        getBytesFromPart( part );
    }

    public void getBytesFromPart(Part part) throws IOException {
        InputStream inputStream = part.getInputStream();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[4096]; // or any other suitable buffer size

        int bytesRead;
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }

        setBytes( outputStream.toByteArray()) ;
    }
    
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getPath() {
        return path;
    }
    public void setPath(String path) {
        this.path = path;
    }
    public byte[] getBytes() {
        return bytes;
    }
    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }

    @Override
    public String toString() {
        return "FileUpload [name=" + name + ", path=" + path + ", bytes=" + Arrays.toString(bytes) + "]";
    }
}
