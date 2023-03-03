class State(nom: String) {

    private var nom: String
    private var outGoing = HashMap<String, State>()

    init {
        this.nom = nom
    }

    /*
    * Get the name of the state
    * @return the name of the state
     */

    fun getNom(): String {
        return this.nom
    }

    /*
    * Add a transition to the state
    * @param key : the key of the transition
    * @param state : the state to go
     */
    fun addOutGoing(key: String, state: State) {
        this.outGoing[key] = state
    }

    /*
    * Get the state to go with the key
    * @param key : the key of the transition
    * @return the state to go
    * @return null if the key doesn't exist
     */
    fun getByKey(key: String): State? {
        return this.outGoing[key]
    }


}