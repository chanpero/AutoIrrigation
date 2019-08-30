package com.example.autoirrigation.Tools;

import android.util.Log;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Objects;

public class HttpConnSoap {
    public ArrayList<String> GetWebServer(String methodName, ArrayList<String> ParNames, ArrayList<String> ParValues) {
        ArrayList<String> values;

        //ServerUrl是指webservice的url
        String ServerUrl = "http://119.23.42.156:8080/WebService1.asmx";

        String soapAction = "http://tempuri.org/" + methodName;
        String soap = "<?xml version=\"1.0\" encoding=\"utf-8\"?>"
                + "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">"
                + "<soap:Body>";
        String tag, val, xmlRow;
        String xmlString;

        xmlString = "<" + methodName + " xmlns=\"http://tempuri.org/\">";
        for (int i = 0; i < ParNames.size(); i++) {
            tag = ParNames.get(i);
            val = ParValues.get(i);
            //将传来的参数转成xml格式
            //<userid>userid</userid>
            //<password>userpwd</password>
            xmlRow = "<" + tag + ">" + val + "</" + tag + ">";
            xmlString = xmlString + xmlRow;
        }
        xmlString += "</" + methodName + ">" + "</soap:Body>";
        String soap2 = "</soap:Envelope>";
        //requestData即soap请求
        String requestData = soap + xmlString + soap2;
        Log.d("RequestData: ", requestData);

        try {
            URL url = new URL(ServerUrl);
            byte[] bytes = requestData.getBytes(StandardCharsets.UTF_8);

            //得到connection对象
            HttpURLConnection con = (HttpURLConnection) url.openConnection();

            //设置允许读，写，不使用缓存
            con.setDoInput(true);
            con.setDoOutput(true);
            con.setUseCaches(false);

            //如果不设置超时，在网络异常情况下，程序会僵死
            con.setConnectTimeout(6000);

            //设置请求方式
            con.setRequestMethod("POST");

            //设置请求头
            con.setRequestProperty("Content-Type", "text/xml;charset=utf-8");
            con.setRequestProperty("Content-Length", "bytes.length");
            con.setRequestProperty("SOAPAction", soapAction);

            //获取Connection的output stream
            OutputStream outStream = con.getOutputStream();
            outStream.write(bytes);
            outStream.flush();
            outStream.close();

            //解析返回的数据流
            InputStream inStream = con.getInputStream();
            values = inputStreamToValuesList(inStream);
            return values;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public ArrayList<String> inputStreamToValuesList(InputStream in) throws IOException {
        //模板代码，读取流中的数据
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len;
        while ((len = in.read(buffer)) != -1) {
            os.write(buffer, 0, len);
        }
        in.close();
        String state = os.toString();
        os.close();

        //获取xml中CheckUserResult中的数据并返回
        return parseXml(state);
    }

    private static ArrayList<String> parseXml(String xml) {
        ArrayList<String> values = null;

        try {
            InputStream is = new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8));
            XmlPullParser parser = Xml.newPullParser();
            parser.setInput(is, "UTF-8");

            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        values = new ArrayList<>();
                        break;
                    case XmlPullParser.START_TAG:
                        if (parser.getName().equals("string")) {
                            Objects.requireNonNull(values).add(parser.nextText());
                        } else if (parser.getName().equals("InsertUserResult")) {
                            Objects.requireNonNull(values).add(parser.nextText());
                        } else if (parser.getName().equals("InsertTaskResult")) {
                            Objects.requireNonNull(values).add(parser.nextText());
                        } else if (parser.getName().equals("DeleteTaskResult")) {
                            Objects.requireNonNull(values).add(parser.nextText());
                        } else if (parser.getName().equals("SwitchTaskResult")) {
                            Objects.requireNonNull(values).add(parser.nextText());
                        }
                        break;
                }
                eventType = parser.next();
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return values;
    }
}
