// Created by Fynn Godau 2019, licensed GNU GPL version 3 or later

package org.schabi.newpipe.extractor.services.bandcamp.extractors;

import com.grack.nanojson.JsonObject;
import com.grack.nanojson.JsonParser;
import com.grack.nanojson.JsonParserException;
import com.grack.nanojson.JsonWriter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.schabi.newpipe.extractor.NewPipe;
import org.schabi.newpipe.extractor.exceptions.ParsingException;
import org.schabi.newpipe.extractor.exceptions.ReCaptchaException;
import org.schabi.newpipe.extractor.localization.DateWrapper;
import org.schabi.newpipe.extractor.utils.Utils;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class BandcampExtractorHelper {

    /**
     * <p>Get an attribute of a web page as JSON
     *
     * <p>Originally a part of bandcampDirect.</p>
     *
     * @param html     The HTML where the JSON we're looking for is stored inside a
     *                 variable inside some JavaScript block
     * @param variable Name of the variable
     * @return The JsonObject stored in the variable with this name
     */
    public static JsonObject getJsonData(String html, String variable) throws JsonParserException, ArrayIndexOutOfBoundsException, ParsingException {
        Document document = Jsoup.parse(html);
        String json = document.getElementsByAttribute(variable).attr(variable);
        return JsonParser.object().from(json);
    }

    /**
     * Translate all these parameters together to the URL of the corresponding album or track
     * using the mobile api
     */
    public static String getStreamUrlFromIds(long bandId, long itemId, String itemType) throws ParsingException {

        try {
            String jsonString = NewPipe.getDownloader().get(
                    "https://bandcamp.com/api/mobile/22/tralbum_details?band_id=" + bandId
                            + "&tralbum_id=" + itemId + "&tralbum_type=" + itemType.substring(0, 1))
                    .responseBody();

            return JsonParser.object().from(jsonString).getString("bandcamp_url").replace("http://", "https://");

        } catch (JsonParserException | ReCaptchaException | IOException e) {
            throw new ParsingException("Ids could not be translated to URL", e);
        }

    }

    /**
     * Concatenate all non-null and non-empty strings together while separating them using
     * the comma parameter
     */
    public static String smartConcatenate(String[] strings, String comma) {
        StringBuilder result = new StringBuilder();

        // Remove empty strings
        ArrayList<String> list = new ArrayList<>(Arrays.asList(strings));
        for (int i = list.size() - 1; i >= 0; i--) {
            if (Utils.isNullOrEmpty(list.get(i)) || list.get(i).equals("null")) {
                list.remove(i);
            }
        }

        // Append remaining strings to result
        for (int i = 0; i < list.size(); i++) {
            String string = list.get(i);
            result.append(string);

            if (i != list.size() - 1) {
                // This is not the last iteration yet
                result.append(comma);
            }

        }

        return String.valueOf(result);
    }

    /**
     * Fetch artist details from mobile endpoint.
     * <a href=https://notabug.org/fynngodau/bandcampDirect/wiki/rewindBandcamp+%E2%80%93+Fetching+artist+details>
     * More technical info.</a>
     */
    public static JsonObject getArtistDetails(String id) throws ParsingException {
        try {
            return
                    JsonParser.object().from(
                            NewPipe.getDownloader().post(
                                    "https://bandcamp.com/api/mobile/22/band_details",
                                    null,
                                    JsonWriter.string()
                                            .object()
                                            .value("band_id", id)
                                            .end()
                                            .done()
                                            .getBytes()
                            ).responseBody()
                    );
        } catch (IOException | ReCaptchaException | JsonParserException e) {
            throw new ParsingException("Could not download band details", e);
        }
    }

    /**
     * @param id    The image ID
     * @param album Whether this is the cover of an album
     * @return Url of image with this ID in size 10 which is 1200x1200 (we could also choose size 0
     * but we don't want something as large as 3460x3460 here, do we?)
     */
    public static String getImageUrl(long id, boolean album) {
        return "https://f4.bcbits.com/img/" + (album ? 'a' : "") + id + "_10.jpg";
    }

    static DateWrapper parseDate(String textDate) throws ParsingException {
        try {
            Date date = new SimpleDateFormat("dd MMM yyyy HH:mm:ss zzz", Locale.ENGLISH).parse(textDate);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            return new DateWrapper(calendar, false);
        } catch (ParseException e) {
            throw new ParsingException("Could not extract date", e);
        }
    }
}
