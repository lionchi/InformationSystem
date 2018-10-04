package ru.gavrilov.tasks;

import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.gavrilov.entrys.PK;
import ru.gavrilov.entrys.VideoCard;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class VideoCardTask extends Task<String> {
    private static final PK pk = PK.INSTANCE;
    private static final Logger LOG = LoggerFactory.getLogger(ComputerSystemTask.class);

    @Override
    protected String call() throws Exception {
        try {
            String filePath = "./foo.txt";
            ProcessBuilder pb = new ProcessBuilder("cmd.exe", "/c", "dxdiag", "/t", filePath);
            Process p = pb.start();
            p.waitFor();

            BufferedReader br = new BufferedReader(new FileReader(filePath));
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().startsWith("Card name:") || line.trim().startsWith("Current Mode:")
                        || line.trim().startsWith("Driver Version:")
                        || line.trim().startsWith("Device Problem Code:") || line.trim().startsWith("Chip type:")
                        || line.trim().startsWith("Device Key:")) {
                    stringBuilder.append(line.trim()).append("\n");
                }
            }
            br.close();
            File f1 = new File(filePath);
            f1.delete();
            pk.setVideoCards(getVideoCards(stringBuilder.toString()));
            stringBuilder = new StringBuilder();
            for (int i = 0; i < pk.getVideoCards().size(); i++) {
                stringBuilder.append(String.format("%s) %s", i + 1, pk.getVideoCards().get(i).toString())).append("\n");
            }
            return stringBuilder.toString();
        } catch (IOException | InterruptedException ex) {
            ex.printStackTrace();
            return "";
        }
    }

    private ArrayList<VideoCard> getVideoCards(String str) {
        String[] split = str.split("\n");
        ArrayList<VideoCard> videoCards = new ArrayList<>();
        VideoCard videoCard = new VideoCard();
        for (String s : split) {
            if (s.startsWith("Card name:")) {
                if (videoCard.getCardName() != null) {
                    videoCards.add(videoCard);
                    videoCard = new VideoCard();
                }
                videoCard.setCardName(s.substring(s.indexOf(":") + 1).trim());
            } else if (s.startsWith("Current Mode:")) {
                videoCard.setCurrentMode(s.substring(s.indexOf(":") + 1).trim());
            } else if (s.startsWith("Driver Version:") &&
                    s.trim().substring(s.indexOf(":") + 1).trim().matches("[0-9]{2}\\.[0-9]{2}\\.[0-9]{2}\\.[0-9]{4}")) {
                videoCard.setDriverVersion(s.substring(s.indexOf(":") + 1).trim());
            } else if (s.startsWith("Device Problem Code:")) {
                videoCard.setDeviceProblemCode(s.trim().substring(s.indexOf(":") + 1).trim());
            } else if (s.trim().startsWith("Chip type:")) {
                videoCard.setChipType(s.substring(s.indexOf(":") + 1).trim());
            } else if (s.trim().startsWith("Device Key:")) {
                videoCard.setDeviceKey(s.substring(s.indexOf(":") + 1).trim());
            }
        }
        videoCards.add(videoCard);
        return videoCards;
    }
}
