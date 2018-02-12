package cloudDownloader;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
//import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Base64;

//import javax.net.ssl.HttpsURLConnection;

public class CloudDownloader {

	private final static String USER_AGENT = "Mozilla/5.0";

	public static void main(String[] args) throws Exception {

		String index = args[0];
		String authorization = args[1];
		//int threadCount = Integer.parseInt(args[2]);
		String[] newAuthorization = authorization.split(":");
		String username = newAuthorization[0];
		String password = newAuthorization[1];
		
		CloudDownloader http = new CloudDownloader();
		URLConnection url =  http.sendGet(index,username, password);
		if(url !=null)
		{
			ArrayList<String> data = getResponse(url);
			//System.out.println("Send Http GET request");
			System.out.println("URL of the index file: " + index);
			System.out.println("File size is " + data.get(1) + " Bytes");
			System.out.println("Index file is downloaded");
			
			ArrayList<String> serverDirectories = findServers(data);
			//System.out.println("Directories: " + serverDirectories.toString());
			ArrayList<String> fileSizes = findFileSize(data);
			//System.out.println("File Sizes: " + fileSizes.toString());
			ArrayList<String> credentials = findCredentials(data);
			//System.out.println("Credentials: " + credentials.toString());
			System.out.println("There are "+ serverDirectories.size()+" servers in the index");
			FileOutputStream fos = new FileOutputStream(new File("./" +data.get(0)));
			int totalSize=0;
			for (int i =0; i<credentials.size();i++){
				URLConnection con = http.sendGet(serverDirectories.get(i),credentials.get(i).split(":")[0], credentials.get(i).split(":")[1]);
				System.out.println("Connected to "+serverDirectories.get(i));
				Integer redundent=0;
				if(i>0)
					redundent = Integer.parseInt(fileSizes.get(i-1).split("-")[1])-Integer.parseInt(fileSizes.get(i).split("-")[0])+1;
				//System.out.println(redundent);
				downloadFiles(con,fos,redundent);
				int start = Integer.parseInt(fileSizes.get(i).split("-")[0])+redundent;
				int end = Integer.parseInt(fileSizes.get(i).split("-")[1]);
				 int size= end-start+1;
				 totalSize +=size;
				System.out.println("Downloaded bytes " + start +" to " + end   + " (size = "+ size + ") ");
			}
			fos.flush();
			fos.close();
			//System.out.println("Done");
			System.out.println("Download of the file is complete (size = " + totalSize+ ") ");
		}
		
	}
	private static  void downloadFiles(URLConnection con,FileOutputStream fos,int skip ) throws IOException {
		
		InputStream in = con.getInputStream();
		if(skip>0)
			in.read(new byte[skip]);
		byte[] buf = new byte[512];
		while (true) {
		    int len = in.read(buf);
		    //System.out.println(len);
		    if (len == -1) {
		        break;
		    }
		    fos.write(buf, 0, len);
		}
		in.close();
		
	}
	private static ArrayList<String> findCredentials(ArrayList<String> data) {
		ArrayList<String> credentials = new ArrayList<String>();
		int numberOfServer = (data.size()-2)/3;
		for(int i = 0; i < numberOfServer; i++)
		{
			credentials.add(data.get(i*3+3));
		}
		return credentials;
	}

	private static ArrayList<String> findFileSize(ArrayList<String> data) {
		ArrayList<String> fileSizes = new ArrayList<String>();
		int numberOfServer = (data.size()-2)/3;
		for(int i = 0; i < numberOfServer; i++)
		{
			fileSizes.add(data.get(i*3+4));
		}
		return fileSizes;
	}

	private static ArrayList<String> findServers(ArrayList<String> data) {
		ArrayList<String> serverDirectories = new ArrayList<String>();
		int numberOfServer = (data.size()-2)/3;
		for(int i = 0; i < numberOfServer; i++)
		{
			serverDirectories.add(data.get(i*3+2));
		}
		return serverDirectories;
	}

	// HTTP GET request
	private static ArrayList<String> getResponse (URLConnection con) throws IOException{
		ArrayList<String> response = new ArrayList<String>();
		BufferedReader in = new BufferedReader(
		        new InputStreamReader(con.getInputStream()));
		
		String inputLine;
		
		while ((inputLine = in.readLine()) != null) {
			response.add(inputLine);
		}
		in.close();

		//print result
		//System.out.println(response.toString());
		return response;
	}
	private URLConnection sendGet(String index_, String username_, String password_) throws IOException 
	{
//		Authenticator.setDefault (new Authenticator() {
//		    protected PasswordAuthentication getPasswordAuthentication() {
//		        return new PasswordAuthentication (username_, password_.toCharArray());
//		    }
//		});
		String url = "http://" + index_;
		URL obj = new URL(url);
		
		URLConnection con = obj.openConnection();
		
		String userpass = username_ + ":" + password_;
		String basicAuth = "Basic " + new String(Base64.getEncoder().encode(userpass.getBytes()));
		con.setRequestProperty ("Authorization", basicAuth);
		
		
		// optional default is GET
		//con.setRequestMethod("GET");

		//add request header
		con.setRequestProperty("User-Agent", USER_AGENT);
		
		int responseCode = Integer.parseInt(con.getHeaderField(0).split(" ")[1]);
		if (responseCode!=200){
			System.out.println("Error: response code: "+ responseCode);
			return null;
		}
				
		int fileSize = con.getContentLength();
		return con;
		//System.out.println("\nSending 'GET' request to URL : " + url);
		//System.out.println("Response Code : " + responseCode);
		//System.out.println("File Size : " + fileSize);
		
		
	}

}