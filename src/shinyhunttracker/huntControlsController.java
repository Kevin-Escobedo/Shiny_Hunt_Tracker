package shinyhunttracker;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.awt.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import static java.lang.Double.parseDouble;
import static java.lang.Integer.parseInt;

public class huntControlsController implements Initializable {
    //controller elements
    Stage huntControls = new Stage();
    public Button encountersButton, pokemonCaughtButton, phaseButton;
    public HBox huntControlsButtonHBox;

    //hunt window elements
    Stage huntWindow = new Stage();
    AnchorPane huntLayout = new AnchorPane();
    int huntLayoutSize = 0;
    ImageView Evo0, Evo1, sprite;
    String evo0, evo1;
    Text currentHuntingMethodText, currentHuntingPokemonText, currentGameText, encountersText, previousEncountersText, currentComboText, oddFractionText;
    String[] fonts;
    int encounters, previousEncounters, combo = 0;
    int increment = 1;

    //hunt settings window elements
    Stage CustomizeHuntStage = new Stage();
    String currentLayout;
    int displayPrevious = 0;

    //current objects
    Pokemon selectedPokemon = new Pokemon();
    Game selectedGame = new Game();
    Method selectedMethod = new Method();
    int methodBase;

    @Override
    public void initialize(URL url, ResourceBundle rb){
        //moves HBox with all the buttons to the middle of the window
        huntControlsButtonHBox.setAlignment(Pos.CENTER);
        huntControlsButtonHBox.setSpacing(10);

        String[] avaliableFonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
        int fontArraySize = 0;
        for (String avaliableFont : avaliableFonts) {
            if (sanitizeAvaliableFontStrings(avaliableFont) != null) {
                fontArraySize++;
            }
        }

        fonts = new String[fontArraySize];
        int index = 0;
        for (String avaliableFont : avaliableFonts) {
            if (sanitizeAvaliableFontStrings(avaliableFont) != null) {
                fonts[index] = avaliableFont;
                index++;
            }
        }
    }

    //sets huntControls window to the given stage
    public void importStage(Stage stage){
        huntControls = stage;
        huntControls.setOnCloseRequest(e -> {
            e.consume();
            saveHunt();
        });
    }

    //creates hunt window
    public void createHuntWindow(Pokemon selectedPokemon, Game selectedGame, Method selectedMethod, Stage newHuntStage, String currentLayout, String evo0, String evo1, int encounters, int combo, int increment){
        huntWindow.setTitle("Hunt Window");
        huntWindow = newHuntStage;
        this.selectedPokemon = selectedPokemon;
        this.selectedGame = selectedGame;
        this.selectedMethod = selectedMethod;
        methodBase = selectedMethod.getBase();
        this.evo0 = evo0;
        this.evo1 = evo1;
        this.encounters = encounters;
        this.combo = combo;
        this.increment = increment;

        currentHuntingPokemonText = new Text(selectedPokemon.getName());
        currentHuntingMethodText= new Text(selectedMethod.getName());
        currentGameText = new Text(selectedGame.getName());
        oddFractionText= new Text("1/"+simplifyFraction(selectedMethod.getModifier(), selectedMethod.getBase()));
        dynamicOddsMethods();
        encountersText= new Text(String.valueOf(encounters));
        currentComboText = new Text(String.valueOf(combo));
        currentComboText.setVisible(false);
        previousEncountersText = new Text();
        previousEncountersText.setVisible(false);

        currentHuntingPokemonText.setStroke(Color.web("0x00000000"));
        currentHuntingMethodText.setStroke(Color.web("0x00000000"));
        currentGameText.setStroke(Color.web("0x00000000"));
        oddFractionText.setStroke(Color.web("0x00000000"));
        encountersText.setStroke(Color.web("0x00000000"));
        previousEncountersText.setStroke(Color.web("0x00000000"));
        currentComboText.setStroke(Color.web("0x00000000"));

        sprite = createPokemonSprite(selectedPokemon.getName(), selectedGame);
        Evo0 = new ImageView();
        Evo1 = new ImageView();
        if(evo0 != null) {
            Evo0 = createPokemonSprite(evo0, selectedGame);
            if(Evo0 == null)
                Evo0 = new ImageView();
        }
        if(evo1 != null) {
            Evo1 = createPokemonSprite(evo1, selectedGame);
            if(Evo1 == null)
                Evo1 = new ImageView();
        }
        huntLayout.getChildren().addAll(sprite, Evo0, Evo1);

        Button resetEncountersButton = new Button("Reset Combo");
        switch(selectedMethod.getName()){
            case "Radar Chaining":
            case "Chain Fishing":
            case "SOS Chaining":
            case "Catch Combo":
                huntControlsButtonHBox.getChildren().add(resetEncountersButton);
                currentComboText.setVisible(true);
                break;
            case "DexNav":
                huntControlsButtonHBox.getChildren().add(resetEncountersButton);
                currentComboText.setVisible(true);
                previousEncountersText.setVisible(true);
                break;
            case "Total Encounters":
                previousEncountersText.setVisible(true);
                break;
            default:
                break;
        }
        huntLayout.getChildren().addAll(currentHuntingPokemonText, currentHuntingMethodText, currentGameText, encountersText, previousEncountersText, currentComboText, oddFractionText);
        huntLayoutSize = huntLayout.getChildren().size();

        int index = 3;
        for(int i = 3; i < huntLayout.getChildren().size(); i++){
            if(huntLayout.getChildren().get(i).isVisible()) {
                if(evo1 != null)
                    huntLayout.getChildren().get(i).setLayoutX(600);
                else if(evo0 != null)
                    huntLayout.getChildren().get(i).setLayoutX(400);
                else
                    huntLayout.getChildren().get(i).setLayoutX(200);
                huntLayout.getChildren().get(i).setLayoutY(65 + (15 * index));
                index++;
            }
        }

        if(this.evo1 != null) {
            Evo0.setLayoutX(0);
            Evo1.setLayoutX(200);
            sprite.setLayoutX(400);
        }else if(this.evo0 != null){
            Evo0.setLayoutX(0);
            sprite.setLayoutX(200);
        }else
            sprite.setLayoutX(0);

        Scene huntScene = new Scene(huntLayout, 750, 480);
        huntWindow.setScene(huntScene);
        huntWindow.show();

        SaveData data = new SaveData();
        if(currentLayout != null && currentLayout.compareTo("null") != 0) {
            this.currentLayout = currentLayout;
            displayPrevious = parseInt(data.getLinefromFile(data.getfileLength("Layouts/" + currentLayout) - 1, "Layouts/" + currentLayout));
            if(displayPrevious > 0)
                createPreviouslyCaught(displayPrevious);
            else
                data.loadLayout(currentLayout, huntLayout, displayPrevious);
        }

        huntWindow.setOnCloseRequest(e -> {
            e.consume();
            saveHunt();
        });
    }

    //returns ImageView with the sprite of the given pokemon
    public ImageView createPokemonSprite(String name, Game selectedGame){
        String filePath = "Images/Sprites/";
        switch(selectedGame.getGeneration()) {
            case 2:
                filePath += "Generation 2/" + selectedGame.getName().toLowerCase() + "/";
                break;
            case 3:
                switch(selectedGame.getName()){
                    case "Ruby":
                    case "Sapphire":
                    case "Emerald":
                        filePath += "Generation 3/ruby-sapphire-emerald/";
                        break;
                    case "FireRed":
                    case "LeafGreen":
                        filePath += "Generation 3/firered-leafgreen/";
                        break;
                    default:
                        break;
                }
                break;
            case 4:
                switch(selectedGame.getName()){
                    case "Diamond":
                    case "Pearl":
                        filePath += "Generation 4/diamond-pearl/";
                        break;
                    case "Platinum":
                        filePath += "Generation 4/platinum/";
                        break;
                    case "HeartGold":
                    case "SoulSilver":
                        filePath += "Generation 4/heartgold-soulsilver/";
                        break;
                    default:
                        break;
                }
                break;
            case 5:
                filePath += "Generation 5/";
                break;
            default:
                filePath += "3d Sprites/";
                break;
        }

        switch (name) {
            case "Type: Null":
                filePath += "type-null";
                break;
            case "Nidoran♀":
                filePath += "nidoran-f";
                break;
            case "Nidoran♂":
                filePath += "nidoran-m";
                break;
            case "Mr. Mime":
                filePath += "mr-mime";
                break;
            case "Mr. Rime":
                filePath += "mr-rime";
                break;
            case "Mime Jr.":
                filePath += "mime-jr";
                break;
            case "Flabébé":
                filePath += "flabebe";
                break;
            case "Tapu Koko":
                filePath += "tapu-koko";
                break;
            case "Tapu Lele":
                filePath += "tapu-lele";
                break;
            case "Tapu Bulu":
                filePath += "tapu-bulu";
                break;
            case "Tapu Fini":
                filePath += "tapu-fini";
                break;
            default:
                filePath += name.toLowerCase();
                break;
        }
        if(selectedGame.getGeneration() < 5)
            filePath += ".png";
        else
            filePath += ".gif";

        try {
            FileInputStream input = new FileInputStream(filePath);
            Image image = new Image(input);
            return new ImageView(image);
        }catch (FileNotFoundException e){
            if(name.compareTo(evo0) == 0)
                evo0 = null;
            else if(name.compareTo(evo1) == 0)
                evo1 = null;
            System.out.println(name + " sprite not found");
            return null;
        }
    }

    //creates window to prompt user for search level or previous encounters
    public void promptPreviousEncounters(){
        Stage promptWindow = new Stage();
        promptWindow.setResizable(false);
        promptWindow.initModality(Modality.APPLICATION_MODAL);

        Label promptLabel = new Label();
        if(selectedMethod.getName().compareTo("DexNav") == 0) {
            promptWindow.setTitle("Enter Search Level");
            promptLabel.setText("Please enter the current Search Level for " + selectedPokemon.getName());
        }
        else if(selectedMethod.getName().compareTo("Total Encounters") == 0) {
            promptWindow.setTitle("Enter Number Battled");
            promptLabel.setText("Please enter the current Number Battled for " + selectedPokemon.getName());
        }

        TextField previousInput = new TextField();

        previousInput.setOnAction(e-> {
            try{
                previousEncounters = parseInt(previousInput.getText());
                previousEncountersText.setText(String.valueOf(previousEncounters));
                if(selectedMethod.getName().compareTo("DexNav") == 0)
                    oddFractionText.setText("1/" + selectedMethod.dexNav(encounters, previousEncounters));
                else
                    oddFractionText.setText("1/" + simplifyFraction((selectedMethod.getModifier() + selectedMethod.totalEncounters(previousEncounters)), methodBase));
                promptWindow.close();
            }catch (NumberFormatException f){
                previousInput.setText("");
            }
        });

        VBox promptLayout = new VBox();
        promptLayout.setSpacing(10);
        promptLayout.setAlignment(Pos.CENTER);
        promptLayout.getChildren().addAll(promptLabel, previousInput);

        Scene promptScene = new Scene(promptLayout, 275, 75);
        promptWindow.setScene(promptScene);
        promptWindow.show();

        promptWindow.setOnCloseRequest(e -> {
            huntControls.close();
            huntWindow.close();
        });
    }

    //creates window for the hunt window settings
    public void CustomizeHuntWindow(){
        CustomizeHuntStage.setTitle("Settings");
        CustomizeHuntStage.setResizable(false);
        VBox spriteSettings = createImageSettings(sprite, selectedPokemon.getName());
        VBox currentPokemonSettings = createLabelSettings(currentHuntingPokemonText, "Pokemon");
        VBox currentMethodSettings = createLabelSettings(currentHuntingMethodText, "Method");
        VBox currentGameSettings = createLabelSettings(currentGameText, "Game");
        VBox encountersSettings = createLabelSettings(encountersText, "Encounters");
        VBox oddsFraction = createLabelSettings(oddFractionText, "Odds");

        VBox backgroundVBox = new VBox();
        Label backgroundGroup = new Label("Background");
        backgroundGroup.setUnderline(true);
        HBox backgroundColorSettings = new HBox();
        backgroundColorSettings.setSpacing(5);
        Label backgroundColorLabel = new Label("Color");
        ColorPicker backgroundColorPicker = new ColorPicker();
        backgroundVBox.setPadding(new Insets(10,10,10,10));
        backgroundVBox.setSpacing(10);
        backgroundColorSettings.getChildren().addAll(backgroundColorLabel, backgroundColorPicker);
        backgroundVBox.getChildren().addAll(backgroundGroup, backgroundColorSettings);

        Accordion accordion = new Accordion();
        TitledPane backgroundTitledPane = new TitledPane("Background", backgroundVBox);
        accordion.getPanes().add(backgroundTitledPane);

        VBox backgroundSettings = new VBox();
        backgroundSettings.getChildren().add(accordion);

        HBox saveClose = new HBox();
        saveClose.setPadding(new Insets(10,10,10,10));
        saveClose.setSpacing(5);
        Button Save = new Button("Save");
        Button Load = new Button("Load");
        saveClose.getChildren().addAll(Save,Load);

        VBox CustomizeHuntVBox = new VBox();
        VBox Evo0Settings;
        VBox Evo1Settings;

        if(evo0 != null){
            Evo0Settings = createImageSettings(Evo0, evo0);
            CustomizeHuntVBox.getChildren().addAll(Evo0Settings);
        }
        if(evo1 != null){
            Evo1Settings = createImageSettings(Evo1, evo1);
            CustomizeHuntVBox.getChildren().add(Evo1Settings);
        }
        CustomizeHuntVBox.getChildren().add(spriteSettings);

        VBox comboSettings;
        VBox previousEncountersSettings;

        switch (selectedMethod.getName()) {
            case "Radar Chaining":
            case "Chain Fishing":
            case "SOS Chaining":
            case "Catch Combo":
                comboSettings = createLabelSettings(currentComboText, "Combo");
                CustomizeHuntVBox.getChildren().addAll(encountersSettings, currentPokemonSettings, currentMethodSettings, currentGameSettings, oddsFraction, comboSettings, backgroundSettings, saveClose);
                break;
            case "DexNav":
                comboSettings = createLabelSettings(currentComboText, "Combo");
                previousEncountersSettings = createLabelSettings(previousEncountersText, "Search Level");
                CustomizeHuntVBox.getChildren().addAll(encountersSettings, currentPokemonSettings, currentMethodSettings, currentGameSettings, oddsFraction, comboSettings, previousEncountersSettings, backgroundSettings, saveClose);
                break;
            case "Total Encounters":
                previousEncountersSettings = createLabelSettings(previousEncountersText, "Total Encounters");
                CustomizeHuntVBox.getChildren().addAll(encountersSettings, currentPokemonSettings, currentMethodSettings, currentGameSettings, oddsFraction, previousEncountersSettings, backgroundSettings, saveClose);
                break;
            default:
                CustomizeHuntVBox.getChildren().addAll(encountersSettings, currentPokemonSettings, currentMethodSettings, currentGameSettings, oddsFraction, backgroundSettings, saveClose);
                break;
        }

        CustomizeHuntVBox.getChildren().add(previouslyCaughtPokemonSettings());
        AnchorPane CustomizeHuntLayout = new AnchorPane();
        CustomizeHuntLayout.getChildren().add(CustomizeHuntVBox);
        AnchorPane.setTopAnchor(CustomizeHuntVBox,0d);

        ScrollPane CustomizeHuntScrollpane = new ScrollPane(CustomizeHuntLayout);
        CustomizeHuntScrollpane.setFitToHeight(true);

        Scene CustomizeHuntScene = new Scene(CustomizeHuntScrollpane, 263, 500);
        CustomizeHuntStage.setScene(CustomizeHuntScene);
        CustomizeHuntStage.show();

        backgroundColorPicker.setOnAction(e -> huntLayout.setBackground(new Background(new BackgroundFill(backgroundColorPicker.getValue(), CornerRadii.EMPTY, Insets.EMPTY))));

        Save.setOnAction(e -> {
            SaveData data = new SaveData();
            Stage promptLayoutSaveName = new Stage();
            promptLayoutSaveName.initModality(Modality.APPLICATION_MODAL);
            promptLayoutSaveName.setResizable(false);

            if(currentLayout != null) {
                promptLayoutSaveName.setTitle("Save Layout");

                VBox selectNewSaveLayout = new VBox();
                selectNewSaveLayout.setAlignment(Pos.CENTER);
                selectNewSaveLayout.setSpacing(5);
                Label newNameLabel = new Label("Would you like to save this layout to a new name?");

                HBox buttons = new HBox();
                buttons.setSpacing(10);
                buttons.setAlignment(Pos.CENTER);
                Button newSaveButton = new Button("New");
                Button oldSaveButton = new Button("Update");
                buttons.getChildren().addAll(newSaveButton, oldSaveButton);

                selectNewSaveLayout.getChildren().addAll(newNameLabel, buttons);

                Scene selectNewSaveScene = new Scene(selectNewSaveLayout, 275, 75);
                promptLayoutSaveName.setScene(selectNewSaveScene);
                promptLayoutSaveName.show();

                newSaveButton.setOnAction(f -> {
                    promptLayoutSaveName.setTitle("Select Layout Name");

                    VBox saveLayoutNameLayout = new VBox();
                    saveLayoutNameLayout.setSpacing(10);
                    saveLayoutNameLayout.setAlignment(Pos.CENTER);

                    Label saveNameLabel = new Label("What would you like this layout to be called?");
                    TextField saveNameText = new TextField();

                    saveLayoutNameLayout.getChildren().addAll(saveNameLabel, saveNameText);
                    Scene saveLayoutNameScene = new Scene(saveLayoutNameLayout, 275, 75);
                    promptLayoutSaveName.setScene(saveLayoutNameScene);
                    promptLayoutSaveName.show();

                    saveNameText.setOnAction(g -> {
                        String text = saveNameText.getText();
                        if (text.contains("\\") || text.contains("/") || text.contains(":") || text.contains("*") || text.contains("?") || text.contains("\"") || text.contains("<") || text.contains(">") || text.contains("|") || text.contains(".")) {
                            saveNameText.setText("");
                        } else {
                            data.saveLayout(saveNameText.getText(), huntLayout, displayPrevious, true);
                            promptLayoutSaveName.close();
                        }
                    });
                });

                oldSaveButton.setOnAction(f -> {
                    data.saveLayout(currentLayout, huntLayout, displayPrevious, false);
                    promptLayoutSaveName.close();
                });
            }else{
                promptLayoutSaveName.setTitle("Select Layout Name");

                VBox saveLayoutNameLayout = new VBox();
                saveLayoutNameLayout.setSpacing(10);
                saveLayoutNameLayout.setAlignment(Pos.CENTER);

                Label saveNameLabel = new Label("What would you like this layout to be called?");
                TextField saveNameText = new TextField();

                saveLayoutNameLayout.getChildren().addAll(saveNameLabel, saveNameText);
                Scene saveLayoutNameScene = new Scene(saveLayoutNameLayout, 275, 75);
                promptLayoutSaveName.setScene(saveLayoutNameScene);
                promptLayoutSaveName.show();

                saveNameText.setOnAction(g -> {
                    String text = saveNameText.getText();
                    if (text.indexOf('\\') > -1 || text.indexOf('/') > -1 || text.indexOf(':') > -1 || text.indexOf('*') > -1 || text.indexOf('?') > -1 || text.indexOf('\"') > -1 || text.indexOf('<') > -1 || text.indexOf('>') > -1 || text.indexOf('|') > -1 || text.indexOf('.') > -1) {
                        saveNameText.setText("");
                    } else {
                        data.saveLayout(saveNameText.getText(), huntLayout, displayPrevious, true);
                        promptLayoutSaveName.close();
                    }
                });
            }
        });

        Load.setOnAction(e -> {
            SaveData data = new SaveData();

            Stage loadSavedLayoutStage = new Stage();
            loadSavedLayoutStage.initModality(Modality.APPLICATION_MODAL);
            loadSavedLayoutStage.setResizable(false);
            loadSavedLayoutStage.setTitle("Select Layout Name");

            TreeView<String> savedLayouts = new TreeView<>();
            TreeItem<String> root = new TreeItem<>();
            for(int i = 0; i < data.getfileLength("Layouts/~Layouts"); i++){
                makeBranch(data.getLinefromFile(i, "Layouts/~Layouts"), root);
            }
            savedLayouts.setRoot(root);
            savedLayouts.setShowRoot(false);
            savedLayouts.setPrefWidth(300);
            savedLayouts.setPrefWidth(500);

            VBox savedLayoutsLayout = new VBox();
            savedLayoutsLayout.setSpacing(10);
            savedLayoutsLayout.setAlignment(Pos.CENTER);
            savedLayoutsLayout.getChildren().add(savedLayouts);

            Scene savedLayoutsScene = new Scene(savedLayoutsLayout, 300, 400);
            loadSavedLayoutStage.setScene(savedLayoutsScene);
            loadSavedLayoutStage.show();

            savedLayouts.getSelectionModel().selectedItemProperty()
                    .addListener((v, oldValue, newValue) -> {
                        currentLayout = newValue.toString().substring(18, String.valueOf(newValue).length() - 2);
                        displayPrevious = parseInt(data.getLinefromFile(data.getfileLength("Layouts/" + currentLayout) - 1, "Layouts/" + currentLayout));
                        if(displayPrevious > 0)
                            createPreviouslyCaught(displayPrevious);
                        else
                            data.loadLayout(currentLayout, huntLayout, displayPrevious);
                        CustomizeHuntWindow();

                        loadSavedLayoutStage.close();
                    });
        });
    }

    //creates elements for previously caught information
    public VBox previouslyCaughtPokemonSettings(){
        Label numberCaught = new Label("Display Previously Caught: ");

        TextField numberCaughtField = new TextField();
        numberCaughtField.setMaxWidth(50);
        numberCaughtField.setPromptText(String.valueOf(displayPrevious));

        HBox numberPreviouslyCaught = new HBox();
        numberPreviouslyCaught.setAlignment(Pos.CENTER);
        numberPreviouslyCaught.setPadding(new Insets(5,5,5,5));
        numberPreviouslyCaught.getChildren().addAll(numberCaught,numberCaughtField);

        VBox caughtSettings = createPreviouslyCaught(displayPrevious);

        VBox previouslyCaughtSettingsLayout = new VBox();
        previouslyCaughtSettingsLayout.setAlignment(Pos.CENTER);
        previouslyCaughtSettingsLayout.setSpacing(5);
        previouslyCaughtSettingsLayout.getChildren().addAll(numberPreviouslyCaught, caughtSettings);

        numberCaughtField.setOnAction(e ->{
            try{
                displayPrevious = parseInt(numberCaughtField.getText());
                VBox caughtPokemonSettings = createPreviouslyCaught(displayPrevious);
                previouslyCaughtSettingsLayout.getChildren().remove(1);
                previouslyCaughtSettingsLayout.getChildren().add(caughtPokemonSettings);
                numberCaughtField.setText("");
                numberCaughtField.setPromptText(String.valueOf(displayPrevious));
            }catch(NumberFormatException f){
                numberCaughtField.setText("");
            }
        });

        return previouslyCaughtSettingsLayout;
    }

    //create elements of the last x previously caught pokemon
    public VBox createPreviouslyCaught(int previouslyCaught){
        huntLayout.getChildren().remove(huntLayoutSize, huntLayout.getChildren().size());
        VBox settings = new VBox();
        SaveData data = new SaveData();
        int numberCaught = data.getfileLength("CaughtPokemon");
        if(numberCaught < previouslyCaught)
            previouslyCaught = numberCaught;
        for(int i = numberCaught - 1; i >= (numberCaught - previouslyCaught); i--){
            String line = data.getLinefromFile(i, "CaughtPokemon");
            Text seperator = new Text("-------------------------------------------");
            Game caughtGame = new Game(data.splitString(line, 1), parseInt(data.splitString(line, 2)));
            selectionPageController findGeneration = new selectionPageController();
            int selectedPokemonGeneration = findGeneration.findGenerationPokemon(data.splitString(line, 0));
            if(caughtGame.getGeneration() < findGeneration.findGenerationPokemon(data.splitString(line, 0)))
                caughtGame.setGeneration(selectedPokemonGeneration);
            ImageView sprite = createPokemonSprite(data.splitString(line, 0), caughtGame);
            Text pokemon = new Text(data.splitString(line, 0));
            Text method = new Text(data.splitString(line, 3));
            Text encounters = new Text(data.splitString(line, 5));

            pokemon.setStroke(Color.web("0x00000000"));
            method.setStroke(Color.web("0x00000000"));
            encounters.setStroke(Color.web("0x00000000"));
            huntLayout.getChildren().addAll(sprite, pokemon, method, encounters);

            if(currentLayout != null){
                data = new SaveData();
                data.loadLayout(currentLayout, huntLayout, numberCaught - i);
            }else {
                sprite.setLayoutX(50 * (numberCaught - i));
                sprite.setLayoutY(50);

                pokemon.setLayoutX(50 * (numberCaught - i));
                pokemon.setLayoutY(75);

                method.setLayoutX(50 * (numberCaught - i));
                method.setLayoutY(90);

                encounters.setLayoutX(50 * (numberCaught - i));
                encounters.setLayoutY(105);
            }

            VBox imageSettings = createImageSettings(sprite, data.splitString(line, 0));
            VBox currentPokemonSettings = createLabelSettings(pokemon, "Pokemon");
            VBox currentMethodSettings = createLabelSettings(method, "Method");
            VBox encountersSettings = createLabelSettings(encounters, "Encounters");

            VBox pokemonSettings = new VBox();
            pokemonSettings.getChildren().addAll(seperator, imageSettings, currentPokemonSettings, currentMethodSettings, encountersSettings);

            settings.getChildren().add(pokemonSettings);
        }

        return settings;
    }

    //creates ImageView settings VBox
    public VBox createImageSettings(ImageView image, String pokemonName){
        HBox groupLabel = new HBox();
        Label Group = new Label(pokemonName + " Sprite:");
        Group.setUnderline(true);
        groupLabel.getChildren().add(Group);

        HBox changeSize = new HBox();
        changeSize.setSpacing(5);
        Label sizeLabel = new Label("Scale:");
        TextField sizeField = new TextField();
        sizeField.setPromptText(String.valueOf(image.getScaleX()));
        changeSize.getChildren().addAll(sizeLabel, sizeField);

        HBox changeX = new HBox();
        changeX.setSpacing(5);
        Label XLabel = new Label("X Location:");
        TextField XField = new TextField();
        XField.setPromptText(String.valueOf(image.getLayoutX()));
        changeX.getChildren().addAll(XLabel, XField);

        HBox changeY = new HBox();
        changeY.setSpacing(5);
        Label YLabel = new Label("Y Location:");
        TextField YField = new TextField();
        YField.setPromptText(String.valueOf(image.getLayoutY()));
        changeY.getChildren().addAll(YLabel, YField);

        HBox visablility = new HBox();
        visablility.setSpacing(5);
        Label visableLabel = new Label("Visable:");
        CheckBox visableCheck = new CheckBox();
        visableCheck.setSelected(image.isVisible());
        visablility.getChildren().addAll(visableLabel, visableCheck);

        VBox imageVBox = new VBox();
        imageVBox.setSpacing(10);
        imageVBox.getChildren().addAll(groupLabel, changeSize, changeX, changeY, visablility);

        Accordion accordion = new Accordion();
        TitledPane imageTitledPane = new TitledPane(pokemonName + " Sprite", imageVBox);
        accordion.getPanes().add(imageTitledPane);

        VBox imageSettings = new VBox();
        imageSettings.getChildren().add(accordion);

        sizeField.setOnAction(e -> {
            double scale = 0;
            try{
                scale = parseDouble(sizeField.getText());
            }catch(NumberFormatException f){
                sizeField.setText("");
            }
            image.setTranslateX(-(image.getImage().getWidth() * ((1 - scale)/2)));
            image.setTranslateY(-(image.getImage().getHeight() * ((1 - scale)/2)));
            image.setScaleX(scale);
            image.setScaleY(scale);
            sizeField.setText("");
            sizeField.setPromptText(String.valueOf(image.getScaleX()));
        });

        XField.setOnAction(e ->{
            try{
                image.setLayoutX(parseDouble(XField.getText()));
                XField.setText("");
                XField.setPromptText(String.valueOf(image.getLayoutX()));
            }catch(NumberFormatException f){
                XField.setText("");
            }
        });

        YField.setOnAction(e ->{
            try{
                image.setLayoutY(parseDouble(YField.getText()));
                YField.setText("");
                YField.setPromptText(String.valueOf(image.getLayoutY()));
            }catch(NumberFormatException f){
                YField.setText("");
            }
        });

        visableCheck.setOnAction(e -> image.setVisible(visableCheck.isSelected()));

        return imageSettings;
    }

    //creates Label settings VBox
    public VBox createLabelSettings(Text label, String labelName){
        HBox groupLabel = new HBox();
        Label Group = new Label(labelName + " Label");
        Group.setUnderline(true);
        groupLabel.getChildren().add(Group);

        HBox changeSize = new HBox();
        changeSize.setSpacing(5);
        Label sizeLabel = new Label("Scale: ");
        TextField sizeField = new TextField();
        sizeField.setPromptText(String.valueOf(label.getScaleX()));
        changeSize.getChildren().addAll(sizeLabel, sizeField);

        HBox changeX = new HBox();
        changeX.setSpacing(5);
        Label XLabel = new Label("X Location:");
        TextField XField = new TextField();
        XField.setPromptText(String.valueOf(label.getLayoutX()));
        changeX.getChildren().addAll(XLabel, XField);

        HBox changeY = new HBox();
        changeY.setSpacing(5);
        Label YLabel = new Label("Y Location:");
        TextField YField = new TextField();
        YField.setPromptText(String.valueOf(label.getLayoutY()));
        changeY.getChildren().addAll(YLabel, YField);

        HBox font = new HBox();
        font.setSpacing(5);
        Label fontLabel = new Label("Font:");
        ComboBox<String> fontNameBox = new ComboBox<>();
        fontNameBox.setEditable(false);
        fontNameBox.getItems().addAll(fonts);
        font.getChildren().addAll(fontLabel, fontNameBox);

        HBox color = new HBox();
        color.setSpacing(5);
        Label colorLabel = new Label("Color:");
        ColorPicker colorField = new ColorPicker();
        colorField.setValue((Color) label.getFill());
        color.getChildren().addAll(colorLabel, colorField);

        HBox stroke = new HBox();
        stroke.setSpacing(5);
        Label strokeLabel = new Label("Stroke:");
        CheckBox strokeCheckbox = new CheckBox();
        stroke.getChildren().addAll(strokeLabel, strokeCheckbox);

        HBox strokeWidth = new HBox();
        strokeWidth.setSpacing(5);
        Label strokeWidthLabel = new Label("Stroke Width:");
        TextField strokeWidthField = new TextField();
        strokeWidthField.setPromptText(String.valueOf(label.getStrokeWidth()));
        strokeWidthLabel.setDisable(true);
        strokeWidthField.setDisable(true);
        strokeWidth.getChildren().addAll(strokeWidthLabel, strokeWidthField);

        HBox strokeColor = new HBox();
        strokeColor.setSpacing(5);
        Label strokeColorLabel = new Label("Stroke Color:");
        ColorPicker strokeColorPicker = new ColorPicker();
        strokeColorPicker.setValue((Color) label.getStroke());
        strokeColorLabel.setDisable(true);
        strokeColorPicker.setDisable(true);
        strokeColor.getChildren().addAll(strokeColorLabel, strokeColorPicker);

        HBox textProperties = new HBox();
        textProperties.setSpacing(5);
        Label italics = new Label("Italics:");
        CheckBox italicsCheck = new CheckBox();
        italicsCheck.setSelected(label.getFont().getName().contains("Italic"));
        Label bold = new Label("Bold:");
        CheckBox boldCheck = new CheckBox();
        boldCheck.setSelected(label.getFont().getName().contains("Bold"));
        Label underlined = new Label("Underlined:");
        CheckBox underlinedCheck = new CheckBox();
        underlinedCheck.setSelected(label.isUnderline());
        textProperties.getChildren().addAll(italics, italicsCheck, bold, boldCheck, underlined, underlinedCheck);

        HBox visablility = new HBox();
        visablility.setSpacing(5);
        Label visableLabel = new Label("Visable:");
        CheckBox visableCheck = new CheckBox();
        visableCheck.setSelected(label.isVisible());
        visablility.getChildren().addAll(visableLabel, visableCheck);

        VBox labelVBox = new VBox();
        labelVBox.setSpacing(10);
        labelVBox.getChildren().addAll(groupLabel, changeSize, changeX, changeY, font, color, stroke, strokeWidth, strokeColor, textProperties, visablility);

        Accordion accordion = new Accordion();
        TitledPane labelTitledPane = new TitledPane(labelName, labelVBox);
        accordion.getPanes().add(labelTitledPane);

        VBox labelSettings = new VBox();
        labelSettings.getChildren().add(accordion);

        sizeField.setOnAction(e -> {
            double scale = 0;
            try{
                scale = parseDouble(sizeField.getText());
                sizeField.setText("");
            }catch(NumberFormatException f){
                sizeField.setText("");
            }
            label.setScaleX(scale);
            label.setScaleY(scale);
            sizeField.setPromptText(String.valueOf(label.getScaleX()));
        });

        XField.setOnAction(e ->{
            double X = 0;
            try{
                X = parseDouble(XField.getText());
                XField.setText("");
            }catch(NumberFormatException f){
                XField.setText("");
            }
            label.setLayoutX(X);
            XField.setPromptText(String.valueOf(label.getLayoutX()));
        });

        YField.setOnAction(e ->{
            double Y = 0;
            try{
                Y = parseDouble(YField.getText());
                YField.setText("");
            }catch(NumberFormatException f){
                YField.setText("");
            }
            label.setLayoutY(Y);
            YField.setPromptText(String.valueOf(label.getLayoutY()));
        });

        fontNameBox.getSelectionModel().selectedItemProperty().addListener((v, oldValue, newValue) -> {
            if(newValue != null){
                if(!canBold(newValue)) {
                    boldCheck.setSelected(false);
                    boldCheck.setDisable(true);
                }else
                    boldCheck.setDisable(false);

                if(!canItalic(newValue)){
                    italicsCheck.setSelected(false);
                    italicsCheck.setDisable(true);
                }else
                    italicsCheck.setDisable(false);

                if(boldCheck.isSelected() && italicsCheck.isSelected())
                    label.setFont(Font.font(newValue, FontWeight.BOLD, FontPosture.ITALIC, 12));
                else if(boldCheck.isSelected())
                    label.setFont(Font.font(newValue, FontWeight.BOLD, 12));
                else if(italicsCheck.isSelected())
                    label.setFont(Font.font(newValue, FontPosture.ITALIC, 12));
                else
                    label.setFont(new Font(newValue, 12));
            }
        });

        colorField.setOnAction(e -> label.setFill(colorField.getValue()));

        strokeCheckbox.setOnAction(e -> {
            boolean selected = strokeCheckbox.isSelected();
            if(selected) {
                strokeWidthLabel.setDisable(false);
                strokeWidthField.setDisable(false);
                strokeColorLabel.setDisable(false);
                strokeColorPicker.setDisable(false);
                label.setStrokeWidth(parseDouble(strokeWidthField.getPromptText()));
            }else {
                label.setStrokeWidth(0);
            }
        });

        strokeWidthField.setOnAction(e -> {
            try{
                double width = parseDouble(strokeWidthField.getText());
                label.setStrokeWidth(width);
                strokeWidthField.setPromptText(String.valueOf(label.getStrokeWidth()));
                strokeWidthField.setText("");
            }catch(NumberFormatException f){
                strokeWidthField.setText("");
            }
        });

        strokeColorPicker.setOnAction(e -> label.setStroke(strokeColorPicker.getValue()));

        italicsCheck.setOnAction(e -> {
            String fontName = sanitizeFontName(String.valueOf(label.getFont().getName()));
            if(boldCheck.isSelected() && italicsCheck.isSelected())
                label.setFont(Font.font(fontName, FontWeight.BOLD, FontPosture.ITALIC, 12));
            else if(boldCheck.isSelected())
                label.setFont(Font.font(fontName, FontWeight.BOLD, 12));
            else if(italicsCheck.isSelected())
                label.setFont(Font.font(fontName, FontPosture.ITALIC, 12));
            else
                label.setFont(new Font(fontName, 12));
        });

        boldCheck.setOnAction(e -> {
            String fontName = sanitizeFontName(String.valueOf(label.getFont().getName()));
            if(boldCheck.isSelected() && italicsCheck.isSelected())
                label.setFont(Font.font(fontName, FontWeight.BOLD, FontPosture.ITALIC, 12));
            else if(boldCheck.isSelected())
                label.setFont(Font.font(fontName, FontWeight.BOLD, 12));
            else if(italicsCheck.isSelected())
                label.setFont(Font.font(fontName, FontPosture.ITALIC, 12));
            else
                label.setFont(new Font(fontName, 12));
        });

        underlinedCheck.setOnAction(e -> label.setUnderline(underlinedCheck.isSelected()));

        visableCheck.setOnAction(e -> label.setVisible(visableCheck.isSelected()));

        return labelSettings;
    }

    //adds increment to the encounters
    public void incrementEncounters(){
        encounters += increment;
        combo += increment;
        encountersText.setText(String.valueOf(encounters));
        dynamicOddsMethods();
    }

    //adds current pokemon to the caught pokemon file
    public void pokemonCaught() {
        SaveData data = new SaveData(selectedPokemon, selectedGame, selectedMethod, encounters, combo, increment, currentLayout);
        data.pokemonCaught();
        createPreviouslyCaught(displayPrevious);

        huntControls.close();
        CustomizeHuntStage.close();

        sprite.setVisible(false);
        currentHuntingPokemonText.setVisible(false);
        currentHuntingMethodText.setVisible(false);
        currentGameText.setVisible(false);
        oddFractionText.setVisible(false);
        encountersText.setVisible(false);
        previousEncountersText.setVisible(false);

        try {
            FXMLLoader selectionPageLoader = new FXMLLoader();
            selectionPageLoader.setLocation(getClass().getResource("selectionPage.fxml"));
            Parent root = selectionPageLoader.load();

            Stage huntSelectionWindow = new Stage();
            huntSelectionWindow.setTitle("Shiny Hunt Tracker");
            huntSelectionWindow.setResizable(false);
            huntSelectionWindow.setScene(new Scene(root, 750, 480));

            selectionPageController selectionPageController = selectionPageLoader.getController();
            selectionPageController.setSelectingNewHunt(true);
            selectionPageController.setnewHuntSelection(huntWindow);
            selectionPageController.setnewHuntSelectionLayout(huntLayout);
            selectionPageController.setcurrentLayout(currentLayout);
            huntSelectionWindow.show();
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    //prompts user for phase pokemon, resets encounters, and adds phased pokemon to the caught pokemon file
    public void phaseHunt(){
        Stage phaseStage = new Stage();
        phaseStage.initModality(Modality.APPLICATION_MODAL);
        phaseStage.setResizable(false);
        phaseStage.setTitle("Phase");

        VBox phaseLayout = new VBox();
        phaseLayout.setAlignment(Pos.CENTER);
        phaseLayout.setSpacing(10);

        Label phaseLabel = new Label("Please enter the phase pokemon");
        TextField phasePokemon = new TextField();
        phaseLayout.getChildren().addAll(phaseLabel, phasePokemon);

        Scene phaseScene = new Scene(phaseLayout, 200,100);
        phaseStage.setScene(phaseScene);
        phaseStage.show();

        phasePokemon.setOnAction(e -> {
            selectionPageController temp = new selectionPageController();
            String userInput = phasePokemon.getText().toLowerCase();
            userInput = userInput.substring(0,1).toUpperCase() + userInput.substring(1);
            if(temp.findGenerationPokemon(userInput) == 0)
                phasePokemon.setText("");
            else{
                SaveData data = new SaveData(new Pokemon(userInput, 0), selectedGame, selectedMethod, encounters, combo, increment, currentLayout);
                data.pokemonCaught();
                resetCombo();
                createPreviouslyCaught(displayPrevious);
                phaseStage.close();
            }
        });
    }

    //resets encounters
    public void resetCombo(){
        combo = 0;
        dynamicOddsMethods();
    }

    //writes objects to previous hunts file
    public void saveHunt(){
        Stage save = new Stage();
        save.setTitle("Save hunt");
        save.initModality(Modality.APPLICATION_MODAL);
        VBox saveLayout = new VBox();
        saveLayout.setSpacing(10);
        saveLayout.setAlignment(Pos.CENTER);
        Label savePrompt = new Label("Would you like to save your hunt?");
        HBox yn = new HBox();
        yn.setSpacing(5);
        yn.setAlignment(Pos.CENTER);
        Button yes = new Button("Yes");
        Button no = new Button("No");
        yn.getChildren().addAll(yes, no);
        saveLayout.getChildren().addAll(savePrompt, yn);

        Scene saveScene = new Scene(saveLayout, 275, 75);
        save.setScene(saveScene);
        save.show();

        yes.setOnAction(e -> {
            huntControls.close();
            huntWindow.close();
            CustomizeHuntStage.close();
            SaveData data = new SaveData(selectedPokemon, selectedGame, selectedMethod, encounters, combo, increment, currentLayout);
            data.saveHunt();
            save.close();
        });

        no.setOnAction(e -> {
            huntControls.close();
            huntWindow.close();
            CustomizeHuntStage.close();
            save.close();
        });
    }

    //changes increment
    public void changeIncrement(){
        Stage changeIncrementStage = new Stage();
        changeIncrementStage.setResizable(false);
        changeIncrementStage.initModality(Modality.APPLICATION_MODAL);
        VBox changeIncrementsLayout = new VBox();
        changeIncrementsLayout.setAlignment(Pos.CENTER);
        changeIncrementsLayout.setSpacing(10);
        Label changeIncrementsLabel = new Label("Enter the number by which the encounters increase for every button press");
        TextField changeIncrementsText = new TextField();
        changeIncrementsLayout.getChildren().addAll(changeIncrementsLabel, changeIncrementsText);
        Scene changeIncrementsScene = new Scene(changeIncrementsLayout, 400, 100);
        changeIncrementStage.setScene(changeIncrementsScene);
        changeIncrementStage.show();

        changeIncrementsText.setOnAction(e -> {
            try{
                increment = parseInt(changeIncrementsText.getText());
                changeIncrementStage.close();
            }catch (NumberFormatException f){
                changeIncrementsText.setText("");
            }
        });
    }

    //since some methods' odds change based on encounters
    private void dynamicOddsMethods(){
        switch(selectedMethod.getName()){
            case "Radar Chaining":
                int tempEncounters;
                if (combo >= 40)
                    tempEncounters = 39;
                else
                    tempEncounters = combo;
                oddFractionText.setText("1/" + simplifyFraction(Math.round(((65535 / (8200.0 - tempEncounters * 200)) + selectedMethod.getModifier() - 1)), (65536 / (1 + (Math.abs(methodBase - 8196) / 4096)))));
                break;
            case "Chain Fishing":
                oddFractionText.setText("1/" + simplifyFraction(selectedMethod.getModifier() + selectedMethod.chainFishing(combo), methodBase));
                break;
            case "DexNav":
                if (previousEncounters < 999) {
                    previousEncounters++;
                    previousEncountersText.setText(String.valueOf(previousEncounters));
                }else
                    previousEncountersText.setText("999");
                oddFractionText.setText("1/" + selectedMethod.dexNav(combo, previousEncounters));
                break;
            case "SOS Chaining":
                oddFractionText.setText("1/" + simplifyFraction(selectedMethod.getModifier() + selectedMethod.sosChaining(combo), methodBase));
                break;
            case "Catch Combo":
                oddFractionText.setText("1/" + simplifyFraction(selectedMethod.getModifier() + selectedMethod.catchCombo(combo), methodBase));
                break;
            case "Total Encounters":
                previousEncounters++;
                oddFractionText.setText("1/" + simplifyFraction(selectedMethod.getModifier() + selectedMethod.totalEncounters(previousEncounters), methodBase));
                break;
            default:
                break;
        }
    }

    private int simplifyFraction(double num, int den){
        return (int)Math.round(den / num);
    }

    //method to create Tree Item branches
    public void makeBranch(String title, TreeItem<String> parent){
        TreeItem<String> item = new TreeItem<>(title);
        parent.getChildren().add(item);
    }

    public String sanitizeFontName(String name){
        int index = 0;
        if(name.contains("Bold"))
            index += 5;
        if(name.contains("Italic"))
            index += 7;
        if(name.contains("Regular"))
            index += 8;
        return name.substring(0, name.length() - index);
    }

    public String sanitizeAvaliableFontStrings(String name){
        Font test = new Font(name, 12);
        if(test.getName().compareTo("System Regular") == 0){
            return null;
        }
        return name;
    }

    public boolean canBold(String name){
        Font test;
        test = Font.font(name,  FontWeight.BOLD, 12);
        if(test.getName().compareTo(name) == 0)
            return false;
        test = new Font(sanitizeFontName(test.getName()), 12);

        return test.getName().compareTo("System") != 0;
    }

    public boolean canItalic(String name){
        Font test;
        test = Font.font(name,  FontPosture.ITALIC, 12);
        if(test.getName().compareTo(name) == 0)
            return false;
        test = new Font(sanitizeFontName(test.getName()), 12);

        return test.getName().compareTo("System") != 0;
    }
}