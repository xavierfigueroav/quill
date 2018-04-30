/*
 * Copyright (C) 2018 Galo Xavier Figueroa Villacreses
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package quill;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

/**
 *
 * @author Galo Xavier Figueroa Villacreses
 */
public class QuillController implements Initializable {
    
    @FXML
    private TextArea paper;
    
    private Path filePath;
    
    @FXML
    private void handleOpenMenu(ActionEvent event){
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new ExtensionFilter("Python files","*.py"));
        File file = fileChooser.showOpenDialog(null);
        String text = readFile(file);
        paper.setText(text);
    }
    
    @FXML
    private void handleSaveMenu(ActionEvent event){
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showSaveDialog(null);
    }
    
    @FXML
    private void handleSaveAsMenu(ActionEvent event){
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showSaveDialog(null);
    }
    
    private String readFile(File file){
        filePath = file.toPath();
        
        List<String> fileLines;
        String text = "";
        try{
            fileLines = Files.readAllLines(filePath);
            
            
            for(int i = 0; i < fileLines.size(); i++){
                if(i == fileLines.size() - 1){
                    text += fileLines.get(i);
                } else {
                    text += fileLines.get(i) + "\n";
                }
            }
            
        } catch(IOException io){
            io.printStackTrace();
        }
        
        return text;
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }
    
}
