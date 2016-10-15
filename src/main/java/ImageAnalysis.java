import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;

import com.google.gson.Gson;
import com.microsoft.projectoxford.vision.VisionServiceClient;
import com.microsoft.projectoxford.vision.VisionServiceRestClient;
import com.microsoft.projectoxford.vision.contract.AnalysisResult;
import com.microsoft.projectoxford.vision.contract.Caption;
import com.microsoft.projectoxford.vision.contract.Category;
import com.microsoft.projectoxford.vision.contract.Face;
import com.microsoft.projectoxford.vision.rest.VisionServiceException;

public class ImageAnalysis {
		
	static String key = "300ade69305046148c4901583b95b4e2";
	static StringBuilder textBuilder = new StringBuilder();
	
	private static VisionServiceClient client;
	
	private static void requestImageAnalyze(InputStream stream) throws VisionServiceException, IOException {
		
		
		Gson gson = new Gson();
        String[] features = {"ImageType", "Color", "Faces", "Adult", "Categories"};
        String[] details = {};
       
        //send image to server and receive result
        AnalysisResult analyze = client.analyzeImage(stream, features, details);
        String resultAnalyze = gson.toJson(analyze);

        //// result AnalyzeActivity
        textBuilder.append("Image format: " + analyze.metadata.format + "\n");
        textBuilder.append("Image width: " + analyze.metadata.width + ", height:" + analyze.metadata.height + "\n");
        textBuilder.append("Clip Art Type: " + analyze.imageType.clipArtType + "\n");
        textBuilder.append("Line Drawing Type: " + analyze.imageType.lineDrawingType + "\n");
        textBuilder.append("Is Adult Content:" + analyze.adult.isAdultContent + "\n");
        textBuilder.append("Adult score:" + analyze.adult.adultScore + "\n");
        textBuilder.append("Is Racy Content:" + analyze.adult.isRacyContent + "\n");
        textBuilder.append("Racy score:" + analyze.adult.racyScore + "\n\n") ;
        
        for (Category category: analyze.categories) {
        	textBuilder.append("Category: " + category.name + ", score: " + category.score + "\n");
        }

        textBuilder.append("\n");
        int faceCount = 0;
        for (Face face: analyze.faces) {
            faceCount++;
            textBuilder.append("face " + faceCount + ", gender:" + face.gender + "(score: " + face.genderScore + "), age: " + + face.age + "\n");
            textBuilder.append("    left: " + face.faceRectangle.left +  ",  top: " + face.faceRectangle.top + ", width: " + face.faceRectangle.width + "  height: " + face.faceRectangle.height + "\n" );
        }
        if (faceCount == 0) {
        	textBuilder.append("No face is detected");
        }
        textBuilder.append("\n");

        textBuilder.append("\nDominant Color Foreground :" + analyze.color.dominantColorForeground + "\n");
        textBuilder.append("Dominant Color Background :" + analyze.color.dominantColorBackground + "\n");

        textBuilder.append("\n--- Raw Data ---\n\n");
        
        textBuilder.append(resultAnalyze);       
        
        System.out.println(textBuilder);
	}
	
	
private static void requestImageDescribe(InputStream stream) throws VisionServiceException, IOException {
		
		
		Gson gson = new Gson();
        
       
        //send image to server and receive result
        AnalysisResult describe = client.describe(stream, 1);
        String resultDescribe = gson.toJson(describe);
       
        //// result DescribeActivity
        textBuilder.append("Image format: " + describe.metadata.format + "\n");
        textBuilder.append("Image width: " + describe.metadata.width + ", height:" + describe.metadata.height + "\n");
        textBuilder.append("\n");

        for (Caption caption: describe.description.captions) {
        	textBuilder.append("Caption: " + caption.text + ", confidence: " + caption.confidence + "\n");
        }
        textBuilder.append("\n");

        for (String tag: describe.description.tags) {
        	textBuilder.append("Tag: " + tag + "\n");
        }
        textBuilder.append("\n");
        textBuilder.append("\n--- Raw Data ---\n\n");
        textBuilder.append(resultDescribe);
        
        System.out.println(textBuilder);
	}
	
	public static void outputCsv(){
		
	}
	

	
	
public static void main(String[] args) throws IOException {
		
		System.out.print("Example: http://static2.businessinsider.com/image/5327148eecad044743a6d9e2-549-411/fantastic-four.png \n");
		System.out.print("Input URL: ");
		Scanner keyboard = new Scanner(System.in);
		URL url = new URL(keyboard.nextLine());
		//http://static2.businessinsider.com/image/5327148eecad044743a6d9e2-549-411/fantastic-four.png
		
		System.out.print("\nUrl: " + url + "\n");
		keyboard.close();
				
		
		if (client==null){
            client = new VisionServiceRestClient(key);
        }
		
		
//		OutputStream os = new FileOutputStream("abc.jpg");
//
//	    byte[] b = new byte[2048];
//	    int length;
//
//	    while ((length = stream.read(b)) != -1) {
//	    	System.out.println("b "+b);
//	        os.write(b, 0, length);
//	    }
//
//	    stream.close();
//	    os.close();
		
		//send image
		try {
			InputStream stream = url.openStream();
			requestImageAnalyze(stream);
		} catch (VisionServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			InputStream stream = url.openStream();
			requestImageDescribe(stream);
		} catch (VisionServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}


}
