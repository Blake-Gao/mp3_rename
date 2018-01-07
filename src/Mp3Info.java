import org.jmusixmatch.MusixMatch;
import org.jmusixmatch.entity.track.PrimaryGenres;
import org.jmusixmatch.entity.track.Track;
import org.jmusixmatch.entity.track.TrackData;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.net.URL;

public class Mp3Info {

    private String artist;
    private String title;
    private TrackData data;
    private String url = "";
    private String apikey = "d4977ac5ecadcb59aab10c3d7001c613";
    private MusixMatch musixMatch = new MusixMatch(apikey);

    public Mp3Info(String title, String artist){
        this.artist = artist;
        this.title = title;
        try{
            Track track = musixMatch.getMatchingTrack(title, artist);
            data = track.getTrack();
            url = data.getTrackEditUrl();
        } catch(Exception e) {
            System.out.println("Could not find track!");
        }
    }

    public String getAlbum(){
        return data.getAlbumName();
    }

    public int getLength(){
        return data.getTrackLength();
    }

    public String getYear(){
        return data.getFirstReleaseDate();
    }

    public String getLyrics(){
        String lyrics = "";
        try {
            Document doc = Jsoup.connect(url)
                    .data("query", "Java")
                    .userAgent("Mozilla")
                    .cookie("auth", "token")
                    .timeout(3000)
                    .post();

            Elements lyricsElement = doc.select("textarea");

            lyrics = lyricsElement.toString();
            lyrics = lyrics.replaceAll("<.*?>","");
        } catch (Exception e) {
            System.out.println("Could not retrieve lyrics");
        }

        return lyrics;
    }

    public void getAlbumArt(){
        try{
            Document doc = Jsoup.connect(url).get();
            Elements media = doc.select("[src]");
            String albumImgLink = "";

            for (Element src : media) {
                if (src.tagName().equals("img")) {
                    if(src.attr("abs:src").contains("images-storage/albums")) {
                        albumImgLink = src.attr("abs:src");
                    }
                }
            }

            URL albumImgURL = new URL(albumImgLink);
            InputStream in = new BufferedInputStream(albumImgURL.openStream());
            OutputStream out = new BufferedOutputStream(new FileOutputStream("album-image.jpg"));

            for ( int i; (i = in.read()) != -1; ) {
                out.write(i);
            }
            in.close();
            out.close();
        } catch (Exception e){
            System.out.println("Could not retrieve photo");
        }
    }


}
