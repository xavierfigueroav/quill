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
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyEvent;
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
    final private EventHandler<KeyEvent> texChangeHandler = event -> handleTextChange(event);
    private boolean textWasChanged = false;
    
    @FXML
    private void handleNew(ActionEvent event){
        paper.setText("");
        filePath = null;
        textWasChanged = false;
        paper.addEventHandler(KeyEvent.KEY_TYPED, texChangeHandler);
    }
    
    @FXML
    private void handleOpenMenu(ActionEvent event){
        if(!paper.getText().isEmpty()){
            System.out.println("Vas a perder lo que no hayas guardado");
        }
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new ExtensionFilter("Text files","*.txt"));
        File file = fileChooser.showOpenDialog(null);
        
        if(file != null){
            String text = readFile(file);
            paper.setText(text);
            textWasChanged = false;
            paper.addEventHandler(KeyEvent.KEY_TYPED, texChangeHandler);
        }
    }
    
    @FXML
    private void handleSaveMenu(ActionEvent event){
        
        if(filePath == null){
            
            handleSaveAsMenu(new ActionEvent());
            
        } else {
            writeFile();
            textWasChanged = false;
            paper.addEventHandler(KeyEvent.KEY_TYPED, texChangeHandler);
        }
    }
    
    @FXML
    private void handleSaveAsMenu(ActionEvent event){
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new ExtensionFilter("Text files","*.txt"));
        File file = fileChooser.showSaveDialog(null);
        
        if(file != null){
            
            if(file.exists()){
                file.delete();
            }

            writeFile(file);
            textWasChanged = false;
            paper.addEventHandler(KeyEvent.KEY_TYPED, texChangeHandler);
        }
    }
    
    @FXML
    private void handleTextChange(KeyEvent event){
        
            System.out.println("Cambió");
            textWasChanged = true;
            paper.removeEventHandler(KeyEvent.KEY_TYPED, texChangeHandler);
        
    }
    
    private String readFile(File file){
        filePath = file.toPath();
        
        String text = null;
        try {
            text = Files.lines(filePath).collect(Collectors.joining("\n"));
        } catch (IOException ex) {
            System.out.println("Error al intentar leer archivo.");
        }
        
        return text;
    }
    
    private void writeFile(File file){
        
        filePath = file.toPath();
        
        try{
            Files.write(filePath,paper.getParagraphs(),StandardCharsets.UTF_8,StandardOpenOption.CREATE);
        }catch(IOException io){
            System.out.println("Error al intentar escribir archivo.");
        }
    }
    
    private void writeFile(){
        
        try{
            Files.write(filePath,paper.getParagraphs(),StandardCharsets.UTF_8,StandardOpenOption.WRITE);
        }catch(IOException io){
            System.out.println("Error al intentar escribir archivo.");
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        
        paper.addEventHandler(KeyEvent.KEY_TYPED, texChangeHandler);
        
    }
    
}
