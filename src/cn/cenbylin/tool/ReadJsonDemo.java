package cn.cenbylin.tool;
import java.io.FileNotFoundException;
import java.io.FileReader;

import com.google.gson.JsonArray;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
public class ReadJsonDemo {
    public static void read(){
        try {  
            //�½�Json������  
            JsonParser parser = new JsonParser();  
            //����parse������ȡ��JsonObject  
            JsonObject object = (JsonObject) parser.parse(new FileReader("example.json"));  
              
            //����һϵ��get������ȡobject��ֱ���Ӷ���  
            System.out.println("type:"+object.get("type").getAsString());  
            System.out.println("mark:"+object.get("mark").getAsBoolean());  
              
            //�½�Json�����ȡobject��ֱ��������  
            JsonArray array = object.get("Data").getAsJsonArray();  
            System.out.println("Data:");  
            //����Json����  
            for(int i=0;i<array.size();i++)  
            {  
                //ʹ��JsonObject��ȡ�������Ԫ��  
                JsonObject temp = (JsonObject) array.get(i);  
                System.out.print(temp.get("city").getAsString()+" ");  
                System.out.print(temp.get("weather").getAsString()+" ");  
                System.out.println(temp.get("temperature").getAsInt());  
            }  
              
        } catch (JsonIOException e1) {  
            e1.printStackTrace();  
        } catch (JsonSyntaxException e11) {  
            e11.printStackTrace();  
        } catch (FileNotFoundException e) {  
            e.printStackTrace();  
        }  
      
	   }
}
