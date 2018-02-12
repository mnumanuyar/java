
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
//import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Base64;
import java.io.FileWriter;
//import javax.net.ssl.HttpsURLConnection;

public class ParallelCloudDownloader extends Thread{
	public static int totalSize=0;
	private final static String USER_AGENT = "Mozilla/5.0";
	public static ArrayList<String> serverDirectories;
	public static ArrayList<String> fileSizes;
	public static ArrayList<String> credentials;
	
	public static void main(String[] args) throws Exception {
		long startTime = System.currentTimeMillis();
		String index = args[0];
		String authorization = args[1];
		int threadCount = Integer.parseInt(args[2]);
		String[] newAuthorization = authorization.split(":");
		String username = newAuthorization[0];
		String password = newAuthorization[1];
		
		
		ParallelCloudDownloader http = new ParallelCloudDownloader(index,username, password);
		http.getResponseCode();
		URLConnection url =  http.getURLConnection();
		if(url !=null)
		{
			ArrayList<String> data = getResponse(url);
			//System.out.println("Send Http GET request");
			System.out.println("URL of the index file: " + index);
			System.out.println("File size is " + data.get(1) + " Bytes");
			System.out.println("Index file is downloaded");
			
			serverDirectories = findServers(data);
			//System.out.println("Directories: " + serverDirectories.toString());
			fileSizes = findFileSize(data);
			//System.out.println("File Sizes: " + fileSizes.toString());
			credentials = findCredentials(data);
			//System.out.println("Credentials: " + credentials.toString());
			System.out.println("There are "+ serverDirectories.size()+" servers in the index");
			ArrayList<ParallelCloudDownloader> threads = new ArrayList<ParallelCloudDownloader>();
			ParallelCloudDownloader thread_;
			for (int i =0; i<credentials.size();i++){
				int start = Integer.parseInt(fileSizes.get(i).split("-")[0]);
				int end = Integer.parseInt(fileSizes.get(i).split("-")[1]);
				int step = (end-start+1)/threadCount;
				int reminder = (end-start+1)%threadCount;
				for (int j=0 ; j<  threadCount; j++){
					int start_ =  step*j;
					int end_ = step*(j+1)-1;
					if (j==threadCount-1) end_ = end_+reminder;
					thread_ = new ParallelCloudDownloader(serverDirectories.get(i),credentials.get(i).split(":")[0], credentials.get(i).split(":")[1]);
					thread_.getURLConnection().setRequestProperty("Range", "bytes="+start_+"-"+end_);
					//System.out.println("Range is bytes = "+start_+"-"+end_);
					//System.out.println("size is " + fileSizes.get(i));
					//thread_.getResponseCode();
					threads.add(thread_);
				}
			}
			//System.out.println("name of the file is "+"./" +data.get(0));
			FileWriter fileWriter = new FileWriter("./" +data.get(0)); 
			for (ParallelCloudDownloader thread : threads) { 
				thread.start();
			}
			for (ParallelCloudDownloader thread : threads) { 
				thread.join();
			}
			for (ParallelCloudDownloader thread : threads) { 
				for(String str: thread.outputData) {
					fileWriter.write(str+"\n");
					}
				
			}
			fileWriter.close();
			//System.out.println("Done");
			long stopTime = System.currentTimeMillis();
			System.out.println(data.get(0) + " is download in "+ (stopTime-startTime) + " miliseconds with "+threadCount*credentials.size()+" threads");
		}
		
	}
	private URLConnection con;
	public URLConnection getURLConnection(){
		return con;
	}
	public ArrayList<String>  outputData;
	public void getResponseCode(){
		int responseCode = Integer.parseInt(con.getHeaderField(0).split(" ")[1]);
		if (responseCode!=200){
			System.out.println("Error: response code: "+ responseCode);
			return ;
		}
				
		int fileSize = con.getContentLength();
		return ;
	}
	public ParallelCloudDownloader (String index_, String username_, String password_) throws IOException{
		outputData = new ArrayList<String>();
		String url = "http://" + index_;
		URL obj = new URL(url);
		
		con = obj.openConnection();
		
		String userpass = username_ + ":" + password_;
		String basicAuth = "Basic " + new String(Base64.getEncoder().encode(userpass.getBytes()));
		con.setRequestProperty ("Authorization", basicAuth);
		con.setRequestProperty("User-Agent", USER_AGENT);
		
	}
	
	public void run (){
		try {
			outputData = getResponse(con);
			//Thread.sleep(1000);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
//	
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
	
	

}