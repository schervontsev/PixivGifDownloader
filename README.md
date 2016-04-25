Warning: doesn't compile on java 1.7 or newer, use 1.6.

PixivGifDownloader
==================

For showing animation pixiv.net uses javascript, canvas, *.zip file with images and json with list of delays. This small script is for downloading such animation and converting it to GIF animation.
Script gets in first argument link to pixiv page with animation and creates a gif file in its folder. Downloaded zip file and unzipped pictures stay too, in case they are needed.

=== Usage ===

Just launch jar file with link to animation page in description.

java -jar PixivDownloader.jar "http://www.pixiv.net/member_illust.php?mode=medium&illust_id=44339903"

I also placed compiled jar in bin folder with cmd file for Windows (start.bat). Just change the link inside and launch it.

=== Acknowledgments ===

I used Wizzardo Tools (https://github.com/wizzardo/Tools) for parsing html and downloading and unzipping files. I also used Animated GIF Encoder (https://github.com/madmaw/animatedgifencoder) for converting images to GIF.

