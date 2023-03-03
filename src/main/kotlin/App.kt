import com.google.gson.Gson
import com.google.gson.JsonObject
import java.io.FileReader

class App {

    /*
    * Function that allows to launch the application
    * Returns null if the number entered is 99, otherwise,
    * Launches the automata corresponding to the number entered with the play function
    * If the number entered is not valid, we relaunch the start function
     */
    fun start(): Automate? {
        println("--------------- Menu de mon TP -------------------------")
        println("1. Smiley (pour reconnaitre un des smileys)")
        println("2. HH:MM (pour reconnaitre une heure bien formée)")
        println("3. Multiple de 3")
        println("4. JJ/MM/AAAA")
        println("5. Multiple de 5")
        println("99. Arrête de l'application")
        println("Votre choix (1-99) ?")
        println("Je vous demanderai ensuite la chaîne à analyser, Merci")
        println("--------------------------------------------------------")
        when (readLine().toString()) {
            "1" -> play(loadFromJson("data/smiley.json"))
            "2" -> play(loadFromJson("data/heure.json"))
            "3" -> { play (loadFromJson("data/multipleDeTrois.json")) }
            "4" -> { play (loadFromJson("data/date.json")) }
            "5" -> { play (loadFromJson("data/multipleDeCinq.json")) }
            "99" -> {
                println("Au revoir !")
                return null
            }
            else -> start()
        }
        return null
    }

    /*
    * Function who allow to play with an automata
    * Ask the user to enter a word
    * If the word is valid, we display a success message
    * Otherwise, we display a failure message
    * We then ask the user if he wants to restart with the same automata, change automata or quit
    * If the user wants to restart (1), we relaunch the play function with the same automata
    * If the user wants to change automate (2), we relaunch the start function
    * If the user wants to quit (3), we display a finish message and quit the application
    * If the user enters an invalid number, we relaunch the start function
     */

    private fun play(automate: Automate) {
        println("--------------------------------------------------------")
        println("Vous avez choisi l'automate ${automate.nom} ! ")
        println("Veuillez entrer un mot :")
        val line = readLine().toString()
        if (!automate.isValid(line)) {
            println("Votre mot n'est pas valide !")
        } else {
            println("Votre mot est valide !")
        }
        println("--------------------------------------------------------")
        println("1. Recommencer ")
        println("2. Changer d'Automate ")
        println("3. Quitter")
        println("--------------------------------------------------------")

        when (readLine().toString()) {
            "1" -> play(automate)
            "2" -> start()
            "3" -> {
                println("Au revoir !")
                return
            }
            else -> start()
        }

    }

    /*
    * Function that load an automate from a JSON file
    * Return the automata corresponding to the JSON file passed in parameter
     */

    private fun loadFromJson(path : String) : Automate {

        val file = FileReader(path)
        val gson = Gson()
        val jsonObject = gson.fromJson(file, JsonObject::class.java)
        val automate = Automate(jsonObject.get("name").asString)
        val list = jsonObject.getAsJsonArray("alphabet").toMutableList()
        val listString = mutableListOf<String>()
        for (el in list){
            listString.add(el.toString().subSequence(1,el.asString.length+1).toString()) // Get all alphabet to a mutable list of string
        }
        automate.setAlphabet(listString)
        val states = jsonObject.getAsJsonArray("S").toMutableList()
        val statesObject = mutableListOf<State>()
        for (s in states){
            statesObject.add(State(s.toString().subSequence(1,s.asString.length+1).toString())) // Get all states to a mutable list of State
        }

        val transi = jsonObject.getAsJsonArray("transition").toMutableList()
        for(t in transi){ // Add all transitions and add them to States object in our statesObject
              for(el in t.asJsonObject.entrySet()){
                  val key = el.key
                  val keyTransi = el.value.asJsonArray[0].asString.subSequence(0,1).toString()
                  val stateName = el.value.asJsonArray[1].asString.subSequence(0,2).toString()
                  val stateIn = statesObject.find { it.getNom() == stateName }
                  val stateOf = statesObject.find { it.getNom() == key }
                  stateOf!!.addOutGoing(keyTransi,stateIn!!)
              }
        }
        val initState = statesObject.find { it.getNom() == jsonObject.get("init").asString.subSequence(0,2).toString() } // get initial state
        automate.setInitialState(initState!!)

        val finalState = mutableListOf<State>()
        val final = jsonObject.getAsJsonArray("final").toMutableList()   // get list of final states
        for (f in final){
            val state = statesObject.find { it.getNom() == f.asString.subSequence(0,2).toString() }
            finalState.add(state!!)
        }
        automate.setFinalStates(finalState)
        return automate
    }

}
