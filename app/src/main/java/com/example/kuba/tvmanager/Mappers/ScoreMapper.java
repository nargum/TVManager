package com.example.kuba.tvmanager.Mappers;

import android.content.Context;
import android.util.Xml;
import android.widget.Toast;

import com.example.kuba.tvmanager.Account;
import com.example.kuba.tvmanager.Favourite;
import com.example.kuba.tvmanager.Score;
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
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

/**
 * Created by Kuba on 12/18/2017.
 */

public class ScoreMapper {
    public static void createDocument(Context ctx){
        FileOutputStream fos;

        try {
            fos = ctx.openFileOutput("score.xml", Context.MODE_APPEND);

            XmlSerializer serializer = Xml.newSerializer();
            serializer.setOutput(fos, "UTF-8");
            serializer.startDocument(null, Boolean.valueOf(true));
            serializer.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);
            serializer.startTag(null, "scoreList");
            serializer.endTag(null, "scoreList");

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
        File xml = new File(ctx.getFilesDir() + "/score.xml");

        if(!xml.exists()){
            createDocument(ctx);
        }
        Document d = null;
        try {
            FileInputStream is = ctx.openFileInput("score.xml");
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

    public static void add(Context ctx, Score score){

        Document xmlDoc = getDocument(ctx);
        Element favourites = xmlDoc.getDocumentElement();
        Element newFavourite = xmlDoc.createElement("scoreRecord");

        Element showId = xmlDoc.createElement("showId");
        Element accountId = xmlDoc.createElement("accountId");
        Element scoreEle = xmlDoc.createElement("score");

        showId.appendChild(xmlDoc.createTextNode(score.getShowId().getId()));
        accountId.appendChild(xmlDoc.createTextNode(score.getAccountId().getId()));
        scoreEle.appendChild(xmlDoc.createTextNode(String.valueOf(score.getScore())));

        newFavourite.appendChild(showId);
        newFavourite.appendChild(accountId);
        newFavourite.appendChild(scoreEle);

        favourites.appendChild(newFavourite);

        DOMSource source = new DOMSource(xmlDoc);
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        try {
            Transformer transformer = transformerFactory.newTransformer();
            StreamResult result = new StreamResult(ctx.openFileOutput("score.xml", Context.MODE_PRIVATE));
            transformer.transform(source, result);
        } catch (TransformerConfigurationException e) {
            Toast.makeText(ctx, "chyba v add favourites", Toast.LENGTH_SHORT).show();
        } catch (FileNotFoundException e) {
            Toast.makeText(ctx, "chyba v add favourites", Toast.LENGTH_SHORT).show();
        } catch (TransformerException e) {
            Toast.makeText(ctx, "chyba v add favourites", Toast.LENGTH_SHORT).show();
        }
    }

    public static ArrayList<Score> getScores(Context ctx){
        File xml = new File(ctx.getFilesDir() + "/score.xml");

        if(!xml.exists()){
            createDocument(ctx);
        }
        ArrayList<Score> favourites = new ArrayList<>();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            FileInputStream is = ctx.openFileInput("score.xml");
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(is);
            Element element = doc.getDocumentElement();
            element.normalize();


            NodeList favouriteList = doc.getElementsByTagName("scoreRecord");
            for(int i = 0; i < favouriteList.getLength(); i++) {
                NodeList childList = favouriteList.item(i).getChildNodes();
                Score favourite  = new Score();
                for (int j = 0; j < childList.getLength(); j++) {

                    Node node = childList.item(j);
                    switch(node.getNodeName()){
                        case "showId":
                            TVShow show = TVShowMapper.selectShow(ctx, Integer.parseInt(node.getTextContent()));
                            favourite.setShowId(show);
                            break;
                        case "accountId":
                            Account account = AccountMapper.selectAccount(ctx, Integer.parseInt(node.getTextContent()));
                            favourite.setAccountId(account);
                            break;
                        case "score":
                            favourite.setScore(Integer.parseInt(node.getTextContent()));
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

    public static void delete(Context ctx){
        File dir = ctx.getFilesDir();
        File file = new File(dir, "score.xml");
        file.delete();
    }

    //funkcni
    public static void update(Context ctx, Score score){
        try {
            Document doc = getDocument(ctx);
            Element element = doc.getDocumentElement();
            element.normalize();


            //boolean removeShow = false;
            //boolean removeAccount = false;
            int previousPosition = 0;
            int previousAccountPosition = 0;
            NodeList favouriteList = doc.getElementsByTagName("scoreRecord");
            for(int i = 0; i < favouriteList.getLength(); i++){
                NodeList childList = favouriteList.item(i).getChildNodes();

                for(int j = 0; j < childList.getLength(); j++){
                    Node node = childList.item(j);

                    if(node.getNodeName().equals("showId")){
                        if(node.getTextContent().equals(score.getShowId().getId())){
                            //removeShow = true;
                            previousPosition = j;
                        }
                    }

                    if(node.getNodeName().equals("accountId")){
                        if(j - 1 == previousPosition && node.getTextContent().equals(score.getAccountId().getId())){
                            //removeAccount = true;
                            previousAccountPosition = j;
                        }else{
                            previousPosition = 0;
                            //removeShow = false;
                        }
                    }

                    if(node.getNodeName().equals("score")){
                        /*if(j - 1 == previousAccountPosition && j - 2 == previousPosition){
                            node.setTextContent(String.valueOf(score.getScore()));
                        }
                        else{
                            previousAccountPosition = 0;
                            previousPosition = 0;
                        }*/

                        if(childList.item(j - 2).getTextContent().equals(score.getShowId().getId()) && childList.item(j - 1).getTextContent().equals(score.getAccountId().getId())){
                            node.setTextContent(String.valueOf(score.getScore()));
                        }
                    }
                }

                /*if(removeShow && removeAccount){
                    element.removeChild(favouriteList.item(i));
                    break;
                }*/
            }


            DOMSource source = new DOMSource(doc);
            TransformerFactory transformerFactory = TransformerFactory.newInstance();

            Transformer transformer = transformerFactory.newTransformer();
            StreamResult result = new StreamResult(ctx.openFileOutput("score.xml", Context.MODE_PRIVATE));
            transformer.transform(source, result);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        }
    }

    //nefunkcni
    public static Score selectSpecific(Context ctx, String accountId, String showId){
        File xml = new File(ctx.getFilesDir() + "/score.xml");

        if(!xml.exists()){
            createDocument(ctx);
        }
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        Score score = null;

        try {
            FileInputStream is = ctx.openFileInput("score.xml");
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(is);
            Element element = doc.getDocumentElement();
            element.normalize();



            boolean removeShow = false;
            boolean removeAccount = false;
            int showPosition = 0;
            int accountPosition = 0;
            //int previousAccountPosition = 0;
            NodeList favouriteList = doc.getElementsByTagName("scoreRecord");
            for(int i = 0; i < favouriteList.getLength(); i++){
                NodeList childList = favouriteList.item(i).getChildNodes();

                for(int j = 0; j < childList.getLength(); j++){
                    Node node = childList.item(j);

                    if(node.getNodeName().equals("showId")){
                        if(node.getTextContent().equals(showId)){
                            removeShow = true;
                            showPosition = j;
                        }
                    }

                    if(node.getNodeName().equals("accountId")){
                        if(j - 1 == showPosition && node.getTextContent().equals(accountId)){
                            removeAccount = true;
                            accountPosition = j;
                           // previousAccountPosition = j;
                        }else{
                            showPosition = 0;
                            removeAccount = false;
                            removeShow = false;
                        }
                    }

                    if(node.getNodeName().equals("score")){
                        if(removeShow && removeAccount && j - 2 == showPosition && j - 1 == accountPosition){
                            score = new Score();
                            score.setAccountId(AccountMapper.selectAccount(ctx, Integer.parseInt(accountId)));
                            score.setShowId(TVShowMapper.selectShow(ctx, Integer.parseInt(showId)));
                            score.setScore(Integer.parseInt(node.getTextContent().toString()));
                        }else{
                            showPosition = 0;
                            accountPosition = 0;
                            removeAccount = false;
                            removeShow = false;
                        }
                    }
                }

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

        return score;
    }

    /*public static void deleteSpecific(Context ctx, String accountId, String showId){

        try {
            Document doc = getDocument(ctx);
            Element element = doc.getDocumentElement();
            element.normalize();


            boolean removeShow = false;
            boolean removeAccount = false;
            int previousPosition = 0;
            NodeList favouriteList = doc.getElementsByTagName("favourite");
            for(int i = 0; i < favouriteList.getLength(); i++){
                NodeList childList = favouriteList.item(i).getChildNodes();

                for(int j = 0; j < childList.getLength(); j++){
                    Node node = childList.item(j);

                    if(node.getNodeName().equals("showId")){
                        if(node.getTextContent().equals(showId)){
                            removeShow = true;
                            previousPosition = j;
                        }
                    }

                    if(node.getNodeName().equals("accountId")){
                        if(j - 1 == previousPosition && node.getTextContent().equals(accountId)){
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
            StreamResult result = new StreamResult(ctx.openFileOutput("fav.xml", Context.MODE_PRIVATE));
            transformer.transform(source, result);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        }

    }*/
}
