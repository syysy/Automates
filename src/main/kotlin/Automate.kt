class Automate(nom: String) {

    var nom: String
    private var S: MutableList<State>
    private var alphabet: MutableList<String>
    private lateinit var initState: State
    private var finialStates: MutableList<State>

    init {
        this.nom = nom
        S = mutableListOf()
        alphabet = mutableListOf()
        finialStates = mutableListOf()
    }

    /*
    * Set the initial state of the automata
    * @param state : the initial state
     */
    fun setInitialState(state: State) {
        this.initState = state
    }

    /*
    * Set the final states of the automata
    * @param states : the final states
     */
    fun setFinalStates(states: MutableList<State>) {
        for (s in states) {
            this.finialStates.add(s)
        }
    }

    /*
    * Set the alphabet of the automata
    * @param list : the alphabet
     */
    fun setAlphabet(list: MutableList<String>) {
        this.alphabet = list
    }

    /*
    * Add a state to the automata
    * @param state : the state to add
    * @return true if at the end of the text the current state is a final state
    * @return false if at the end of the text the current state is not a final state
    * @return false if the text contains a character that is not in the alphabet
    * @return false if the text is empty or null
    * @return false if the current value of the text is not in the transition of the current state
     */

    fun isValid(text: String): Boolean {
        var currentState = this.initState
        for (value in text) {
            if (!this.alphabet.contains(value.toString())) {
                return false
            }
            val state = currentState.getByKey(value.toString()) ?: return false
            currentState = state
        }
        return this.finialStates.contains(currentState)

    }

}