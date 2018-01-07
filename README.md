# mp3_rename

mp3_rename is a program that retrieves and updates the fields of an mp3 file.

It currently only updates:
* release year
* album title
* album cover art
* artist
* title
* lyrics

It uses a drag-and-drop GUI built with Java swing. 

## How it works:
The program uses JSoup (https://jsoup.org/)

musixmatch API Java wrapper (https://github.com/sachin-handiekar/jMusixMatch/blob/master/src/main/java/org/jmusixmatch/entity/track/MusicGenre.java)

mp3agic (https://github.com/mpatric/mp3agic/blob/master/src/main/java/com/mpatric/mp3agic/ID3v2.java).

To get it started, an mp3 file must be dropped and the track title and track artist have to be given.

TThe release year of the album and album the track belongs to is found using musixmatch API.

Since the musixmatch wrapper doesn't provide the album cover art, the link to the track's page on musixmatch is obtained using the wrapper, then using Jsoup, the page and parsed and the link to the album covert art is retrieved. From the link, the image is downloaded, then converted to a byte[] to update the id3v2 tag.

For lyrics, JSoup parses through the HTML of the track page and retrieves the lyics.

Using all this information retrieved, the id3v2 tags are updated accordingly.

