package com.example.kuba.tvmanager.Mappers;

import android.content.Context;
import android.util.Xml;
import android.widget.Toast;

import com.example.kuba.tvmanager.Account;
import com.example.kuba.tvmanager.Episode;
import com.example.kuba.tvmanager.Score;
import com.example.kuba.tvmanager.TVShow;
import com.example.kuba.tvmanager.Watched;

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
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

/**
 * Created by Kuba on 12/19/2017.
 */

public class WatchedMapper {

    public static void createDocument(Context ctx){
        FileOutputStream fos;

        try {
            fos = ctx.openFileOutput("watched.xml", Context.MODE_APPEND);

            XmlSerializer serializer = Xml.newSerializer();
            serializer.setOutput(fos, "UTF-8");
            serializer.startDocument(null, Boolean.valueOf(true));
            serializer.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);
            serializer.startTag(null, "watchedEpisodes");
            serializer.endTag(null, "watchedEpisodes");

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
        File xml = new File(ctx.getFilesDir() + "/watched.xml");

        if(!xml.exists()){
            createDocument(ctx);
        }
        Document d = null;
        try {
            FileInputStream is = ctx.openFileInput("watched.xml");
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            d = builder.parse(is);
            // is.close();
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

    public static void add(Context ctx, Account accountId, Episode episodeId){

        Document xmlDoc = getDocument(ctx);
        Element favourites = xmlDoc.getDocumentElement();
        Element newFavourite = xmlDoc.createElement("watched");

        Element accountEle = xmlDoc.createElement("accountId");
        Element episodeEle = xmlDoc.createElement("episodeId");

        accountEle.appendChild(xmlDoc.createTextNode(accountId.getId()));
        episodeEle.appendChild(xmlDoc.createTextNode(String.valueOf(episodeId.getId())));

        newFavourite.appendChild(accountEle);
        newFavourite.appendChild(episodeEle);

        favourites.appendChild(newFavourite);

        DOMSource source = new DOMSource(xmlDoc);
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        try {
            Transformer transformer = transformerFactory.newTransformer();
            StreamResult result = new StreamResult(ctx.openFileOutput("watched.xml", Context.MODE_PRIVATE));
            transformer.transform(source, result);
        } catch (TransformerConfigurationException e) {
            Toast.makeText(ctx, "chyba v add favourites", Toast.LENGTH_SHORT).show();
        } catch (FileNotFoundException e) {
            Toast.makeText(ctx, "chyba v add favourites", Toast.LENGTH_SHORT).show();
        } catch (TransformerException e) {
            Toast.makeText(ctx, "chyba v add favourites", Toast.LENGTH_SHORT).show();
        }
    }

    public static ArrayList<Watched> getRecords(Context ctx){
        File xml = new File(ctx.getFilesDir() + "/watched.xml");

        if(!xml.exists()){
            createDocument(ctx);
        }
        ArrayList<Watched> favourites = new ArrayList<>();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            FileInputStream is = ctx.openFileInput("watched.xml");
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(is);
            Element element = doc.getDocumentElement();
            element.normalize();


            NodeList favouriteList = doc.getElementsByTagName("watched");
            for(int i = 0; i < favouriteList.getLength(); i++) {
                NodeList childList = favouriteList.item(i).getChildNodes();
                Watched favourite  = new Watched();
                for (int j = 0; j < childList.getLength(); j++) {

                    Node node = childList.item(j);
                    switch(node.getNodeName()){
                        case "accountId":
                            Account account = AccountMapper.selectAccount(ctx, Integer.parseInt(node.getTextContent()));
                            favourite.setAccountId(account);
                            break;
                        case "episodeId":
                            Episode episode = EpisodeMapper.selectEpisode(ctx, Integer.parseInt(node.getTextContent()));
                            favourite.setEpisodeId(episode);
                            break;
                        default:
                            continue;
                    }

                }
                favourites.add(favourite);
            }

            is.close();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return favourites;
    }

    public static void deleteSpecific(Context ctx, Account accountId, Episode episodeId){

        try {
            Document doc = getDocument(ctx);
            Element element = doc.getDocumentElement();
            element.normalize();


            boolean removeShow = false;
            boolean removeAccount = false;
            int previousPosition = 0;
            NodeList favouriteList = doc.getElementsByTagName("watched");
            for(int i = 0; i < favouriteList.getLength(); i++){
                NodeList childList = favouriteList.item(i).getChildNodes();

                for(int j = 0; j < childList.getLength(); j++){
                    Node node = childList.item(j);

                    if(node.getNodeName().equals("accountId")){
                        if(node.getTextContent().equals(accountId.getId())){
                            removeShow = true;
                            previousPosition = j;
                        }
                    }

                    if(node.getNodeName().equals("episodeId")){
                        if(j - 1 == previousPosition && node.getTextContent().equals(episodeId.getId())){
                            removeAccount = true;
                        }else{
                            previousPosition = 0;
                            removeShow = false;
                        }
                    }
                }

                if(removeShow && removeAccount){
                    element.removeChild(favouriteList.item(i));
                    break;
                }
            }


            DOMSource source = new DOMSource(doc);
            TransformerFactory transformerFactory = TransformerFactory.newInstance();

            Transformer transformer = transformerFactory.newTransformer();
            StreamResult result = new StreamResult(ctx.openFileOutput("watched.xml", Context.MODE_PRIVATE));
            transformer.transform(source, result);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        }

    }
}
