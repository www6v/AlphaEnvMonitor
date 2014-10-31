package com.gw.monitor.alphaenvmonitor.test;
import java.io.File;  
import java.io.FileNotFoundException;  
import java.io.FileReader;  
import java.io.IOException;  
import java.util.ArrayList;  
import java.util.LinkedHashMap;  
import java.util.List;  
import java.util.Map;  
  
import net.sf.ezmorph.bean.MorphDynaBean;  
import net.sf.json.JSONArray;  
import net.sf.json.JSONFunction;  
import net.sf.json.JSONObject;  
  
public class JsonTest {  
  
    public static void main(String args[]) {  
//        //javaArray��json����ת��  
//        javaArrayAndJsonInterChange();  
//        System.out.println("-------------------------------------");  
//        //javaList��json����ת��  
//        javaListAndJsonInterChange();  
//        System.out.println("-------------------------------------");  
//        //javaMpa��Json��ת  
//        javaMapAndJsonInterChange();  
//        System.out.println("-------------------------------------");  
        
        
        //javaObject��jsonObject��ת  
        javaObjectAndJsonInterChange();  
    }  
  
    /** 
     * javaArray��json����ת�� 
     */  
    public static void javaArrayAndJsonInterChange() {  
        // java ת����  
        boolean[] boolArray = new boolean[] { true, false, true };  
        JSONArray jsonArray = JSONArray.fromObject(boolArray);  
        String s = jsonArray.toString();  
        System.out.println(s);  
  
        // ͨ��json��ȡ�����е�����  
        String result = readJson("configdata");  
  
        JSONArray jsonR = JSONArray.fromObject(result);  
        int size = jsonR.size();  
        for (int i = 0; i < size; i++) {  
            System.out.println(jsonR.get(i));  
        }  
    }  
  
    /** 
     * javaList��json����ת�� 
     */  
    public static void javaListAndJsonInterChange() {  
        List list = new ArrayList();  
        list.add(new Integer(1));  
        list.add(new Boolean(true));  
        list.add(new Character('j'));  
        list.add(new char[] { 'j', 's', 'o', 'n' });  
        list.add(null);  
        list.add("json");  
        list.add(new String[] { "json", "-", "lib" });  
  
        // listתJSONArray  
        JSONArray jsArr = JSONArray.fromObject(list);  
        System.out.println(jsArr.toString(4));  
  
        // ��JSON����JSONArray  
        jsArr = JSONArray.fromObject(jsArr.toString());  
        // --��JSONArray���ȡ  
        // print: json  
        System.out.println(((JSONArray) jsArr.get(6)).get(0));  
    }  
  
    /** 
     * javaMpa��Json��ת 
     */  
    public static void javaMapAndJsonInterChange() {  
        Map map = new LinkedHashMap();  
        map.put("integer", new Integer(1));  
        map.put("boolean", new Boolean(true));  
        map.put("char", new Character('j'));  
        map.put("charArr", new char[] { 'j', 's', 'o', 'n' });  
        // ע:������nullΪ�������������б�net.sf.json.JSONException:  
        // java.lang.NullPointerException:  
        // JSON keys must not be null nor the 'null' string.  
        map.put("nullAttr", null);  
  
        map.put("str", "json");  
        map.put("strArr", new String[] { "json", "-", "lib" });  
        map.put("jsonFunction", new JSONFunction(new String[] { "i" },"alert(i)"));  
        map.put("address", new Address("P.O BOX 54534", "Seattle, WA", 42452,"561-832-3180", "531-133-9098"));  
        // mapתJSONArray  
        JSONObject jsObj = JSONObject.fromObject(map);  
        System.out.println(jsObj.toString(4));  
          
        // ��JSON����JSONObject  
        jsObj = JSONObject.fromObject(jsObj.toString());  
  
        //��һ�ַ�ʽ����JSONObject���ȡ  
        // print: json  
        System.out.println(jsObj.get("str"));  
        // print: address.city = Seattle, WA    
        System.out.println("address.city = " + ((JSONObject) jsObj.get("address")).get("city"));    
  
          
        //�ڶ��ַ�ʽ���Ӷ�̬Bean���ȡ���ݣ����ڲ���ת���ɾ����Bean���о�û�ж���ô�  
        MorphDynaBean mdBean = (MorphDynaBean) JSONObject.toBean(jsObj);  
        // print: json  
        System.out.println(mdBean.get("str"));  
        //print: address.city = Seattle, WA    
        System.out.println("address.city = " + ((MorphDynaBean) mdBean.get("address")).get("city"));    
  
    }  
      
    /** 
     * javaObject��jsonObject��ת 
     */  
    public static void  javaObjectAndJsonInterChange(){  
        Address address=new Address("P.O BOX 54534", "Seattle, WA", 42452,"561-832-3180", "531-133-9098");  
        //objectתJSONObject  
        JSONObject jsObj = JSONObject.fromObject(address);  
        System.out.println(jsObj.toString(4));  
          
        //JsonObjectתjava Object  
          
        Address addressResult=(Address) JSONObject.toBean(jsObj, Address.class);  
        System.out.println("address.city = "+ addressResult.getCity());  
        System.out.println("address.street="+addressResult.getStreet());  
        System.out.println("address.tel = "+ addressResult.getTel());  
        System.out.println("address.telTwo="+addressResult.getTelTwo());  
        System.out.println("address.zip="+addressResult.getZip());  
    }  
  
    /** 
     * ��ȡjson�ļ� 
     * @param fileName �ļ���,����Ҫ��׺ 
     * @return 
     */  
    public static String readJson(String fileName) {  
        String result = null;  
        try {  
            File myFile = new File("./config/" + fileName + ".json");  
            FileReader fr = new FileReader(myFile);  
            char[] contents = new char[(int) myFile.length()];  
            fr.read(contents, 0, (int) myFile.length());  
            result = new String(contents);  
            fr.close();  
        } catch (FileNotFoundException e) {  
            e.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
        return result;  
    }  
}  