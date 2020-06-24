package shinyhunttracker;

import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    public BorderPane shinyTrackerScene;
    public TreeView<String> PokemonList, GameList, MethodList;
    public Label pokemonLabel, gameLabel, methodLabel;
    public RadioButton galarianRadioButton, alolanRadioButton;
    public CheckBox shinyCharmCheckBox, lureCheckBox;
    Game selectedGame = new Game();
    Pokemon selectedPokemon;
    Method selectedMethod = new Method();
    String newSelectionPokemon, oldSelectionPokemon = "";
    String newSelectionGame = "";
    int oldSelectionGeneration = 0;
    String[] gen1 = {"Bulbasaur", "Ivysaur", "Venusaur", "Charmander", "Charmeleon", "Charizard", "Squirtle", "Wartortle", "Blastoise", "Caterpie", "Metapod", "Butterfree", "Weedle", "Kakuna", "Beedrill", "Pidgey", "Pidgeotto", "Pidgeot", "Rattata", "Raticate", "Spearow", "Fearow", "Ekans", "Arbok", "Pikachu", "Raichu", "Sandshrew", "Sandslash", "Nidoran♀", "Nidorina", "Nidoqueen", "Nidoran♂", "Nidorino", "Nidoking", "Clefairy", "Clefable", "Vulpix", "Ninetales", "Jigglypuff", "Wigglytuff", "Zubat", "Golbat", "Oddish", "Gloom", "Vileplume", "Paras", "Parasect", "Venonat", "Venomoth", "Diglett", "Dugtrio", "Meowth", "Persian", "Psyduck", "Golduck", "Mankey", "Primeape", "Growlithe", "Arcanine", "Poliwag", "Poliwhirl", "Poliwrath", "Abra", "Kadabra", "Alakazam", "Machop", "Machoke", "Machamp", "Bellsprout", "Weepinbell", "Victreebel", "Tentacool", "Tentacruel", "Geodude", "Graveler", "Golem", "Ponyta", "Rapidash", "Slowpoke", "Slowbro", "Magnemite", "Magneton", "Farfetch’d", "Doduo", "Dodrio", "Seel", "Dewgong", "Grimer", "Muk", "Shellder", "Cloyster", "Gastly", "Haunter", "Gengar", "Onix", "Drowzee", "Hypno", "Krabby", "Kingler", "Voltorb", "Electrode", "Exeggcute", "Exeggutor", "Cubone", "Marowak", "Hitmonlee", "Hitmonchan", "Lickitung", "Koffing", "Weezing", "Rhyhorn", "Rhydon", "Chansey", "Tangela", "Kangaskhan", "Horsea", "Seadra", "Goldeen", "Seaking", "Staryu", "Starmie", "Mr. Mime", "Scyther", "Jynx", "Electabuzz", "Magmar", "Pinsir", "Tauros", "Magikarp", "Gyarados", "Lapras", "Ditto", "Eevee", "Vaporeon", "Jolteon", "Flareon", "Porygon", "Omanyte", "Omastar", "Kabuto", "Kabutops", "Aerodactyl", "Snorlax", "Articuno", "Zapdos", "Moltres", "Dratini", "Dragonair", "Dragonite", "Mewtwo", "Mew"};
    String[] gen2 = {"Chikorita", "Bayleef", "Meganium", "Cyndaquil", "Quilava", "Typhlosion", "Totodile", "Croconaw", "Feraligatr", "Sentret", "Furret", "Hoothoot", "Noctowl", "Ledyba", "Ledian", "Spinarak", "Ariados", "Crobat", "Chinchou", "Lanturn", "Pichu", "Cleffa", "Igglybuff", "Togepi", "Togetic", "Natu", "Xatu", "Mareep", "Flaaffy", "Ampharos", "Bellossom", "Marill", "Azumarill", "Sudowoodo", "Politoed", "Hoppip", "Skiploom", "Jumpluff", "Aipom", "Sunkern", "Sunflora", "Yanma", "Wooper", "Quagsire", "Espeon", "Umbreon", "Murkrow", "Slowking", "Misdreavus", "Unown", "Wobbuffet", "Girafarig", "Pineco", "Forretress", "Dunsparce", "Gligar", "Steelix", "Snubbull", "Granbull", "Qwilfish", "Scizor", "Shuckle", "Heracross", "Sneasel", "Teddiursa", "Ursaring", "Slugma", "Magcargo", "Swinub", "Piloswine", "Corsola", "Remoraid", "Octillery", "Delibird", "Mantine", "Skarmory", "Houndour", "Houndoom", "Kingdra", "Phanpy", "Donphan", "Porygon2", "Stantler", "Smeargle", "Tyrogue", "Hitmontop", "Smoochum", "Elekid", "Magby", "Miltank", "Blissey", "Raikou", "Entei", "Suicune", "Larvitar", "Pupitar", "Tyranitar", "Lugia", "Ho-Oh", "Celebi"};
    String[] gen3 = {"Treecko", "Grovyle", "Sceptile", "Torchic", "Combusken", "Blaziken", "Mudkip", "Marshtomp", "Swampert", "Poochyena", "Mightyena", "Zigzagoon", "Linoone", "Wurmple", "Silcoon", "Beautifly", "Cascoon", "Dustox", "Lotad", "Lombre", "Ludicolo", "Seedot", "Nuzleaf", "Shiftry", "Taillow", "Swellow", "Wingull", "Pelipper", "Ralts", "Kirlia", "Gardevoir", "Surskit", "Masquerain", "Shroomish", "Breloom", "Slakoth", "Vigoroth", "Slaking", "Nincada", "Ninjask", "Shedinja", "Whismur", "Loudred", "Exploud", "Makuhita", "Hariyama", "Azurill", "Nosepass", "Skitty", "Delcatty", "Sableye", "Mawile", "Aron", "Lairon", "Aggron", "Meditite", "Medicham", "Electrike", "Manectric", "Plusle", "Minun", "Volbeat", "Illumise", "Roselia", "Gulpin", "Swalot", "Carvanha", "Sharpedo", "Wailmer", "Wailord", "Numel", "Camerupt", "Torkoal", "Spoink", "Grumpig", "Spinda", "Trapinch", "Vibrava", "Flygon", "Cacnea", "Cacturne", "Swablu", "Altaria", "Zangoose", "Seviper", "Lunatone", "Solrock", "Barboach", "Whiscash", "Corphish", "Crawdaunt", "Baltoy", "Claydol", "Lileep", "Cradily", "Anorith", "Armaldo", "Feebas", "Milotic", "Castform", "Kecleon", "Shuppet", "Banette", "Duskull", "Dusclops", "Tropius", "Chimecho", "Absol", "Wynaut", "Snorunt", "Glalie", "Spheal", "Sealeo", "Walrein", "Clamperl", "Huntail", "Gorebyss", "Relicanth", "Luvdisc", "Bagon", "Shelgon", "Salamence", "Beldum", "Metang", "Metagross", "Regirock", "Regice", "Registeel", "Latias", "Latios", "Kyogre", "Groudon", "Rayquaza", "Jirachi", "Deoxys"};
    String[] gen4 = {"Turtwig", "Grotle", "Torterra", "Chimchar", "Monferno", "Infernape", "Piplup", "Prinplup", "Empoleon", "Starly", "Staravia", "Staraptor", "Bidoof", "Bibarel", "Kricketot", "Kricketune", "Shinx", "Luxio", "Luxray", "Budew", "Roserade", "Cranidos", "Rampardos", "Shieldon", "Bastiodon", "Burmy", "Wormadam", "Mothim", "Combee", "Vespiquen", "Pachirisu", "Buizel", "Floatzel", "Cherubi", "Cherrim", "Shellos", "Gastrodon", "Ambipom", "Drifloon", "Drifblim", "Buneary", "Lopunny", "Mismagius", "Honchkrow", "Glameow", "Purugly", "Chingling", "Stunky", "Skuntank", "Bronzor", "Bronzong", "Bonsly", "Mime Jr.", "Happiny", "Chatot", "Spiritomb", "Gible", "Gabite", "Garchomp", "Munchlax", "Riolu", "Lucario", "Hippopotas", "Hippowdon", "Skorupi", "Drapion", "Croagunk", "Toxicroak", "Carnivine", "Finneon", "Lumineon", "Mantyke", "Snover", "Abomasnow", "Weavile", "Magnezone", "Lickilicky", "Rhyperior", "Tangrowth", "Electivire", "Magmortar", "Togekiss", "Yanmega", "Leafeon", "Glaceon", "Gliscor", "Mamoswine", "Porygon-Z", "Gallade", "Probopass", "Dusknoir", "Froslass", "Rotom", "Uxie", "Mesprit", "Azelf", "Dialga", "Palkia", "Heatran", "Regigigas", "Giratina", "Cresselia", "Phione", "Manaphy", "Darkrai", "Shaymin", "Arceus"};
    String[] gen5 = {"Victini", "Snivy", "Servine", "Serperior", "Tepig", "Pignite", "Emboar", "Oshawott", "Dewott", "Samurott", "Patrat", "Watchog", "Lillipup", "Herdier", "Stoutland", "Purrloin", "Liepard", "Pansage", "Simisage", "Pansear", "Simisear", "Panpour", "Simipour", "Munna", "Musharna", "Pidove", "Tranquill", "Unfezant", "Blitzle", "Zebstrika", "Roggenrola", "Boldore", "Gigalith", "Woobat", "Swoobat", "Drilbur", "Excadrill", "Audino", "Timburr", "Gurdurr", "Conkeldurr", "Tympole", "Palpitoad", "Seismitoad", "Throh", "Sawk", "Sewaddle", "Swadloon", "Leavanny", "Venipede", "Whirlipede", "Scolipede", "Cottonee", "Whimsicott", "Petilil", "Lilligant", "Basculin", "Sandile", "Krokorok", "Krookodile", "Darumaka", "Darmanitan", "Maractus", "Dwebble", "Crustle", "Scraggy", "Scrafty", "Sigilyph", "Yamask", "Cofagrigus", "Tirtouga", "Carracosta", "Archen", "Archeops", "Trubbish", "Garbodor", "Zorua", "Zoroark", "Minccino", "Cinccino", "Gothita", "Gothorita", "Gothitelle", "Solosis", "Duosion", "Reuniclus", "Ducklett", "Swanna", "Vanillite", "Vanillish", "Vanilluxe", "Deerling", "Sawsbuck", "Emolga", "Karrablast", "Escavalier", "Foongus", "Amoonguss", "Frillish", "Jellicent", "Alomomola", "Joltik", "Galvantula", "Ferroseed", "Ferrothorn", "Klink", "Klang", "Klinklang", "Tynamo", "Eelektrik", "Eelektross", "Elgyem", "Beheeyem", "Litwick", "Lampent", "Chandelure", "Axew", "Fraxure", "Haxorus", "Cubchoo", "Beartic", "Cryogonal", "Shelmet", "Accelgor", "Stunfisk", "Mienfoo", "Mienshao", "Druddigon", "Golett", "Golurk", "Pawniard", "Bisharp", "Bouffalant", "Rufflet", "Braviary", "Vullaby", "Mandibuzz", "Heatmor", "Durant", "Deino", "Zweilous", "Hydreigon", "Larvesta", "Volcarona", "Cobalion", "Terrakion", "Virizion", "Tornadus", "Thundurus", "Reshiram", "Zekrom", "Landorus", "Kyurem", "Keldeo", "Meloetta", "Genesect"};
    String[] gen6 = {"Chespin", "Quilladin", "Chesnaught", "Fennekin", "Braixen", "Delphox", "Froakie", "Frogadier", "Greninja", "Bunnelby", "Diggersby", "Fletchling", "Fletchinder", "Talonflame", "Scatterbug", "Spewpa", "Vivillon", "Litleo", "Pyroar", "Flabébé", "Floette", "Florges", "Skiddo", "Gogoat", "Pancham", "Pangoro", "Furfrou", "Espurr", "Meowstic", "Honedge", "Doublade", "Aegislash", "Spritzee", "Aromatisse", "Swirlix", "Slurpuff", "Inkay", "Malamar", "Binacle", "Barbaracle", "Skrelp", "Dragalge", "Clauncher", "Clawitzer", "Helioptile", "Heliolisk", "Tyrunt", "Tyrantrum", "Amaura", "Aurorus", "Sylveon", "Hawlucha", "Dedenne", "Carbink", "Goomy", "Sliggoo", "Goodra", "Klefki", "Phantump", "Trevenant", "Pumpkaboo", "Gourgeist", "Bergmite", "Avalugg", "Noibat", "Noivern", "Xerneas", "Yveltal", "Zygarde", "Diancie", "Hoopa", "Volcanion"};
    String[] gen7 = {"Rowlet", "Dartrix", "Decidueye", "Litten", "Torracat", "Incineroar", "Popplio", "Brionne", "Primarina", "Pikipek", "Trumbeak", "Toucannon", "Yungoos", "Gumshoos", "Grubbin", "Charjabug", "Vikavolt", "Crabrawler", "Crabominable", "Oricorio", "Cutiefly", "Ribombee", "Rockruff", "Lycanroc", "Wishiwashi", "Mareanie", "Toxapex", "Mudbray", "Mudsdale", "Dewpider", "Araquanid", "Fomantis", "Lurantis", "Morelull", "Shiinotic", "Salandit", "Salazzle", "Stufful", "Bewear", "Bounsweet", "Steenee", "Tsareena", "Comfey", "Oranguru", "Passimian", "Wimpod", "Golisopod", "Sandygast", "Palossand", "Pyukumuku", "Type: Null", "Silvally", "Minior", "Komala", "Turtonator", "Togedemaru", "Mimikyu", "Bruxish", "Drampa", "Dhelmise", "Jangmo-o", "Hakamo-o", "Kommo-o", "Tapu Koko", "Tapu Lele", "Tapu Bulu", "Tapu Fini", "Cosmog", "Cosmoem", "Solgaleo", "Lunala", "Nihilego", "Buzzwole", "Pheromosa", "Xurkitree", "Celesteela", "Kartana", "Guzzlord", "Necrozma", "Magearna", "Marshadow", "Poipole", "Naganadel", "Stakataka", "Blacephalon", "Zeraora"};
    String[] gen8 = {"Grookey", "Thwackey", "Rillaboom", "Scorbunny", "Raboot", "Cinderace", "Sobble", "Drizzile", "Skwovet", "Greedent", "Rookidee", "Corvisquire", "Corviknight", "Blipbug", "Dottler", "Orbeetle", "Nickit", "Thievul", "Gossifleur", "Eldegoss", "Wooloo", "Dubwool", "Chewtle", "Drednaw", "Yamper", "Boltund", "Rolycoly", "Carkol", "Coalossal", "Applin", "Flapple", "Appletun", "Silicobra", "Sandaconda", "Cramorant", "Arrokuda", "Barraskewda", "Toxel", "Toxtricity", "Sizzlipede", "Centiskorch", "Clobbopus", "Grapploct", "Sinistea", "Polteaseist", "Hatenna", "Hattrem", "Hatterene", "Impidimp", "Morgrem", "Grimmsnarl", "Obstagoon", "Perrserker", "Cursola", "Sirfetch'd", "Mr. Rime", "Runerigus", "Milcery", "Alcremie", "Falinks", "Pincurchin", "Snom", "Frosmoth", "Stonjourner", "Eiscue", "Indeedee", "Morpeko", "Cufant", "Copperajah", "Dracozolt", "Actozolt", "Dracovish", "Arctovish", "Duraludon", "Dreepy", "Drakloak", "Dragapult", "Zacian", "Zamazenta", "Eternatus", "Kubfu", "Urshifu", "Zarude"};
    String[] Games1= {"Red", "Green", "Blue", "Yellow"};
    String[] Games2= {"Gold", "Silver", "Crystal"};
    String[] Games3= {"Ruby", "Sapphire", "FireRed", "LeafGreen", "Emerald"};
    String[] Games4= {"Diamond", "Pearl", "Platinum", "HeartGold", "SoulSilver"};
    String[] Games5= {"Black", "White","Black 2", "White 2"};
    String[] Games6= {"X", "Y", "Omega Ruby", "Alpha Sapphire"};
    String[] Games7= {"Sun", "Moon", "Ultra Sun", "Ultra Moon", "Let's Go Pikachu", "Let's Go Eevee"};
    String[] Games8= {"Sword", "Shield"};

    @Override
    public void initialize(URL url, ResourceBundle rb){
        InitializePokemonList();
        PokemonList.getSelectionModel().selectedItemProperty()
                .addListener((v, oldValue, newValue) -> {
                    if(newValue != null) {
                        newSelectionPokemon = newValue.toString().substring(18, newValue.toString().length()-2);
                        if(findGeneration(newSelectionPokemon) != 0) {
                            if (oldValue != null) {
                                oldSelectionPokemon = oldValue.toString().substring(18, oldValue.toString().length() - 2);
                                if (findGeneration(oldSelectionPokemon) != 0) {
                                    oldSelectionGeneration = findGeneration(oldSelectionPokemon);
                                }
                            }
                            selectedPokemon = new Pokemon(newSelectionPokemon, findGeneration(newSelectionPokemon));
                            pokemonLabel.setText(selectedPokemon.getName());
                            alolanRadioButton.setDisable(!selectedPokemon.isAlolan());
                            alolanRadioButton.setSelected(false);
                            galarianRadioButton.setDisable(!selectedPokemon.isGalarian());
                            galarianRadioButton.setSelected(false);
                            if (oldValue == null) {
                                InitializeGameList(selectedPokemon.getGeneration());
                            }else if (oldSelectionGeneration != findGeneration(newSelectionPokemon)){
                                InitializeGameList(selectedPokemon.getGeneration());
                                InitializeMethodList(selectedGame.getMethods());
                            }
                            if (selectedGame.getName() != null) {
                                selectedGame = new Game(selectedGame.getName(), selectedGame.getGeneration(), selectedPokemon);
                                InitializeMethodList(selectedGame.getMethods());
                            }
                        }
                    }
                });
        GameList.getSelectionModel().selectedItemProperty()
                .addListener((v, oldValue, newValue) -> {
                    if(newValue != null) {
                        newSelectionGame = newValue.toString().substring(18, newValue.toString().length() - 2);
                        if(findGeneration(newSelectionGame) != 0) {
                            selectedGame = new Game(newSelectionGame, findGeneration(newSelectionGame), selectedPokemon);
                            gameLabel.setText(selectedGame.getName());
                            if(selectedGame.generation >= 5) {
                                if (!(selectedGame.getName().compareTo("Black") == 0) || !(selectedGame.getName().compareTo("White") == 0))
                                    shinyCharmCheckBox.setDisable(false);
                            }else
                                shinyCharmCheckBox.setDisable(true);
                            lureCheckBox.setDisable(!(selectedGame.getName().substring(0,3).compareTo("Let") == 0));
                            InitializeMethodList(selectedGame.getMethods());
                        }
                    }
                });
        MethodList.getSelectionModel().selectedItemProperty()
                .addListener((v, oldValue, newValue) -> {
                    if(newValue != null) {
                        selectedMethod = new Method(newValue.toString().substring(18, newValue.toString().length() - 2), selectedGame.getGeneration());
                        methodLabel.setText(selectedMethod.getName());
                    }
                });
    }

    public void InitializePokemonList(){
        selectedPokemon = new Pokemon();
        pokemonLabel.setText(selectedPokemon.getName());
        TreeItem<String> pokemonRoot, Gen1, Gen2, Gen3, Gen4, Gen5, Gen6, Gen7, Gen8;
        pokemonRoot = new TreeItem<>();

        Gen1 = makeBranch("Generation 1", pokemonRoot);
        Gen2 = makeBranch("Generation 2", pokemonRoot);
        Gen3 = makeBranch("Generation 3", pokemonRoot);
        Gen4 = makeBranch("Generation 4", pokemonRoot);
        Gen5 = makeBranch("Generation 5", pokemonRoot);
        Gen6 = makeBranch("Generation 6", pokemonRoot);
        Gen7 = makeBranch("Generation 7", pokemonRoot);
        Gen8 = makeBranch("Generation 8", pokemonRoot);

        for(String i: gen1)
            makeBranch(i, Gen1);
        for(String i: gen2)
            makeBranch(i, Gen2);
        for(String i: gen3)
            makeBranch(i, Gen3);
        for(String i: gen4)
            makeBranch(i, Gen4);
        for(String i: gen5)
            makeBranch(i, Gen5);
        for(String i: gen6)
            makeBranch(i, Gen6);
        for(String i: gen7)
            makeBranch(i, Gen7);
        for(String i: gen8)
            makeBranch(i, Gen8);

        PokemonList.setRoot(pokemonRoot);
        PokemonList.setShowRoot(false);
    }

    public void InitializeGameList(int generation){
        selectedGame = new Game();
        shinyCharmCheckBox.setSelected(false);
        lureCheckBox.setSelected(false);
        gameLabel.setText(selectedGame.getName());
        TreeItem<String> gameRoot, Gen1, Gen2, Gen3, Gen4, Gen5, Gen6, Gen7, Gen8;
        gameRoot = new TreeItem<>();
        Gen1 = new TreeItem<>();
        Gen2 = new TreeItem<>();
        Gen3 = new TreeItem<>();
        Gen4 = new TreeItem<>();
        Gen5 = new TreeItem<>();
        Gen6 = new TreeItem<>();
        Gen7 = new TreeItem<>();
        Gen8 = new TreeItem<>();

        if(generation == 0)
            return;
        if(generation <= 1)
            Gen1 = makeBranch("Generation 1", gameRoot);
            for (String i: Games1)
                makeBranch(i, Gen1);
        if(generation <= 2)
            Gen2 = makeBranch("Generation 2", gameRoot);
            for(String i: Games2)
                makeBranch(i,Gen2);
        if(generation <= 3)
            Gen3 = makeBranch("Generation 3", gameRoot);
            for(String i: Games3)
                makeBranch(i,Gen3);
        if(generation <= 4)
            Gen4 = makeBranch("Generation 4", gameRoot);
            for(String i: Games4)
                makeBranch(i,Gen4);
        if(generation <= 5)
            Gen5 = makeBranch("Generation 5", gameRoot);
            for(String i: Games5)
                makeBranch(i,Gen5);
        if(generation <= 6)
            Gen6 = makeBranch("Generation 6", gameRoot);
            for(String i: Games6)
                makeBranch(i,Gen6);
        if(generation <= 7)
            Gen7 = makeBranch("Generation 7", gameRoot);
            for(String i: Games7)
                makeBranch(i,Gen7);
        if(generation <= 8)
            Gen8 = makeBranch("Generation 8", gameRoot);
            for(String i: Games8)
                makeBranch(i,Gen8);

        GameList.setRoot(gameRoot);
        GameList.setShowRoot(false);
    }

    public void InitializeMethodList(String[] gameMethods){
        selectedMethod = new Method();
        methodLabel.setText(selectedMethod.getName());
        TreeItem<String> methodRoot;
        methodRoot = new TreeItem<>();
        for(String i: gameMethods)
            if(i != null)
                makeBranch(i, methodRoot);

        MethodList.setRoot(methodRoot);
        MethodList.setShowRoot(false);
    }

    public int findGeneration(String name){
        for(String i: Games1)
            if(name.compareTo(i) == 0)
                return 1;
        for(String i: Games2)
            if(i.compareTo(name) == 0)
                return 2;
        for(String i: Games3)
            if(i.compareTo(name) == 0)
                return 3;
        for(String i: Games4)
            if(i.compareTo(name) == 0)
                return 4;
        for(String i: Games5)
            if(i.compareTo(name) == 0)
                return 5;
        for(String i: Games6)
            if(i.compareTo(name) == 0)
                return 6;
        for(String i: Games7)
            if(i.compareTo(name) == 0)
                return 7;
        for(String i: Games8)
            if(i.compareTo(name) == 0)
                return 8;

        for(String i: gen1)
            if(name.compareTo(i) == 0)
                return 1;
        for(String i: gen2)
            if(i.compareTo(name) == 0)
                return 2;
        for(String i: gen3)
            if(i.compareTo(name) == 0)
                return 3;
        for(String i: gen4)
            if(i.compareTo(name) == 0)
                return 4;
        for(String i: gen5)
            if(i.compareTo(name) == 0)
                return 5;
        for(String i: gen6)
            if(i.compareTo(name) == 0)
                return 6;
        for(String i: gen7)
            if(i.compareTo(name) == 0)
                return 7;
        for(String i: gen8)
            if(i.compareTo(name) == 0)
                return 8;
        return 0;
    }

    public TreeItem<String> makeBranch(String title, TreeItem<String> parent){
        TreeItem<String> item = new TreeItem<>(title);
        parent.getChildren().add(item);
        return item;
    }

    public void buttonClick(){
        System.out.println("This is a test");
    }
}
