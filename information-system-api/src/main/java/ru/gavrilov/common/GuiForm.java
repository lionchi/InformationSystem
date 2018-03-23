package ru.gavrilov.common;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.gavrilov.MainApp;

import java.net.URL;

public class GuiForm<P extends Parent, C extends Controller> {
    private static final Logger LOG = LoggerFactory.getLogger(GuiForm.class);
    private P parent;
    private C controller;

    public GuiForm(String[] filePath) {
        try {
            StringBuilder builder = new StringBuilder("ui");
            for (String pack : filePath) {
                builder.append("/");
                builder.append(pack);
            }

            FXMLLoader fxmlLoader = new FXMLLoader();
            URL url = MainApp.class.getClassLoader().getResource(builder.toString());
            Guard.notNull(url,"FXML file not found!");
            fxmlLoader.setLocation(url);
            parent = fxmlLoader.load();
            controller = fxmlLoader.getController();
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new IllegalArgumentException("FXML file load error!");
        }
    }

    public GuiForm(String fileName) {
        this(new String[]{fileName});
    }

    public P getParent() {
        return parent;
    }

    public C getController() {
        return controller;
    }
}
