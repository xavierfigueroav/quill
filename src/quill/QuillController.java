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
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyEvent;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.WindowEvent;

/**
 *
 * @author Galo Xavier Figueroa Villacreses
 */
public class QuillController implements Initializable {
    
    @FXML
    private TextArea paper;
    private Path filePath;
    final private EventHandler<KeyEvent> texChangeHandler = event -> handleTextChange(event);
    private boolean changesAreSaved;
    
    @FXML
    private void handleCloseRequest(WindowEvent event){
        
        if(!changesAreSaved){
        
            ButtonData choice = showSaveConfirmation().get().getButtonData();
            
            if(choice.equals(ButtonData.YES)) {
                
                handleSaveMenu(new ActionEvent());
                
                if(!changesAreSaved) event.consume();
                
            }
            
            else if(choice.equals(ButtonData.CANCEL_CLOSE)) event.consume();
            
            
        } else {
            // This else is necessary for the program to stop when there is no
            // changes in the document and the window is closed.
        }
        
    }
    
    @FXML
    private void handleNew(ActionEvent event){
        
        if(!changesAreSaved){
            
            ButtonData choice = showSaveConfirmation().get().getButtonData();
            
            if(choice.equals(ButtonData.YES)) handleSaveMenu(new ActionEvent());
            
            else if(choice.equals(ButtonData.NO)) generateANewDocument();
            
        } else generateANewDocument();
    }
    
    private void generateANewDocument(){
        
        paper.setText("");
        filePath = null;
        changesAreSaved = true;
        paper.addEventHandler(KeyEvent.KEY_TYPED, texChangeHandler);
        
    }
    
    @FXML
    private void handleOpenMenu(ActionEvent event){
        
        if(!changesAreSaved){
            
            ButtonData choice = showSaveConfirmation().get().getButtonData();
            
            if(choice.equals(ButtonData.YES)) handleSaveMenu(new ActionEvent());
            
            else if(choice.equals(ButtonData.NO)) openADocument();
            
        } else openADocument();
        
        
    }
    
    private void openADocument(){
        
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new ExtensionFilter("Text files","*.txt"));
        File file = fileChooser.showOpenDialog(null);
        
        if(file != null){
            String text = readFile(file);
            paper.setText(text);
            changesAreSaved = true;
            paper.addEventHandler(KeyEvent.KEY_TYPED, texChangeHandler);
        }
        
    }
    
    @FXML
    private void handleSaveMenu(ActionEvent event){
        
        if(filePath == null){
            
            handleSaveAsMenu(new ActionEvent());
            
        } else {
            writeFile();
            changesAreSaved = true;
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
            changesAreSaved = true;
            paper.addEventHandler(KeyEvent.KEY_TYPED, texChangeHandler);
        }
    }
    
    @FXML
    private void handleTextChange(KeyEvent event){
        
            changesAreSaved = false;
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
    
    private Optional<ButtonType> showSaveConfirmation(){
        
        ButtonType save = new ButtonType("Save", ButtonData.YES);
        ButtonType doNotSave = new ButtonType("Do not save", ButtonData.NO);
        ButtonType cancel = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);
        
        Alert alert = new Alert(AlertType.CONFIRMATION);
        
        alert.setTitle("Save confirmation");
        alert.setHeaderText("Some changes have not been saved.\nDo you want to save them?");
        alert.getButtonTypes().clear();
        alert.getButtonTypes().addAll(save,doNotSave,cancel);
        
        return alert.showAndWait();
        
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
        changesAreSaved = true;
        
    }
    
}
