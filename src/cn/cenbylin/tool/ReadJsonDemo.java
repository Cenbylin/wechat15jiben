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
            //新建Json解析器  
            JsonParser parser = new JsonParser();  
            //调用parse方法获取到JsonObject  
            JsonObject object = (JsonObject) parser.parse(new FileReader("example.json"));  
              
            //调用一系列get方法获取object的直接子对象  
            System.out.println("type:"+object.get("type").getAsString());  
            System.out.println("mark:"+object.get("mark").getAsBoolean());  
              
            //新建Json数组获取object的直接子数组  
            JsonArray array = object.get("Data").getAsJsonArray();  
            System.out.println("Data:");  
            //遍历Json数组  
            for(int i=0;i<array.size();i++)  
            {  
                //使用JsonObject获取到数组的元素  
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
