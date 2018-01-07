# mp3_rename

mp3_rename is a program that retrieves and updates the fields of an mp3 file.

It currently only updates:
-release year
-album title
-album cover art
-artist
-title
-lyrics

It uses a drag-and-drop GUI built with Java swing. 

##How it works:
The program uses JSoup (https://jsoup.org/), musixmatch API Java wrapper (https://github.com/sachin-handiekar/jMusixMatch/blob/master/src/main/java/org/jmusixmatch/entity/track/MusicGenre.java), and mp3agic (https://github.com/mpatric/mp3agic/blob/master/src/main/java/com/mpatric/mp3agic/ID3v2.java).

First, an mp3 file must be dropped and the track title and track artist have to be given.

Then, the release year and album the track belongs to is found using musixmatch API.

Since the musixmatch wrapper doesn't provide the album cover art, I get the link to the track page on musixmatch through the wrapper. Then using JSoup, I parse through the page to collect all media links, and specifically the link to the album cover art. With the link I can download the image, then convert it to a byte[] and update the album image field of the mp3's id3v2 tag using mp3agic.

Similarly for lyrics, I use JSoup to parse through the track page and retrieve the lyrics for the track, then update the tag accordingly.

