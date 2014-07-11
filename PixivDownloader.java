package pixivdownloader;

import com.wizzardo.tools.http.HttpSession;
import com.wizzardo.tools.json.JsonObject;
import com.wizzardo.tools.io.ZipTools;
import com.wizzardo.tools.json.JsonArray;
import com.wizzardo.tools.json.JsonItem;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import javax.imageio.ImageIO;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 *
 * @author ACME
 */
public class PixivDownloader {
    
    private HttpSession session;
      
    void downloadUrl(String url) throws Exception {
        session = new HttpSession();
        session.maxRetryCount(10);
        session.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:28.0) Gecko/20100101 Firefox/28.0");
        
        Document doc = Jsoup.parse(session.createRequest(url).get().asString("UTF-8"));
        String data = doc.select("script#template-show-work-more-register-text").first().previousElementSibling().data();
        
        JsonObject json = JsonObject.parse(data.substring(data.indexOf("{"), data.lastIndexOf(";"))).asJsonObject();
        
        String newUrl = json.get("src").asString();
        JsonArray frames = json.get("frames").asJsonArray();
        
       
        session.setHeader("Referer", url);
        File newFile = new File(newUrl.substring(newUrl.lastIndexOf("/") + 1));

        FileOutputStream fos = new FileOutputStream(newFile);
        fos.write(session.createRequest(newUrl).get().asBytes());
        fos.close();
        
        File outDir = new File(newFile.getName().substring(0, newFile.getName().indexOf(".")));
        outDir.mkdirs();
        
        byte[] buffer = new byte[1024];
        
        ZipInputStream zis = new ZipInputStream(new FileInputStream(newFile));
        ZipEntry ze = zis.getNextEntry();
        while(ze!=null){
 
    	   String fileName = ze.getName();
           File frameFile = new File(outDir.getPath() + File.separator + fileName);
           
            FileOutputStream nfos = new FileOutputStream(frameFile);             
 
            int len;
            while ((len = zis.read(buffer)) > 0) {
       		nfos.write(buffer, 0, len);
            }
 
            nfos.close();   
            ze = zis.getNextEntry();
        }
        
        AnimatedGifEncoder e = new AnimatedGifEncoder();
        e.start(outDir.getPath() + ".gif");
        e.setRepeat(0);
        for (JsonItem it : frames) {
            BufferedImage img = ImageIO.read(new File(outDir.getPath() + File.separator + it.asJsonObject().get("file").asString()));
            
            e.setDelay(it.asJsonObject().get("delay").asInteger());
            e.addFrame(img);
        }
        e.finish();
        System.out.println("saved file: '" + outDir.getPath() + ".gif" + "'");
        
    }

    public static void main(String[] args) throws Exception {
        PixivDownloader downloader = new PixivDownloader();
        downloader.downloadUrl(args[0]);
        
        
    }
    
}
