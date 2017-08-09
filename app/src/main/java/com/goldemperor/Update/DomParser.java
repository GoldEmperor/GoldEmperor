package com.goldemperor.Update;//package com.cxstockin.Public;
//
//import java.io.InputStream;
//import java.util.ArrayList;
//import java.util.List;
//
//import javax.xml.parsers.DocumentBuilder;
//import javax.xml.parsers.DocumentBuilderFactory;
//
//import org.w3c.dom.Document;
//import org.w3c.dom.Element;
//import org.w3c.dom.Node;
//import org.w3c.dom.NodeList;
//
//public class DomParser {
//    /*
//     * 解析xml文件，返回对象集合
//     * @param is xml文件的输入流
//     * @return 对象集合
//     * @throws Exception
//     */
//    public static List<UpdataInfo> parseXml(InputStream is) throws Exception {
//        //新建一个集合，用于存放解析后的对象
//        List<UpdataInfo> personList = new ArrayList<UpdataInfo>();
//        //创建对错引用
//        Person person = null;
//        //得到Dom解析对象工厂
//        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
//        //通过工厂创建Dom解析对象实例
//        DocumentBuilder db = factory.newDocumentBuilder();
//        //将xml文件的输入流交给Dom解析对象进行解析，并将Dom树返回
//        Document document = db.parse(is);
//        //通过Dom树接收到根元素
//        Element rootElement = document.getDocumentElement();
//        //通过根元素获得下属的所有名字为person节点
//        NodeList nodeList = rootElement.getElementsByTagName("person");
//        //遍历取出来的person节点集合
//        for (int i = 0; i < nodeList.getLength(); i++) {
//            //得到一个person节点
//            Element personElement = (Element) nodeList.item(i);
//            //新建一个Person对象
//            person = new Person();
//            //将xml标签的id属性值赋值给Person对象的Id属性
//            person.setId(new Integer(personElement.getAttribute("id")));
//            //得到person标签的下属所节点
//            NodeList personChildList = personElement.getChildNodes();
//            //循环得到的下属标签
//            for (int y = 0; y < personChildList.getLength(); y++) {
//                //创建一个引用，指向循环的标签
//                Node node = personChildList.item(y);
//                //如果此循环出来的元素是Element对象，即标签元素，那么执行以下代码
//                if (Node.ELEMENT_NODE == node.getNodeType()) {
//                    //如果这个标签的名字是name,那么得到它的值，赋值给Person对象
//                    if ("name".equals(node.getNodeName())) {
//                        String nameValue = node.getFirstChild().getNodeValue();
//                        person.setName(nameValue);
//                    }
//                    //如果这个标签的名字是age,那么得到它的值，赋值给Person对象
//                    if ("age".equals(node.getNodeName())) {
//                        String ageValue = node.getFirstChild().getNodeValue();
//                        person.setAge(new Short(ageValue));
//                    }
//                }
//            }
//            //将此person对象添加到personList中
//            personList.add(person);
//            //将person置空
//            person = null;
//        }
//        //返回xml解析后得到的对象集合
//        return personList;
//    }
//}
//</person></person></person>