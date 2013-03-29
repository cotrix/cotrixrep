package org.cotrix.web.importwizard.server;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

public class FileUpload extends HttpServlet{
    public void doPost(HttpServletRequest request, HttpServletResponse response)  throws ServletException, IOException {
        ServletFileUpload upload = new ServletFileUpload();
        try{
            FileItemIterator iter = upload.getItemIterator(request);
            while (iter.hasNext()) {
                FileItemStream item = iter.next();

                String name = item.getFieldName();
                System.out.println("name = "+name);
                InputStream stream = item.openStream();


                // Process the input stream
                FileOutputStream out = new FileOutputStream("/Users/ton/Desktop/oooo/uploadfile.txt");
                int len;
                byte[] buffer = new byte[8192];
                while ((len = stream.read(buffer, 0, buffer.length)) != -1) {
                    out.write(buffer, 0, len);
                    
                }

                int maxFileSize = 10*(1024*1024); //10 megs max 
                /*if (out.size() > maxFileSize) { 
                    throw new RuntimeException("File is > than " + maxFileSize);
                }*/
                out.flush();
                out.close();
            }
        }
        catch(Exception e){
            throw new RuntimeException(e);
        }

    }
}