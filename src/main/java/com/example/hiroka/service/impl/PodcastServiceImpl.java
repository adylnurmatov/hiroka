package com.example.hiroka.service.impl;

import com.example.hiroka.podcast.Podcast;
import com.example.hiroka.repo.PodcastRepository;
import com.example.hiroka.service.PodcastService;
import com.example.hiroka.views.podcast.Podcasts;
import com.google.cloud.texttospeech.v1.*;
import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;
import com.google.protobuf.ByteString;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class PodcastServiceImpl {
    private final PodcastRepository podcastRepository;


    public List<Podcast> findAllPodcast(String filterText){
        if(filterText == null || filterText.isEmpty()){
            return podcastRepository.findAll();
        } else {
            return podcastRepository.search(filterText);
        }
    }

    public String getLanguageCode(String s){
        Map<String, String> map = new HashMap<>();
        map.put("en", "en-US");
        map.put("ru", "ru-RU");
        map.put("ko", "ko-KR");
        map.put("fr", "fr-FR");
        map.put("ge", "de-DE");
        return map.get(s);
    }

    public String getLanguageCode2(String s){
        Map<String, String> map = new HashMap<>();
        map.put("en", "en");
        map.put("ru", "ru");
        map.put("ko", "ko");
        map.put("fr", "fr");
        map.put("ge", "de");
        return map.get(s);
    }

    public void deletePodcast(Podcast contact){
        podcastRepository.delete(contact);
    }
    public void savePodcast(Podcast podcast){
        Podcast save = new Podcast();
        save.setAuthor(podcast.getAuthor());
        save.setCategory(podcast.getCategory());
        save.setTitle(podcast.getTitle());
        save.setDescription(podcast.getDescription());
        save.setSubtitle(podcast.getSubtitle());
        save.setFileName(podcast.getFileName());
        podcastRepository.save(save);
    }

    public void text2Speech(String language) {
        List<Podcast> podcasts = podcastRepository.findAll();
        for(Podcast podcast: podcasts){
            String translatedText = translateText(podcast.getSubtitle(), language);
            saveAudio(translatedText, language, podcast.getFileName());
        }
    }

    public void saveAudio(String text, String language, String fileName){
        String directoryPath = "/home/marlen/Desktop/hiroka/src/main/resources/audio";  // e.g., "C:/Users/YourName/Music" or "/home/yourname/Music"
        File directory = new File(directoryPath);
        if (!directory.exists()) {
            directory.mkdirs(); // Create the directory if it doesn't exist
        }
        File outputFile = new File(directory, fileName);
        try (FileOutputStream fos = new FileOutputStream(outputFile)) {
            fos.write(synthesizeText(text, language));
//            return "File written successfully!";
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
//            return "Error writing the file: " + e.getMessage();
        }
    }

    public byte[] synthesizeText(String text, String language) {
        try (TextToSpeechClient textToSpeechClient = TextToSpeechClient.create()) {
//            TextToSpeechClient textToSpeechClient = TextToSpeechClient.create();
            SynthesisInput input = SynthesisInput.newBuilder().setText(text).build();
            VoiceSelectionParams voice = VoiceSelectionParams.newBuilder()
                    .setLanguageCode(getLanguageCode(language))
                    .setSsmlGender(SsmlVoiceGender.NEUTRAL)
                    .build();
            AudioConfig audioConfig = AudioConfig.newBuilder()
                    .setAudioEncoding(AudioEncoding.MP3)
                    .build();

            SynthesizeSpeechResponse response = textToSpeechClient.synthesizeSpeech(input, voice, audioConfig);
            ByteString audioContents = response.getAudioContent();

//            byte[] byteArray = Arrays.toString(audioContents.toByteArray()).getBytes();
            return audioContents.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Failed to create speech output", e);
        }
    }

    public String translateText(String text, String language){
        Translate translate = TranslateOptions.newBuilder().setApiKey("AIzaSyCwejMMJCXhfTpQRX-733ZiS6zBpGjsz9I").build().getService();

        Translation translation = translate.translate(text, Translate.TranslateOption.targetLanguage(getLanguageCode2(language)));

        return translation.getTranslatedText();
    }

    public List<Podcasts.UserPodcast> allNamesAndPaths() {
        List<Podcasts.UserPodcast> podcasts = new ArrayList<>();
        for(Podcast podcast: podcastRepository.findAll()){
            podcasts.add(new Podcasts.UserPodcast(podcast.getTitle(), "audio/" + podcast.getFileName()));
        }
        return podcasts;
    }

    public static class TextResponse {
        private String text;

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }
    }




}