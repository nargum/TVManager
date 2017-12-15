package com.example.kuba.tvmanager.Mappers;

import android.content.Context;
import android.os.Environment;
import android.util.Xml;

import com.example.kuba.tvmanager.Account;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

/**
 * Created by Kuba on 12/9/2017.
 */

public class AccountMapper {

    public static void createDocument(Context ctx){
        FileOutputStream fos;

        try {
            fos = ctx.openFileOutput("accounts.xml", Context.MODE_APPEND);

            XmlSerializer serializer = Xml.newSerializer();
            serializer.setOutput(fos, "UTF-8");
            serializer.startDocument(null, Boolean.valueOf(true));
            serializer.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);
            serializer.startTag(null, "accounts");

            serializer.startTag(null, "account");
            serializer.startTag(null, "id");
            serializer.text("0");
            serializer.endTag(null, "id");

            serializer.startTag(null, "name");
            serializer.text("admin");
            serializer.endTag(null, "name");

            serializer.startTag(null, "surname");
            serializer.text("admin");
            serializer.endTag(null, "surname");

            serializer.startTag(null, "email");
            serializer.text("admin@lobo.cz");
            serializer.endTag(null, "email");

            serializer.startTag(null, "login");
            serializer.text("admin");
            serializer.endTag(null, "login");

            serializer.startTag(null, "password");
            serializer.text("1234");
            serializer.endTag(null, "password");

            serializer.endDocument();

            serializer.flush();

            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Document getDocument(Context ctx){
        File xml = new File(ctx.getFilesDir() + "/accounts.xml");

        if(!xml.exists()){
            createDocument(ctx);
        }
        Document d = null;
        try {
            FileInputStream is = ctx.openFileInput("accounts.xml");
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            d = builder.parse(is);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return d;
    }

    public static void add(Context ctx, Account account){

        Document xmlDoc = getDocument(ctx);
        Element accounts = xmlDoc.getDocumentElement();
        Element newAccount = xmlDoc.createElement("account");

        Element newId = xmlDoc.createElement("id");
        Element newName = xmlDoc.createElement("name");
        Element newSurname = xmlDoc.createElement("surname");
        Element newEmail = xmlDoc.createElement("email");
        Element newLogin = xmlDoc.createElement("login");
        Element newPassword = xmlDoc.createElement("password");

        newId.appendChild(xmlDoc.createTextNode(account.getId()));
        newName.appendChild(xmlDoc.createTextNode(account.getName()));
        newSurname.appendChild(xmlDoc.createTextNode(account.getSurname()));
        newEmail.appendChild(xmlDoc.createTextNode(account.getEmail()));
        newLogin.appendChild(xmlDoc.createTextNode(account.getLogin()));
        newPassword.appendChild(xmlDoc.createTextNode(account.getPassword()));

        newAccount.appendChild(newId);
        newAccount.appendChild(newName);
        newAccount.appendChild(newSurname);
        newAccount.appendChild(newEmail);
        newAccount.appendChild(newLogin);
        newAccount.appendChild(newPassword);

        accounts.appendChild(newAccount);

        DOMSource source = new DOMSource(xmlDoc);
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        try {
            Transformer transformer = transformerFactory.newTransformer();
            StreamResult result = new StreamResult(ctx.openFileOutput("accounts.xml", Context.MODE_PRIVATE));
            transformer.transform(source, result);
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<Account> getAccounts(Context ctx){
        File xml = new File(ctx.getFilesDir() + "/accounts.xml");

        if(!xml.exists()){
            createDocument(ctx);
        }
        ArrayList<Account> accounts = new ArrayList<>();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            FileInputStream is = ctx.openFileInput("accounts.xml");
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(is);
            Element element = doc.getDocumentElement();
            element.normalize();

            NodeList accountList = doc.getElementsByTagName("account");
            for(int i = 0; i < accountList.getLength(); i++) {
                NodeList childList = accountList.item(i).getChildNodes();
                Account account  = new Account();
                for (int j = 0; j < childList.getLength(); j++) {

                    Node node = childList.item(j);
                    switch(node.getNodeName()){
                        case "id":
                            account.setId(node.getTextContent());
                            break;
                        case "name":
                            account.setName(node.getTextContent());
                            break;
                        case "surname":
                            account.setSurname(node.getTextContent());
                            break;
                        case "email":
                            account.setEmail(node.getTextContent());
                            break;
                        case "login":
                            account.setLogin(node.getTextContent());
                            break;
                        case "password":
                            account.setPassword(node.getTextContent());
                            break;
                        default:
                            continue;
                    }

                }
                accounts.add(account);
            }
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return accounts;
    }

    public static String path(Context ctx){
        return String.valueOf(ctx.getFilesDir());
    }

    public static void delete(Context ctx){
        File dir = ctx.getFilesDir();
        File file = new File(dir, "accounts.xml");
        file.delete();
    }
}
