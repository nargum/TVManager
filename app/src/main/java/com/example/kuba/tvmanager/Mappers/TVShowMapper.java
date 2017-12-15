package com.example.kuba.tvmanager.Mappers;

import android.content.Context;
import android.util.Xml;

import com.example.kuba.tvmanager.Account;
import com.example.kuba.tvmanager.TVShow;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * Created by Kuba on 12/13/2017.
 */

public class TVShowMapper {
    public static void createDocument(Context ctx){
        FileOutputStream fos;

        try {
            fos = ctx.openFileOutput("shows.xml", Context.MODE_APPEND);

            XmlSerializer serializer = Xml.newSerializer();
            serializer.setOutput(fos, "UTF-8");
            serializer.startDocument(null, Boolean.valueOf(true));
            serializer.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);
            serializer.startTag(null, "TVShows");

            ArrayList<TVShow> shows = new ArrayList<>();
            shows.add(new TVShow("1", "The Walking Dead", 1));
            shows.add(new TVShow("2", "Game of Thrones", 1));
            shows.add(new TVShow("3", "Lucifer", 1));
            shows.add(new TVShow("4", "Arrow", 1));
            shows.add(new TVShow("5", "The Flash", 1));
            shows.add(new TVShow("6", "Spartacus", 1));
            shows.add(new TVShow("7", "The Big Bang Theory", 1));
            shows.add(new TVShow("8", "How I Meet Your Mother", 1));
            shows.add(new TVShow("9", "Two and a half men", 1));
            shows.add(new TVShow("10", "Band of Brothers", 1));

            for(int i = 0; i < shows.size(); i++){
                serializer.startTag(null, "TVShow");
                serializer.startTag(null, "id");
                serializer.text(shows.get(i).getId());
                serializer.endTag(null, "id");

                serializer.startTag(null, "name");
                serializer.text(shows.get(i).getName());
                serializer.endTag(null, "name");

                serializer.startTag(null, "score");
                serializer.text(String.valueOf(shows.get(i).getScore()));
                serializer.endTag(null, "score");
               // serializer.endTag(null, "TVShow");
            }

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
        File xml = new File(ctx.getFilesDir() + "/shows.xml");

        if(!xml.exists()){
            createDocument(ctx);
        }
        Document d = null;
        try {
            FileInputStream is = ctx.openFileInput("shows.xml");
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

    public static ArrayList<TVShow> getShows(Context ctx){
        File xml = new File(ctx.getFilesDir() + "/shows.xml");

        if(!xml.exists()){
            createDocument(ctx);
        }
        ArrayList<TVShow> shows = new ArrayList<>();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            FileInputStream is = ctx.openFileInput("shows.xml");
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(is);
            Element element = doc.getDocumentElement();
            element.normalize();

            NodeList accountList = doc.getElementsByTagName("TVShow");
            for(int i = 0; i < accountList.getLength(); i++) {
                NodeList childList = accountList.item(i).getChildNodes();
                TVShow show  = new TVShow();
                for (int j = 0; j < childList.getLength(); j++) {

                    Node node = childList.item(j);
                    switch(node.getNodeName()){
                        case "id":
                            show.setId(node.getTextContent());
                            break;
                        case "name":
                            show.setName(node.getTextContent());
                            break;
                        case "score":
                            show.setScore(Integer.parseInt(node.getTextContent()));
                            break;
                        default:
                            continue;
                    }

                }
                shows.add(show);
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
        return shows;
    }

    public static TVShow selectShow(Context ctx, int id){
        File xml = new File(ctx.getFilesDir() + "/shows.xml");

        if(!xml.exists()){
            createDocument(ctx);
        }

        TVShow show = new TVShow();
        String myId = String.valueOf(id);
        boolean passInformation = false;

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            FileInputStream is = ctx.openFileInput("shows.xml");
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(is);
            Element element = doc.getDocumentElement();
            element.normalize();

            NodeList accountList = doc.getElementsByTagName("TVShow");
            for(int i = 0; i < accountList.getLength(); i++) {
                NodeList childList = accountList.item(i).getChildNodes();
                for (int j = 0; j < childList.getLength(); j++) {

                    Node node = childList.item(j);
                    switch(node.getNodeName()){
                        case "id":
                            if(myId.equals(String.valueOf(node.getTextContent()))){
                                show.setId(node.getTextContent());
                                passInformation = true;
                            }
                            break;
                        case "name":
                            if(passInformation){
                                show.setName(node.getTextContent());
                            }
                            break;
                        case "score":
                            if(passInformation){
                                show.setScore(Integer.parseInt(node.getTextContent()));
                            }
                            break;
                        default:
                            continue;
                    }

                }
                if(passInformation)
                    break;
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

        return show;
    }
}
