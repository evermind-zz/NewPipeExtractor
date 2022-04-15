package org.schabi.newpipe.extractor.services.bitchute.extractor;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.schabi.newpipe.downloader.DownloaderTestImpl;
import org.schabi.newpipe.extractor.NewPipe;
import org.schabi.newpipe.extractor.StreamingService;
import org.schabi.newpipe.extractor.exceptions.ExtractionException;
import org.schabi.newpipe.extractor.services.DefaultStreamExtractorTest;
import org.schabi.newpipe.extractor.stream.StreamExtractor;
import org.schabi.newpipe.extractor.stream.StreamType;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.schabi.newpipe.extractor.ServiceList.Bitchute;

public class BitchuteStreamExtractorTest extends DefaultStreamExtractorTest {

    private static BitchuteStreamExtractor extractor;

    private static String url = "https://www.bitchute.com/video/m4iEhq4L1qU/";
    private static String expectedUrl = "https://www.bitchute.com/video/m4iEhq4L1qU";
    private static String expectedName = "BATTLE BUS LIVE | THE WAY FORWARD FOR LONDON - TRAILER";
    private static String expectedId = "m4iEhq4L1qU";
    private static String expectedDesc = "#BrianForMayor";
    private static String expectedCategory = "Entertainment";
    private static int expectedAgeLimit = 16;
    private static long expectedViewCountAtLeast = 230;
    private static String expectedUploaderName = "London Real";
    private static String expectedUploadDate = "2021-04-05 22:00:00.000";
    private static String expectedTextualUploadDate = "First published at 19:26 UTC on April 6th, 2021.";
    private static StreamExtractor.Privacy expectedPrivacy = StreamExtractor.Privacy.OTHER;
    private static String expectedUploaderUrl = "https://www.bitchute.com/channel/londonrealtv/";
    private static String expectedSupportInfo = "https://www.bitchute.com/help-us-grow/";
    private static boolean expectedHasAudioStreams = false;
    private static boolean expectedHasVideoStreams = true;
    private static String expectedArtistProfilePictureInfix = ".bitchute.com/live/channel_images/";

    @BeforeAll
    public static void setUp() throws ExtractionException, IOException {
        NewPipe.init(DownloaderTestImpl.getInstance());

        extractor = (BitchuteStreamExtractor) Bitchute
                .getStreamExtractor(url);
        extractor.fetchPage();
    }

    @Override
    public StreamExtractor extractor() {
        return extractor;
    }

    @Override
    public StreamingService expectedService() {
        return Bitchute;
    }

    @Override
    public String expectedName() {
        return expectedName;
    }

    @Override
    public String expectedId() {
        return expectedId;
    }

    @Override
    public String expectedUrlContains() {
        return expectedUrl;
    }

    @Override
    public String expectedOriginalUrlContains() {
        return expectedUrl;
    }

    @Override
    public StreamType expectedStreamType() {
        return StreamType.VIDEO_STREAM;
    }

    @Override
    public String expectedUploaderName() {
        return expectedUploaderName;
    }

    @Override
    public String expectedUploaderUrl() {
        return expectedUploaderUrl;
    }

    @Override
    public List<String> expectedDescriptionContains() {
        return Collections.singletonList(expectedDesc);
    }

    @Override
    public long expectedLength() {
        return 0;
    }

    @Override
    public long expectedViewCountAtLeast() {
        return expectedViewCountAtLeast;
    }

    @Override
    public String expectedUploadDate() {
        return expectedUploadDate;
    }

    @Override
    public String expectedTextualUploadDate() {
        return expectedTextualUploadDate;
    }

    @Override
    public long expectedLikeCountAtLeast() {
        return Long.MIN_VALUE;
    }

    @Override
    public long expectedDislikeCountAtLeast() {
        return Long.MIN_VALUE;
    }

    @Override
    public boolean expectedHasVideoStreams() {
        return expectedHasVideoStreams;
    }

    @Override
    public boolean expectedHasRelatedItems() {
        return true;
    }

    @Override
    public boolean expectedHasSubtitles() {
        return false;
    }

    @Override
    public boolean expectedHasFrames() {
        return false;
    }

    @Override
    public String expectedCategory() {
        return expectedCategory;
    }

    @Test
    public void testArtistProfilePicture() throws Exception {
        final String url = extractor().getUploaderAvatarUrl();
        assertTrue(url.contains(expectedArtistProfilePictureInfix) && url.endsWith(".jpg"));
    }

    @Override
    public int expectedAgeLimit() {
        return expectedAgeLimit;
    }

    @Override
    public StreamExtractor.Privacy expectedPrivacy() {
        return expectedPrivacy;
    }

    @Override
    public String expectedSupportInfo() {
        return expectedSupportInfo;
    }

    @Override
    public boolean expectedHasAudioStreams() {
        return expectedHasAudioStreams;
    }
}