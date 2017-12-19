package com.example.kuba.tvmanager.Mappers;

import android.content.Context;
import android.util.Xml;
import android.widget.Toast;

import com.example.kuba.tvmanager.Account;
import com.example.kuba.tvmanager.Episode;
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
 * Created by Kuba on 12/15/2017.
 */

public class EpisodeMapper {
    public static void createDocument(Context ctx){
        FileOutputStream fos;

        try {
            fos = ctx.openFileOutput("episodes.xml", Context.MODE_APPEND);

            XmlSerializer serializer = Xml.newSerializer();
            serializer.setOutput(fos, "UTF-8");
            serializer.startDocument(null, Boolean.valueOf(true));
            serializer.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);
            serializer.startTag(null, "episodes");

            //String id, String season, String episodeNumber, String name, TVShow showId
            ArrayList<Episode> episodes = new ArrayList<Episode>();
            episodes.add(new Episode("1", "1", "1", "Days Gone Bye", TVShowMapper.selectShow(ctx, 1)));
            episodes.add(new Episode("2", "1", "2", "Guts", TVShowMapper.selectShow(ctx, 1)));
            episodes.add(new Episode("3", "1", "3", "Tell It to the Frogs", TVShowMapper.selectShow(ctx, 1)));
            episodes.add(new Episode("4", "2", "1", "What Lies Ahead", TVShowMapper.selectShow(ctx, 1)));
            episodes.add(new Episode("5", "2", "2", "Bloodletting", TVShowMapper.selectShow(ctx, 1)));
            episodes.add(new Episode("6", "2", "3", "Save the Last One", TVShowMapper.selectShow(ctx, 1)));
            episodes.add(new Episode("7", "1", "1", "Winter is Coming", TVShowMapper.selectShow(ctx, 2)));
            episodes.add(new Episode("8", "1", "2", "The Kingsroad", TVShowMapper.selectShow(ctx, 2)));
            episodes.add(new Episode("9", "2", "1", "The North Remembers", TVShowMapper.selectShow(ctx, 2)));
            episodes.add(new Episode("10", "2", "2", "The Night Lands", TVShowMapper.selectShow(ctx, 2)));
            episodes.add(new Episode("11", "3", "1", "Valar Dohaeris", TVShowMapper.selectShow(ctx, 2)));
            episodes.add(new Episode("12", "3", "2", "Dark Wings, Dark Words", TVShowMapper.selectShow(ctx, 2)));
            episodes.add(new Episode("13", "1", "1", "Pilot", TVShowMapper.selectShow(ctx, 3)));
            episodes.add(new Episode("14", "1", "2", "Lucifer, Stay. Good Devil", TVShowMapper.selectShow(ctx, 3)));
            episodes.add(new Episode("15", "1", "3", "The Would-Be Prince of Darkness", TVShowMapper.selectShow(ctx, 3)));
            episodes.add(new Episode("16", "1", "1", "Pilot", TVShowMapper.selectShow(ctx, 4)));
            episodes.add(new Episode("17", "1", "2", "Honor Thy Father", TVShowMapper.selectShow(ctx, 4)));
            episodes.add(new Episode("18", "1", "3", "Lone Gunman", TVShowMapper.selectShow(ctx, 4)));
            episodes.add(new Episode("19", "2", "1", "City of Heroes", TVShowMapper.selectShow(ctx, 4)));
            episodes.add(new Episode("20", "2", "2", "Identity", TVShowMapper.selectShow(ctx, 4)));
            episodes.add(new Episode("21", "1", "1", "City of Heroes", TVShowMapper.selectShow(ctx, 5)));
            episodes.add(new Episode("22", "1", "2", "Fastest Man Alive", TVShowMapper.selectShow(ctx, 5)));
            episodes.add(new Episode("23", "1", "1", "The Red Serpent", TVShowMapper.selectShow(ctx, 6)));
            episodes.add(new Episode("24", "1", "2", "Sacramentum Gladiatorum", TVShowMapper.selectShow(ctx, 6)));
            episodes.add(new Episode("25", "1", "3", "Legends", TVShowMapper.selectShow(ctx, 6)));
            episodes.add(new Episode("26", "1", "4", "The Thing in the Pit", TVShowMapper.selectShow(ctx, 6)));
            episodes.add(new Episode("27", "1", "5", "Shadow Games", TVShowMapper.selectShow(ctx, 6)));
            episodes.add(new Episode("28", "1", "1", "Pilot", TVShowMapper.selectShow(ctx, 7)));
            episodes.add(new Episode("29", "1", "1", "Pilot", TVShowMapper.selectShow(ctx, 8)));
            episodes.add(new Episode("30", "1", "1", "Pilot", TVShowMapper.selectShow(ctx, 9)));
            episodes.add(new Episode("31", "1", "1", "Pilot", TVShowMapper.selectShow(ctx, 10)));

            for(int i = 0; i < episodes.size(); i++){
                serializer.startTag(null, "episode");
                serializer.startTag(null, "id");
                serializer.text(episodes.get(i).getId());
                serializer.endTag(null, "id");

                serializer.startTag(null, "season");
                serializer.text(episodes.get(i).getSeason());
                serializer.endTag(null, "season");

                serializer.startTag(null, "episodeNumber");
                serializer.text(episodes.get(i).getEpisodeNumber());
                serializer.endTag(null, "episodeNumber");

                serializer.startTag(null, "name");
                serializer.text(episodes.get(i).getName());
                serializer.endTag(null, "name");

                serializer.startTag(null, "showId");
                serializer.text(episodes.get(i).getShowId().getId());
                serializer.endTag(null, "showId");
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
        File xml = new File(ctx.getFilesDir() + "/episodes.xml");

        if(!xml.exists()){
            createDocument(ctx);
        }
        Document d = null;
        try {
            FileInputStream is = ctx.openFileInput("episodes.xml");
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

    public static ArrayList<Episode> getEpisodes(Context ctx){
        File xml = new File(ctx.getFilesDir() + "/episodes.xml");

        if(!xml.exists()){
            createDocument(ctx);
        }
        ArrayList<Episode> episodes = new ArrayList<>();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            FileInputStream is = ctx.openFileInput("episodes.xml");
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(is);
            Element element = doc.getDocumentElement();
            element.normalize();
            //String id, String season, String episodeNumber, String name, TVShow showId
            NodeList episodeList = doc.getElementsByTagName("episode");
            for(int i = 0; i < episodeList.getLength(); i++) {
                NodeList childList = episodeList.item(i).getChildNodes();
                Episode episode = new Episode();
                for (int j = 0; j < childList.getLength(); j++) {

                    Node node = childList.item(j);
                    switch(node.getNodeName()){
                        case "id":
                            episode.setId(node.getTextContent());
                            break;
                        case "name":
                            episode.setName(node.getTextContent());
                            break;
                        case "season":
                            episode.setSeason(node.getTextContent());
                            break;
                        case "episodeNumber":
                            episode.setEpisodeNumber(node.getTextContent());
                            break;
                        case "showId":
                            episode.setShowId(TVShowMapper.selectShow(ctx, Integer.valueOf(node.getTextContent())));
                            break;
                        default:
                            continue;
                    }

                }
                episodes.add(episode);
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
        return episodes;
    }

    public static Episode selectEpisode(Context ctx, int episodeId){
        File xml = new File(ctx.getFilesDir() + "/episodes.xml");

        if(!xml.exists()){
            createDocument(ctx);
        }

        Episode show = new Episode();
        String myId = String.valueOf(episodeId);
        boolean passInformation = false;

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            FileInputStream is = ctx.openFileInput("episodes.xml");
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(is);
            Element element = doc.getDocumentElement();
            element.normalize();

            NodeList accountList = doc.getElementsByTagName("episode");
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
                        case "season":
                            if(passInformation){
                                show.setSeason(node.getTextContent());
                            }
                            break;
                        case "episodeNumber":
                            if(passInformation){
                                show.setEpisodeNumber(node.getTextContent());
                            }
                            break;
                        case "showId":
                            if(passInformation){
                                TVShow myShow = TVShowMapper.selectShow(ctx, Integer.parseInt(node.getTextContent()));
                                show.setShowId(myShow);
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
            Toast.makeText(ctx, "chyba v select account", Toast.LENGTH_SHORT).show();
        } catch (FileNotFoundException e) {
            Toast.makeText(ctx, "chyba v select account", Toast.LENGTH_SHORT).show();
        } catch (SAXException e) {
            Toast.makeText(ctx, "chyba v select account", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Toast.makeText(ctx, "chyba v select account", Toast.LENGTH_SHORT).show();
        }

        return show;
    }


}
