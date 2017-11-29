package com.bentengwu.nannytoolforredissupport;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;


public class CommonUtils {
    public static final ObjectMapper mapper = new ObjectMapper();
   
    static {
    	mapper.setDateFormat(new  SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
    }

    // 获取类实际路径
    public static String getClassAbsolutePath() {
        return getClassAbsolutePath("");
    }

    public static String getClassAbsolutePath(String suffix) {
        String absolutePath = "";
        try {
            absolutePath = URLDecoder.decode(CommonUtils.class.getClassLoader()
                    .getResource("").getFile(), "utf-8")
                    + suffix;
        } catch (Exception e) {
        }
        return absolutePath;
    }

    /*
     * 取得stream中的内容，转化成字符串,utf-8
     */
    public static String getContentFromStream(InputStream in)
            throws IOException {
        return getContentFromStream(in, "UTF-8");
    }

    /*
     * 取得stream中的内容，转化成指定编码的字符串
     */
    public static String getContentFromStream(InputStream in, String encoding)
            throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int bLen = 0;
        while ((bLen = in.read(buffer)) > 0) {
            baos.write(buffer, 0, bLen);
        }
        String xml = new String(baos.toByteArray(), encoding);
        baos.close();
        in.close();
        buffer = null;
        return xml;
    }

    /*
     * 取得stream的内容，返回字节数组
     */
    public static byte[] getBytesFromStream(InputStream in) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int bLen = 0;
        while ((bLen = in.read(buffer)) > 0) {
            baos.write(buffer, 0, bLen);
        }
        in.close();
        return baos.toByteArray();
    }

    /**
     * 把参数封装成对象
     *
     * @param object
     * @param parameterMap 欲封装的参数值
     * @param fieldMap     参数封装映射关系
     */
    public static void assembleParametersToObject(Object object,
                                                  Map<String, String> parameterMap, Map<String, String> fieldMap) {
        if (object == null || parameterMap == null) {
            return;
        }
        for (String parameter : fieldMap.keySet()) {
            try {
                BeanUtils.setProperty(object, fieldMap.get(parameter),
                        parameterMap.get(parameter.toLowerCase()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static Map<String, String> convertMapKeyToLowerCase(
            Map<String, String> map) {
        Map<String, String> resultMap = new HashMap<String, String>();
        if (map != null) {
            for (String parameter : map.keySet()) {
                resultMap.put(parameter.toLowerCase(), map.get(parameter));
            }
        }
        return resultMap;
    }

    /**
     * @param source
     * @return
     */
    public static Map<String, String> parseParameter(String source) {
        Map<String, String> map = new HashMap<String, String>();
        if (StringUtils.isNotEmpty(source)) {
            String s[] = source.split("&");
            for (int i = 0; i < s.length; i++) {
                String s1[] = s[i].split("=");
                if (s1.length >= 2) {
                    map.put(s1[0].toLowerCase(), URLDecoder.decode(s1[1]));
                }
            }
        }
        return map;
    }

    public static String stripNonValidXMLCharacters(String in) {
        StringBuffer out = new StringBuffer(); // Used to hold the output.
        char current; // Used to reference the current character.

        if (in == null || ("".equals(in)))
            return ""; // vacancy test.
        for (int i = 0; i < in.length(); i++) {
            current = in.charAt(i); // NOTE: No IndexOutOfBoundsException caught
            // here; it should not happen.
            if ((current == 0x9) || (current == 0xA) || (current == 0xD)
                    || ((current >= 0x20) && (current <= 0xD7FF))
                    || ((current >= 0xE000) && (current <= 0xFFFD))
                    || ((current >= 0x10000) && (current <= 0x10FFFF)))
                out.append(current);
        }
        return out.toString().trim();
    }

    /*
     * 根据xml解析出所有节点与节点值的映射
     */
    public static HashMap<String, String> convertXmlToMap(String xml)
            throws Exception {
        HashMap<String, String> valueMap = new HashMap<String, String>();
        DocumentBuilderFactory dbf = null;
        DocumentBuilder db = null;
        BufferedReader br = null;
        Document doc = null;
        Element root = null;
        NodeList nodes = null;
        try {
            dbf = DocumentBuilderFactory.newInstance();
            db = dbf.newDocumentBuilder();
            br = new BufferedReader(new StringReader(xml));
            doc = db.parse(new InputSource(br));
            root = doc.getDocumentElement();
            nodes = root.getChildNodes();
            if (nodes != null) {
                for (int i = 0; i < nodes.getLength(); i++) {
                    Node node = nodes.item(i);
                    String nodeName = node.getNodeName().toLowerCase();
                    Node firstChild = node.getFirstChild();
                    String nodeValue = "";
                    if (firstChild != null) {
                        nodeValue = firstChild.getNodeValue();
                    }
                    valueMap.put(nodeName, nodeValue);
                }
            }
        } catch (Exception ex) {
            throw ex;
        } finally {
            nodes = null;
            root = null;
            doc = null;
            if (br != null) {
                try {
                    br.close();
                } catch (Exception e) {
                }
            }
            db = null;
            dbf = null;

        }
        return valueMap;
    }

    /**
     * 判断容器中是否包含某个对象。
     * 用toString()方法确定。
     *
     * @param list 容器
     * @param t    对象
     * @return 包含true  不包含false
     */
    public static boolean isContain(List list, Object t) {
        for (Object tt : list) {
            if (tt == null) {
                continue;
            }
            if (tt.toString().trim().equals(t.toString().trim())) {
                return true;
            }
        }
        return false;
    }
    
    
    public static List<Object> batchToChangeType(List<String> list, Class clazz) throws JsonParseException, JsonMappingException, IOException
    {
    	List<Object> oLists = Lists.newArrayList();
    	for(String s: list)
    	{
    		Object t = mapper.readValue(s, clazz);
    		oLists.add(t);
    	}
    	
    	return oLists;
    	
    }

    public static void main(String args[]) {
        /*
         * SkyChargeTransferObj mo = new SkyChargeTransferObj(); Map map1 = new
		 * HashMap(); map1.put("a", "18"); Map map2 = new HashMap();
		 * map2.put("A", "spcode"); assembleParametersToObject(mo, map1, map2);
		 * System.out.println(mo);
		 */
        String file = CommonUtils.class.getClassLoader().getResource(
                "log4j.properties").getPath();
        System.out.println(file);
        // ClassLoader string=CommonUtils.class.getClassLoader();
    }

}
